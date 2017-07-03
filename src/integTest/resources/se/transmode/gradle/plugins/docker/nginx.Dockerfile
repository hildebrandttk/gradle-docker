FROM ubuntu
MAINTAINER Guillaume J. Charmes "guillaume@dotcloud.com"
RUN echo "deb http://archive.ubuntu.com/ubuntu precise main universe" > /etc/apt/sources.list
RUN apt-get update
RUN apt-get install -y inotify-tools nginx apache2 openssh-server
HEALTHCHECK \
  CMD /healthCheck.sh
HEALTHCHECK --interval=1s --timeout=5s \
  CMD curl -f http://localhost/ || exit 1
HEALTHCHECK --interval=1s --timeout=300s --start-period=3600s --retries=5 \
  CMD /bin/false
