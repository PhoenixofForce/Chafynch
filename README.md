# Tea Tracker

Track your tea stash and take notes as you progress through your sessions.

# Table of Contents

1. [Features](#features)
   1. [Planned Features](#planned-features)
2. [System Overview](#system-overview)
3. [Deployment](#deployment)
4. [Contributing](#contributing)
5. [License](#license)

# Features

- Keep all your teas in one place with vendor, harvest date, origin and more
  - Fetch data automatically from vendors website instead of copy-pasting endless fields
  - See where your teas come from on a map
- Log your sessions while drinking
  - Time your infusions
  - Keep track of how tasting notes develop over time
  - Leave a message for your next session

## Planned Features

- Stock tracking (always know how much tea you got left)
- user management
- Teaware tracking
- Label Generator
- Collaborative tea sessions
- LLM integration

# System Overview

The frontend is built as a static site and served from an nginx container. The nginx also proxies `/api` back to the backend, so only one container needs to publish its ports.

This project uses
- [Spring](https://spring.io) (+ Java 25) as a backend framework
- **PostgreSQL** used as the database
- **Flyway** for database migrations
- [SvelteKit](https://svelte.dev) (+ Typescript) as a frontend framework
- [Tailwind](https://tailwindcss.com) for fancy css classes
- [DaisyUi](https://daisyui.com) for components and theming

# Deployment

1. Download the [docker/compose.prod.yml](docker/compose.prod.yml)
2. Adjust the environment variables to your liking by creating a `.env` file
3. `docker compose up -d`
4. Open the ui at http://localhost:3000

Or in one command for testing
```bash
curl -fsSL https://raw.githubusercontent.com/PhoenixofForce/TeaTracker/refs/heads/main/docker/compose.prod.yml -o compose.yml && docker compose up -d
```

> **Important**
> The service in the compose file has to stay called *service*. Otherwise the frontend cannot reach it.

# Contributing

See [how to contribute](CONTRIBUTING.md).

# License

[MIT](LICENSE)
