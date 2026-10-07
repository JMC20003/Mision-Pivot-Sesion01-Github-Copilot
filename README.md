# Tarjetas API

API REST para consultar y administrar tarjetas. Requiere Java 17 y Maven. Los datos se guardan en memoria y se restablecen al reiniciar la aplicación.

## Levantar el proyecto

Desde la carpeta raíz del proyecto, ejecuta:

```bash
mvn spring-boot:run
```

La API estará disponible en `http://localhost:8080`. También puedes consultar y probar los endpoints en [Swagger UI](http://localhost:8080/swagger-ui.html).

## Endpoints

Todos los endpoints usan la ruta base `/api/v1/tarjetas`.

| Método | Ruta | Descripción | Respuestas |
|---|---|---|---|
| GET | `/api/v1/tarjetas` | Lista todas las tarjetas. | 200 |
| GET | `/api/v1/tarjetas/{numero}` | Busca una tarjeta por número. | 200, 404 |
| GET | `/api/v1/tarjetas/tipo/{tipo}` | Lista tarjetas por tipo (`CREDITO` o `DEBITO`). | 200, 400 |
| POST | `/api/v1/tarjetas` | Registra una tarjeta; requiere número de 16 caracteres, titular y tipo válido. Las tarjetas de crédito requieren límite mayor que 0. | 201, 400, 409 |
| PUT | `/api/v1/tarjetas/{numero}/bloquear` | Bloquea una tarjeta existente. | 200, 404 |
