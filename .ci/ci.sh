#! /bin/sh

echo "DOCKER_HOST: ${DOCKER_HOST}"
echo "CI_PROJECT_DIR: ${CI_PROJECT_DIR}"

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e DOCKER_HOST="${DOCKER_HOST}" \
    -v "${CI_PROJECT_DIR}":/project \
    -v "${CI_PROJECT_DIR}/.cache/.gradle":/root/.gradle \
    --workdir /project \
    -it builder_jdk8 \
    $@
