package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, SecurityUtil.authUserId()));
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 1, 15, 0), "Завтрак пользователя типа Админ", 2000), 2);
        save(new Meal(LocalDateTime.of(2021, Month.JANUARY, 1, 17, 0), "Второй завтрак пользователя типа Админ", 1000), 2);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMeals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMeals.put(meal.getId(), meal);
            return meal;
        }
        return userMeals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return false;
        } else {
            return userMeals.remove(id) != null;
        }
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return null;
        } else {
            return userMeals.get(id);
        }
    }

    private List<Meal> getAllByPredicate(int userId, Predicate<Meal> predicate) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) {
            return (new ArrayList<>());
        } else {
            return userMeals.values().stream()
                    .filter(predicate)
                    .sorted(Comparator.comparing(Meal::getDate).reversed())
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return getAllByPredicate(userId, meal -> true);
    }

    public List<Meal> getAllByDateTime(int userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return getAllByPredicate(userId, meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(), startDateTime, endDateTime));
    }
}