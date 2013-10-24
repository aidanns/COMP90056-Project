# Streams Project

## Author

Aidan Nagorcka-Smith (aidann@student.unimelb.edu.au)

## Running the whole project

1. Install `vagrant` for your system from http://downloads.vagrantup.com/
2. Install `virtualbox` for your system from https://www.virtualbox.org/
3. `$ vagrant up` from the root of the project to build and start a virtual machine running the applications.
4. `$ vagrant ssh -c "/vagrant/bin/start_webapp.sh"` to start the webapp.
5. `$ vagrant ssh -c "/vagrant/bin/start_streamapp.sh"` to start the streamapp.

You should then be able to interact with the web-application on `http://192.168.33.10:9000`.

## Overview

The application is made up of the following sub-projects:

### cdr-models:

A jar built using Maven that contains the POJO representations of the Rules and Constraints. These are used both in cdr-webapp to serialize and de-serialize requests to the REST API and in the cdr-streamapp to handle the logic of matching rules against streams of Call Data Records (CDRs).

The jar built from `cdr-models` is a dependecy for both `cdr-webapp` and `cdr-streamapp`. To install it to your local maven repository:

    $ cd models && mvn install

### cdr-webapp

A Play! framework application that serves both a Backbone.js web application and a REST api. 

The Backbone web application allows for CRUD operations on rules from a web-base GUI.

The API allows for CRUD operations on JSON representations of `Rule` objects (Java representations defined in `cdr-models`). The current set of `Rule`s are saved in an in-memory database and wiped each time the application is restarted.

To run the Play! application in debug mode:

    $ cd webapp && play run

To run the Play! application in production mode:

    $ cd webapp && play clean compile stage && target/universal/stage/bin/cdr-webapp

### cdr-streamapp

A Storm stream processing application that processes a stream of CDRs and matches them against a set of rules recorded in the database of cdr-webapp. The CDRs are read from a file.

To run the Storm application:

    $ cd streamapp && mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=com.aidanns.streams.project.Project

#### Configuration

The rate of CDR generation by the spout may be limited by setting the `spout.cdr.rate.tuples_per_second` property in `conf/project.properties`.