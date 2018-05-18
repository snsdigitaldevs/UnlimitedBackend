#!/bin/bash

env = $1
project_home_dir=$2
new_jar_package=$3

set -m

cd $project_home_dir

# stop old jdk application
pkill java

# delete old jar package
rm -f `ls | grep -v "${new_jar_package}" | grep jar | xargs echo`

# start new jar package
java -jar -Dspring.profiles.active=${env} ${project_home_dir}/${new_jar_package} &
