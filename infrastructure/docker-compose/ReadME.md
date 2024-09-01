docker-compose -f .\common.yaml -f .\zookeeper.yaml up

docker-compose -f .\common.yaml -f .\kafka_cluster.yaml up

docker-compose -f .\common.yaml -f .\init_kafka.yaml up


http://localhost:9000/ 
    - add clusters = food-ordering-system-cluster
    - add zookeeper hosts = zookeeper:2181

[//]: # (    - enable offsetCache)