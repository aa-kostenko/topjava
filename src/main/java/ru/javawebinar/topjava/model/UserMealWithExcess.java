package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.IExcessDefiner;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final boolean excess;

    private final IExcessDefiner excessDefiner;

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess, IExcessDefiner excessDefiner) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.excessDefiner = excessDefiner;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
        this.excessDefiner = null;
    }

    public UserMealWithExcess(LocalDateTime dateTime, String description, int calories, IExcessDefiner execsDefiner) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = false;
        this.excessDefiner = execsDefiner;
    }

    private boolean getExcess(){
        if (excessDefiner == null)
        {
            return excess;
        }
        else
        {
            return excessDefiner.isExcess(dateTime.toLocalDate());
        }
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + getExcess() +
                '}';
    }
}
