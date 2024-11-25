pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnpl
    image: jenkins
    resources:
      requests:
        cpu: 500m
        memory: 512Mi
    limits:
        cpu: 1000m
        memory: 2048Mi
    command:
    - cat
    tty: true
  - name: aws-cli
    image: public.ecr.aws/bitnami/aws-cli:2.4.25
    resources:
      requests:
        cpu: 200m
        memory: 400Mi
      limits:
        cpu: 1024m
        memory: 2048Mi
    command:
    - cat
    tty: true
  securityContext:
    runAsUser: 0
    fsGroup: 0
'''
        }
    }
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