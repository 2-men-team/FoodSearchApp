#!/bin/bash

if [[ $1 == 'copy' ]]; then
    cp out/artifacts/RestaurantSearchApp_jar/RestaurantSearchApp.jar ./
    exit
fi

Lang='russian'
Mode='SERVER'

while (( "$#" >= 2 )); do
    if [[ $1 == '-L' ]]; then
        Lang="$2"
    elif [[ $1 == '-M' ]]; then
        Mode=${2^^}
    fi
    shift 2
done

java -Dua.kpi.restaurants.Config.properties=resources/${Lang}.properties -jar RestaurantSearchApp.jar --mode=${Mode} "$@"
