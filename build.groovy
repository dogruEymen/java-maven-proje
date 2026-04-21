node {

    String BUILD_STAGE = "Build"


    try {

        deleteDir()

        stage ('Get Code') {

            checkout scm

        }
        echo 'Code is successfully obtained.'

        def projects = readYaml(file: projects.yml)['projects']

        echo 'Build Stage is starting...'

        stage (BUILD_STAGE) {
            
            projects.each { project ->
                
                String PROJECT_NAME = project['name']
                String PROJECT_PATH = project['path']
                
                echo "${PROJECT_NAME} project is building..."

                dir(PROJECT_PATH) {
                    
                    sh 'mvn clean install'

                }

            }
            
        }
    }

    catch (Exception e) {
    
        echo 'Build failed. Error: ${e.message}'
    
    }

}