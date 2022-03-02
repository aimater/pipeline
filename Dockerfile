FROM debian:stretch
RUN apt-get update && apt-get install -y openjdk-8-jdk \
                                         maven \
                                         golang \
                                         ruby \
                                         build-essential \
                                         zip \
                                         libxml2-utils \
                                         git \
                                         curl \
                                         lame \
                                         espeak
RUN gem install bundle
# the following lines are only for 64-bit systems, change to i368 if needed
RUN curl -L -o /tmp/#1 http://ftp.br.debian.org/debian/pool/main/n/nsis/{nsis-common_3.04-1_all.deb,nsis_3.04-1_amd64.deb}
RUN dpkg -i /tmp/nsis*.deb
ADD . /pipeline
WORKDIR /pipeline
RUN make clean
RUN eval $(make dump-maven-cmd)
RUN mvn package -pl modules/tts/tts-adapters/tts-adapter-ilona -am
RUN mvn install -pl modules/tts/tts-adapters/tts-adapter-ilona -am
RUN make dist-deb
