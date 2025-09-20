
# Quasar Ledger (Java • Spring Boot)

Quasar Ledger é um microserviço de contabilidade em partidas dobradas (double‑entry) escrito em Java 21 + Spring Boot. 
Ele fornece endpoints REST para contas, lançamentos (journal entries) e relatórios como balancete e DRE — com persistência H2, OpenAPI e testes.

## Recursos
- Modelo double‑entry com validação (Débitos = Créditos).
- API REST com Swagger UI.
- H2 por padrão, configurável para bancos externos.
- Relatórios: Balancete e DRE por período.
- Seed automático com dados de demonstração.
- Testes JUnit + Spring Boot Test.
- Dockerfile e GitHub Actions (CI).

## Como rodar
```bash
mvn -B clean package
java -jar target/quasar-ledger-0.1.0.jar
```
Abra `http://localhost:8080/swagger-ui/index.html`.

## Endpoints (exemplos)
- POST /api/accounts
- GET /api/accounts
- GET /api/accounts/{id}/balance?from=YYYY-MM-DD&to=YYYY-MM-DD
- POST /api/entries
- GET /api/reports/trial-balance?from=YYYY-MM-DD&to=YYYY-MM-DD
- GET /api/reports/income-statement?from=YYYY-MM-DD&to=YYYY-MM-DD

## Docker
```bash
docker build -t quasar-ledger:latest .
docker run --rm -p 8080:8080 quasar-ledger:latest
```

## Licença
MIT. Veja LICENSE.
