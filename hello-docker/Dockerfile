FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER domain sly@creditft.com

RUN mkdir -p /opt/adm/hello-docker

WORKDIR /opt/adm/hello-docker

EXPOSE 8761

ADD ./target/hello-docker-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]

CMD ["--spring.profiles.active=dev"]