FROM openjdk:17-jdk
EXPOSE 25565
WORKDIR work

ADD https://api.papermc.io/v2/projects/paper/versions/1.19.2/builds/177/downloads/paper-1.19.2-177.jar .


COPY zombiehero/build/libs ./plugins
COPY data/defaultPlugins ./plugins
COPY data/worlds/zombiehero/ ./world
COPY data/server.properties .
COPY data/spigot.yml .
COPY data/start.sh .
COPY data/ops.json .

RUN touch eula.txt
RUN echo "eula=true" >> eula.txt

CMD bash start.sh