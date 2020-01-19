pipeline {
    agent none
    stages {
        stage("Deploy") {
            agent any
            steps {
                echo "Deploy to ${ENVIRONMENT}"
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
            sshagent(["${hostcertid}"]) {
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m file -a 'path=~/${project_name} state=directory'"
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m copy -a 'src=${ci_build_package} dest=~/${project_name}/${package_name} mode=755 backup=yes'"
                sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m script -a 'jenkinsfiles/scripts/startupApp.sh  ${env} ~/${project_name} ${package_name}'"
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
