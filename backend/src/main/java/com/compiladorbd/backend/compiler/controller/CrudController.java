package com.compiladorbd.backend.compiler.controller;

import com.compiladorbd.backend.compiler.service.CrudService;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/crud")
public class CrudController {

    private final CrudService crudService;

    public CrudController(CrudService crudService) {
        this.crudService = crudService;
    }

    @GetMapping("/{database}/{tableName}")
    public ResponseEntity<List<Map<String, Object>>> listAll(
            @PathVariable String database,
            @PathVariable String tableName) {
        return ResponseEntity.ok(crudService.listAll(database, tableName));
    }

    @GetMapping("/{database}/{tableName}/{id}")
    public ResponseEntity<Map<String, Object>> getById(
            @PathVariable String database,
            @PathVariable String tableName,
            @PathVariable String id) {
        Map<String, Object> result = crudService.getById(database, tableName, id);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @PostMapping("/{database}/{tableName}")
    public ResponseEntity<Map<String, Object>> insert(
            @PathVariable String database,
            @PathVariable String tableName,
            @RequestBody Map<String, Object> data) {
        int rows = crudService.insert(database, tableName, data);
        return ResponseEntity.ok(Map.of("success", true, "rowsAffected", rows));
    }

    @PutMapping("/{database}/{tableName}/{id}")
    public ResponseEntity<Map<String, Object>> update(
            @PathVariable String database,
            @PathVariable String tableName,
            @PathVariable String id,
            @RequestBody Map<String, Object> data) {
        int rows = crudService.update(database, tableName, id, data);
        return ResponseEntity.ok(Map.of("success", true, "rowsAffected", rows));
    }

    @DeleteMapping("/{database}/{tableName}/{id}")
    public ResponseEntity<Map<String, Object>> delete(
            @PathVariable String database,
            @PathVariable String tableName,
            @PathVariable String id) {
        int rows = crudService.delete(database, tableName, id);
        return ResponseEntity.ok(Map.of("success", true, "rowsAffected", rows));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", ex.getMessage()));
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<Map<String, Object>> handleBadSqlGrammar(BadSqlGrammarException ex) {
        String msg = ex.getCause() != null ? ex.getCause().getMessage() : ex.getMessage();
        if (msg != null && msg.toLowerCase().contains("doesn't exist")) {
            msg = "La tabla o base de datos no existe. Asegurate de ejecutar 'Ejecutar en MySQL' primero.";
        }
        return ResponseEntity.badRequest().body(Map.of("success", false, "message", msg));
    }
}
