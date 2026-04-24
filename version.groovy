node {

    stage ('Versioning Stage') {
        
        String versionFilePath = './maven-project/version.yml'
        def versionFile = readYaml(file: versionFilePath)
        String currentVersion = versionFile['current_version']
        
        println "Current Version: ${currentVersion}"

        String updatedVersion = patchVersionUpdater(currentVersion)
        versionFile['current_version'] = updatedVersion

        writeYaml(file: versionFilePath, data: versionFile, overwrite: true)

    }
    
}

// Standart version format : "#.#.#"
def patchVersionUpdater(String currentVer) {

    def tokenized = currentVer.tokenize(".")

    String updatedVer = tokenized[0] + "." + tokenized[1] + "."

    updatedVer = updatedVer + (tokenized[2].toInteger() + 1).toString()
    
    return updatedVer

}