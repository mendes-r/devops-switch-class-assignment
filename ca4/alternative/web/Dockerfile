FROM tomcat

RUN apt-get update -y
RUN apt-get install -f
RUN apt-get install git -y
RUN apt-get install nodejs -y
RUN apt-get install npm -y

RUN mkdir -p /tmp/build

WORKDIR /tmp/build/

RUN git clone https://mendes-r@bitbucket.org/mendes-r/devops-20-21-1201779.git

WORKDIR /tmp/build/devops-20-21-1201779/ca2/part2/tut-basic-ca2

RUN ./gradlew clean build
RUN cp build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

EXPOSE 8080
