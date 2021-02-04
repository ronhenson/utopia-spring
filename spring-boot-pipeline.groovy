pipeline {
    agent any

    tools {
        maven 'M3'
    }

    stages {
        stage('Build Service Discovery') {
            steps {
                git 'https://github.com/dereklance/Spring-Boot-API.git'
                sh 'git checkout change-java-version-for-jenkins'
                sh 'git pull origin change-java-version-for-jenkins'
                dir('service-discovery') {
                    mvn clean package
                }
            }
            post {
                success {
                    s3Upload consoleLogLevel: 'INFO', dontSetBuildResultOnFailure: false, dontWaitForConcurrentBuildCompletion: false, entries: [[bucket: 'jenkins-spring-boot', excludedFile: '', flatten: false, gzipFiles: false, keepForever: false, managedArtifacts: false, noUploadOnFailure: true, selectedRegion: 'us-east-2', showDirectlyInBrowser: false, sourceFile: '*.jar', storageClass: 'STANDARD', uploadFromSlave: false, useServerSideEncryption: false]], pluginFailureResultConstraint: 'FAILURE', profileName: 'dlance', userMetadata: []
                }
            }
        }
    }
}

