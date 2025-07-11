#сборка
FROM maven:3-openjdk-17  as builder
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
ADD ./pom.xml $PROJECT_DIR
#RUN mvn -B clean install -DskipTests -Dcheckstyle.skip -Dasciidoctor.skip
RUN mvn dependency:resolve
ADD ./src/ $PROJECT_DIR/src
RUN mvn install


#запуск
FROM openjdk:17-jdk
ENV PROJECT_DIR=/opt/project
RUN mkdir -p $PROJECT_DIR
WORKDIR $PROJECT_DIR
COPY --from=builder $PROJECT_DIR/target/library* $PROJECT_DIR/
EXPOSE 8080
CMD ["java", "-jar", "/opt/project/library-0.0.1-SNAPSHOT.jar"]