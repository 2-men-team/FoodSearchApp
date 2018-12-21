#!/bin/bash

if [[ $1 == 'copy' ]]; then
    cp out/artifacts/RestaurantSearchApp_jar/RestaurantSearchApp.jar ./
    exit
fi

Lang='russian'
Dir='resources'
Command="$1"

shift

while (( "$#" >= 2 )); do
    if [[ $1 == '-L' ]]; then
        Lang="$2"
    elif [[ $1 == '-D' ]]; then
        Dir="$2"
    fi
    shift 2
done

java -Dua.kpi.restaurants.data.Config.properties=${Dir}/${Lang}.properties \
    -Djava.util.logging.config.file=resources/logging.properties \
    -jar RestaurantSearchApp.jar ${Command} "$@"
