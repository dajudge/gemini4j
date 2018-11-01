#! /bin/sh

echo "Running gemini4j build in $CI_PROJECT_DIR..."
echo "DOCKER_HOST: $DOCKER_HOST"

if [ -z "$var" ]; then
    DOCKER_HOST_VAR=DISABLED_DOCKER_HOST
else
    DOCKER_HOST_VAR=DOCKER_HOST
fi

DIND_CACHE=$CI_PROJECT_DIR/.cache/.dind
mkdir -p $DIND_CACHE
echo "Docker image cache: $DIND_CACHE"

for filename in $DIND_CACHE/*.tar; do
    [ -e "$filename" ] || continue
    echo "Loading cached docker image '$filename'..."
    docker load -i "$filename"
done

docker build -t builder_jdk8 builder/jdk8
docker run --rm \
    --net host \
    -e $DOCKER_HOST_VAR=$DOCKER_HOST \
    -v "$CI_PROJECT_DIR":/project \
    -v $(pwd)/.cache/.gradle:/root/.gradle \
    -v /var/run/docker.sock:/var/run/docker.sock \
    --workdir /project \
    builder_jdk8 \
    $@

docker images
for i in $(cat $CI_PROJECT_DIR/.ci/dind-cache.config); do
    escaped=`echo "$i" | sed -r 's/\\//___/g'`
    filename="$DIND_CACHE/${escaped}.tar"
    [ -e "$filename" ] && echo "Cached image $filename already exists." && continue
    echo "Caching image $i..."
    docker save -o "$filename" "${i}"
done
