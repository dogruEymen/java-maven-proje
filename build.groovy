node {

    String BUILD_STAGE = "Build"


    try {

        deleteDir()

        stage ('Get Code') {

            checkout scm

        }

        def projects = readYaml(file: projects.yml)

        stage (BUILD_STAGE) {
            
            projects.each { project - >
                
                String PROJECT_NAME = project.['name']
                String PROJECT_PATH = project.['path']
                
                echo "${PROJECT_NAME} project is building..."

                dir(PROJECT_PATH) {
                    
                    sh 'mvn clean install'

                }

            }
            
        }
    }

}