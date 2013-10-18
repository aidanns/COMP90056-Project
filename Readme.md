# Streams Project

## Author

Aidan Nagorcka-Smith (aidann@student.unimelb.edu.au)

## Running

1. Install `vagrant` for your system from http://downloads.vagrantup.com/
3. Install `virtualbox` for your system from https://www.virtualbox.org/
4. `vagrant up` from the root of the project to build and start a virtual machine running the application.

## Overview

The application is made up of the following sub-projects:

* cdr-models: A jar built using Maven that contains the POJO representations of the Rules and Constraints. These are used both in cdr-webapp to serialize and de-serialize requests to the REST API and in the cdr-streamapp to handle the logic of matching rules against streams of Call Data Records (CDRs).

* cdr-webapp: A Play! application that serves a Backbone.js web application allowing a user to interact with a REST api (also implemented in cdr-webapp). The API allows the addition and removal of rules that will be used by cdr-streamapp.

* cdr-streamapp: A Storm stream processing application that processes a stream of CDRs and matches them against a set of rules recorded in the database of cdr-webapp. The CDRs are read from a file.