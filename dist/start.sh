#!/bin/bash
set -e

export APP=./worker-1.0.0.jar
export MAIN_CLASS=io.inprice.scrapper.worker.Application

export JVM_ARGS='-Dlog4j.configuration=file:./conf/log4j.properties -Xms256m -Xmx512m'

DIR="$( pwd )"
source "$DIR"/setenv.sh
"$DIR"/daemon/start-daemon.sh $APP $@
