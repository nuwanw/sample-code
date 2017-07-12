java -jar Hello-Service-0.1-SNAPSHOT.jar

netty.yaml
listenerConfigurations:
-
 id: "jaxrs-http"
 host: "127.0.0.1"
 port: 8888
 bossThreadPoolSize: 2
 workerThreadPoolSize: 250
 enableDisruptor: false
 parameters:
  -
   name: "executor.workerpool.size"
   value: 60

java -jar -Dtransports.netty.conf=netty.yaml Hello-Service-0.1-SNAPSHOT.jar

Request:
curl -X POST -H "Content-Type:application/json" -d "{"name":"nuwanw"}" http://127.0.0.1:8888/service/hello

Response:
{"msg":"hello nuwanw"}