pipeline {
    agent {
        kubernetes {
            yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: jnpl
    image: jenkins/inbound-agent:3273.v4cfe589b_fd83-1
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
  - name: python
    image: python
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
        stage('Build') {
            steps {
                container('python') {
                    sh '''
                  pip install detect-secrets
                  '''
                }
            }
        }
    }
}