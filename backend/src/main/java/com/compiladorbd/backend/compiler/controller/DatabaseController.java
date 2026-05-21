package com.compiladorbd.backend.compiler.controller;

import com.compiladorbd.backend.compiler.dto.CreateDatabaseRequest;
import com.compiladorbd.backend.compiler.dto.CreateDatabaseResponse;
import com.compiladorbd.backend.compiler.service.DatabaseService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/database")
public class DatabaseController {

    private final DatabaseService databaseService;

    public DatabaseController(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @PostMapping("/create")
    public CreateDatabaseResponse createDatabase(@RequestBody CreateDatabaseRequest request) {
        if (request == null || request.sql() == null || request.sql().isBlank()) {
            return new CreateDatabaseResponse(false, "No hay SQL generado para crear la base de datos.");
        }

        return databaseService.createFromSql(request.sql());
    }
}
