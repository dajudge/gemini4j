#! /bin/sh

echo "CI_PROJECT_DIR: ${CI_PROJECT_DIR}"

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -v "${CI_PROJECT_DIR}":/project \
    -v "${CI_PROJECT_DIR}/.cache/.gradle":/root/.gradle \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --workdir /project \
    builder_jdk8 \
    $@
