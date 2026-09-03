FROM node:24.12.0-alpine AS build_ui
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app

COPY ui/package.json ui/package-lock.json ./
RUN npm ci
RUN npm run --silent license-file > /THIRD-PARTY.txt

COPY ui/ .
RUN npm pkg set version="$APP_VERSION"
RUN npm run build

FROM node:24.12.0-alpine AS build_docs
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app

COPY docs/package.json docs/package-lock.json ./
RUN npm ci
RUN npm run --silent license-file > /THIRD-PARTY.txt

COPY docs/ .
RUN npm pkg set version="$APP_VERSION"
RUN npm run build

FROM nginx:alpine-slim

COPY --from=build_docs /THIRD-PARTY.txt /usr/share/nginx/html/docs/THIRD-PARTY.txt
COPY --from=build_docs /app/dist /usr/share/nginx/html
COPY --from=build_ui /THIRD-PARTY.txt /usr/share/nginx/html/THIRD-PARTY.txt
COPY --from=build_ui /app/build /usr/share/nginx/html
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000
