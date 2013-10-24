#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR/../webapp 
/usr/local/share/play-2.2.0/play clean compile stage
target/universal/stage/bin/cdr-webapp &
