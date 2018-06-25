FROM node:alpine

RUN apk update && apk upgrade && \
    apk add --no-cache bash git openssh

RUN npm install -g @angular/cli
RUN npm install -g @angular/compiler-cli@latest

RUN mkdir -p /usr/src/app

COPY . /usr/src/app

WORKDIR /usr/src/app

RUN npm install
