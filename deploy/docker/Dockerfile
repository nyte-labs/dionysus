FROM eclipse-temurin:25-jre-alpine AS extractor
WORKDIR /workspace
COPY build/libs/dionysus-0.0.1.jar app.jar
RUN java -Djarmode=tools -jar app.jar extract --layers --launcher --destination app

FROM eclipse-temurin:25-jre-alpine
WORKDIR /application

COPY --from=extractor /workspace/app/dependencies/ ./
COPY --from=extractor /workspace/app/spring-boot-loader/ ./
COPY --from=extractor /workspace/app/snapshot-dependencies/ ./
COPY --from=extractor /workspace/app/application/ ./

EXPOSE 8080
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
