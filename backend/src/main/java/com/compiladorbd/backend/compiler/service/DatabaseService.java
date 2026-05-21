package com.compiladorbd.backend.compiler.service;

import com.compiladorbd.backend.compiler.dto.CreateDatabaseResponse;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public CreateDatabaseResponse createFromSql(String sql) {
        if (sql == null || sql.isBlank()) {
            return new CreateDatabaseResponse(false, "No se recibio SQL para ejecutar.");
        }

        List<String> rawStatements = parseStatements(sql);
        if (rawStatements.isEmpty()) {
            return new CreateDatabaseResponse(false, "El SQL recibido no contiene sentencias validas.");
        }

        String databaseName = null;
        List<String> createTableStatements = new ArrayList<>();

        for (String stmt : rawStatements) {
            String upper = stmt.toUpperCase();
            if (upper.startsWith("CREATE DATABASE")) {
                databaseName = extractDatabaseName(stmt);
            } else if (upper.startsWith("USE ")) {
                databaseName = extractUseDatabaseName(stmt);
            } else {
                createTableStatements.add(stmt);
            }
        }

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {

            for (String stmt : rawStatements) {
                String upper = stmt.toUpperCase();
                if (upper.startsWith("CREATE DATABASE")) {
                    statement.execute(stmt);
                }
            }

            if (databaseName != null && !databaseName.isBlank()) {
                connection.setCatalog(databaseName);
            }

            for (String stmt : createTableStatements) {
                statement.execute(stmt);
            }

            return new CreateDatabaseResponse(true, "Base de datos y tablas creadas correctamente en MySQL.");
        } catch (SQLException exception) {
            return new CreateDatabaseResponse(false, "Error al ejecutar SQL: " + exception.getMessage());
        }
    }

    private List<String> parseStatements(String sql) {
        List<String> statements = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        for (String line : sql.split("\n")) {
            String trimmed = line.trim();
            if (trimmed.isEmpty() || trimmed.startsWith("--")) {
                continue;
            }
            current.append(" ").append(trimmed);
            if (trimmed.endsWith(";")) {
                String stmt = current.toString().trim();
                if (!stmt.isEmpty()) {
                    statements.add(stmt);
                }
                current.setLength(0);
            }
        }
        String remaining = current.toString().trim();
        if (!remaining.isEmpty()) {
            statements.add(remaining);
        }
        return statements;
    }

    private String extractDatabaseName(String createDatabaseStmt) {
        String[] tokens = createDatabaseStmt.split("\\s+");
        for (int i = 0; i < tokens.length; i++) {
            String upper = tokens[i].toUpperCase();
            if ((upper.equals("DATABASE") || upper.equals("EXISTS")) && i + 1 < tokens.length) {
                String candidate = tokens[i + 1].replace(";", "").trim();
                if (!candidate.isEmpty()) {
                    return candidate;
                }
            }
        }
        return null;
    }

    private String extractUseDatabaseName(String useStmt) {
        String cleaned = useStmt.replaceFirst("(?i)^USE\\s+", "").replace(";", "").trim();
        return cleaned.isEmpty() ? null : cleaned;
    }
}
