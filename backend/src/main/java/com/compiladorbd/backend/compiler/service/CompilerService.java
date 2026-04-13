package com.compiladorbd.backend.compiler.service;

import com.compiladorbd.backend.compiler.dto.CompileError;
import com.compiladorbd.backend.compiler.dto.CompileResponse;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
            Object parser = createParser(program);
            invokeScript(parser);

            if (getSyntaxErrorsCount(parser) > 0) {
                return new CompileResponse(
                        List.of(new CompileError(1, "El script contiene errores de sintaxis LenguajeDB.")),
                        "",
                        ""
                );
            }

            String databaseName = getStringField(parser, "nombreBaseDatos");
            List<?> tables = getListField(parser, "tablas");
            return buildResponse(databaseName, tables);
        } catch (InvocationTargetException invocationTargetException) {
            Throwable rootCause = invocationTargetException.getTargetException();
            String message = rootCause == null ? invocationTargetException.getMessage() : rootCause.getMessage();
            return new CompileResponse(
                    List.of(new CompileError(1, "Error de sintaxis: " + safeMessage(message))),
                    "",
                    ""
            );
        } catch (Exception exception) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo procesar la gramatica: " + safeMessage(exception.getMessage()))),
                    "",
                    ""
            );
        }
    }

    private Object createParser(String program) throws Exception {
        Class<?> antlrInputClass = Class.forName("org.antlr.runtime.ANTLRStringStream");
        Object input = antlrInputClass.getConstructor(String.class).newInstance(program);

        Class<?> charStreamClass = Class.forName("org.antlr.runtime.CharStream");
        Class<?> lexerClass = Class.forName("com.compiladorbd.backend.compiler.antlr.LenguajeDBLexer");
        Object lexer = lexerClass.getConstructor(charStreamClass).newInstance(input);

        Class<?> tokenSourceClass = Class.forName("org.antlr.runtime.TokenSource");
        Class<?> commonTokenStreamClass = Class.forName("org.antlr.runtime.CommonTokenStream");
        Object tokenStream = commonTokenStreamClass.getConstructor(tokenSourceClass).newInstance(lexer);

        Class<?> parserClass = Class.forName("com.compiladorbd.backend.compiler.antlr.LenguajeDBParser");
        Class<?> tokenStreamClass = Class.forName("org.antlr.runtime.TokenStream");
        return parserClass.getConstructor(tokenStreamClass).newInstance(tokenStream);
    }

    private void invokeScript(Object parser) throws Exception {
        Method scriptMethod = parser.getClass().getMethod("script");
        scriptMethod.invoke(parser);
    }

    private int getSyntaxErrorsCount(Object parser) throws Exception {
        Class<?> baseRecognizerClass = Class.forName("org.antlr.runtime.BaseRecognizer");
        Method getSyntaxErrorsMethod = baseRecognizerClass.getMethod("getNumberOfSyntaxErrors");
        Object value = getSyntaxErrorsMethod.invoke(parser);
        return value instanceof Integer ? (Integer) value : 0;
    }

    private String getStringField(Object parser, String fieldName) throws Exception {
        Field field = parser.getClass().getField(fieldName);
        Object value = field.get(parser);
        return value == null ? "" : value.toString();
    }

    private List<?> getListField(Object parser, String fieldName) throws Exception {
        Field field = parser.getClass().getField(fieldName);
        Object value = field.get(parser);
        return value instanceof List<?> ? (List<?>) value : List.of();
    }

    private CompileResponse buildResponse(String databaseName, List<?> tablas) {
        if (databaseName == null || databaseName.isBlank()) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo obtener el nombre de la base de datos.")),
                    "",
                    ""
            );
        }

        List<?> safeTables = tablas == null ? List.of() : tablas;

        List<String> sqlBlocks = new ArrayList<>();
        sqlBlocks.add("CREATE DATABASE IF NOT EXISTS " + databaseName + ";");
        sqlBlocks.add("USE " + databaseName + ";");

        try {
            for (Object table : safeTables) {
                String tableName = readPublicStringField(table, "nombre");
                List<?> atributos = readPublicListField(table, "atributos");

                List<String> sqlColumns = new ArrayList<>();
                for (Object atributo : atributos) {
                    String nombre = readPublicStringField(atributo, "nombre");
                    String tipo = readPublicStringField(atributo, "tipo");
                    sqlColumns.add("  " + nombre + " " + sqlType(tipo));
                }

                sqlBlocks.add("CREATE TABLE IF NOT EXISTS " + tableName + " (\\n"
                        + String.join(",\\n", sqlColumns)
                        + "\\n);");
            }

            List<String> structureLines = new ArrayList<>();
            structureLines.add("Base de datos: " + databaseName);
            structureLines.add("Cantidad de tablas: " + safeTables.size());

            for (Object table : safeTables) {
                String tableName = readPublicStringField(table, "nombre");
                List<?> atributos = readPublicListField(table, "atributos");

                structureLines.add("");
                structureLines.add("Tabla: " + tableName);
                for (Object atributo : atributos) {
                    String nombre = readPublicStringField(atributo, "nombre");
                    String tipo = readPublicStringField(atributo, "tipo");
                    structureLines.add("  - " + nombre + ": " + tipo);
                }
            }

            return new CompileResponse(List.of(), String.join("\n\n", sqlBlocks), String.join("\n", structureLines));
        } catch (Exception exception) {
            return new CompileResponse(
                    List.of(new CompileError(1, "No se pudo leer el resultado del parser ANTLR3: " + safeMessage(exception.getMessage()))),
                    "",
                    ""
            );
        }
    }

    private String readPublicStringField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getField(fieldName);
        Object value = field.get(target);
        return value == null ? "" : value.toString();
    }

    private List<?> readPublicListField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getField(fieldName);
        Object value = field.get(target);
        return value instanceof List<?> ? (List<?>) value : List.of();
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
