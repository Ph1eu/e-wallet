# Stage 1: Build
FROM openjdk:17-jdk-slim AS build
WORKDIR /workspace/app

COPY mvnw .
#COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Stage 2: Run
FROM openjdk:17-jdk-slim
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Create a non-root user and switch to it
RUN addgroup --system java && adduser --system --group java
USER java

ENTRYPOINT ["java","-cp","app:app/lib/*","com.example.Application"]