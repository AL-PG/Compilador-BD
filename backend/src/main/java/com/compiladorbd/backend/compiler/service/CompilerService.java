package com.compiladorbd.backend.compiler.service;

import com.compiladorbd.backend.compiler.antlr.LenguajeDBLexer;
import com.compiladorbd.backend.compiler.antlr.LenguajeDBParser;
import com.compiladorbd.backend.compiler.dto.CompileError;
import com.compiladorbd.backend.compiler.dto.CompileResponse;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CharStream;
import org.antlr.runtime.CommonTokenStream;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompilerService {

    public CompileResponse compile(String program) {
        if (program == null || program.isBlank()) {
            return new CompileResponse(
                    List.of(new CompileError(1, "Debes enviar un script con la gramatica LenguajeDB.")),
                    "",
                    ""
            );
        }

        try {
            LenguajeDBParser parser = createParser(program);
            invokeScript(parser);

            List<CompileError> syntaxErrors = extractSyntaxErrors(parser);
            if (!syntaxErrors.isEmpty()) {
                return new CompileResponse(syntaxErrors, "", "");
            }

            if (getSyntaxErrorsCount(parser) > 0) {
                return new CompileResponse(
                        List.of(new CompileError(1, "El script contiene errores de sintaxis LenguajeDB.")),
                        "",
                        ""
                );
            }

            String databaseName = parser.nombreBaseDatos == null ? "" : parser.nombreBaseDatos;
            List<LenguajeDBParser.Tabla> tables = parser.tablas == null ? List.of() : parser.tablas;
            return buildResponse(databaseName, tables);
        } catch (Exception exception) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo procesar la gramatica: " + safeMessage(exception.getMessage()))),
                    "",
                    ""
            );
        }
    }

    private List<CompileError> extractSyntaxErrors(LenguajeDBParser parser) {
        List<LenguajeDBParser.ErrorSintaxis> rawErrors = parser.erroresSintaxis == null
                ? List.of()
                : parser.erroresSintaxis;
        if (rawErrors.isEmpty()) {
            return List.of();
        }

        List<CompileError> parsedErrors = new ArrayList<>();
        for (LenguajeDBParser.ErrorSintaxis rawError : rawErrors) {
            int line = rawError == null ? 1 : Math.max(rawError.line, 1);
            String message = rawError == null ? "Sin detalle." : safeMessage(rawError.message);
            parsedErrors.add(new CompileError(Math.max(line, 1), safeMessage(message)));
        }

        return parsedErrors;
    }

    private LenguajeDBParser createParser(String program) {
        CharStream input = new ANTLRStringStream(program);
        LenguajeDBLexer lexer = new LenguajeDBLexer(input);
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        return new LenguajeDBParser(tokenStream);
    }

    private void invokeScript(LenguajeDBParser parser) throws Exception {
        parser.script();
    }

    private int getSyntaxErrorsCount(LenguajeDBParser parser) {
        return parser.getNumberOfSyntaxErrors();
    }

    private CompileResponse buildResponse(String databaseName, List<LenguajeDBParser.Tabla> tablas) {
        if (databaseName == null || databaseName.isBlank()) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo obtener el nombre de la base de datos.")),
                    "",
                    ""
            );
        }

        List<LenguajeDBParser.Tabla> safeTables = tablas == null ? List.of() : tablas;

        List<String> sqlBlocks = new ArrayList<>();
        sqlBlocks.add("CREATE DATABASE IF NOT EXISTS " + databaseName + ";");
        sqlBlocks.add("USE " + databaseName + ";");

        try {
            for (LenguajeDBParser.Tabla table : safeTables) {
                String tableName = table == null || table.nombre == null ? "" : table.nombre;
                List<LenguajeDBParser.Atributo> atributos = table == null || table.atributos == null
                        ? List.of()
                        : table.atributos;

                List<String> sqlColumns = new ArrayList<>();
                for (LenguajeDBParser.Atributo atributo : atributos) {
                    String nombre = atributo == null || atributo.nombre == null ? "" : atributo.nombre;
                    String tipo = atributo == null || atributo.tipo == null ? "" : atributo.tipo;
                    sqlColumns.add("  " + nombre + " " + sqlType(tipo));
                }

                sqlBlocks.add("CREATE TABLE IF NOT EXISTS " + tableName + " (\n"
                    + String.join(",\n", sqlColumns)
                    + "\n);");
            }

            List<String> structureLines = new ArrayList<>();
            structureLines.add("Base de datos: " + databaseName);
            structureLines.add("Cantidad de tablas: " + safeTables.size());

            for (LenguajeDBParser.Tabla table : safeTables) {
                String tableName = table == null || table.nombre == null ? "" : table.nombre;
                List<LenguajeDBParser.Atributo> atributos = table == null || table.atributos == null
                        ? List.of()
                        : table.atributos;

                structureLines.add("");
                structureLines.add("Tabla: " + tableName);
                for (LenguajeDBParser.Atributo atributo : atributos) {
                    String nombre = atributo == null || atributo.nombre == null ? "" : atributo.nombre;
                    String tipo = atributo == null || atributo.tipo == null ? "" : atributo.tipo;
                    structureLines.add("  - " + nombre + ": " + tipo);
                }
            }

            return new CompileResponse(List.of(), String.join("\n\n", sqlBlocks), String.join("\n", structureLines));
        } catch (Exception exception) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo leer el resultado del parser ANTLR: " + safeMessage(exception.getMessage()))),
                    "",
                    ""
            );
        }
    }

    private String sqlType(String logicalType) {
        String normalizedType = logicalType == null ? "" : logicalType.toLowerCase();
        return switch (normalizedType) {
            case "texto" -> "VARCHAR(255)";
            case "numero" -> "INT";
            case "fecha" -> "DATE";
            default -> "VARCHAR(255)";
        };
    }

    private String safeMessage(String message) {
        return message == null || message.isBlank() ? "Sin detalle." : message;
    }
}
