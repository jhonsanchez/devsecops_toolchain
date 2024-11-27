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
        KUBECONFIG = "${HOME}/.kube/config"
    }
    stages {
        stage('minikube') {
            steps {
                script {
                    sh "cat $KUBECONFIG"
                    sh 'eval $(minikube docker-env)'
                }
            }
        }
    }
}