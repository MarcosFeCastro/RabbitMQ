# RabbitMQ Kubernetes Cluster
[Clustering](https://www.rabbitmq.com/clustering.html)
[Kubernetes Peer Discovery](https://www.rabbitmq.com/cluster-formation.html#peer-discovery-k8s)

## Kind
```
$ kind create cluster --name rabbit
```

## Namespace
```
$ kubectl create ns rabbits
```

## Storage Class
```
$ kubectl get storageclass
```

## Deployment
```
$ kubectl apply -n rabbits -f kubernetes/rabbit-rbac.yaml
$ kubectl apply -n rabbits -f kubernetes/rabbit-secret.yaml
$ kubectl apply -n rabbits -f kubernetes/rabbit-configmap.yaml
$ kubectl apply -n rabbits -f kubernetes/rabbit-statefulset.yaml
```

```
$ kubectl -n rabbits get pods
$ kubectl -n rabbits port-forward rabbitmq-0 15672:15672
```

### Applications
```
$ cd ../applications/java/publisher
$ docker build -t marcosfecastro/rabbitmq-java-publisher:v1.0.0 .

$ cd ../applications/java/consumer
$ docker build -t marcosfecastro/rabbitmq-java-consumer:v1.0.0 .
```
```
$ cd ../k8s
$ kubectl apply -n rabbits -f publisher-deployment.yaml
$ kubectl apply -n rabbits -f consumer-deployment.yaml
```
```
$ kubectl -n rabbits get pods
$ kubectl -n rabbits port-forward publisher-deployment-6446f4565b-67qcc 5000:5000
```
```
$ kubectl -n rabbits logs publisher-deployment-6446f4565b-67qcc
```

## Mirrors - [Documentation link](https://www.rabbitmq.com/ha.html)
```
$ kubectl -n rabbits exec -it rabbitmq-0 bash

$ rabbitmqctl set_policy ha-fed ".*" '{ \
        "federation-upstream-set":"all", \
        "ha-sync-mode":"automatic", \
        "ha-mode":"nodes", \
        "ha-params":[ \
            "rabbit@rabbitmq-0.rabbitmq.rabbits.svc.cluster.local", \
            "rabbit@rabbitmq-1.rabbitmq.rabbits.svc.cluster.local", \
            "rabbit@rabbitmq-2.rabbitmq.rabbits.svc.cluster.local" \
        ] \
    }' \
    --priority 1 \
    --apply-to queues
```