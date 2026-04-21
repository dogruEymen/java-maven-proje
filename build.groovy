node {

    String SUCCESS = "SUCCESS"
    String FAILURE = "FAILURE"


    def mvnHome = tool 'maven_3.9'

    stage('Checkout') {
        checkout scm
    }

    stage('BUILD STAGE') {
        try {
            echo 'Build işlemi başlatılıyor'
            sh 'mvn clean install'
        }
        catch (Exception e) {

            currentBuild.result = FAILURE
            echo 'Build başarısız, hata: ${e.message}'
        }
        finally {

            echo 'Build aşaması bitti'
        }
    }

    stage('CREATE IMAGE') {
        
    }

}