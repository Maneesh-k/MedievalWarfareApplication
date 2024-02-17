FROM amazoncorretto:17-alpine-jdk as builder

WORKDIR /app

COPY gradle gradle

COPY gradlew settings.gradle ./

COPY service/medievalwarefare ./service/medievalwarefare

COPY libs ./libs

RUN ./gradlew clean :services:medievalwarefare:build

FROM amazoncorretto:17-alpine-jdk

WORKDIR /app

COPY --from=builder app/service/medievalwarefare/build/libs/medievalwarefare.jar .

CMD ["java", "-jar", "medievalwarefare.jar"]