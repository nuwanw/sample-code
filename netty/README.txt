Copy the public.crt and private.key into same location and point the response file,

http chunk length will vary from 6000 to 9999
java -Xmx4096m -Dresponse.file.path=100kb-payload.json -jar netty-close-backend-connection-jar-with-dependencies.jar


java -Xmx4096m -Dresponse.file.path=100kb-payload.json -Dchunk.length=9446  -jar target/netty-close-backend-connection-jar-with-dependencies.jar
