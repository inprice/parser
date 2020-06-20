#!/bin/bash
set -e

APP=./worker-1.0.0.jar

DIR="$( pwd )"
source "$DIR"/setenv.sh
"$DIR"/daemon/start-daemon.sh $APP $@