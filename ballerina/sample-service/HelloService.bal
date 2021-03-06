import ballerina/http;
import ballerina/io;
import ballerina/runtime;
import ballerina/log;
// Listener endpoint that a service binds to
listener http:Listener endpoint = new(9090);

// Annotations decorate code.
@http:ServiceConfig {basePath: "/hello"}
service hello on endpoint {

    // Decorate the 'hello' resource to accept POST requests.
    //curl -v  -X POST http://localhost:9090/hello
    @http:ResourceConfig { path: "/", methods: ["GET", "POST"] }
    resource function sayHello(http:Caller caller, http:Request request) {
        http:Response response = new;
        xml xmlMessage = xml `<msg>Hello</msg>`;
        response.setXmlPayload(xmlMessage);
        var result = caller->respond(response);
        if (result is error) {
            log:printError("Error sending response", err = result);
        }
        return;
    }
}

