#!/bin/bash

# Install internal dependencies
cd /vagrant/models
mvn install

# Compile the streamapp
cd /vagrant/streamapp && mvn compile