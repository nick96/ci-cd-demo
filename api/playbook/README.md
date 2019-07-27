# Playbook

[playbook.yml](./playbook.yml) deploys the API in the parent directory behind
Traefik. It intalls docker on the host, configures traefik and the API to run in
docker containers.

## Variables

- `api_image`: Docker image of the API to deploy
- `email`: Email to use for Traefik's let's encrpyt