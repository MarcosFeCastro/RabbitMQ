# RabbitMQ
## Running with Docker - [Docker Hub](https://hub.docker.com/_/rabbitmq)
```
$ docker network create rabbits
$ docker run -d --rm --net rabbits --hostname rabbit-1 --name rabbit-1 -p 5672:5672 -p 15672:15672 rabbitmq:3.9.8-management
```
RabbitMQ needs to know the instance's name to find each other, so remenber to set the host name.
Run as a StatfullSet on Kubernetes.

## Rabbit CLI
```
$ docker exec -it rabbit-1 bash
$ rabbitmqctl
```

## Rabbit Management
### If Management is disable
```
$ rabbitmq-plugins list
$ rabbitmq-plugins enable rabbitmq_management
```
### Default Login
username: guest  
password: guest  

## Java publisher application
```
$ cd applications/java/publisher
$ ./gradlew clean build
```
### Build image
```
$ docker image build -t marcosfecastro/publisher:v1.0.0 .
```
### Run
```
$ docker run --rm -p 5000:5000 --link rabbits marcosfecastro/publisher:v1.0.0
```

## Java consumer application
```
$ cd applications/java/consumer
$ ./gradlew clean build
```
### Build image
```
$ docker image build -t marcosfecastro/consumer:v1.0.0 .
```
### Run
```
$ docker run --rm -p 5050:5050 --link rabbits marcosfecastro/consumer:v1.0.0
```

# Docker Compose
```
$ docker-compose up --build
```

# RabbitMQ Cluster

## Manually with rabbitmqctl
```
$ docker network create rabbits
$ docker secret create erlang-cookie-secret $HOME/.erlang.cookie
```
```
$ docker run -d --rm --net rabbits --hostname rabbit-1 --name rabbit-1 -p 15672:15672 -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE rabbitmq:3.9.8-management
$ docker run -d --rm --net rabbits --hostname rabbit-2 --name rabbit-2 -p 15673:15672 -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE rabbitmq:3.9.8-management
$ docker run -d --rm --net rabbits --hostname rabbit-3 --name rabbit-3 -p 15674:15672 -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE rabbitmq:3.9.8-management
```
```
$ docker exec -it rabbit-1 rabbitmqctl cluster_status
```
```
$ docker exec -it rabbit-2 rabbitmqctl stop_app
$ docker exec -it rabbit-2 rabbitmqctl reset
$ docker exec -it rabbit-2 rabbitmqctl join_cluster rabbit@rabbit-1
$ docker exec -it rabbit-2 rabbitmqctl start_app
$ docker exec -it rabbit-2 rabbitmqctl cluster_status
```
```
$ docker exec -it rabbit-3 rabbitmqctl stop_app
$ docker exec -it rabbit-3 rabbitmqctl reset
$ docker exec -it rabbit-3 rabbitmqctl join_cluster rabbit@rabbit-1
$ docker exec -it rabbit-3 rabbitmqctl start_app
$ docker exec -it rabbit-3 rabbitmqctl cluster_status
```

## Decleratively by listing cluster nodes in config file
[Documentation link](https://www.rabbitmq.com/configure.html)
[Cluster Formation](https://www.rabbitmq.com/cluster-formation.html)

```
$ docker run -d --rm --net rabbits --hostname rabbit-1 --name rabbit-1 \
    -p 15672:15672 \
    -v ${PWD}/config/rabbit-1/:/config/ \
    -e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
    -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE \
    rabbitmq:3.9.8-management

$ docker run -d --rm --net rabbits --hostname rabbit-2 --name rabbit-2 \
    -p 15673:15672 \
    -v ${PWD}/config/rabbit-2/:/config/ \
    -e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
    -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE \
    rabbitmq:3.9.8-management

$ docker run -d --rm --net rabbits --hostname rabbit-3 --name rabbit-3 \
    -p 15674:15672 \
    -v ${PWD}/config/rabbit-3/:/config/ \
    -e RABBITMQ_CONFIG_FILE=/config/rabbitmq \
    -e RABBITMQ_ERLANG_COOKIE=COOKIEVALUE \
    rabbitmq:3.9.8-management
```

# RabbitMQ Mirrors -[Documentation link](https://www.rabbitmq.com/ha.html)
```
$ docker exec -it rabbit-1 rabbitmq-plugins enable rabbitmq_federation
$ docker exec -it rabbit-2 rabbitmq-plugins enable rabbitmq_federation
$ docker exec -it rabbit-3 rabbitmq-plugins enable rabbitmq_federation
```
```
$ docker exec -it rabbit-1 bash

$ rabbitmqctl set_policy ha-fed ".*" '{ \
        "federation-upstream-set":"all", \
        "ha-sync-mode":"automatic", \
        "ha-mode":"nodes", \
        "ha-params":["rabbit@rabbit-1","rabbit@rabbit-2","rabbit@rabbit-3"] \
    }' \
    --priority 1 \
    --apply-to queues
```
