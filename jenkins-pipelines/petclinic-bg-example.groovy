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
        cpu: 2000m
        memory: 4000Mi
      limits:
        cpu: 4000m
        memory: 10240Mi
    command:
    - cat
    tty: true
  securityContext:
    runAsUser: 0
    fsGroup: 0
'''
        }
    }
    environment {
        GRADLE_OPTS='-Xmx3g -Xms2g -Dfile.encoding=UTF-8 -XX:MaxMetaspaceSize=1g -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8'
    }
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/jhonsanchez/petclinic-bg-example.git'
            }
        }
        stage('Detect secrets'){
            steps {
                container('python') {
                    script {
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

        }
        stage('Build') {
            steps {
                script {
                    sh './gradlew clean build' //run a gradle task
                }
            }
        }
        stage('Test') {
            steps {
                script {
                    sh './gradlew test'
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