FROM eclipse-temurin:25-jdk-alpine AS build

WORKDIR /app

COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY src src
RUN ./mvnw package -DskipTests -B

RUN jar xf target/*.jar

RUN jdeps \
    --ignore-missing-deps \
    -q --recursive \
    --multi-release 25 \
    --print-module-deps \
    --class-path 'BOOT-INF/lib/*' \
    target/*.jar > deps.info

RUN jlink \
    --add-modules $(cat deps.info),jdk.crypto.ec \
    --strip-java-debug-attributes \
    --no-header-files \
    --no-man-pages \
    --output /jre-minimalist

FROM alpine:3.21

ENV JAVA_HOME=/opt/java/jre-minimalist
ENV PATH=$JAVA_HOME/bin:$PATH

COPY --from=build /jre-minimalist $JAVA_HOME

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
