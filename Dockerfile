# ETAP 1: Budowanie (Maven + Java 21)
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Kopiowanie plików konfiguracyjnych Mavena
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Nadanie uprawnień do pliku mvnw (ważne na Linux/Mac)
RUN chmod +x mvnw
# Pobranie zależności (to przyspieszy kolejne budowania)
RUN ./mvnw dependency:go-offline

# Kopiowanie kodu źródłowego
COPY src ./src

# Budowanie pliku .jar (pomijamy testy dla szybkości na produkcji)
RUN ./mvnw clean package -DskipTests

# ETAP 2: Uruchamianie (Lekka Java 21 JRE)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Kopiujemy tylko zbudowany plik .jar z pierwszego etapu
COPY --from=builder /app/target/*.jar app.jar

# Wystawiamy port 8080
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]