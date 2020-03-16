#/bin/bash

env=$1
origin_package=./target/unlimited-0.0.1-SNAPSHOT.jar
new_package_name=UnlimitedBackend.jar
project_name=UnlimitedBackend


function obtain_git_branch {
  branch=`git branch | grep "*"`
  echo ${branch/* /}
}

function create_package_name {
    current_branch=`obtain_git_branch`
    current_branch_latest_short_id=`git rev-parse --short HEAD`
    new_package_name=UnlimitedBackend-$current_branch-$current_branch_latest_short_id.jar
    echo $new_package_name
}

function deploy_package_to_host {
    env_upper_case=$(echo $env | tr '[a-z]' '[A-Z]')
    env_lower_case=$(echo $env | tr '[A-Z]' '[a-z]')

    . ./jenkinsfiles/config/config.properties

    host_name_key=${env_upper_case}_UnlimitedBackend_HostName
    host_name_value=$(eval echo '$'"$host_name_key")

    host_user=${Host_User}
    host_name_array=(${host_name_value//,/})

    mvn clean install
    create_package_name
    echo $new_package_name

    for host in ${host_name_array[*]}
    do
        echo "start to deploy $new_package_name to $host"
        scp -i ./pimsleur.pem $origin_package $host_user@$host:~/$project_name/$new_package_name
        scp -i ./pimsleur.pem ./jenkinsfiles/scripts/startupApp.sh $host_user@$host:~/$project_name/startupApp.sh
        ssh -i ./pimsleur.pem $host_user@$host "~/$project_name/startupApp.sh $env_lower_case ~/$project_name $new_package_name"
    done
}
deploy_package_to_host
