# **Class Assignment 4**

Topic of this assignment: Containers with Docker

> The goal of this assignment is to use Docker to setup a containerized environment to execute your version of the gradle version of spring basic tutorial application

---

## **1. Analysis, Design and Implementation**

## **1.1 The tool**

[Docker](https://www.docker.com/) is a popular and open-source container engine that uses the Linux Kernel features to create containers on top of operating systems.

There are many advantages of using containers, but the main one is the possibility to create a self contained environment that can run an application of your choice. This enables an easy way to develop well defined specifications that can be quickly reproduced on any machine.

During software developing this feature is like a gift from heaven: all the necessary requirements to run a software in a easy to use package. Marvelous!

A container has its own dependencies, completely independent from host or other containers.

That's maybe not entirely true because the memory allocation of some resources can be safely shared between containers from the same image. For example, Docker layering helps to manage the problems of scaling: Docker uses internally a copy-to-write mechanism. When you create a new container from an image you only copy the data that is changing in comparison with another container that used the same image.

During the assignment there were some difficulties handling this type of optimization. Similar container share resources and when building some images we can see from the logs that they use some cached information. In some cases when we wanted to clone a repository for a second time, because we pushed something new to it, the _docker build_ command instead of cloning the repo, just uses the one that was in the cache.

To resolve this, the ```docker build --no-cache .``` was used.

Docker is also a great way to replace VMs, in the sense that is a more friendly resource-consumption method. With VM you virtualize a lot of unnecessary resources that aren't needed to run the application you want.

During the class assignment it also noticeable that the modeling of networks is as easy as it can get.

In terms of its architecture, when you install Docker on your machine, you will have a Docker client and a Docker deamon that talk to each other using HTTP protocol. Interesting. The client is used to get information from the deamon or to give instructions to it. Every time the docker command is called on the command line interface (CLI) we are invoking the Docker client.

An important fact: on macOS the Docker _daemon_ just starts to run when the Docker Desktop is started. To this date, there isn't any CLI command to control the it.

Another important information about Docker workflow and structure are the concepts of Dockerfile, image and the container itself.

A Dockerfile is like a configuration file that has the recipe to build a Docker image. Its important that all the instructions are in the correct order. By running _docker build_ command we can create an image.

The Docker image consists of read-only layers. Each instruction in the Dockerfile represents an independent layer. This packs together all the necessities - such as dependencies, source code ... - needed to set up a functional container.

To start a container from a Docker image you use the _docker run_ command.

---

## **1.2 Class assignment**

## **1.2.1 Beginning**

To start the class assignment let's first create the necessary directories and files.

```console
ricardo$ 
.../Switch/devops-20-21-1201779

ricardo$ mkdir ca4
ricardo$ cd !$
ricardo$ mkdir web data db img

ricardo$ touch README.md
ricardo$ touch docker-compose.yml
ricardo$ touch db/Dockerfile
ricardo$ touch web/Dockerfile

ricardo$ ls
README.md		db			img
data			docker-compose.yml	web
```

To answer the requirements of the class assignment we need a folder for the web-server's Dockerfile - web-, a folder for the data-base's Dockerfile - db - and an _yml_ file that defines and runs multi-container Docker applications, docker-compose.yml. This file concentrates all the necessary information to create and start all the needed services.

But let's start with the individual Dockerfile.

---

## **1.2.2 The Dockerfile DB**

### **1.2.2.1 The easy way**

To create an image for our db-server we first need to specify its configurations in a Dockerfile.
The example given during the class is identical to what we need for this assignment.

No tweaks needed!

```dockerfile
FROM ubuntu

RUN apt-get update && \
  apt-get install -y openjdk-8-jdk-headless && \
  apt-get install unzip -y && \
  apt-get install wget -y

RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app/

RUN wget https://repo1.maven.org/maven2/com/h2database/h2/1.4.200/h2-1.4.200.jar

EXPOSE 8082
EXPOSE 9092

CMD java -cp ./h2-1.4.200.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists
```

As already mentioned, Docker runs instructions in a Dockerfile in order.

It must always start with a _FROM_ instruction. This instruction defines a _parent image_ from which we are building, in this case, the data-base server.

So, we first pull an image of an Ubuntu system, and then install the necessary dependencies to download and run our data-base. For that we use the _RUN_ instruction. It will execute any commands in a new layer on top of the current image and commit the results. The next instruction will run on top of that new layer.

Next, we change our working directory by using the _WORKDIR_ instruction.

The _EXPOSE_ instruction informs Docker that the container listens on the specified network ports at runtime. You can specify the port's protocol, but by default is a TCP.

The _CMD_ instructions has the specifics on how to execute the container.
There can only be one _CMD_ instruction in a Dockerfile. If there is more than one, then only the last one will take effect.

### **1.2.2.2 A different way**

But... Let's try another approach.
Maybe pulling an image from DockerHub.

For this case we will chose the [buildo/h2database](https://hub.docker.com/r/buildo/h2database)

Let's pull the image by using the _pull_ 

```console  
ricardo$ docker pull buildo/h2database

Using default tag: latest
latest: Pulling from buildo/h2database
5040bd298390: Pull complete
fce5728aad85: Pull complete
76610ec20bf5: Pull complete
60170fec2151: Pull complete
66b144b1d5b0: Pull complete
6263baad4f89: Pull complete
ff0ae206c9ab: Pull complete
e42eb700ca37: Pull complete
1286769af81f: Pull complete
4600a949b180: Pull complete
Digest: sha256:962c094d93676d060f3e8ef88f81af50d872f9e5fcdc1a0ec2fce5ee5797d009
Status: Downloaded newer image for buildo/h2database:latest
docker.io/buildo/h2database:latest
```

Let's see its [Dockerfile](https://github.com/buildo/docker-h2database) from this image:

```dockerfile
FROM java:7

ENV RELEASE_DATE 2017-04-23
ENV H2DATA /h2-data

RUN curl http://www.h2database.com/h2-$RELEASE_DATE.zip -o h2.zip \
    && unzip h2.zip -d . \
    && rm h2.zip

RUN ln -s $(ls /h2/bin/*jar) /h2/bin/h2.jar

RUN mkdir /docker-entrypoint-initdb.d

VOLUME /h2-data

EXPOSE 8082 9092

COPY docker-entrypoint.sh /usr/local/bin/
ENTRYPOINT ["docker-entrypoint.sh"]

CMD java -cp /h2/bin/h2.jar org.h2.tools.Server \
  -web -webAllowOthers -tcp -tcpAllowOthers -baseDir $H2DATA
```

The image will expose the same ports defined in the docker-compose file presented during the first class. This will make it easier to edit it for this assignment.

```console
ricardo$ docker pull buildo/h2database
Using default tag: latest
latest: Pulling from buildo/h2database
5040bd298390: Pull complete
fce5728aad85: Pull complete
76610ec20bf5: Pull complete
60170fec2151: Pull complete
66b144b1d5b0: Pull complete
6263baad4f89: Pull complete
ff0ae206c9ab: Pull complete
e42eb700ca37: Pull complete
1286769af81f: Pull complete
4600a949b180: Pull complete
Digest: sha256:962c094d93676d060f3e8ef88f81af50d872f9e5fcdc1a0ec2fce5ee5797d009
Status: Downloaded newer image for buildo/h2database:latest
docker.io/buildo/h2database:latest

ricardo$ docker images
REPOSITORY                                  TAG       IMAGE ID       CREATED        SIZE
buildo/h2database                           latest    c9082322bbc8   5 months ago   605MB
```

According to the documentation available, to run this image we use the following command:

> Then in your browser open http://localhost:8082/ and use the following connection parameters:
>
> Driver Class: org.h2.Driver JDBC URL: jdbc:h2:my-db-name User Name: (empty) Password: (empty)

```console
ricardo$ docker run -p 8082:8082 -d buildo/h2database
cd4146574cef26db7a93489449a37977273cf83bcf6d4ac7b6e5baf8bd9c844c

ricardo$ docker ps
CONTAINER ID   IMAGE               COMMAND                  CREATED          STATUS          PORTS                                                 NAMES
069ea5321ce4   buildo/h2database   "docker-entrypoint.s…"   22 seconds ago   Up 21 seconds   0.0.0.0:8082->8082/tcp, :::8082->8082/tcp, 9092/tcp   confident_booth
```

The _-p_ flag in the first command publishes a container's port to the host. We are doing this just to show that the image is working fine.

The _-d_ flag runs the container in background and print container ID

![buildo h2 console](./img/buildo-h2-success.png)

It's working fine.

To stop the container we use the... yes, the _stop_ sub-command.

```console
ricardo$ docker stop 069ea5321ce4
069ea5321ce4

ricardo$ docker ps
CONTAINER ID   IMAGE     COMMAND   CREATED   STATUS    PORTS     NAMES
```

---

## **1.2.3 Dockerfile Web**

### **1.2.3.1 The easy way**

For the web's Dockerfile, I started again to analyse the project that was made available during the first class about containers.

Given that this class assignment has a lot of similarities, it was evident that the Dockerfile for the web-server needed only some changes:

```dockerfile
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
```

Here we are cloning the application from Git and run the _./gradlew_ script to obtain the necessary _war_ file. This package is copied to the _Tomcat_ "appBase", a directory which is configured on a per-Host basis. So _Tomcat_ will deploy this application on start-up. _Tomcat_ is a HTTPS web server that, in contrast to Apache, has the ability to manage Java Servlets and JSP.

### **1.2.3.2 A different way**

But to makes things a little more interesting, I tried to use another approach already mentioned during the class: copy the _war_ file that I have in my machine.

This is a not so versatile approach as the first one, but it will introduce some new information about Docker.

This was the first attempt:

```dockerfile
FROM tomcat

RUN apt-get update -y
RUN apt-get install -f
RUN apt-get install nodejs -y
RUN apt-get install npm -y

RUN mkdir -p /tmp/build

WORKDIR /tmp/build/

COPY ../../ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

EXPOSE 8080
```

I deleted the installation of Git because we are not going to need it.
Instead of the _git clone_, the change of directory and the new _gradle build_, we are just using the _COPY_ instructions from our docker-compose.

This instruction copies new files or directories from the first parameter and adds them to the filesystem of the container at the path defined in the second parameter.

Let's give it a run...

```console
ricardo$ 
.../devops-20-21-1201779/ca4/web

ricardo$ docker build .
[+] Building 4.7s (13/13) FINISHED
 => [internal] load build definition from Dockerfile                                                                                                                                                                                     
 => => transferring dockerfile: 574B                                                                                                                                                                                                     
 => [internal] load .dockerignore                                                                                                                                                                                                        
 => => transferring context: 2B                                                                                                                                                                                                          
 => [internal] load metadata for docker.io/library/tomcat:latest                                                                                                                                                                         4.6s
 => [1/9] FROM docker.io/library/tomcat@sha256:0509684774ac53d8b152f4a50dd92889218a8633c0feddb4b35d092e55cd215d                                                                                                                          
 => [internal] load build context                                                                                                                                                                                                        
 => => transferring context: 2B                                                                                                                                                                                                          
 => CACHED [2/9] RUN apt-get update -y                                                                                                                                                                                                   
 => CACHED [3/9] RUN apt-get install -f                                                                                                                                                                                                  
 => CACHED [4/9] RUN apt-get install git -y                                                                                                                                                                                              
 => CACHED [5/9] RUN apt-get install nodejs -y                                                                                                                                                                                           
 => CACHED [6/9] RUN apt-get install npm -y                                                                                                                                                                                              
 => CACHED [7/9] RUN mkdir -p /tmp/build                                                                                                                                                                                                 
 => CACHED [8/9] WORKDIR /tmp/build/                                                                                                                                                                                                     
 => ERROR [9/9] COPY ../../ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/                                                                                                                
------
 > [9/9] COPY ../../ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/:
------
failed to compute cache key: "/ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war" not found: not found
```

This doesn't work! And the _COPY_ instruction is to blame. 

After some research I stumble upon this information:

> `COPY` has two forms:
>
> `COPY [--chown=<user>:<group>] <src>... <dest>`
> 
> `COPY [--chown=<user>:<group>] ["<src>",... "<dest>"]`
> (...)

> `COPY` obeys the following rules:
>
>The `<src>` path must be inside the context of the build; you cannot `COPY` ../something /something, because the first step of a docker build is to send the context directory (and subdirectories) to the docker daemon.
>
>If `<src>` is a directory, the entire contents of the directory are copied, including filesystem metadata.

[source](https://docs.docker.com/engine/reference/builder/#copy)

So, the only way to make this work, is to change the build _source_.
To be able to access the _war_ file and the Dockerfile, I defined the repository main folder as the _source_.

```console
. devops-20-21-1201779
│
├── ca1
├── ca2
├── ca3
└── ca4
    ├── README.md
    ├── data
    ├── db
    │   └── Dockerfile
    ├── docker-compose.yml
    ├── img
    └── web
        └── Dockerfile
```

This made me change the above _COPY_ command for the following line:

```dockerfile
COPY ./ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/
```

And the _build_ command must now use the _-f_ flag to specify the Dockerfile's location, because we are running the command in another location.

Let´s try again!

```console
ricardo$ 
.../devops-20-21-1201779

ricardo$ docker build -f ca4/web/Dockerfile -t web_devops .
[+] Building 5.6s (14/14) FINISHED
 => [internal] load build definition from Dockerfile                                                                                                                                                                                     
 => => transferring dockerfile: 570B                                                                                                                                                                                                     
 => [internal] load .dockerignore                                                                                                                                                                                                        
 => => transferring context: 2B                                                                                                                                                                                                          
 => [internal] load metadata for docker.io/library/tomcat:latest                                                                                                                                                                         
 => [1/9] FROM docker.io/library/tomcat@sha256:0509684774ac53d8b152f4a50dd92889218a8633c0feddb4b35d092e55cd215d                                                                                                                          
 => [internal] load build context                                                                                                                                                                                                        
 => => transferring context: 42.04MB                                                                                                                                                                                                     
 => CACHED [2/9] RUN apt-get update -y                                                                                                                                                                                                   
 => CACHED [3/9] RUN apt-get install -f                                                                                                                                                                                                  
 => CACHED [4/9] RUN apt-get install git -y                                                                                                                                                                                              
 => CACHED [5/9] RUN apt-get install nodejs -y                                                                                                                                                                                           
 => CACHED [6/9] RUN apt-get install npm -y                                                                                                                                                                                              
 => CACHED [7/9] RUN mkdir -p /tmp/build                                                                                                                                                                                                 
 => CACHED [8/9] WORKDIR /tmp/build/                                                                                                                                                                                                     
 => CACHED [9/9] COPY ./ca2/part2/tut-basic-ca2/build/libs/tut-basic-ca2-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/                                                                                                                   
 => exporting to image                                                                                                                                                                                                                   
 => => exporting layers                                                                                                                                                                                                                  
 => => writing image sha256:bd2f0d08bbe58521374408676c1742ae892bfa0cb4a75b7eeb47fe32ff28a267                                                                                                                                             

ricardo$ docker images

REPOSITORY          TAG       IMAGE ID       CREATED        SIZE
web-image           latest    bd2f0d08bbe5   20 hours ago   1GB
buildo/h2database   latest    c9082322bbc8   5 months ago   605MB
```

Perfect!

---

## **1.2.4 Docker Compose file**

Here is the _docker-compose_ file to handle the Dockerfiles mentioned above.

```dockerfile
version: '3'
services:
  web:
    build:
        context: ..
        dockerfile: ca4/web/Dockerfile
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.33.10
    depends_on:
      - "db"
  db:
    image: buildo/h2database
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data
    networks:
      default:
        ipv4_address: 192.168.33.11
networks:
  default:
    ipam:
      driver: default
      config:
        - subnet: 192.168.33.0/24
```

As we talked in the chapter about the Dockerfile for the web-server, the source of the build command must be a common parent of the files we need to use. So, the _build: context:_ was specially defined here.

To start the network and containers define in the docker-compose.yml file we run the following command:

```console
ricardo$ docker-compose up
...
```

This command builds, (re)creates and starts the containers for a service.
If you add a _-d_ to the command it will run in _daemon_ mode, in the background.

Let's see if it is working.

The _ps_ sub-command enable us to seen the status of the containers. As we see above, booth defined containers are running.

```console
ricardo$ docker-compose ps
  Name                 Command              State              Ports
--------------------------------------------------------------------------------
ca4_db_1    docker-entrypoint.sh /bin/      Up      0.0.0.0:8082->8082/tcp,:::80
            ...                                     82->8082/tcp, 0.0.0.0:9092->
                                                    9092/tcp,:::9092->9092/tcp
ca4_web_1   catalina.sh run                 Up      0.0.0.0:8080->8080/tcp,:::80
                                                    80->8080/tcp
```

Now _o teste do algodão_!

![web success](img/web-success.png)
![h2 console success](img/h2-console-success.png)
![h2 success](img/h2-success.png)

To stop this service we use the ... yes, the _stop_ sub-command:

```console
ricardo$ docker-compose stop
Stopping ca4_web_1 ... done
Stopping ca4_db_1  ... done

ricardo$ docker-compose ps
  Name                 Command                State     Ports
-------------------------------------------------------------
ca4_db_1    docker-entrypoint.sh /bin/ ...   Exit 137
ca4_web_1   catalina.sh run                  Exit 143
```

Let's start the services again, just for the fun.

```console
ricardo$ docker-compose start
Starting db  ... done
Starting web ... done
```

---

## **1.2.5 Push my docker images to Docker Hub**

## **1.2.5.1 Create a DockerHub account**

First we need to create a [DockerHub](https://hub.docker.com/) account. This works as GitHub but for Docker images.

![Sign-up](./img/dockerhub-signup.png)

Its a simple and painless process.

## **1.2.5.2 Push images**

To recap, let's see our images that were created during the docker-compose

```console
ricardo$ docker-compose images
REPOSITORY          TAG       IMAGE ID       CREATED        SIZE
ca4_web             latest    27c25316d640   35 hours ago   1GB
buildo/h2database   latest    c9082322bbc8   5 months ago   605MB
```

Through the CLI, we first need to log into Docker Hub and enter the username and password that were defined during the registration.

```console
ricardo$ docker login
Login with your Docker ID to push and pull images from Docker Hub. If you don't have a Docker ID, head over to https://hub.docker.com to create one.
Username: *******
Password: *******
Login Succeeded
```

Now, we just need to push the desired images.
That simple.

```console
ricardo$ docker push ca4_web
Using default tag: latest
The push refers to repository [docker.io/library/ca4_web]
20564ec9873b: Preparing
5f70bf18a086: Preparing
84f3e4184a80: Preparing
4972648f3580: Preparing
7b00578e0bcf: Preparing
64434d1f5cf3: Waiting
e0ce91071174: Waiting
e3fdfa5b02fb: Waiting
daf63ef0ddbb: Waiting
3307ffa538c1: Waiting
8f8b5acac684: Waiting
15786a1cf1cb: Waiting
6f770cdc9ebf: Waiting
3fc095fab4a2: Waiting
685934357c89: Waiting
ccb9b68523fd: Waiting
00bcea93703b: Waiting
688e187d6c79: Waiting
denied: requested access to the resource is denied
```

Not that simple after all.

After some research, it turns out that you need to include the namespace for Docker Hub to associate it with your account.

For that I needed to use the _docker tag_ command:

```console
ricardo$ docker tag ca4_web isepmendes/devops2020:web_image

ricardo$ docker images
REPOSITORY              TAG         IMAGE ID       CREATED        SIZE
isepmendes/devops2020   web_image   27c25316d640   35 hours ago   1GB
ca4_web                 latest      27c25316d640   35 hours ago   1GB
buildo/h2database       latest      c9082322bbc8   5 months ago   605MB
```

Let's try again.

```console
ricardo$ docker push isepmendes/devops2020:web_image
The push refers to repository [docker.io/isepmendes/devops2020]
20564ec9873b: Pushed
5f70bf18a086: Pushed
84f3e4184a80: Pushed
4972648f3580: Pushed
7b00578e0bcf: Pushed
64434d1f5cf3: Pushed
e0ce91071174: Pushed
e3fdfa5b02fb: Pushed
daf63ef0ddbb: Pushed
3307ffa538c1: Pushed
8f8b5acac684: Pushed
15786a1cf1cb: Pushed
6f770cdc9ebf: Pushed
3fc095fab4a2: Pushed
685934357c89: Pushed
ccb9b68523fd: Pushed
00bcea93703b: Pushed
688e187d6c79: Pushed
web_image: digest: sha256:74c77fc8489e079d22183536ecb1c6f66ac5bfeb379786652b78ac0eba0c1830 size: 4096
```

Voilá.

Now it works.
The data base image that we pulled from Docker Hub stays were it was.

![docker-hub-images](img/docker-hub-images.png)

## **1.2.6 Backup DB**

An approach would be to actually SSH into the container and see the file, but sometimes we need to choose the lazy way.

Because I don't know exactly the name of the file that I want to make a backup from, I tried first using some _regular expressions_ to retrieve it from the container.

So, I know my working directory from the Dockerfile, and I know to which folder I need to copy the data base so that I can access it from may host machine - defined in our docker-compose file in the volume section.

```console
ricardo$ docker exec -it ca4_db_1 cp *.db ../data/
zsh: no matches found: *.db
```

This doesn't work because the _regular expressions_ is first being expanded by my shell - in the host machine - before that shell starts a program. So, to bypass this problem , we need to first run the shell and then define the command that needs to be run. Like this:

```console
ricardo$ docker exec -it ca4_db_1 /bin/sh -c 'cp *.db ../data/'
ricardo$ ls ./data
jpadb.mv.db
```

Success!

There is another possibility to retrieve the file. The _docker cp_ command would also be a possible solution for this problem.

But it doesn't make use of the volume as it is a requirement of this assignment.

```console
ricardo$ docker cp ca4_db_1:/usr/src/app/jpadb.mv.db ./data
```

The _docker cp_ command assumes container paths are relative to the container’s / (root) directory. So we need to be a little more specific.

---

## ****1.2.7 Other interesting information**

During the first class about containers, we started a service that assigned some IP to some containers. Through the configuration from the docker-compose file a new network is created.

The _docker network ls_ command show us the created networks.

During the class assignment I had some problems because the given network address was the same. To make the address available and instead of assigning a new one for this class assignment I removed the network by using the _docker network rm networkID_ command.

---

## **2. Analysis of an Alternative**

## **2.1 Heroku**

[Heroku](https://www.heroku.com) is a container-based cloud Platform as a Service (PaaS). It is essentially Docker containers in the cloud.

It can be used to deploy, manage and scale your applications.

It doesn't need infrastructure maintenance. That is a great thing, if you want it to be quick and easy to use. In its website they state that "they are the fastest way to go from idea to URL, bypassing all those infrastructure headaches." This summarize pretty well what this service should offer.

There are some similarities and analogies that we can make between Heroku and Docker.

Booth have a repository where you can push and pull container images. For Docker is Docker Hub and for Heroku is Heroku Container Registry.

For Heroku it allows you to deploy your Docker images to Heroku. And that's what we want to do in this class assignment.

Other similarities: the cloud containers are called _Dynos_, and the images are called _Slugs_. There is also an counterpart for the Dockerfile called _BuildPack_.

All this resemblance is because the technology behind Heroku is essentially the same as in Docker: all the Linux technologies to support containerization of applications are also used, in some way, in the Heroku's _Dynos_.

---

## **3. Implementation of the Alternative**

## **3.1 Install the Heroku CLI**

First of all we will install the Heroku CLI to be able to work through the command line interface.

To installed on my machine I followed this [article](https://devcenter.heroku.com/articles/heroku-cli#download-and-install).

So on macOS we will be using the [Homebrew](https://brew.sh/) as our Package Manager.

```console
ricardo$ brew tap heroku/brew && brew install heroku
...

==> heroku
To use the Heroku CLI's autocomplete --
  Via homebrew's shell completion:
    1) Follow homebrew's install instructions https://docs.brew.sh/Shell-Completion
        NOTE: For zsh, as the instructions mention, be sure compinit is autoloaded
              and called, either explicitly or via a framework like oh-my-zsh.
    2) Then run
      $ heroku autocomplete --refresh-cache
  OR
  Use our standalone setup:
    1) Run and follow the install steps:
      $ heroku autocomplete

zsh completions have been installed to:
  /usr/local/share/zsh/site-functions
```

After installing the Heroku CLI program we need to create an account. If we run the Heroku's sub-command _login_ we will be asked to open the browser to do the login, or in this case, the sign up.

```console
ricardo$ heroku login
 ›   Warning: Our terms of service have changed: https://dashboard.heroku.com/terms-of-service
heroku: Press any key to open up the browser to login or q to exit:
```

![heroku sign-in](img/heroku-signin.png)

After the registration, the web page will alert uns that now we can return to the CLI.

![heroku logged](img/heroku-logged.png)

```console
Logging in... done
Logged in as 1201779@isep.ipp.pt
```

If you are already registered and want to stay in the CLI, you just need to run the same login command but with the _-i_ flag, as shown bellow; 'i' is for interactive.

```console
ricardo$ heroku login -i
```

Now we are ready to use Heroku.

### **3.2 Create an Heroku app**

Now we need to create an Heroku app.

Navigate to the app’s directory, in this case the location of the Dockerfile for the web server, and create a Heroku app with the following command:

```console
ricardo$ 
.../devops-20-21-1201779/ca4/alternative/web

ricardo$ heroku create devops-ca4
Creating ⬢ devops-ca4... done
https://devops-ca4.herokuapp.com/ | https://git.heroku.com/devops-ca4.git
```

In the _create_ sub-command we can specify the app name by given it an argument.

If we go to our Heroku's web page, we can see that an app was created.

![heroku create](img/heroku-create.png)

Another way to visualize our apps is running the _heroku apps_ command.

```console
ricardo$ heroku apps
=== 1201779@isep.ipp.pt Apps
devops-ca4 (eu)
```

### **3.3 Push the image to Heroku**

To push the image to Heroku Container Registry, we also need to log in to it.
For that we use the following command:

```console
ricardo$ heroku container:login
Login Succeeded 
```

Now it should be possible to push an image to Heroku and get it deployed.

```console
ricardo$ heroku container:push web --app ca4-web
...
latest: digest: sha256:e35e9f754fa7dd2d116b64a0767bd1edf54bbc020af12cf7655a61a3acf0592f size: 4728
Your image has been successfully pushed. You can now release it with the 'container:release' command.
```

This command builds, then pushes Docker images to deploy your Heroku app. That means we don´t need to build an image from the Docker file. It does it automatically.

But attention. For this to work, the Docker _daemon_ needs to be running on your machine.

### **3.4 Deploy**

To deploy the container there is the _release_ sub-command.

```console
ricardo$ heroku container:release web -a devops-ca4
Releasing images web to devops-ca4... done
```

And here is were the problems start.

To see the result of the deployment we can use the _open_ sub-command that opens the application in our browser.

```console
ricardo$ heroku open -a devops-ca4
```

After some seconds with the page just loading - this because the container is being started for the first time - the page loads this:

![heroku error](img/heruko-error.png)

Disappointing! But let's see the log.

```console
ricardo$ heroku logs --tail -a devops-ca4

...
2021-05-17T18:11:21.188471+00:00 app[web.1]:
2021-05-17T18:11:21.188851+00:00 app[web.1]: .   ____          _            __ _ _
2021-05-17T18:11:21.188921+00:00 app[web.1]: /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
2021-05-17T18:11:21.188975+00:00 app[web.1]: ( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
2021-05-17T18:11:21.189039+00:00 app[web.1]: \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
2021-05-17T18:11:21.189079+00:00 app[web.1]: '  |____| .__|_| |_|_| |_\__, | / / / /
2021-05-17T18:11:21.189136+00:00 app[web.1]: =========|_|==============|___/=/_/_/_/
2021-05-17T18:11:21.201267+00:00 app[web.1]: :: Spring Boot ::        (v2.2.5.RELEASE)
2021-05-17T18:11:21.201316+00:00 app[web.1]:
2021-05-17T18:11:22.892013+00:00 app[web.1]: 2021-05-17 18:11:22.886  INFO 4 --- [           main] c.g.payroll.ServletInitializer           : Starting ServletInitializer on 1b3505cb-dd5e-434e-998f-ff9c6fa70762 with PID 4 (/usr/local/tomcat/webapps/basic-0.0.1-SNAPSHOT/WEB-INF/classes started by u22606 in /usr/local/tomcat)
2021-05-17T18:11:22.948407+00:00 app[web.1]: 2021-05-17 18:11:22.948  INFO 4 --- [           main] c.g.payroll.ServletInitializer           : No active profile set, falling back to default profiles: default
2021-05-17T18:11:31.535741+00:00 app[web.1]: 2021-05-17 18:11:31.526  INFO 4 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
2021-05-17T18:11:31.910621+00:00 app[web.1]: 2021-05-17 18:11:31.910  INFO 4 --- [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 314ms. Found 1 JPA repository interfaces.
2021-05-17T18:11:38.583941+00:00 app[web.1]: 2021-05-17 18:11:38.578  INFO 4 --- [           main] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 14832 ms
2021-05-17T18:11:39.680663+00:00 heroku[web.1]: Process running mem=545M(106.6%)
2021-05-17T18:11:39.682633+00:00 heroku[web.1]: Error R14 (Memory quota exceeded)
2021-05-17T18:11:43.681495+00:00 app[web.1]: 2021-05-17 18:11:43.681  INFO 4 --- [           main] com.zaxxer.hikari.HikariDataSource       : HikariPool-1 - Starting...
2021-05-17T18:11:45.098611+00:00 heroku[web.1]: Error R10 (Boot timeout) -> Web process failed to bind to $PORT within 60 seconds of launch
2021-05-17T18:11:45.264617+00:00 heroku[web.1]: Stopping process with SIGKILL
2021-05-17T18:11:45.642946+00:00 heroku[web.1]: Process exited with status 137
2021-05-17T18:11:45.758349+00:00 heroku[web.1]: State changed from starting to crashed
2021-05-17T18:11:46.765927+00:00 heroku[router]: at=error code=H10 desc="App crashed" method=GET path="/" host=cry2getter.herokuapp.com request_id=4fcdc377-05c3-4e68-8dfe-73eebe2eeca7 fwd="78.130.18.25" dyno= connect= service= status=503 bytes= protocol=https
2021-05-17T18:11:47.288801+00:00 heroku[router]: at=error code=H10 desc="App crashed" method=GET path="/favicon.ico" host=cry2getter.herokuapp.com request_id=e7ae569b-27e9-4ba2-8a6a-3f093c42aa10 fwd="78.130.18.25" dyno= connect= service= status=503 bytes= protocol=https
```

From here we can identify two types of errors:

- R14 - memory quota exceeded
- R10 - (Boot timeout) -> Web process failed to bind to $PORT within 60 seconds of launch

About the R14 error we can do little. Heroku for free costumers limits the _Dynos_ memory to max 512Mb.

About the R10 error, we can try to connect it to a data-base. Maybe a quick connection to the data base will ease initialization of Spring Boot.

### **3.5 Addons**

Heroku has addons. These are services integrated into Heroku that can be used by our applications.

To try to tackle the R10 error, we will try to add the Heroku Postgres Data Base.
Some research pointed that the defined H2 for this app, also supports the usage of the PostgresSQL.

To add this service to our app, we went to Heroku's web site.

![heroku postgres](img/heroku-postgres.png)

We need also to obtain the data base url.

And there is a command for that:

```console
ricardo$ heroku config:get DATABASE_URL -a devops-ca4
postgres://xmlyyxaffkkhlq:3288ab33cd93cf9bbb781b99a7b614181107f30df2942f0fb9673e9ddabc170c@ec2-34-193-113-223.compute-1.amazonaws.com:5432/d101q3gfo6uurj
```

According to the documentation:

> Heroku apps use the DATABASE_URL config var to designate the URL of an app’s primary database. If your app only has one database, its URL is automatically assigned to this config var.
>
>For apps with multiple databases, you can set the primary database with the heroku pg:promote command:
[https://devcenter.heroku.com/articles/heroku-postgresql](https://devcenter.heroku.com/articles/heroku-postgresql)

To be certain that we are connection our container to the data base, we will follow the second instructions.

```console
ricardo$ heroku pg:promote postgresql-opaque-47991 -a devops-ca4
Ensuring an alternate alias for existing DATABASE_URL... !
▸    postgresql-opaque-47991 is already promoted on ⬢ devops-ca4
```

### **3.7 Last attempt**

So we try it again...

```console
ricardo$ heroku container:release web -a devops-ca4
Releasing images web to devops-ca4... done

ricardo$ heroku open -a devops-ca4
```

And the same result:

![heroku error](img/heruko-error.png)

... with the same error messages.

This was supposed to be simple. The concept is simple, but unfortunately I couldn't put it to work.

---

## **4. An alternative for the alternative**

## **4.1 Digital Ocean**

[Digital Ocean](https://www.digitalocean.com/), aka Ocean's Eleven, is also a cloud infrastructure provider. It provides developers cloud services that help to deploy and scale applications that run simultaneously on multiple computers.

So there are some similarities with Heroku, because recently they have the possibility to create app just like with Heroku. The only difference is the lack of services that you can attach to this app automatically.

Another disadvantage, is that it's a payed service... but for students they give an 100$ credit. Perfect for the assignment.

![Digital Ocean](img/ocean-main.png)

## **4.2 Push the images**

For this alternative, we will not be using the CLI.

So, to start an app we choose the _Create_ option:

![Ocean create](img/ocean-options.png)

To create an app we can choose between three sources. For this class assignment we will use Docker Hub of course.

![Ocean source](img/ocean-souce.png)

After that we just need to specify the repository and the tag of the image.

![Ocean docker](img/ocean-docker.png)

Simple!

## **4.3 Connection to the data base**

Ocean's Eleven is a kind of container orchestration and a stable IP for the data base container is never ensured.

To be able to link the web-server to the data base we need to change some lines in the application.properties file.

```properties
#spring.datasource.url=jdbc:h2:tcp://192.168.33.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
#Use with Ocean Eleven, aka Digital Ocean
spring.datasource.url=jdbc:h2:tcp://db/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
```

The name of our data base container and the TCP port was added to the file.

## **4.4 Starting the app**

After adding both images that we pushed to Docker Hub during the first part of this assignment and editing some files, we are ready to see if it works.

![Ocean ready](img/ocean-ready.png)

Ocean's Eleven makes a url available to access our app.

By clicking on it, we are redirected to a web page were the can see our app working as it should.

![Ocean success](img/ocean-web-1.png)
![Ocean success](img/ocean-web-2.png)
![Ocean success](img/ocean-web-3.png)
![Ocean success](img/ocean-web-4.png)

The data base is also working.

Success!

THE END