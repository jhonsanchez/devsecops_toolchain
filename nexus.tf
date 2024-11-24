resource "kubernetes_service" "nexus_service" {
  metadata {
    name      = "nexus-service"
    namespace = "nexus"
    annotations = {
      "prometheus.io/scrape" = "true"
      "prometheus.io/path"   = "/"
      "prometheus.io/port"   = "8081"
    }
  }

  spec {
    selector = {
      app = "nexus-server"
    }

    type = "NodePort"

    port {
      port        = 8081
      target_port = 8081
      node_port   = 32000
    }
  }
}
resource "kubernetes_deployment" "nexus" {
  metadata {
    name      = "nexus"
    namespace = "nexus"
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "nexus-server"
      }
    }

    template {
      metadata {
        labels = {
          app = "nexus-server"
        }
      }

      spec {
        container {
          name  = "nexus"
          image = "sonatype/nexus3:latest"

          env {
            name  = "MAX_HEAP"
            value = "800m"
          }

          env {
            name  = "MIN_HEAP"
            value = "300m"
          }

          resources {
            limits = {
              memory = "6Gi"
              cpu    = "1200m"
            }
            requests = {
              memory = "4Gi"
              cpu    = "800m"
            }
          }

          port {
            container_port = 8081
          }

          volume_mount {
            name       = "nexus-vol"
            mount_path = "/setup"
          }
        }

        volume {
          name = "nexus-vol"

          empty_dir {}
        }
      }
    }
  }
}
resource "kubernetes_namespace" "namespace_nexus" {
  metadata {
    name = "nexus"
  }
}