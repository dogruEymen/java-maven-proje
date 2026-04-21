# --- AŞAMA 1: Build (İnşa) ---
# Java 21 ve Maven içeren bir imaj seçiyoruz.
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Konteynır içinde çalışacağımız dizini belirliyoruz.
WORKDIR /app

# Önce pom.xml'i kopyalayıp bağımlılıkları indiriyoruz (Cache mekanizması için).
COPY pom.xml .
RUN mvn dependency:go-offline

# Kaynak kodları kopyalayıp uygulamayı paketliyoruz.
COPY src ./src
RUN mvn clean package -DskipTests

# --- AŞAMA 2: Run (Çalıştırma) ---
# Sadece JRE içeren hafif bir imaj kullanıyoruz (Güvenlik ve boyut için).
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Build aşamasında oluşan JAR dosyasını buraya kopyalıyoruz.
COPY --from=build /app/target/*.jar app.jar

# Uygulamanın çalışacağı portu belirtiyoruz.
EXPOSE 8080

# Uygulamayı başlatan komut.
ENTRYPOINT ["java", "-jar", "app.jar"]