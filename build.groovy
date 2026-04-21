node {

    String BUILD_STAGE = "Build"
    String MAVEN_PATH = tool 'maven_3.9'

    try {

        deleteDir()

        stage ('Get Code') {

            checkout scm

        }
        echo 'Code is successfully obtained.'

        def projects = readYaml(file: "projects.yml")['projects']

        echo 'Build Stage is starting...'

        stage (BUILD_STAGE) {
            
            projects.each { project ->
                
                String PROJECT_NAME = project['name']
                String PROJECT_PATH = project['path']
                
                echo "${PROJECT_NAME} project is building..."

                dir(PROJECT_PATH) {
                    
                    withEnv("PATH+MAVEN=${MAVEN_PATH}/bin/"){
                        sh 'mvn clean install'
                    }
    
                }

            }
            
        }
    }

    catch (Exception e) {
    
        echo "Build failed. Error: ${e.message}"
    
    }

}