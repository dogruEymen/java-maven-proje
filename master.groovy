node {
        
        // Trigger BUILD JOB
    try {

        stage ('BUILD') {

            def buildResult = build job: 'java-maven', wait: true, propagation: false
            
            if (buildResult.result == 'SUCCESS') {
                // If build success then start versioning 
                try {

                    stage ('VERSION') {

                        echo 'Build success, versioning stage is starting...'

                        def versionResult = build job: 'version-job', wait: true, propagation: false
                    
                        if (versionResult.result == 'SUCCESS') {

                            echo 'Versioning completed successfully...'

                        } else {

                            echo 'Something went wrong in VERSION STAGE'

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

    }
    catch (Exception e) {

        echo "Error: ${e.message}"

    }
    finally {

        echo 'Merge result is sending via email etc.'
    }

}