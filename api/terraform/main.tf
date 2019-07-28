variable "DO_TOKEN" {}

variable "PIPELINE_SSHKEY" {}

variable "PERSONAL_SSHKEY" {}

provider "digitalocean" {
  token = "${var.DO_TOKEN}"
}

resource "digitalocean_ssh_key" "pipeline" {
  name       = "CircleCI pipeline"
  public_key = "${file("${var.PIPELINE_SSHKEY}")}"
}

resource "digitalocean_ssh_key" "personal" {
  name       = "Nick - Personal SSH Key"
  public_key = "${file("${var.PERSONAL_SSHKEY}")}"
}

resource "digitalocean_droplet" "api" {
  image    = "ubuntu-18-04-x64"
  name     = "api-1"
  region   = "sgp1"
  size     = "s-1vcpu-1gb"
  ssh_keys = ["${digitalocean_ssh_key.pipeline.fingerprint}", "${digitalocean_ssh_key.personal.fingerprint}"]
}

resource "digitalocean_domain" "domain" {
  name = "nspain.me"
}

resource "digitalocean_record" "www_api" {
  domain = "${digitalocean_domain.domain.name}"
  type   = "A"
  name   = "demo"
  value  = "${digitalocean_droplet.api.ipv4_address }"
}

resource "digitalocean_firewall" "www_api" {
  name = "only-22-80-and-443"

  droplet_ids = ["${digitalocean_droplet.api.id}"]

  inbound_rule {
    protocol         = "tcp"
    port_range       = "22"
    source_addresses = ["0.0.0.0/0", "::/0"]
  }

  inbound_rule {
    protocol         = "tcp"
    port_range       = "80"
    source_addresses = ["0.0.0.0/0", "::/0"]
  }

  inbound_rule {
    protocol         = "tcp"
    port_range       = "443"
    source_addresses = ["0.0.0.0/0", "::/0"]
  }

  inbound_rule {
    protocol         = "icmp"
    source_addresses = ["0.0.0.0/0", "::/0"]
  }

  outbound_rule {
    protocol   = "tcp"
    port_range = "1-65535"
    destination_addresses   = ["0.0.0.0/0", "::/0"]
  }

  outbound_rule {
    protocol   = "udp"
    port_range = "1-65535"
    destination_addresses   = ["0.0.0.0/0", "::/0"]
  }

  outbound_rule {
    protocol   = "icmp"
    port_range = "1-65535"
    destination_addresses   = ["0.0.0.0/0", "::/0"]
  }
}

output "fqdn" {
  value = "${digitalocean_record.www_api.fqdn}"
}

output "ip" {
  value = "${digitalocean_droplet.api.ipv4_address}"
}
