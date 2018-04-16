node {
 
      def  project_name = "UmlimitedBackend"
      def  build_package_name = "unlimited-0.0.1-SNAPSHOT.jar"
      def  project_git_url = "https://gitlab.com/luckygirlalisa/UmlimitedBackend.git"
    
      def  build_package = "${WORKSPACE}/target/${build_package_name}"  
      def  build_package_new_name = "${project_name}_${BUILD_ID}.jar"   
      def  all_build_package_dir = "~/jenkins_build_package/${project_name}" 
      

      // Read Config From Properties File

      dir("jenkinsfile") {

          checkout scm
      }

      def config = readProperties  file: 'jenkinsfile/config/config.properties'

      def  deploy_env = "DEV"

      def  hostnames

      switch("${deploy_env}") {

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

      // Stages

      stage('PullCode') { 
           
            checkout scm: [$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'itwye_gitlab', url: "${project_git_url}"]]]
      }
       
      stage("Compile-Test-Package") {
           
            sh "/usr/local/bin/mvn -f pom.xml clean install"

      }

      stage("SavePackage") {

            sh "mkdir -p  ${all_build_package_dir}"
            sh "cp ${build_package} ${all_build_package_dir}/${build_package_new_name}"
      }
      
      // Host deploy and check  

      stage("DEV-Deploy-Check") {

            def description = ""

            for (int n=0; n<hostnames.size(); n++) {

                def hostname = hostnames[n]
                def appurl = "http://${hostname}:${apport}"

                try {
                      sshagent(["${hostcertid}"]) {
                            sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m copy -a 'src=${build_package} dest=~/${build_package_new_name} mode=755 backup=yes'"
                            sh "/usr/local/bin/ansible -i ${hostname}, all -u ${hostuser} -m shell -a 'pkill java;/usr/bin/nohup java -jar ~/${build_package_new_name} > /tmp/${project_name}.log 2>&1 &'"
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









