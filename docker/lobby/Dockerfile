FROM openjdk:17-jdk
EXPOSE 25565
WORKDIR work

ADD https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/177/downloads/paper-1.19.2-177.jar .

COPY docker/lobby/ .
COPY lobby/build/libs ./plugins
COPY data/worlds/lobby/ ./world
COPY data/server.properties .
COPY data/spigot.yml .

RUN touch eula.txt
RUN echo "eula=true" >> eula.txt

RUN touch start.sh
RUN echo "java -jar paper-1.19.2-177.jar" >> start.sh

CMD bash start.sh