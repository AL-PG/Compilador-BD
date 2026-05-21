package com.compiladorbd.backend.compiler.service;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CrudService {

    private final JdbcTemplate jdbcTemplate;

    public CrudService(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Map<String, Object>> listAll(String database, String tableName) {
        validateIdentifier(database);
        validateIdentifier(tableName);
        String sql = "SELECT * FROM " + database + "." + tableName;
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> getById(String database, String tableName, String id) {
        validateIdentifier(database);
        validateIdentifier(tableName);
        String pk = resolvePrimaryKey(database, tableName);
        String sql = "SELECT * FROM " + database + "." + tableName + " WHERE " + pk + " = ?";
        try {
            return jdbcTemplate.queryForMap(sql, id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int insert(String database, String tableName, Map<String, Object> data) {
        validateIdentifier(database);
        validateIdentifier(tableName);
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para insertar.");
        }

        List<String> columns = new ArrayList<>(data.keySet());
        List<Object> values = new ArrayList<>();
        for (String col : columns) {
            validateIdentifier(col);
            values.add(data.get(col));
        }

        String placeholders = String.join(", ", java.util.Collections.nCopies(columns.size(), "?"));
        String sql = "INSERT INTO " + database + "." + tableName +
                " (" + String.join(", ", columns) + ") VALUES (" + placeholders + ")";

        return jdbcTemplate.update(sql, values.toArray());
    }

    public int update(String database, String tableName, String id, Map<String, Object> data) {
        validateIdentifier(database);
        validateIdentifier(tableName);
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("No hay datos para actualizar.");
        }

        String pk = resolvePrimaryKey(database, tableName);
        List<String> setClauses = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String col = entry.getKey();
            validateIdentifier(col);
            if (col.equalsIgnoreCase(pk)) {
                continue;
            }
            setClauses.add(col + " = ?");
            values.add(entry.getValue());
        }

        if (setClauses.isEmpty()) {
            throw new IllegalArgumentException("No hay campos para actualizar (solo se envio la clave primaria).");
        }

        values.add(id);

        String sql = "UPDATE " + database + "." + tableName +
                " SET " + String.join(", ", setClauses) +
                " WHERE " + pk + " = ?";

        return jdbcTemplate.update(sql, values.toArray());
    }

    public int delete(String database, String tableName, String id) {
        validateIdentifier(database);
        validateIdentifier(tableName);
        String pk = resolvePrimaryKey(database, tableName);
        String sql = "DELETE FROM " + database + "." + tableName + " WHERE " + pk + " = ?";
        return jdbcTemplate.update(sql, id);
    }

    private String resolvePrimaryKey(String database, String tableName) {
        String sql = "SHOW KEYS FROM " + database + "." + tableName + " WHERE Key_name = 'PRIMARY'";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        if (rows.isEmpty()) {
            throw new IllegalStateException("No se encontro clave primaria para la tabla " + tableName);
        }
        Object columnName = rows.get(0).get("Column_name");
        if (columnName == null) {
            throw new IllegalStateException("No se pudo determinar la columna PK de " + tableName);
        }
        return columnName.toString();
    }

    private void validateIdentifier(String name) {
        if (name == null || !name.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Identificador invalido: " + name);
        }
    }
}
