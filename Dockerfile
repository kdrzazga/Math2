FROM debian:stable-slim

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates \
        curl \
        gnupg \
        lsb-release \
        sudo

RUN echo 'deb http://opensource.wandisco.com/debian buster svn' > /etc/apt/sources.list.d/wandisco.list \
    && curl -L https://opensource.wandisco.com/wandisco-debian.gpg | sudo apt-key add - \
    && apt-get update \
    && apt-get install -y --no-install-recommends gentoo-prefix

RUN mkdir -p /opt/gentoo \
    && echo "PORTDIR_OVERLAY=\"/usr/local/portage\"" > /opt/gentoo/etc/make.conf \
    && echo "EMERGE_DEFAULT_OPTS=\"--jobs=2 --load-average=2.0\"" >> /opt/gentoo/etc/make.conf \
    && chown -R root:root /opt/gentoo \
    && ln -s /opt/gentoo/usr/bin/prefix /usr/local/bin/prefix \
    && prefix-overlay add gentoo "https://github.com/gentoo/gentoo.git" master \
    && prefix-keyword accept '~amd64' \
    && prefix-sync

ENV EPREFIX=/opt/gentoo \
    PATH=/opt/gentoo/usr/bin:/opt/gentoo/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin \
    LANG=en_US.utf8 \
    LC_ALL=en_US.utf8 \
    TERM=xterm-256color

RUN prefix emerge -q dev-java/openjdk:8

ENV JAVA_HOME=/opt/gentoo/usr/lib/jvm/java-1.8-openjdk \
    PATH=$PATH:/opt/gentoo/usr/lib/jvm/java-1.8-openjdk/bin

COPY target/Math2*with-dependencies.jar /Math2.jar

CMD ["java", "-jar", "/Math2.jar"]
