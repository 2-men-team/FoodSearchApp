#!/bin/bash

LoggingFolder='log'

if [[ ! -d "$LoggingFolder" ]]; then
    mkdir ${LoggingFolder}
fi

Lang='russian'
Dir='resources'
Command="$1"

shift

for (( i=1; i<="$#"; i++ )); do
    if [[ "${!i}" == '-L' ]]; then
        j=$((i+1))
        Lang="${!j}"
        i=$((i-1))
        shift 2
    elif [[ "${!i}" == '-D' ]]; then
        j=$((i+1))
        Dir="${!j}"
        i=$((i-1))
        shift 2
    fi
done

java -Dua.kpi.restaurants.data.Config.properties=${Dir}/${Lang}.properties \
    -Djava.util.logging.config.file=resources/logging.properties \
    -jar bin/RestaurantSearchApp.jar ${Command} "$@"
