FROM java:openjdk-8u111-jdk-alpine

RUN apk update && apk add 'py-pip==8.1.2-r0' curl wget bash && \
    # Docker compose
    pip install 'docker-compose==1.8.0' && \
    # Docker client tools
    mkdir -p /opt && \
    wget -qO- https://download.docker.com/linux/static/stable/x86_64/docker-17.12.1-ce.tgz | tar xvz -C /opt && \
    ln -s /opt/docker/docker /usr/local/bin/docker
