# Backend Spring Boot

API REST para conectar el frontend React con la logica de compilacion y la creacion de base de datos.

## Requisitos

- Java 17+
- Maven 3.9+

## Ejecutar

```bash
mvn spring-boot:run
```

Antes de ejecutar por primera vez, puedes forzar la generacion del parser/lexer desde la gramatica:

```bash
mvn clean generate-sources
```

Maven genera clases Java ANTLR3 en:

- `target/generated-sources/antlr3`

Servidor por defecto: `http://localhost:8080`

## Endpoints

- `POST /api/compiler/compile`

```json
{
  "program": "crear base tienda; tabla usuarios con: id como numero, nombre como texto;"
}
```

- `POST /api/database/create`

```json
{
  "sql": "CREATE DATABASE ...;"
}
```

## Gramatica LenguajeDB (ANTLR3)

El sistema compila scripts con esta sintaxis:

```txt
script            : declaracion_base declaracion_tabla+ EOF
declaracion_base  : 'crear base' ID ';'
declaracion_tabla : 'tabla' ID 'con:' campo (',' campo)* ';'
campo             : ID 'como' tipo
tipo              : 'texto' | 'numero' | 'fecha'
```

Ejemplo completo:

```txt
crear base tienda_virtual;
tabla usuarios con: id como numero, nombre como texto, correo como texto;
tabla pedidos con: id como numero, usuario_id como numero, fecha_pedido como fecha;
```

Archivo de gramatica ANTLR3 en el repositorio:

- `src/main/antlr3/LenguajeDB.g`

El backend consume directamente las clases generadas por ANTLR3 desde `CompilerService`.

## CORS

Permitido por defecto: `http://localhost:5173`

Configurable en `application.properties`:

```properties
app.cors.allowed-origins=http://localhost:5173
```

## Conexion real a MySQL (opcional)

Si quieres que el endpoint `/api/database/create` ejecute SQL real, configura:

```properties
app.database.jdbc-url=jdbc:mysql://localhost:3306/?allowMultiQueries=true
app.database.username=root
app.database.password=tu_password
```

Si no configuras estas propiedades, el endpoint responde exitosamente para validar la integracion front-back sin ejecutar SQL.
