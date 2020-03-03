pipeline {
    agent none
    stages {
        stage("Compile") {
            agent any
            steps {
                echo "Compile"

                script {
                    env.GIT_BRANCH_NAME = env.GIT_BRANCH.substring(7)
                    echo "${env.GIT_BRANCH_NAME}"
                    checkout scm

                    def project_name = "UnlimitedBackend"
                    def build_package_name = "unlimited-0.0.1-SNAPSHOT.jar"

                    sh "/usr/local/bin/mvn -f pom.xml clean install"

                    def build_package = "${WORKSPACE}/target/${build_package_name}"
                    def all_build_package_dir = "~/jenkins_build_package/${project_name}"
                    def build_package_new_name = "${project_name}-${env.GIT_BRANCH_NAME}-${BUILD_ID}.jar"
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
                withSonarQubeEnv('sonarqube-host') {
                    sh '''
                    /usr/local/bin/mvn sonar:sonar 
                    '''
                }
            }
        }

        stage("Quality Gate") {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
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

