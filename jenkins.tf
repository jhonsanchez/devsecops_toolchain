resource "helm_release" "jenkins" {
  name      = "jenkins"
  namespace = "jenkins"

  create_namespace = true
  repository       = "https://charts.jenkins.io"
  chart            = "jenkins"
  values = [
    file("${path.module}/jenkins-values.yaml")
  ]

}