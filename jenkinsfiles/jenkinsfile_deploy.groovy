
def  retResult
def  userInput
def  project_name = "UmlimitedBackend"
def  all_build_package_dir = "~/jenkins_build_package/${project_name}"

node {

    checkout scm
    retResult = sh([returnStdout: true, script: "python jenkinsfiles/scripts/getPkgsName.py ${all_build_package_dir}"])

}

try {
    stage("Select Parameters") {

         userInput = input(
         message: 'Please select the deployment parameters!', ok: 'execute', 
         parameters: [choice(choices: "${retResult}", description: 'Please choose to deploy which package!', name: 'dpkg'),choice(choices: 'DEV\nQA\nUAT\nPROD', description: 'Please choose to deploy which environment!', name: 'denv')], 
         submitter: 'admin', submitterParameter: 'execUser'
         )

    }
} catch(err) {
     return
}

node {

      currentBuild.displayName = "${userInput['dpkg']}-${userInput['denv']}"

      userInput.each {
          key,value -> env[key] = value
      }

      stage("Deploy-Check") {

              def config = readProperties  file: 'jenkinsfiles/config/config.properties'

              def  hostnames

              def denv = this.env.denv
              def dpkg = this.env.dpkg

              switch("${denv}") {

                case "CI":
                    hostnames = config.CI_UnlimitedBackend_HostName.split(",")
                    break

                case "DEV":
                    hostnames = config.DEV_UnlimitedBackend_HostName.split(",")
                    break

                case "QA":
                    hostnames = config.QA_UnlimitedBackend_HostName.split(",")
                    break

                case "UAT":
                    hostnames = config.UAT_UnlimitedBackend_HostName.split(",")
                    break

                case "PROD":
                    hostnames = config.PROD_UnlimitedBackend_HostName.split(",")
                    break
                
              }


              def  hostuser = config.Host_User
              def  hostcertid = config.Host_Cert_ID
              def  apport = config.APP_UnlimitedBackend_Port

              def  ci_build_package = "${all_build_package_dir}/${dpkg}"

              def description = ""

              for (int n=0; n<hostnames.size(); n++) {

                  def hostname = hostnames[n]
                  def appurl = "http://${hostname}:${apport}"

                  try {
                        sshagent(["${hostcertid}"]) {
                              sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m file -a 'path=~/${project_name} state=directory'"
                              sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m copy -a 'src=${ci_build_package} dest=~/${project_name}/${dpkg} mode=755 backup=yes'"
                              sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m script -a 'jenkinsfiles/scripts/startupApp.sh ~/${project_name} ${dpkg}'"
                        }

                        sleep 5

                        timeout(time:3,unit:'SECONDS') {
                              retry(3) {
                                def response = httpRequest url:"${appurl}",validResponseContent: 'Greetings from Spring Boot',validResponseCodes: '200'
                                println("Response Status: "+response.status)
                                println("Response Content: "+response.content)
                              }  
                        }

                        description = "${description}\n${hostname.split("\\.")[0]} deploy success!"
                  } 
                  catch(exc) { 

                    description = "${description}\n${hostname.split("\\.")[0]} deploy failure!"
                    throw exc
                    
                  }
                  finally {
                    
                    currentBuild.description = "${description}"

                  } 
            
              }


        }

}










