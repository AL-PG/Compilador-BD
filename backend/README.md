# Backend Spring Boot

API REST para conectar el frontend React con la logica de compilacion y la creacion de base de datos.

## Requisitos

- Java 17+
- Maven 3.9+

## Ejecutar

```bash
mvn spring-boot:run
```

Servidor por defecto: `http://localhost:8080`

## Endpoints

- `POST /api/compiler/compile`

```json
{
  "program": "CREAR BD tienda; ..."
}
```

- `POST /api/database/create`

```json
{
  "sql": "CREATE DATABASE ...;"
}
```

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
