pipeline {
    agent none
    stages {
        stage("Compile") {
            agent any
            steps {
                echo "Compile"

                script {
                    env.GIT_BRANCH = sh(returnStdout: true, script: 'git symbolic-ref --short -q HEAD').trim()
                    env.GIT_REVISION = sh(returnStdout: true, script: 'git rev-parse --short HEAD').trim()
                    echo "${GIT_BRANCH}-${env.GIT_REVISION}"
                    checkout scm

                    def project_name = "UnlimitedBackend"
                    def build_package_name = "unlimited-0.0.1-SNAPSHOT.jar"

                    sh "/usr/local/bin/mvn -f pom.xml clean install"

                    def build_package = "${WORKSPACE}/target/${build_package_name}"
                    def all_build_package_dir = "~/jenkins_build_package/${project_name}"
                    def build_package_new_name = "${project_name}-${env.GIT_BRANCH}.${env.GIT_REVISION}-${BUILD_ID}.jar"
                    echo "${build_package_new_name}"

                    sh "mkdir -p  ${all_build_package_dir}"
                    sh([returnStdout: true, script: "python jenkinsfiles/scripts/savePkgLocal.py ${build_package}  ${all_build_package_dir} ${build_package_new_name}"])
                    currentBuild.description = "${build_package_new_name} build success!"
                }
            }
        }
    }
    post {
        aborted {
            script {
                currentBuild.result = 'SUCCESS'
            }
        }
    }
}

