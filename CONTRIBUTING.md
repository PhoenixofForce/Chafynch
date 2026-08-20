# Table of contents

1. [Project Structure](#project-structure)
2. [Setting up the project](#setting-up-the-project)
   1. [...with mise](#with-mise-recommended)
   2. [...manually](#manually)
3. [Starting dev environment](#starting-dev-environment)
4. [Coding Guidelines](#coding-guidelines)
   1. [Issues and Pull Requests](#issues-and-pull-requests)
   2. [Format](#format)
      1. [Intellij Setup](#intellij-setup)
      2. [VSCode Setup](#vscode-setup)
   3. [Commit Style](#commit-style)
5. [AI Guidelines](#ai-guidelines)
6. [Testing](#testing)
7. [Other Commands](#other-commands)
   1. [Generate API](#generate-api)
   2. [Generate countries](#generate-countries)
   3. [Reset Database](#reset-database)
   4. [Building locally](#building-locally)

# Project Structure

```
├── .config/   # configs for dev tooling and java formatting
├── docker/    # docker build- and docker compose files 
├── scripts/   # helper scripts for repo-wide actions
├── src/       # Spring Backend
└── ui/        # SvelteKit frontend
```

The backend uses the `dev.phoenixofforce.tea.tracker` base package, classes are sorted into packages by features and subfeatures.
Repositories, Services, Controllers, DTOs and Entities are flat in the package. Migrations are written in sql and located in `src/main/resources/db`

The frontend follows the SvelteKit default with `src/routes` containing the pages with their specific components next to them. 
`src/lib` contains shared code, also nested by feature. Here `api/gen` is generated from the openapi specs coming from the backend, do not change it manually.   

# Setting up the project

There are two ways of setting up the project:
- automatically with [mise-en-place](https://mise.jdx.dev)
- or manually

Either way, you need to make sure you have [docker](https://www.docker.com/get-started/) installed.

## ...with mise (recommended)

Follow [this guide](https://mise.jdx.dev/installing-mise.html) to install [mise-en-place](https://mise.jdx.dev) if you do not have it installed yet.

First you need to confirm that you trust the configured mise file with `mise trust`.
You can check the file you are trusting under `.config/mise.toml`

Then just run `mise install` in your terminal to
- install the correct versions for all the devtools (java, maven, node, lefthook)
- install the dependencies for the backend
- install the dependencies for the frontend
- activate the provided git hooks (just formatting) via lefthook

If you want to deactivate the git hooks run `lefthook uninstall`

## ...manually

Make sure you are running
- `java -version` 25
- `node --version` 24.12.0
- (Optional) the latest version of [lefthook](https://lefthook.dev/install/)

Afterward you can run
- `./mvnw install -DskipTests` to install the maven dependencies (`.\mvnw install -DskipTests` for Windows)
- `cd ui && npm install` for the node dependencies.

To install the git hooks simply run `lefthook install`

# Starting dev environment

Start up the required external services (such as the postgres db) with one of the following commands 
```bash
mise run external
docker compose --file docker/compose.dev.yml up -d
```
You can stop the containers again with `mise run external:down` or `docker compose --file docker/compose.dev.yml down`.

Start the backend either via your normal IDE tools (with the `dev` profile being active) or from the command line via
```bash
mise run service
./mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"
```
On Windows its `.\mvnw spring-boot:run "-Dspring-boot.run.profiles=dev"`.

Finally, the frontend can be run with
```bash
mise run ui
cd ui && npm run dev
```

Now you can access the ui at http://localhost:5173

The backend is accessible at http://localhost:8080

> As a shortcut you can run `mise run dev` to run all these steps for you.
> 
> Also `mise run service` starts up the docker compose before. So you do not have to do this manually
 
# Coding Guidelines

- Use early returns/ guard clauses when applicable
- DRY for big pieces of code
- Only repeat smaller pieces of code a few times
- Migrations must be named `V<n>__name.sql` and never touched again once on main 

## Issues and Pull Requests

Bugs, enhancements and feature ideas all belong into issues. Make sure to check for duplicates first. Also, use descriptive titles and descriptions.

For anything bigger than a small fix, open an issue first so the direction is clear before you write the code. If you start working on an existing issue, leave a comment under it so nobody does the work twice.

Pull requests branch off `main` and merge back into `main`. Keep one topic per pull request, describe what you did and why.

A pull request is ready when
- the pipeline is green (can be reproduced locally with `mise run ci`)
- new code is tested
- you tested everything locally
- the commits carry a `Changelog-*` trailer where the change is worth mentioning
- nothing unrelated was reformatted along the way
- (if necessary) documentation is updated
- (if you can) translations are added

Please do not open pull requests that only contain cosmetic changes, bump dependencies, or refactor large parts of the project without talking about it first.

> This is a spare time project, so a review can take a few days. Change requests are normal and not a rejection.

## Format

- The backend code is formatted by [spotless](https://github.com/diffplug/spotless) with the eclipse formatter config in `.config/eclipse-formatter.xml`. It also removes unused imports and enforces import order.
- The frontend code is formatted by prettier (configured in `ui/package.json`) and auto fixed by eslint (`ui/eslint.config.js`). The tailwind prettier plugin sorts your classes.
- Indentation, line endings and encoding of all other files come from `.editorconfig`.

There are three ways to format your code, choose the one that fits your style best:

1. a git hook formats your staged files and commits the result (`mise install` or `lefthook install`)
2. `mise run fix` formats the whole repo
3. Your IDE can apply the formats on save (see setup down below)

Structure is checked by checkstyle (`.config/checkstyle.xml`): nesting depth, naming, braces and similar rules. A single justified violation can be silenced with `@SuppressWarnings("checkstyle:NestedIfDepth")`.

The pipeline will check if all the formats are correct and fail if some errors were found. Locally everything warns as a default, so a broken format never blocks your local development.

### Intellij Setup

1. Install [Adapter for Eclipse Code Formatter](https://plugins.jetbrains.com/plugin/6546-adapter-for-eclipse-code-formatter)
2. Under `Settings > Adapter for Eclipse Code Formatter` enable it
3. Choose `.config/eclipse-formatter.xml` as the config file
4. Choose `TeaTracker` as the profile
5. Enable `Optimize Imports`
6. Select the same file for `Import Order from file`
7. Install [CheckStyle-IDEA](https://plugins.jetbrains.com/plugin/1065-checkstyle-idea)
8. Under `Tools > Checkstyle` add `.config/checkstyle.xml`, name it `TeaTracker` as well and enable it

Prettier and eslint need no plugin, both of them are picked up from `ui/`, you only have to enable them under `Settings > Languages & Frameworks > JavaScript`.

### VSCode Setup

1. Install [Svelte for VS Code](https://marketplace.visualstudio.com/items?itemName=svelte.svelte-vscode) for svelte language support
2. Install [ESLint](https://marketplace.visualstudio.com/items?itemName=dbaeumer.vscode-eslint)
3. Install [Prettier](https://marketplace.visualstudio.com/items?itemName=esbenp.prettier-vscode)
4. Install [Tailwind CSS IntelliSense](https://marketplace.visualstudio.com/items?itemName=bradlc.vscode-tailwindcss) for class autocompletion
5. Install [EditorConfig](https://marketplace.visualstudio.com/items?itemName=EditorConfig.EditorConfig), without it vscode ignores `.editorconfig`
6. Put the following into your own `.vscode/settings.json`

```json
{
	"files.associations": { "*.css": "tailwindcss" },
	"tailwindCSS.classAttributes": ["class", "className", ".*Class"],
	"editor.codeActionsOnSave": { "source.fixAll.eslint": "explicit" }
}
```

> There is no equivalent to the eclipse formatter plugin for java, so the backend is only formatted by the hook or `mise run fix`.

## Commit Style

You do **not** have to use [conventional commits](https://www.conventionalcommits.org). Instead, the changelog is generated by dedicated commit trailers. These types are available
- Changelog-breaking
- Changelog-feature
- Changelog-enhancement
- Changelog-fix

**Example**
```
fix some error by doing something technical 

Changelog-fix: some feature no longer breaks
Changelog-fix: some other feature is also fixed
```

> When commiting from the command line use `git commit -m "subject" -m "Changelog-fix: ..."` to get this format.

# AI Guidelines

Using AI tools is fine, the result is still yours. You are the author of every pull request you open, and you have to be able to explain every line of it.

- Add an `Assisted-by` trailer when a relevant part of a commit came out of a tool mostly unchanged. Autocompletion and small suggestions do not need one
- Never open a pull request with code you do not understand. "The AI wrote it" is not an answer in a review
- Texts have to be mostly human written, that includes documentation, the readme, ui labels and translations. Letting a tool fix your spelling or rephrase a sentence is fine, but the wording should be yours
- The same goes for issues and pull request descriptions, a generated wall of text costs more time to read than it saved you writing it
- By opening a pull request you confirm that the code can be contributed under this projects MIT license. That does not change when a tool wrote it

**Example**
```
add extraction for some vendor

Assisted-by: claude-opus-5
Changelog-feature: teas from some vendor can now be imported
```

# Testing

Run tests with
- Backend: `mise run test:service` or `./mvnw test` (Windows `.\mvnw test`)
- Frontend: `mise run test:ui` or `npm run test` from the ui directory
- Frontend with watch: `mise run test:ui:watch` or `npm run test:watch` from the ui directory
- `mise run ci` runs everything the pipeline runs

Backend tests live in `src/test/java` mirroring the package they test, frontend tests sit next to their component as `<Component>.test.ts`.

What should have a test:

- Services and anything with branches, calculations or special cases. Small helpers are nice to have, bigger logic is not optional
- Controllers as soon as they do more than delegating
- The basic components in `src/lib/basics`
- Above that level test whole pages instead of single components

> These expectations are for new code. Existing gaps are not your problem - unless you would like to write tests.

In frontend tests select things the way a user would find them: `getByText` and `getByLabelText` first, `getByRole` for interactive elements where the behavior matters. `data-testid` is meant for containers and for elements that have neither text nor label.

# Other Commands

The most used commands are available with `mise run` where you get an interactive menu with all the commands to choose from.
Other less used commands are found in `ui/package.json`

## Generate API

To generate the frontend types and methods used for communicating with the backend run `mise run generate:api` or `npm run generate:api` (from the ui folder). You need a running backend for this to work.

## Generate countries

To generate the country data needed for the frontends origin cards run `mise run generate:countries` or `npm run generate:countries` (from the ui folder). 

## Reset Database

To reset your database, stop the compose file with its volumes `docker compose --file docker/compose.dev.yml down -v`.

## Building locally

If you want to build the images locally, everything needed is inside the docker files. The easiest way for building is `docker compose --file docker/compose.test.yml build`
