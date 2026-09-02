---
title: Quick Start
description: Get your instance running asap
---

## Requirements

- [Docker](https://docs.docker.com/engine/install/)

## Setting up

Create a folder for Chafynch to live in, download the latest docker compose directly from the repository:

```bash
curl -fsSL https://raw.githubusercontent.com/PhoenixofForce/Chafynch/refs/heads/main/docker/compose.prod.yml -o compose.yml
```

And start the container with `docker compose up -d`. Once started you can access the application in you browser at [http://localhost:3000](http://localhost:3000).

:::caution[If you want to adjust the compose file]
The container name for the backend has to be `service`. The nginx of the ui uses this name to proxy to the docker container.
:::
