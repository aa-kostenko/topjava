package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealConcurrentMapCrudRepository;
import ru.javawebinar.topjava.repository.MealCrudRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private MealCrudRepository mealRepository = new MealConcurrentMapCrudRepository();

    private static final DateTimeFormatter mealDateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        List<Meal> mealList = new ArrayList<Meal>(mealRepository.findAll());
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealList,LocalTime.MIN, LocalTime.MAX, 2000);
        request.setAttribute("mealToList", mealToList);
        request.setAttribute("mealDateTimeFormatter", mealDateTimeFormatter);
        request.getRequestDispatcher("meals.jsp").forward(request, response);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        List<Meal> meals = MealsUtil.getMealListForTest();
        mealRepository.saveAll(meals);
    }
}
