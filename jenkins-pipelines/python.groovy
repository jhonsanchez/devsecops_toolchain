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