import ballerina/http;
import ballerina/io;
import ballerina/runtime;
// Listener endpoint that a service binds to
endpoint http:Listener listener {
  port:9090
};

// Annotations decorate code.
@http:ServiceConfig {
    basePath:"/hello"
}
service<http:Service> greeting bind listener {

  // Decorate the 'hello' resource to accept POST requests.
  //curl -v  -X POST http://localhost:9090/hello
  @http:ResourceConfig{
    path: "/",
    methods: ["GET","POST"]
  }
  hello (endpoint caller, http:Request request) {
    http:Response response = new;
    xml xmlMessage = xml `<msg>Hello</msg>`;
    response.setXmlPayload(xmlMessage);
    _ = caller -> respond(response);
  }

}

