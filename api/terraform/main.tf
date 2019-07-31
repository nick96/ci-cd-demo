variable "DO_TOKEN" {}

variable "PIPELINE_SSHKEY" {}

terraform {
  required_version = "~> 0.12.0"
  backend "remote" {}
}

provider "digitalocean" {
  token = "${var.DO_TOKEN}"
}

resource "digitalocean_ssh_key" "pipeline" {
  name       = "CircleCI pipeline"
  public_key = "${file("${var.PIPELINE_SSHKEY}")}"
}

resource "digitalocean_droplet" "api" {
  image    = "ubuntu-18-04-x64"
  name     = "api-1"
  region   = "sgp1"
  size     = "s-1vcpu-1gb"
  ssh_keys = ["${digitalocean_ssh_key.pipeline.fingerprint}"]
}

resource "digitalocean_domain" "domain" {
  name = "nspain.me"
}

resource "digitalocean_record" "www_api" {
  domain = "${digitalocean_domain.domain.name}"
  type   = "A"
  name   = "demo"
  value  = "${digitalocean_droplet.api.ipv4_address}"
}

output "fqdn" {
  value = "${digitalocean_record.www_api.fqdn}"
}

output "ip" {
  value = "${digitalocean_droplet.api.ipv4_address}"
}
