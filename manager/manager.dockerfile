FROM maven:latest
WORKDIR /app

COPY . /app

RUN mvn clean install -DskipTests

EXPOSE ${SERVER_PORT}

CMD ["mvn", "spring-boot:run"]
