#! /bin/sh

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e DOCKER_HOST=$DOCKER_HOST \
    -v "$CI_PROJECT_DIR":/project \
    -v $(pwd)/.cache/.gradle:/root/.gradle \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --workdir /project \
    builder_jdk8 \
    $@
