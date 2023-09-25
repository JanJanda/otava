FROM maven:3.9.4

WORKDIR /usr/src/otava/java-code
COPY . .

RUN mvn package

CMD [ "java", "-jar", "web_worker/target/web_worker-1.0-jar-with-dependencies.jar" ]
