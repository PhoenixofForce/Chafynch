FROM node:24.12.0-alpine AS build
ARG APP_VERSION=0.0.1-SNAPSHOT

WORKDIR /app

COPY ui/package.json ui/package-lock.json ./
RUN npm ci

COPY ui/ .
RUN npm pkg set version="$APP_VERSION"
RUN npm run build

FROM nginx:alpine-slim

COPY --from=build /app/build /usr/share/nginx/html
COPY docker/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 3000
