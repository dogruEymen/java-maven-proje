node {

    //String PROJECT_ID = params.PROJECT_ID
    //println("PROJECT_ID: $PROJECT_ID")

        deleteDir()

    stage ('Get Code') {

        checkout scm
    
    }
    
    String BUILD_STAGE = "Build"
    String DOCKER_BUILD_STAGE = "Version"
    String mavenPath = tool 'maven_3.9'


    String javaCodePath = './maven-project'
    String projectsFilePath = './projects.yml'
    String versionFilePath = './maven-project/version.yml'

    def projects = readYaml(file: projectsFilePath)['projects']
    def project = projects['MAVEN']
    String projectName = project['name']
    String projectRepoUrl = project['codeRepo']['url']
    String projectRepoCredentialsId = project['codeRepo']['credentialsId']
    String builderImage = project['builder_name']
    String runnerImage = project['runner_name']
    String version = readYaml(file: versionFilePath)['current_version']


    try {

        echo 'Code is successfully obtained.'

        

        echo 'Build Stage is starting...'


        stage (BUILD_STAGE) {

            echo "$projectName is building..."
            
            dir (javaCodePath) {

                withEnv(["PATH+MAVEN=$mavenPath/bin"]) {
                    
                    sh 'mvn clean install'

                }

            }
            
        }

        String registry = "ghcr.io"
        String repository = "dogrueymen/java-maven-project"
        String fullImageName = "${registry}/${repository}:${version}".toLowerCase()

        stage (DOCKER_BUILD_STAGE) {

            dir (javaCodePath) {
            
                sh """docker build --build-arg BUILDER_NAME=${builderImage} \
                --build-arg RUNNER_NAME=${runnerImage} --build-arg VERSION=${version} \
                -t ${fullImageName} ."""

                echo 'Container başlatılıyor...'
                sh "docker rm -f test-app-${version} || true"
                sh "docker run -d -p 4040:4040 --name test-app-${version} ${fullImageName}"

                withCredentials([usernamePassword(credentialsId: 'github-ghcr-token',
                                passwordVariable: 'GHCR_PASSWORD',
                                usernameVariable: 'GHCR_USER')]) {

                
                echo 'GHCR Giriş Yapılıyor...'
                sh "docker login ${registry} -u ${GHCR_USER} --password-stdin"

                echo "imaj gönderiliyor..."
                sh "docker push ${fullImageName}"
                }
            } 
        } 
    }

    catch (Exception e) {
    
        echo "Build failed. Error: ${e.message}"
    
    }

}


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