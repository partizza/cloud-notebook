FROM gradle:8.7.0-jdk17-alpine as builder
COPY . /src/transcript-service

WORKDIR /src/transcript-service

RUN gradle installDist

FROM openjdk:17-alpine as service

RUN mkdir -p /opt/transcript-service-app

COPY --from=builder /src/transcript-service/build/install/transcript-service /opt/transcript-service-app

ENTRYPOINT ["/opt/transcript-service-app/bin/transcript-service"]