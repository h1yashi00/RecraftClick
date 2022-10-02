stop_docker_service() {
  set -e
  docker-compose down
}

build_image() {
  set -e
  gradle :bungee:jar
  gradle :zombiehero:jar
  gradle :bungee:jar

  docker-compose build -m 5g
}

export -f build_image
export -f stop_docker_service

# shellcheck disable=SC2046
docker rm $(docker stop $(docker ps -q))
pkill -f socat
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CONNECT:/var/run/docker.sock &
set -e

echo "stop_docker_service build_image" | xargs -P 0 -n 1 bash -c
docker-compose up --abort-on-container-exit lobby bungee redis nginx && docker-compose rm -fsv
