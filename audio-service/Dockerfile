FROM gradle:8.6.0-jdk17-alpine as service_builder
COPY . /src/audio-service

WORKDIR /src/audio-service

RUN gradle installDist

FROM openjdk:17-alpine as audio_service

RUN mkdir -p /opt/audio-service-app

COPY --from=service_builder /src/audio-service/build/install/audio-service /opt/audio-service-app

ENTRYPOINT ["/opt/audio-service-app/bin/audio-service"]