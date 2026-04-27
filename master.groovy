node {
        
        // Trigger BUILD JOB
    try {

        checkout scm

        def changedFiles = sh(script: 'git diff --name-only HEAD~1', returnStdout: true).trim()
        def shouldRun = (changedFiles.contains('/src') || changedFiles.contains('pom.xml'))
        
        if (shouldRun) {
            
            stage ('BUILD') {

                def buildResult = build job: 'java-maven', wait: true, propagate: false
                
                if (buildResult.result == 'SUCCESS') {
                    // If build success then start versioning 
                    try {

                        stage ('VERSION') {

                            echo 'Build success, versioning stage is starting...'

                            def versionResult = build job: 'version-job', wait: true, propagate: false
                        
                            if (versionResult.result == 'SUCCESS') {

                                echo 'Versioning completed successfully...'

                            } else {

                                echo 'Something went wrong in VERSION STAGE !'

                            }

                        }    

                    }
                    catch (Exception e) {

                        echo "Error: ${e.message}"

                    }

                } else if (buildResult.result == 'FAILURE') {

                    echo 'BUILD FAILED, VERSIONING STAGE IS SKIPPING'

                }

            }

        } else {
            echo 'Changes is not in /src or pom.xlm, no need to build.'
        }

    }
    catch (Exception e) {

        echo "Error: ${e.message}"

    }
    finally {

        echo 'Merge result is sending via email etc..'
    }

}