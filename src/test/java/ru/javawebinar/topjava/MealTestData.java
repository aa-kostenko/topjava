package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {


    public static final int USER_BREAKFAST_ID = START_SEQ + 2;
    public static final int USER_LUNCH_ID = START_SEQ + 3;
    public static final int USER_DINNER_ID = START_SEQ + 4;
    public static final int ADMIN_NIGHT_MEAL_ID = START_SEQ + 5;
    public static final int ADMIN_BREAKFAST_ID = START_SEQ + 6;
    public static final int ADMIN_LUNCH_ID = START_SEQ + 7;
    public static final int ADMIN_DINNER_ID = START_SEQ + 8;

    public static final Meal userBreakfast = new Meal(USER_BREAKFAST_ID,
            LocalDateTime.parse("2021-01-30 10:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. User. Завтрак",
            500);
    public static final Meal userLunch = new Meal(USER_LUNCH_ID,
            LocalDateTime.parse("2021-01-30 13:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. User. Обед",
            1000);
    public static final Meal userDinner = new Meal(USER_DINNER_ID,
            LocalDateTime.parse("2021-01-30 20:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. User. Ужин",
            500);

    public static final Meal adminNightMeal = new Meal(ADMIN_NIGHT_MEAL_ID,
            LocalDateTime.parse("2021-01-31 00:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. Admin. Еда на граничное значение",
            100);
    public static final Meal adminBreakfast = new Meal(ADMIN_BREAKFAST_ID,
            LocalDateTime.parse("2021-01-31 10:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. Admin. Завтрак",
            1000);
    public static final Meal adminLunch = new Meal(ADMIN_LUNCH_ID,
            LocalDateTime.parse("2021-01-31 13:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. Admin. Обед",
            500);
    public static final Meal adminDinner = new Meal(ADMIN_DINNER_ID,
            LocalDateTime.parse("2021-01-31 20:00", DateTimeUtil.DATE_TIME_FORMATTER),
            "БД. Admin. Ужин",
            410);

    public static Meal getNew() {
        return new Meal(
                LocalDateTime.parse("2021-01-30 11:00", DateTimeUtil.DATE_TIME_FORMATTER),
                "User. Второй завтрак",
                250
        );
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(userBreakfast);
        updated.setDescription("БД. User. Завтрак (обновленный)");
        updated.setCalories(700);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToComparingFieldByField(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }

}
