package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.repository.MealConcurrentMapCrudRepository;
import ru.javawebinar.topjava.repository.MealCrudRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealCrudRepository mealRepository = new MealConcurrentMapCrudRepository();

    private static final DateTimeFormatter mealDateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private static final Logger log = getLogger(UserServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        String forward="";
        String action = request.getParameter("action");
        //это нужно практически в каждом случае кроме вставки
        request.setAttribute("mealDateTimeFormatter", mealDateTimeFormatter);

        //тут пока просто удаляем
        if (action.equalsIgnoreCase("delete")){
            Long mealId = Long.parseLong(request.getParameter("mealId"));
            mealRepository.deleteById(mealId);
        }

        //а сюда идем или прямым путем или после удаления рациона
        if ( action.equalsIgnoreCase("delete") || action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;
            List<Meal> mealList = new ArrayList<Meal>(mealRepository.findAll());
            List<MealTo> mealToList = MealsUtil.filteredByStreams(mealList,LocalTime.MIN, LocalTime.MAX, 2000);
            request.setAttribute("mealToList", mealToList);
        }
        //хотим редактировать рацион
        else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            Long mealId = Long.parseLong(request.getParameter("mealId"));
            Meal meal = mealRepository.findById(mealId);
            request.setAttribute("meal", meal);
            request.setAttribute("actionForTitle", "Edit meal");
        }
        //иначе - создаем новый
        else {
            forward = INSERT_OR_EDIT;
            request.setAttribute("actionForTitle", "Add meal");
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LocalDateTime dateTime = null;
        try {
            dateTime = LocalDateTime.parse(req.getParameter("dateTime"), mealDateTimeFormatter);
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        String description = req.getParameter("description");

        int calories = 0;
        try {
            calories = Integer.parseInt(req.getParameter("dateTime"));
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }

        String idStr = req.getParameter("id");
        Long id = null;
        if(idStr == null || idStr.isEmpty()){
            mealRepository.save(new Meal(dateTime,description,calories));
        }
        else{
            try {
                id = Long.parseLong(idStr);
            } catch (NumberFormatException e) {
                log.error(e.getMessage());
                e.printStackTrace();
            }
            mealRepository.save(new Meal(id, dateTime,description,calories));
        }
        List<Meal> mealList = new ArrayList<Meal>(mealRepository.findAll());
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealList,LocalTime.MIN, LocalTime.MAX, 2000);
        req.setAttribute("mealToList", mealToList);
        req.setAttribute("mealDateTimeFormatter", mealDateTimeFormatter);
        RequestDispatcher view = req.getRequestDispatcher(LIST_MEAL);
        view.forward(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
        List<Meal> meals = MealsUtil.getMealListForTest();
        mealRepository.saveAll(meals);
    }
}
