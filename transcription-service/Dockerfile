FROM gradle:8.6.0-jdk17-alpine as service_builder
COPY . /src/transcription-service

WORKDIR /src/transcription-service

RUN gradle installDist

FROM openjdk:17-alpine as transcription_service

RUN mkdir -p /opt/transcription-service-app

COPY --from=service_builder /src/transcription-service/build/install/transcription-service /opt/transcription-service-app

CMD ["/opt/transcription-service-app/bin/transcription-service"]