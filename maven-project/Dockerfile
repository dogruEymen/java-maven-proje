ARG BUILDER_NAME
ARG RUNNER_NAME
ARG VERSION=1.0.0


FROM ${BUILDER_NAME} AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# --- AŞAMA 2: Run (Çalıştırma) ---
FROM ${RUNNER_NAME} as runner

LABEL org.opencontainers.image.source=https://github.com/dogruEymen/java-maven-project
LABEL org.opencontainers.image.description="Java Maven Projesi"

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar


EXPOSE 4040


ENTRYPOINT ["java", "-jar", "app.jar"]