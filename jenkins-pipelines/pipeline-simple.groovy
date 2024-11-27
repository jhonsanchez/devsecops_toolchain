pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                script {
                    sleep(2000)
                    echo 'checking out code...'
                }
            }
        }
        stage('build') {
            steps {
                script {
                    sleep(3000)
                    echo 'building...'
                }
            }
        }
        stage('unit test') {
            steps {
                script {
                    sleep(1000)
                    echo 'unit testing with code coverage...'
                }
            }
        }
        stage('integration test') {
            steps {
                script {
                    sleep(4000)
                    echo 'integration testing...'
                }
            }
        }
        stage('Detect secrets') {
            steps {
                script {
                    sleep(3000)
                    echo 'Detecting secrets...'
                }
            }
        }
        stage('Sonarqube analysis') {
            steps {
                script {
                    sleep(6000)
                    echo 'Running sonarqube analysis...'
                }
            }
        }
        stage('Sast scan') {
            steps {
                script {
                    sleep(7000)
                    echo 'Running SAST Scan' //run a gradle task
                }
            }
        }
        stage('Nexus') {
            steps {
                script {
                    sleep(1000)
                    echo 'Storing artifact on Nexus' //run a gradle task
                }
            }
        }
        stage('Deploy to dev') {
            steps {
                script {
                    sleep(4000)
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
                    sleep(4000)
                    echo 'deploy to prod'
                }
            }
        }
    }
}