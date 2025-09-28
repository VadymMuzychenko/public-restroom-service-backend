## Running the Project

### Running with Docker

Start services:

```bash
docker-compose up -d
```

Access:
- API: [http://localhost:8080](http://localhost:8080)
- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

### Running without Docker

1. Set up MySQL manually.
2. Run the application via your IDE or:

```bash
./mvnw spring-boot:run
```
