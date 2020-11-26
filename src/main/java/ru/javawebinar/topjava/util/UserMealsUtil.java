package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetweenHalfOpen;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        System.out.println("--filteredByCycles(Base)___________________");
        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);
        System.out.println("-------------------------------------------");
        System.out.println("--filteredByStreams(Optional 1)___________________");
        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println("-------------------------------------------");
        System.out.println("--filteredByCyclesOptional2 (Optional 2)___________________");
        List<UserMealWithExcess> mealsTo2 = filteredByCyclesOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo2.forEach(System.out::println);
        System.out.println("-------------------------------------------");
        System.out.println("--filteredByStreamsOptional2(Optional 2)___________________");
        System.out.println(filteredByStreamsOptional2(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
        System.out.println("-------------------------------------------");
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalorieMap = new HashMap<>();
        for (UserMeal meal : meals) {
            dayCalorieMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        for (UserMeal meal : meals) {
            boolean isExcess = (dayCalorieMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay);
            boolean inTime = isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime);
            if (inTime) {
                mealsWithExcess.add(
                        new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), isExcess));
            }

        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalorieMap = meals.stream()
                .collect(Collectors.toMap(p -> p.getDateTime().toLocalDate(), UserMeal::getCalories, Integer::sum));

        return meals.stream()
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(
                                m.getDateTime(),
                                m.getDescription(),
                                m.getCalories(),
                                dayCalorieMap.get(m.getDateTime().toLocalDate()) > caloriesPerDay
                        )
                )
                .collect(Collectors.toList());

    }

    public static List<UserMealWithExcess> filteredByCyclesOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsWithExcess = new ArrayList<>();
        Map<LocalDate, Integer> dayCalorieMap = new HashMap<>();

        IExcessDefiner excessDefiner = day -> (dayCalorieMap.get(day) > caloriesPerDay);

        for (UserMeal meal : meals) {
            dayCalorieMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
            if (isBetweenHalfOpen(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithExcess.add(
                        new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), excessDefiner));
            }
        }
        return mealsWithExcess;
    }

    public static List<UserMealWithExcess> filteredByStreamsOptional2(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> dayCalorieMap = new HashMap<>();
        IExcessDefiner excessDefiner = day -> (dayCalorieMap.get(day) > caloriesPerDay);

        return meals.stream()
                .peek(m -> dayCalorieMap.merge(m.getDateTime().toLocalDate(), m.getCalories(), Integer::sum))
                .filter(m -> isBetweenHalfOpen(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExcess(
                        m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        excessDefiner)
                )
                .collect(Collectors.toList());
    }
}
