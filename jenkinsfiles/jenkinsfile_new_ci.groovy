pipeline {
    agent none
    triggers {
        pollSCM('H/5 * * * *')
    }
    stages {
        stage("Compile") {
            agent any
            steps {
                echo "Compile"

                script {
                    echo "${env.BRANCH_NAME}"
                    checkout scm

                    def project_name = "UnlimitedBackend"
                    def build_package_name = "unlimited-0.0.1-SNAPSHOT.jar"

                    sh "mvn -f pom.xml clean install"

                    def build_package = "${WORKSPACE}/target/${build_package_name}"
                    def all_build_package_dir = "~/jenkins_build_package/${project_name}"
                    def build_package_new_name = "${project_name}-${env.BRANCH_NAME}-${BUILD_ID}.jar"
                    echo "${build_package_new_name}"

                    sh "mkdir -p  ${all_build_package_dir}"
                    sh "cp ${build_package} ${all_build_package_dir}/${build_package_new_name}"
                    sh([returnStdout: true, script: "python jenkinsfiles/scripts/delHistoryPkg.py ${all_build_package_dir}"])
                    currentBuild.description = "${build_package_new_name} build success!"
                }
            }
        }
        stage('Sonar-Scan'){
            agent any
            steps{
                script {
                    withSonarQubeEnv('sonarqube-host') {
                        def project_key="UnlimitedBackend_${env.BRANCH_NAME}"
                        echo "${project_key}"
                        sh "mvn sonar:sonar -Dsonar.projectKey=${project_key} -Dsonar.projectName=${project_key}"
                    }
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

