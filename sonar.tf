resource "helm_release" "sonarqube" {
  name             = "sonarqube"
  namespace        = "sonarqube"
  create_namespace = true

  repository = "https://SonarSource.github.io/helm-chart-sonarqube"
  chart      = "sonarqube"
}