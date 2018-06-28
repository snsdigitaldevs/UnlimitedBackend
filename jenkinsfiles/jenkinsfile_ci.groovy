pipeline {
    agent none
    stages {
        stage("Compile") {
            agent any
            steps {
                echo "Compile"

                script {
                    checkout scm

                    def project_name = "UnlimitedBackend"
                    def build_package_name = "unlimited-0.0.1-SNAPSHOT.jar"

                    sh "/usr/local/bin/mvn -f pom.xml clean install"

                    def build_package = "${WORKSPACE}/target/${build_package_name}"
                    def all_build_package_dir = "~/jenkins_build_package/${project_name}"
                    def build_package_new_name = "${project_name}_${BUILD_ID}.jar"

                    sh "mkdir -p  ${all_build_package_dir}"
                    sh([returnStdout: true, script: "python jenkinsfiles/scripts/savePkgLocal.py ${build_package}  ${all_build_package_dir} ${build_package_new_name}"])
                }
            }
        }

        stage("Test") {
            agent any
            steps {
                echo "Test"
//                sh "mvn clean install"
            }
        }

        stage("Deploy to Dev") {
            agent any
            steps {
                echo "Deploy to Dev"
                script {
                    def config = readProperties file: 'jenkinsfiles/config/config.properties'
                    def hostnames = config.DEV_UnlimitedBackend_HostName.split(",")
                    deploy(hostnames, "dev")
                }
            }
        }

        stage("Deploy to QA") {
            steps {
                echo "Deploy to QA"
                script {
                    try {
                        timeout(time: 30, unit: 'MINUTES') {
                            input message: 'build QA version?'
                        }

                        node {
                            def config = readProperties file: 'jenkinsfiles/config/config.properties'
                            def hostnames = config.QA_UnlimitedBackend_HostName.split(",")
                            deploy(hostnames, "qa")
                        }
                    } catch (err) {
                        currentBuild.result = 'SUCCESS'
                        return true
                    }
                }
            }
        }

        stage("Deploy to UAT") {
            steps {
                script {
                    try {
                        echo "Deploy to UAT"
                        timeout(time: 30, unit: 'MINUTES') {
                            input message: 'build UAT version?'
                        }

                        node {
                            def config = readProperties file: 'jenkinsfiles/config/config.properties'
                            def hostnames = config.UAT_UnlimitedBackend_HostName.split(",")
                            deploy(hostnames, "uat")
                        }
                    } catch (err) {
                        currentBuild.result = 'SUCCESS'
                        return true
                    }
                }

            }
        }

        stage("Deploy to PROD") {
            steps {
                script {
                    try {
                        echo "Deploy to PROD"
                        timeout(time: 30, unit: 'MINUTES') {
                            input message: 'build PROD version?'
                        }

                        node {
                            def config = readProperties file: 'jenkinsfiles/config/config.properties'
                            def hostnames = config.PROD_UnlimitedBackend_HostName.split(",")
                            deploy(hostnames, "prod")
                        }
                    } catch (err) {
                        currentBuild.result = 'SUCCESS'
                        return true
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

def deploy(hostnames, env) {

    def project_name = "UnlimitedBackend"
    def config = readProperties file: 'jenkinsfiles/config/config.properties'

    def all_build_package_dir = "~/jenkins_build_package/${project_name}"

    def dpkg = "${project_name}_${BUILD_ID}.jar"

    def hostuser = config.Host_User
    def hostcertid = config.Host_Cert_ID
    def apport = config.APP_UnlimitedBackend_Port

    def ci_build_package = "${all_build_package_dir}/${dpkg}"

    def description = ""

    for (int n = 0; n < hostnames.size(); n++) {

        def hostname = hostnames[n]
        def appurl = "http://${hostname}:${apport}"

        try {
            sshagent(["${hostcertid}"]) {
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m file -a 'path=~/${project_name} state=directory'"
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m copy -a 'src=${ci_build_package} dest=~/${project_name}/${dpkg} mode=755 backup=yes'"
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m script -a 'jenkinsfiles/scripts/startupApp.sh  ${env} ~/${project_name} ${dpkg}'"
            }

            sleep 5

            timeout(time: 30, unit: 'SECONDS') {
                retry(3) {
                    def response = httpRequest url: "${appurl}", validResponseContent: 'Greetings from Spring Boot', validResponseCodes: '200'
                    println("Response Status: " + response.status)
                    println("Response Content: " + response.content)
                }
            }

            description = "${description}\n${hostname.split("\\.")[0]} deploy success!"
        }
        catch (exc) {

            description = "${description}\n${hostname.split("\\.")[0]} deploy failure!"
            throw exc

        }
        finally {

            currentBuild.description = "${description}"

        }

    }

}

