#!/bin/bash

# Install internal dependencies
cd /vagrant/models
mvn install

# Compile the webapp
cd /vagrant/webapp
/usr/local/share/play-2.2.0/play clean compile stage

# Compile the streamapp
cd /vagrant/streamapp && mvn compile