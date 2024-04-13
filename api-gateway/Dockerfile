FROM gradle:8.7.0-jdk17-alpine as builder
COPY . /src/api-gateway

WORKDIR /src/api-gateway

RUN gradle installDist

FROM openjdk:17-alpine as service

RUN mkdir -p /opt/api-gateway-app

COPY --from=builder /src/api-gateway/build/install/api-gateway /opt/api-gateway-app

ENTRYPOINT ["/opt/api-gateway-app/bin/api-gateway"]