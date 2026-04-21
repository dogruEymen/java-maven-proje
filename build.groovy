node {

    String PROJECT_ID = params.PROJECT_ID
    println("PROJECT_ID: $PROJECT_ID")


    String BUILD_STAGE = "Build"
    String VERSION_STAGE = "Version"
    String mavenPath = tool 'maven_3.9'


    String javaCodePath = './maven-project'
    String projectsFilePath = './projects.yml'

    def projects = readYaml(file: projectsFilePath)['projects']
    def project = projects[PROJECT_ID]
    String projectName = project['name']
    String projectRepoUrl = project['codeRepo']{'url'}
    String projectRepoCredentialsId = project['codeRepo']['credentialsId']


    try {

        deleteDir()

        stage ('Get Code') {

            checkout scm

        }

        // echo 'Code is successfully obtained.'

        

        echo 'Build Stage is starting...'
        /*
        stage (BUILD_STAGE) {
            
            projects.each { project ->
                
                String PROJECT_NAME = project['name']
                String PROJECT_PATH = project['path']
                
                echo "${PROJECT_NAME} project is building..."

                dir(PROJECT_PATH) {
                    
                    withEnv(["PATH+MAVEN=${MAVEN_PATH}/bin"]){
                        sh 'mvn clean install'
                    }
    
                }

            }
            
        }*/

        stage (BUILD_STAGE) {

            echo "$projectName is building..."
            
            dir (javaCodePath) {

                withEnv(["PATH+MAVEN=$mavenPath/bin"]) {
                    
                    sh 'mvn clean install'

                }

            }
            
        }

        stage (VERSION_STAGE) {

        }
    }

    catch (Exception e) {
    
        echo "Build failed. Error: ${e.message}"
    
    }

}