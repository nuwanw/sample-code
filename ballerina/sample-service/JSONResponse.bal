import ballerina/http;
import ballerina/io;
import ballerina/runtime;
import ballerina/log;

// Annotations decorate code.
@http:ServiceConfig { basePath: "/hello" }
service hello on new http:Listener(9090) {

    // Decorate the 'hello' resource to accept POST requests.
    //curl -v  -X POST http://localhost:9090/hello
    @http:ResourceConfig { path: "/", methods: ["GET", "POST"] }
    resource function sayHello(http:Caller caller, http:Request request) {
        http:Response response = new;
        json jsonMessage = { "msg": "hello" };
        response.setJsonPayload(jsonMessage);
        var result = caller->respond(response);
        if (result is error) {
            log:printError("Error sending response", err = result);
        }
        return;
    }
}
