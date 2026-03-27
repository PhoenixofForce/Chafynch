FROM node:22-alpine AS build

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm ci

COPY . .
RUN npm run build

FROM alpine:3.21

RUN apk add --no-cache nodejs

WORKDIR /app

COPY --from=build /app/build ./build

ENV PORT=3000
EXPOSE 3000

CMD ["node", "build"]
