#! /bin/sh

CI_PROJECT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null && pwd )"

echo "Running gemini4j build in $CI_PROJECT_DIR..."
echo "DOCKER_HOST: $DOCKER_HOST"

if [ -z "$var" ]; then
    DOCKER_HOST_VAR=DISABLED_DOCKER_HOST
else
    DOCKER_HOST_VAR=DOCKER_HOST
fi

docker build --pull -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e $DOCKER_HOST_VAR=$DOCKER_HOST \
    -v "$CI_PROJECT_DIR":/project \
    -v $(pwd)/.cache/.gradle:/root/.gradle \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --workdir /project \
    builder_jdk8 \
    $@
