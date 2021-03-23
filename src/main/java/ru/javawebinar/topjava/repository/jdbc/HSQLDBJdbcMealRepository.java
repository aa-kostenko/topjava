package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;

@Repository
@Profile(HSQL_DB)
public class HSQLDBJdbcMealRepository extends JdbcMealRepository<Timestamp>{
    public HSQLDBJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Timestamp convertLocalDateTime(LocalDateTime dateTime) {
        return Timestamp.valueOf(dateTime);
    }
}
