import ballerina/http;
import ballerina/io;
import ballerina/runtime;
// Listener endpoint that a service binds to
endpoint http:Listener listener {
  port:9090
};

// Annotations decorate code.
// Change the service URL base to '/greeting'.
@http:ServiceConfig {
    basePath:"/sleep"
}
service<http:Service> greeting bind listener {

  // Decorate the 'greet' resource to accept POST requests.
  @http:ResourceConfig{
    path: "/",
    methods: ["POST"]
  }
  greet (endpoint caller, http:Request request) {
    http:Response response = new;

    // Check statement matches the output type of the
    // getPayloadAsString method to a string. If not it throws
    // an error.
    xml reqPayload = check request.getXmlPayload();
    io:println("Sleeping..." + reqPayload.getTextValue());
    int count = check <int>reqPayload.getTextValue();
    runtime:sleep(count);
    xml xmlMessage = xml `<msg>Hello</msg>`;
    response.setXmlPayload(xmlMessage);
    _ = caller -> respond(response);
  }
}

