FROM tomcat

RUN apt-get update -y
RUN apt-get install -f
RUN apt-get install nodejs -y
RUN apt-get install npm -y

RUN mkdir -p /tmp/build

WORKDIR /tmp/build/

COPY ./ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

EXPOSE 8080