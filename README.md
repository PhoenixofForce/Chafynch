# Tea Tracker

Track your tea stash and take notes as you progress through your sessions.

## Table of Contents
- [System Overview](#system-overview)
- [Development](#development)
- [Deployment](#deployment)
- [Contributing](#contributing)

# System Overview

This Project requires
- Java 25
- npm, node

and uses
- [Spring](https://spring.io) as a backend framework
- **PostgreSQL** used as the database                                                                                                                                                
- **Flyway** for database migrations
- [Lombok](https://projectlombok.org) to avoid writing boilerplate
- [SvelteKit](https://svelte.dev) as a frontend framework
- [Tailwind](https://tailwindcss.com) for fancy css classes
- [DaisyUi](https://daisyui.com) for components and theming
- **Typescript**

# Development

## Side Services

Start the `docker-compose.dev.yml` to start the database. 
```bash
docker compose --file ./docker-compose.dev.yml up -d
```

## Backend

Run `./mvnw clean install` to install dependencies, then you can run the backend.
```bash
./mvnw clean install
```

## Frontend

Run `npm install`, then start deployment with `npm run dev`.
```bash
cd ui
npm install
npm run dev
```

## Scripts

All frontend scripts are run from the `ui` directory.

| Script | Description |
|--------|-------------|
| `npm run dev` | Start the dev server |
| `npm run build` | Build for production |
| `npm run check` | Run svelte-check for type errors |
| `npm run lint` | Lint the source code |
| `npm run lint-fix` | Auto-fix lint issues |
| `npm run format` | Check formatting |
| `npm run format-fix` | Auto-fix formatting issues |
| `npm run fix` | Run lint-fix and format-fix |
| `npm run ci` | Run check, lint, build, and format |
| `npm run generate:api` | Generate API types from the running backend's OpenAPI schema |
| `npm run generate:countries` | Generate the country list |

# Deployment

Docker Compose. TBD

# Contributing

- If you want to **report a bug** or **request a feature** simply open an issue. Please use the appropriate label bug/enhancement
- If you want to **contribute code** open a Pull Request (to the currently non-existent develop branch) with a meaningful description. Please refrain from opening a pull request that only contains cosmetic changes.

## Loose Code Guidelines
- Use early returns/ guard clauses when applicable
- Don't indent *too* deep
- DRY for big pieces of code
- Only repeat smaller pieces of code a few times