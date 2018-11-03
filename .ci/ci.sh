#! /bin/sh

echo "DOCKER_HOST: ${DOCKER_HOST}"

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e DOCKER_HOST="${DOCKER_HOST}" \
    -v "${CI_PROJECT_DIR}":/project \
    -v "${CI_PROJECT_DIR}/.cache/.gradle":/root/.gradle \
    --workdir /project \
    builder_jdk8 \
    $@
