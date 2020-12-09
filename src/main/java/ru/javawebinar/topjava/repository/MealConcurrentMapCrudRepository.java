package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealConcurrentMapCrudRepository implements MealCrudRepository {

    private final ConcurrentMap<Long, Meal> map = new ConcurrentHashMap<Long, Meal>();
    private AtomicLong longGenerator = new AtomicLong();

    @Override
    public long count() {
        return map.size();
    }

    @Override
    public void delete(Meal entity) {
        Long id = getKey(entity);
        if (id != null) {
            map.remove(id);
        }
    }

    @Override
    public void deleteAll() {
        map.clear();
    }

    @Override
    public void deleteAll(Iterable<? extends Meal> entities) {
        for(Meal meal: entities){
            delete(meal);
        }
    }

    @Override
    public void deleteById(Long id) {
        map.remove(id);
    }

    @Override
    public boolean existsById(Long id) {
        return map.containsKey(id);
    }

    @Override
    public List<Meal> findAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public List<Meal> findAllById(Iterable<Long> ids) {
        List<Meal> meals = new ArrayList<>();
        for(Long id: ids){
            Meal meal = map.get(id);
            if (meal != null){
                meals.add(meal);
            }
        }
        return meals;
    }

    @Override
    public Meal findById(Long id) {
        return map.get(id);
    }

    @Override
    public <S extends Meal> S save(S meal) {
        Long id = getKey(meal);
        if (id == null){
            id = generateId();
            meal.setId(id);
            map.put(id, meal);
        }
        else{
            map.replace(id, meal);
        }
        return meal;
    }

    @Override
    public <S extends Meal> Iterable<S> saveAll(Iterable<S> entities) {
        for(S entity: entities){
            save(entity);
        }
        return entities;
    }

    private Long getKey(Meal value){
        for (Map.Entry<Long, Meal> entry : map.entrySet()) {
            if (entry.getValue().equals(value)) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Long generateId(){
        return longGenerator.incrementAndGet();
    }
}
