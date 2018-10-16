FROM openjdk:8

ENV SCALA_VERSION 2.11.8

ENV SBT_VERSION 0.13.8

# Scala
RUN set -x \
    && mkdir -p /usr/src/scala \
    && curl -SL "https://downloads.lightbend.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz" -o scala_install.tgz \
    && tar -xC /usr/src/scala --strip-components=1 -f scala_install.tgz \
    && rm scala_install.tgz* \
    && mv /usr/src/scala /usr/lib/

RUN ln -s /usr/lib/scala/bin/scala /usr/local/bin

# install SBT
RUN curl -L -o sbt-$SBT_VERSION.deb http://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb \
  && dpkg -i sbt-$SBT_VERSION.deb \
  && rm sbt-$SBT_VERSION.deb \
  && apt-get update \
  && apt-get install sbt \
  && apt-get install -y vim \
  && sbt sbtVersion

WORKDIR /DSSRec
ADD . /DSSRec

RUN sbt clean compile

CMD sbt "run src/main/resources/user_video_watches_10k.csv /tmp/test1.csv"
