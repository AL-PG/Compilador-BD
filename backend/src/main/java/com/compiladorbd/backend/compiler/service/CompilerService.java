package com.compiladorbd.backend.compiler.service;

import com.compiladorbd.backend.compiler.dto.CompileError;
import com.compiladorbd.backend.compiler.dto.CompileResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CompilerService {

    private static final Pattern CREATE_DB_PATTERN = Pattern.compile("^CREAR\\s+BD\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern USE_DB_PATTERN = Pattern.compile("^USAR\\s+BD\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*;?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern CREATE_TABLE_PATTERN = Pattern.compile("^CREAR\\s+TABLA\\s+([A-Za-z_][A-Za-z0-9_]*)\\s*\\($", Pattern.CASE_INSENSITIVE);
    private static final Pattern CLOSE_TABLE_PATTERN = Pattern.compile("^\\)\\s*;?\\s*$");
    private static final Pattern COLUMN_PATTERN = Pattern.compile("^([A-Za-z_][A-Za-z0-9_]*)\\s+([A-Za-z]+(?:\\(\\d+(?:,\\d+)?\\))?)(.*)$", Pattern.CASE_INSENSITIVE);

    public CompileResponse compile(String program) {
        List<String> lines = List.of(program.split("\\R", -1));
        List<CompileError> errors = new ArrayList<>();
        List<String> sqlBlocks = new ArrayList<>();
        List<TableDef> tables = new ArrayList<>();
        String dbName = "";

        int index = 0;
        while (index < lines.size()) {
            String rawLine = lines.get(index);
            String line = rawLine.trim();
            int lineNumber = index + 1;

            if (line.isEmpty() || line.startsWith("//") || line.startsWith("#")) {
                index++;
                continue;
            }

            Matcher createDbMatcher = CREATE_DB_PATTERN.matcher(line);
            if (createDbMatcher.matches()) {
                if (!line.endsWith(";")) {
                    errors.add(new CompileError(lineNumber, "Falta el punto y coma al final de CREAR BD."));
                }

                dbName = createDbMatcher.group(1);
                sqlBlocks.add("CREATE DATABASE IF NOT EXISTS " + dbName + ";");
                sqlBlocks.add("USE " + dbName + ";");
                index++;
                continue;
            }

            Matcher useDbMatcher = USE_DB_PATTERN.matcher(line);
            if (useDbMatcher.matches()) {
                if (!line.endsWith(";")) {
                    errors.add(new CompileError(lineNumber, "Falta el punto y coma al final de USAR BD."));
                }

                String selectedDb = useDbMatcher.group(1);
                if (dbName.isBlank()) {
                    dbName = selectedDb;
                }
                sqlBlocks.add("USE " + selectedDb + ";");
                index++;
                continue;
            }

            Matcher createTableMatcher = CREATE_TABLE_PATTERN.matcher(line);
            if (createTableMatcher.matches()) {
                String tableName = createTableMatcher.group(1);
                List<ColumnDef> columns = new ArrayList<>();
                boolean closed = false;

                index++;
                while (index < lines.size()) {
                    String tableRawLine = lines.get(index);
                    String tableLine = tableRawLine.trim();
                    int tableLineNumber = index + 1;

                    if (tableLine.isEmpty() || tableLine.startsWith("//") || tableLine.startsWith("#")) {
                        index++;
                        continue;
                    }

                    if (CLOSE_TABLE_PATTERN.matcher(tableLine).matches()) {
                        if (!tableLine.endsWith(";")) {
                            errors.add(new CompileError(tableLineNumber, "La tabla debe cerrarse con \");\"."));
                        }
                        closed = true;
                        index++;
                        break;
                    }

                    String normalizedLine = tableLine.endsWith(",")
                            ? tableLine.substring(0, tableLine.length() - 1).trim()
                            : tableLine;

                    Matcher columnMatcher = COLUMN_PATTERN.matcher(normalizedLine);
                    if (!columnMatcher.matches()) {
                        errors.add(new CompileError(
                                tableLineNumber,
                                "Definicion de columna invalida. Formato esperado: nombre TIPO [restricciones]."
                        ));
                        index++;
                        continue;
                    }

                    String columnName = columnMatcher.group(1);
                    String columnType = columnMatcher.group(2).toUpperCase(Locale.ROOT);
                    String constraints = normalizeConstraints(columnMatcher.group(3));

                    columns.add(new ColumnDef(columnName, columnType, constraints));
                    index++;
                }

                if (!closed) {
                    errors.add(new CompileError(lineNumber, "La tabla " + tableName + " no tiene cierre \");\"."));
                    break;
                }

                if (columns.isEmpty()) {
                    errors.add(new CompileError(lineNumber, "La tabla " + tableName + " no contiene columnas."));
                }

                tables.add(new TableDef(tableName, columns));

                List<String> sqlColumns = columns.stream()
                        .map(column -> "  " + column.name() + " " + column.type() + (column.constraints().isBlank() ? "" : " " + column.constraints()))
                        .toList();

                sqlBlocks.add("CREATE TABLE IF NOT EXISTS " + tableName + " (\n" + String.join(",\n", sqlColumns) + "\n);");
                continue;
            }

            errors.add(new CompileError(lineNumber, "Instruccion no reconocida. Usa CREAR BD, USAR BD o CREAR TABLA."));
            index++;
        }

        List<String> structureLines = new ArrayList<>();
        structureLines.add("Base de datos: " + (dbName.isBlank() ? "No definida" : dbName));
        structureLines.add("Cantidad de tablas: " + tables.size());

        if (tables.isEmpty()) {
            structureLines.add("No se encontraron tablas en el programa.");
        } else {
            for (TableDef table : tables) {
                structureLines.add("");
                structureLines.add("Tabla: " + table.name());
                if (table.columns().isEmpty()) {
                    structureLines.add("  - Sin columnas definidas");
                    continue;
                }

                for (ColumnDef column : table.columns()) {
                    String extra = column.constraints().isBlank() ? "" : " (" + column.constraints() + ")";
                    structureLines.add("  - " + column.name() + ": " + column.type() + extra);
                }
            }
        }

        return new CompileResponse(errors, String.join("\n\n", sqlBlocks), String.join("\n", structureLines));
    }

    private String normalizeConstraints(String rawConstraints) {
        return rawConstraints
                .replaceAll("(?i)\\bPK\\b", "PRIMARY KEY")
                .replaceAll("(?i)\\bUNICO\\b", "UNIQUE")
                .replaceAll("(?i)\\bAUTOINC\\b", "AUTO_INCREMENT")
                .replaceAll("(?i)\\bNN\\b", "NOT NULL")
                .replaceAll("\\s+", " ")
                .trim();
    }

    private record TableDef(String name, List<ColumnDef> columns) {
    }

    private record ColumnDef(String name, String type, String constraints) {
    }
}
