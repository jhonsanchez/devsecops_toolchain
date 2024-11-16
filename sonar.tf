resource "helm_release" "sonarqube" {
  name       = "sonarqube"
  namespace       = "sonarqube"

  repository = "https://SonarSource.github.io/helm-chart-sonarqube"
  chart      = "sonarqube"
}