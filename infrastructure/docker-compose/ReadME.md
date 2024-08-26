docker-compose -f .\common.yaml -f .\zookeeper.yml up

docker-compose -f .\common.yaml -f .\kafka_cluster.yml up

docker-compose -f .\common.yaml -f .\init_kafka.yml up


http://localhost:9000/ 
    - add clusters = food-ordering-system