package com.compiladorbd.backend.compiler.service;

import com.compiladorbd.backend.compiler.dto.CreateDatabaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

@Service
public class DatabaseService {

    @Value("${app.database.jdbc-url:}")
    private String jdbcUrl;

    @Value("${app.database.username:}")
    private String username;

    @Value("${app.database.password:}")
    private String password;

    public CreateDatabaseResponse createFromSql(String sql) {
        if (sql == null || sql.isBlank()) {
            return new CreateDatabaseResponse(false, "No se recibio SQL para ejecutar.");
        }

        List<String> statements = Arrays.stream(sql.split(";"))
                .map(String::trim)
                .filter(statement -> !statement.isBlank())
                .toList();

        if (statements.isEmpty()) {
            return new CreateDatabaseResponse(false, "El SQL recibido no contiene sentencias validas.");
        }

        if (jdbcUrl == null || jdbcUrl.isBlank()) {
            return new CreateDatabaseResponse(
                    true,
                    "Backend conectado correctamente. Configura app.database.jdbc-url para ejecutar SQL real."
            );
        }

        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
             Statement sqlStatement = connection.createStatement()) {
            for (String statementSql : statements) {
                sqlStatement.execute(statementSql);
            }
            return new CreateDatabaseResponse(true, "Base de datos creada correctamente desde el backend.");
        } catch (Exception exception) {
            return new CreateDatabaseResponse(false, "Error al ejecutar SQL: " + exception.getMessage());
        }
    }
}
