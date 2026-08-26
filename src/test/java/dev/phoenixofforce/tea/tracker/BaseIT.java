package dev.phoenixofforce.tea.tracker;

import org.junit.jupiter.api.AfterEach;
import org.testcontainers.postgresql.PostgreSQLContainer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Objects;

@SpringBootTest(classes = Application.class)
public abstract class BaseIT {

    @ServiceConnection
    static final PostgreSQLContainer POSTGRES = new PostgreSQLContainer("postgres:17");
    static {
        POSTGRES.start();
    }

    @Autowired
    protected JdbcTemplate template;

    @AfterEach
    void afterEach() {
        List<String> tables = template
            .queryForList(
                "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'",
                String.class)
            .stream()
            .filter(Objects::nonNull)
            .filter(table -> !table.contains("flyway") && !table.contains("tea_type"))
            .toList();

        template.execute("TRUNCATE TABLE " + String.join(", ", tables) + " RESTART IDENTITY CASCADE");
    }

}
