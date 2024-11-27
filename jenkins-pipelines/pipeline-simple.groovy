pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                script {
                    sleep(2)
                    echo 'checking out code...'
                }
            }
        }
        stage('build') {
            steps {
                script {
                    sleep(3)
                    echo 'building...'
                }
            }
        }
        stage('unit test') {
            steps {
                script {
                    sleep(1)
                    echo 'unit testing with code coverage...'
                }
            }
        }
        stage('integration test') {
            steps {
                script {
                    sleep(4)
                    echo 'integration testing...'
                }
            }
        }
        stage('Detect secrets') {
            steps {
                script {
                    sleep(3)
                    echo 'Detecting secrets...'
                }
            }
        }
        stage('Sonarqube analysis') {
            steps {
                script {
                    sleep(6)
                    echo 'Running sonarqube analysis...'
                }
            }
        }
        stage('Sast scan') {
            steps {
                script {
                    sleep(7)
                    echo 'Running SAST Scan' //run a gradle task
                }
            }
        }
        stage('Nexus') {
            steps {
                script {
                    sleep(1)
                    echo 'Storing artifact on Nexus' //run a gradle task
                }
            }
        }
        stage('Deploy to dev') {
            steps {
                script {
                    sleep(4)
                    echo 'Deploy to dev'
                }
            }
        }
        stage('Approve based on environment lead') {
            input {
                message 'Please select environment'
                id 'envId'
                ok 'Submit'
                submitterParameter 'approverId'
                parameters {
                    choice choices: ['Prod', 'Pre-Prod'], name: 'envType'
                }
            }

            steps {
                echo "Deployment approved to ${envType} by ${approverId}."

            }
        }
        stage("Deploy to prod") {
            steps {
                script {
                    sleep(4)
                    echo 'deploy to prod'
                }
            }
        }
    }
}