FROM openjdk:8-jdk-alpine

RUN apk --no-cache add curl \
    && curl -sSL "https://github.com/AdoptOpenJDK/openjdk8-binaries/releases/download/jdk8u312-b07/OpenJDK8U-jdk_x64_linux_hotspot_8u312b07.tar.gz" | tar xz \
    && mkdir /opt \
    && mv jdk* /opt/openjdk \
    && ln -s /opt/openjdk/bin/java /usr/bin/java \
    && rm -rf /var/cache/apk/*

COPY target/Math2*with-dependencies.jar /Math2.jar

CMD ["java", "-jar", "Math2.jar"]
