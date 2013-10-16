# Docker containers to run the streams project.

FROM ubuntu:12.04

MAINTAINER Aidan Nagorcka-Smith (aidanns@gmail.com)

# Add a docker user that we'll use to run the app.
RUN groupadd docker; \
  useradd -d /home/docker -m -g docker docker

# Add all the apt repos.
RUN echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise main restricted universe multiverse" > /etc/apt/sources.list; \
  echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-updates main restricted universe multiverse" >> /etc/apt/sources.list; \
  echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-backports main restricted universe multiverse" >> /etc/apt/sources.list; \
  echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-security main restricted universe multiverse" >> /etc/apt/sources.list; \
  apt-get update

# Install the deb proxy client to autodiscover local deb proxies.
RUN apt-get install -y squid-deb-proxy-client language-pack-en

# Download the Play! framework
RUN apt-get install -y wget unzip; \
  cd /home/docker; \
  wget http://downloads.typesafe.com/play/2.2.0/play-2.2.0.zip; \
  mkdir -p /usr/local/share; \
  unzip play-2.2.0.zip -d /usr/local/share; \
  rm /home/docker/play-2.2.0.zip; \
  chmod -R 755 /usr/local/share/play-2.2.0

# Fix permissions on the docker home directory.
RUN chown -R docker:docker /home/docker; chmod -R 700 /home/docker

# Install some basic and useful tools.
RUN apt-get install -y vim man

USER root
#EXPOSE 
#ENTRYPOINT