FROM openjdk:8-jdk

WORKDIR work

ADD https://ci.md-5.net/job/BungeeCord/lastSuccessfulBuild/artifact/bootstrap/target/BungeeCord.jar .

COPY docker/bungee/ .
RUN mkdir ./plugins

#COPY bungee/build/libs/bungee-1.0-SNAPSHOT.jar ./plugins

RUN touch start.sh
RUN echo "java -jar BungeeCord.jar" >> start.sh

CMD bash start.sh