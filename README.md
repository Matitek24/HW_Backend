# Headwear Designer - Backend API

Stabilna i bezpieczna warstwa serwerowa dla konfiguratora produktów. System zarządza logiką biznesową, bezpieczną autoryzacją użytkowników oraz automatyzacją procesów produkcyjnych.

---

### Stack Technologiczny

| Technologia | Zastosowanie                                |
| :--- |:--------------------------------------------|
| **Java 17 / Spring Boot 3** | Główny framework aplikacji                  |
| **Spring Security + JWT** | Bezstanowa autoryzacja i zabezpieczenie API |
| **PostgreSQL** | Relacyjna baza danych                       |
| **Liquibase** | Migracja schematu bazy danych               |
| **Hibernate / JPA** | Mapowanie Obiektowe                         |
| **Maven** | Zarządzanie zależnościami                   |

---

### Kluczowe Funkcjonalności

* **JWT Security:** Logowanie z podziałem na role uzytkownik/administrator
* **JSON Data Persistence:** Zapisywanie obiektów json w bazie danych Postgres.
* **Email Service:** Integracja z serwerem SMTP do obsługi haseł i powiadomień oraz przekazywanie dynamicznych linków do klientów.
* **Admin Dashboard API:** Rozbudowane endpointy do zarządzania statusem produkcji i filtrowania zamówień.

---

### Główne Endpointy API

#### Autoryzacja (`/api/auth`)
- `POST /login` – Logowanie i generowanie tokena JWT.
- `POST /register` – Rejestracja nowego konta.

#### Projekty (`/api/projects`)
- `POST /` – Zapisanie nowej konfiguracji czapki.
- `GET /{id}` – Pobranie detali projektu.

#### Administracja (`/api/admin`)
- `GET /projects` – Lista wszystkich zamówień (tylko dla Admina).
- `PATCH /projects/{id}` – Aktualizacja statusu zamówienia (np. "W produkcji").

---

### Struktura Projektu

Aplikacja została zaprojektowana zgodnie z zasadami programowania Obiektowego oraz struktury w Java Spring Boot:
- **Controller** – Obsługa Api
- **Service** – Logika
- **Repository** – Klasy zapewniające bezpieczny dostęp do danych
- **Security** – Konfiguracja filtrów JWT i Spring Security.
- **Resources** – Konfiguracja aplikacji głównie Liquibase.

---
