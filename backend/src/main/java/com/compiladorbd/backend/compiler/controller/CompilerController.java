package com.compiladorbd.backend.compiler.controller;

import com.compiladorbd.backend.compiler.dto.CompileError;
import com.compiladorbd.backend.compiler.dto.CompileRequest;
import com.compiladorbd.backend.compiler.dto.CompileResponse;
import com.compiladorbd.backend.compiler.service.CompilerService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/compiler")
@CrossOrigin(origins = {"https://compilador-bd.vercel.app", "http://localhost:5173"})
public class CompilerController {

    private final CompilerService compilerService;

    public CompilerController(CompilerService compilerService) {
        this.compilerService = compilerService;
    }

    @PostMapping("/compile")
    public CompileResponse compile(@RequestBody CompileRequest request) {
        if (request == null || request.program() == null || request.program().isBlank()) {
            return new CompileResponse(
                    List.of(new CompileError(1, "Debes enviar el programa a compilar.")),
                    "",
                    "Base de datos: No definida\nCantidad de tablas: 0"
            );
        }

        return compilerService.compile(request.program());
    }
}
