#!/bin/bash

if [[ $1 == 'copy' ]]
then
  cp out/artifacts/RestaurantSearchApp_jar/RestaurantSearchApp.jar ./
  shift
fi

if [[ -z "$1" ]]
then
  Mode='SERVER'
else
  Mode=${1^^}
fi

if [[ -z "$2" ]]
then
  Lang='russian'
else
  Lang="$2"
fi

java -Dua.kpi.restaurants.Config.properties=resources/${Lang}.properties -jar RestaurantSearchApp.jar --mode=${Mode}
