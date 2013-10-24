#!/bin/bash

# Make sure we've got access to all the precise repos.
echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise main restricted universe multiverse" > /etc/apt/sources.list
echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-updates main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-backports main restricted universe multiverse" >> /etc/apt/sources.list
echo "deb mirror://mirrors.ubuntu.com/mirrors.txt precise-security main restricted universe multiverse" >> /etc/apt/sources.list
apt-get update

# Install the deb proxy client to autodiscover local deb proxies.
apt-get install -y squid-deb-proxy-client

# Install the english language pack to stop the warning.
apt-get install -y language-pack-en

# Install Maven
apt-get install -y maven

# Install Java JRE 7
apt-get install -y openjdk-7-jdk
update-java-alternatives -s java-1.7.0-openjdk-amd64

# Add the vagrant use to the staff group, so it can write to the play folder while it's in /usr/local/share.
usermod -G staff vagrant

# Download the Play! framework and store it in /usr/local/share.
apt-get install -y wget unzip
wget http://downloads.typesafe.com/play/2.2.0/play-2.2.0.zip
mkdir -p /usr/local/share
unzip play-2.2.0.zip -d /usr/local/share
rm play-2.2.0.zip
chown -R root:staff /usr/local/share/play-2.2.0
chmod -R 775 /usr/local/share/play-2.2.0

# Install some basic useful stuff.
apt-get install -y vim man