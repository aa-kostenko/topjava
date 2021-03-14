package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;

import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles("data-jpa")
public class UserServiceDataJpaTest extends AbstractUserServiceTest{
    @Autowired
    private UserService service;

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        Assert.assertEquals(user.getMeals().size(), MealTestData.meals.size());
    }
}
