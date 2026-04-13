package com.compiladorbd.backend.compiler.dto;

import java.util.List;

public record CompileResponse(List<CompileError> errors, String sql, String structure) {
}
