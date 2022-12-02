docker network create g5test

docker run --name=mongo-container --rm -d --network=g5test mongo

docker build -t g5 .

docker run --name=g5-container --rm -d -p 8080:8080 --network=g5test g5