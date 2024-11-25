start minikube
```bash
minikube start --cpus 16 --memory 16384
```

# Jenkins
```bash
kubectl get -n jenkins secret jenkins -o json | jq '.data | map_values(@base64d)'
kubectl port-forward -n jenkins statefulset/jenkins 8080:8080
```
- Configure jdk 21

# Sonar
```bash
kubectl port-forward -n sonarqube statefulset/sonarqube-sonarqube 9000:9000
```
- use `admin` and `admin` as credentials
- change your password
- create a new question
- go to your account (top right corner) and go to security tab
- enter a new token name of type ```User Token``` and generate a token
- add that token to your jenkins credentials as ```sonarqube-token```

# Nexus
```bash
kubectl exec -n nexus -it deployment/nexus -- sh
```

# Dependency check

# Detect Secrets

Configure based on this URL: https://plugins.jenkins.io/shiningpanda/
- configure Python installations, step in ```Manage Jenkins > Global Tool Configuration.``` Then look for the Python section.
