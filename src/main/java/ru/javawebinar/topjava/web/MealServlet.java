package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private ConfigurableApplicationContext appCtx;
    private MealRestController mealRestController;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml");
        mealRestController = appCtx.getBean(MealRestController.class);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String postAction = request.getParameter("postAction");

        switch (postAction == null ? "all" : postAction) {
            case "createUpdate":
                String id = request.getParameter("id");
                Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                        LocalDateTime.parse(request.getParameter("dateTime")),
                        request.getParameter("description"),
                        Integer.parseInt(request.getParameter("calories")));

                log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
                mealRestController.create(meal);
                response.sendRedirect("meals");
                break;
            case "filter":
                String startDateStr = request.getParameter("startDate");
                LocalDate startDate = startDateStr != "" ? LocalDate.parse(startDateStr) : null;
                String endDateStr = request.getParameter("endDate");
                LocalDate endDate = endDateStr != "" ? LocalDate.parse(endDateStr) : null;
                String startTimeStr = request.getParameter("startTime");
                LocalTime startTime = startTimeStr != "" ? LocalTime.parse(startTimeStr, TIME_FORMATTER) : null;
                String endTimeStr = request.getParameter("endTime");
                LocalTime endTime = endTimeStr != "" ? LocalTime.parse(endTimeStr, TIME_FORMATTER) : null;

                log.info("getAllByDateAndTime {}, {}, {}, {}", startDate, endDate, startTime, endTime);

                request.setAttribute("startDate", startDateStr);
                request.setAttribute("endDate", endDateStr);
                request.setAttribute("startTime", startTimeStr);
                request.setAttribute("endTime", endTimeStr);

                request.setAttribute(
                        "meals",
                        mealRestController.getAllByDateAndTime(startDate, endDate, startTime, endTime));

                request.getRequestDispatcher("/meals.jsp").forward(request, response);
            case "all":
            default:
                log.info("getAll from doPost");
                response.sendRedirect("meals");
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                mealRestController.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        mealRestController.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                request.setAttribute("meals", mealRestController.getAll());
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    @Override
    public void destroy() {
        log.info("Start Servlet.destroy");
        super.destroy();
        appCtx.close();
        log.info("ApplicationContext closed");
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
