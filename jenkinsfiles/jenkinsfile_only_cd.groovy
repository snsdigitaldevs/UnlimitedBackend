pipeline {
    agent none
    stages {
        stage("Deploy to ${ENVIRONMENT}") {
            agent any
            steps {
                echo "Deploy to ${ENVIRONMENT}"
                script {
                    def env_lower_case = ${ENVIRONMENT}.toLowerCase()
                    def env_upper_case = ${ENVIRONMENT}.toUpperCase()

                    def config = readProperties file: 'jenkinsfiles/config/config.properties'
                    def hostname_parameter = ${env_upper_case}_UnlimitedBackend_HostName
                    def hostnames = config.${hostname_parameter}.split(",")
                    deploy(hostnames, ${env_lower_case})
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

    def dpkg = "${project_name}_${BUILD_VERSION}.jar"

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

            sleep 10

            timeout(time: 30, unit: 'SECONDS') {
                retry(3) {
                    def response = httpRequest url: "${appurl}", validResponseContent: 'Greetings from Spring Boot', validResponseCodes: '200'
                    println("Response Status: " + response.status)
                    println("Response Content: " + response.content)
                }
            }

            description = "${description}\n${project_name}_${BUILD_VERSION}.jar deploy to ${ENVIRONMENT} on ${hostname.split("\\.")[0]} success!"

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

