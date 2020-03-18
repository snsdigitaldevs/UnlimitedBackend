pipeline {
    agent none
    parameters {
        choice(name: "ENVIRONMENT", choices: "dev\nqa\nitg\nuat\nprod", description: "请选择部署环境")
        string(name: "BUILD_VERSION", defaultValue: "master-1", description: "请输入构建分支和版本")
    }
    stages {
        stage("Deploy") {
            agent any
            steps {
                echo "Deploy ${BUILD_VERSION} to ${ENVIRONMENT}"
                script {
                    def environment_upper_case = "${ENVIRONMENT}".toUpperCase()

                    def config = readProperties file: 'jenkinsfiles/config/config.properties'
                    def hostname_parameter = "${environment_upper_case}_UnlimitedBackend_HostName"
                    def hostnames = config."${hostname_parameter}".split(",")
                    deploy(hostnames, "${ENVIRONMENT}".toLowerCase())
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
    def package_name = "${project_name}-${BUILD_VERSION}.jar"

    def hostuser = config.Host_User
    def hostcertid = config.Host_Cert_ID
    def apport = config.APP_UnlimitedBackend_Port

    def ci_build_package = "~/jenkins_build_package/${project_name}/${package_name}"

    def description = ""

    for (int n = 0; n < hostnames.size(); n++) {

        def hostname = hostnames[n]
        def appurl = "http://${hostname}:${apport}"

        try {
            if ( env == "dev" ) {
                sh "sudo cp ${ci_build_package} /home/${hostuser}/${project_name}/${package_name}"
                sh "sudo cp jenkinsfiles/scripts/startupApp.sh  /home/${hostuser}/${project_name}/startupApp.sh"
                sh "sudo /home/${hostuser}/${project_name}/startupApp.sh ${env} /home/${hostuser}/${project_name} ${package_name}  > /dev/null 2>&1 & "
            } else {
                sh "scp ${ci_build_package} ${hostuser}@${hostname}:~/${project_name}/${package_name}"
                sh "scp jenkinsfiles/scripts/startupApp.sh  ${hostuser}@${hostname}:~/${project_name}/startupApp.sh"
                sh "ssh ${hostuser}@${hostname} '~/${project_name}/startupApp.sh ${env} ~/${project_name} ${package_name}'  > /dev/null 2>&1 & "
            }

            sleep 10

            timeout(time: 30, unit: 'SECONDS') {
                retry(3) {
                    def response = httpRequest url: "${appurl}", validResponseContent: 'Greetings from Spring Boot', validResponseCodes: '200'
                    println("Response Status: " + response.status)
                    println("Response Content: " + response.content)
                }
            }

            description = "${description}\n${package_name} deploy to ${ENVIRONMENT} on ${hostname.split("\\.")[0]} success!"

        }
        catch (exc) {

            description = "${description}\n${package_name} deploy to ${ENVIRONMENT} on ${hostname.split("\\.")[0]} success!"
            throw exc

        }
        finally {

            currentBuild.description = "${description}"

        }

    }

}
