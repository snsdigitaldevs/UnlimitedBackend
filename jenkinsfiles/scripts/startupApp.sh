#!/bin/bash

project_home_dir=$1
new_jar_package=$2

set -m

cd $project_home_dir

# stop old jdk application
pkill java

# delete old jar package
rm -f `ls | grep -v "${new_jar_package}" | xargs echo`

# start new jar package
java -jar ${project_home_dir}/${new_jar_package} & 
