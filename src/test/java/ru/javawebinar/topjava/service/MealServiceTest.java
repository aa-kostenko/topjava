package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService service;

    @Test
    public void create() {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        assertMatch(created, newMeal);
        assertMatch(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(
                        new Meal(
                                null,
                                LocalDateTime.parse("2021-01-30 10:00", DateTimeUtil.DATE_TIME_FORMATTER),
                                "User. Еще один рацион в 10 утра.",
                                150
                        ), USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_BREAKFAST_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(USER_BREAKFAST_ID, USER_ID));
    }

    @Test
    public void deleteOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.delete(USER_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, NOT_FOUND));
    }

    @Test
    public void get() {
        Meal meal = service.get(USER_BREAKFAST_ID, USER_ID);
        assertMatch(meal, meal);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, NOT_FOUND));
    }

    @Test
    public void getOtherUserMeal() {
        assertThrows(NotFoundException.class, () -> service.get(USER_BREAKFAST_ID, ADMIN_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> meals = service.getBetweenInclusive(
                LocalDate.parse("2021-01-30", DateTimeUtil.DATE_FORMATTER),
                LocalDate.parse("2021-01-31", DateTimeUtil.DATE_FORMATTER),
                USER_ID);
        assertThat(meals.size()).isEqualTo(userMeals.size());
    }

    @Test
    public void update() {
        Meal updated = MealTestData.getUpdated();
        service.update(updated, USER_ID);
        assertMatch(service.get(USER_BREAKFAST_ID, USER_ID), updated);
    }

    @Test
    public void updateOtherUserMeal() {
        Meal userMeal = service.get(USER_BREAKFAST_ID, USER_ID);
        assertThrows(NotFoundException.class, () -> service.update(userMeal, ADMIN_ID));
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        List<Meal> sortDescDateUserMeals = userMeals.stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
        assertMatch(all, sortDescDateUserMeals);
    }


}