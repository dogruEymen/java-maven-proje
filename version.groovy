node {

    stage ('Versioning Stage') {

        checkout scm

        String versionFilePath = './maven-project/version.yml'
        def versionFile = readYaml(file: versionFilePath)
        String currentVersion = versionFile['current_version']
        
        println "Current Version: ${currentVersion}"

        String updatedVersion = patchVersionUpdater(currentVersion)
        versionFile['current_version'] = updatedVersion

        writeYaml(file: versionFilePath, data: versionFile, overwrite: true)
        
        println "${currentVersion} updated to ${updatedVersion}"

    }

    stage ('Push Changes') {
        
        withCredentials([usernamePassword(credentialsId: 'github-webhook',
                        passwordVariable: 'GHCR_PASSWORD',
                        usernameVariable: 'GHCR_USERNAME')]) {

            sh """
            git config --global user.email "muh.eymendogru@gmail.com"
            git config --global user.name "Jenkins CI"
            git add .
            git commit -m "version.yml updated to new app version [ci skip]"
            git push https://${GHCR_USERNAME}:${GHCR_PASSWORD}@github.com/dogruEymen/java-maven-proje.git HEAD:main
            """
        }
    }
    
}

// Standart version format : "#.#.#"
def patchVersionUpdater(String currentVer) {

    def tokenized = currentVer.tokenize(".")

    String updatedVer = tokenized[0] + "." + tokenized[1] + "."

    updatedVer = updatedVer + (tokenized[2].toInteger() + 1).toString()
    
    return updatedVer

}