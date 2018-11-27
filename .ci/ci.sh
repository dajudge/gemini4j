#! /bin/sh

echo "CI_PROJECT_DIR: ${CI_PROJECT_DIR}"

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e CI_COMMIT_REF_NAME="$CI_COMMIT_REF_NAME" \
    -e BINTRAY_API_USER="$BINTRAY_API_USER" \
    -e BINTRAY_API_KEY="$BINTRAY_API_KEY" \
    -v "${CI_PROJECT_DIR}":/project \
    -v "${CI_PROJECT_DIR}/.cache/.gradle":/root/.gradle \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --workdir /project \
    builder_jdk8 \
    $@
