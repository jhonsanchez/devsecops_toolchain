pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'test', url: 'https://github.com/jhonsanchez/petclinic-bg-example.git'
            }
        }
        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build -x test --no-daemon' //run a gradle task
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    sh './gradlew clean test --no-daemon' //run a gradle task
                }
            }
        }
        stage("build & SonarQube analysis") {
            environment {
                SONAR_HOST_URL = 'http://sonarqube-sonarqube.sonarqube:9000'
                SONAR_TOKEN = credentials('sonarqube-token')
            }
            steps {
                script {
                    sh './gradlew sonar'
                }
            }
        }
//        stage("Quality Gate") {
//            steps {
//                timeout(time: 1, unit: 'HOURS') {
//                    waitForQualityGate abortPipeline: true
//                }
//            }
//        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}