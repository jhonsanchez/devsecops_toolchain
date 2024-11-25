pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:  
  - name: python
    image: python
    resources:
      requests:
        cpu: 200m
        memory: 400Mi
      limits:
        cpu: 4000m
        memory: 6000Mi
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
        stage('Detect secrets'){
            steps {
                container('python') {
                    try {
                        // Run detect-secrets scan
                        sh 'pip install detect-secrets'
                        sh 'detect-secrets scan > detectSecretsResults.json'

                        // Archive the scan results
                        archiveArtifacts artifacts: 'detectSecretsResults.json', allowEmptyArchive: true

                        // Read and print results to console
                        def detectSecretsResults = readFile('detectSecretsResults.json')
                        echo "Detect Secrets Scan Results: ${detectSecretsResults}"
                    } catch (Exception e) {
                        echo "Error running detect-secrets: ${e.getMessage()}"
                    }
                }
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