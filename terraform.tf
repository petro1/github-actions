terraform {
  required_providers {
    kubernetes = {
      source  = "hashicorp/kubernetes"
      version = ">= 2.0.0"
    }
  }
}

provider "kubernetes" {
  config_path = "~/.kube/config"
}

resource "kubernetes_deployment" "app-namespace" {
  metadata {
    name      = "quarkus-car"
    namespace = "app-namespace"
  }
  spec {
    replicas = 1
    selector {
      match_labels = {
        app = "quarkus-car"
      }
    }
    template {
      metadata {
        labels = {
          app = "quarkus-car"
        }
      }
      spec {
        container {
          image = "localhost/quarkus-car:1.0.6"
          name  = "quarkus-container"
          port {
            container_port = 8080
          }
          env {
            name  = "QUARKUS_DATASOURCE_JDBC_URL"
            value = "jdbc:postgresql://10.244.1.3:5432/AppDB"
          }
          env {
            name  = "QUARKUS_DATASOURCE_USERNAME"
            value_from {
              secret_key_ref {
                name = "pgadmin-secret"
                key = "username"
              }
            }
          }
          env {
            name  = "QUARKUS_DATASOURCE_PASSWORD"
            value_from {
              secret_key_ref {
                name = "pgadmin-secret"
                key = "password"
              }
            }
          }
        }
      }
    }
  }
}