#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR/../streamapp && mvn compile exec:java -Dexec.classpathScope=compile -Dexec.mainClass=com.aidanns.streams.project.Project