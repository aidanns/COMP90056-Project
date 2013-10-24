#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
cd $DIR/../webapp && target/universal/stage/bin/cdr-webapp > /dev/null 2>&1 &
