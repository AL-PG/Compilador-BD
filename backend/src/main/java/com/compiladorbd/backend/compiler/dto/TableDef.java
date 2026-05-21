package com.compiladorbd.backend.compiler.dto;

import java.util.List;

public record TableDef(String name, List<ColumnDef> columns) {
}
