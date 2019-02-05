import ballerina/http;
import ballerina/io;
import ballerina/runtime;
// Listener endpoint that a service binds to
endpoint http:Listener listener {
  port:9090
};

// Annotations decorate code.
@http:ServiceConfig {
    basePath:"/sleep"
}
service<http:Service> greeting bind listener {

  //curl -v  -X POST http://localhost:9090/sleep -H "Content-Type:application/xml" -d "<sleep>1000</sleep>"
  @http:ResourceConfig{
    path: "/",
    methods: ["POST"]
  }
  sleepPost (endpoint caller, http:Request request) {
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
// Decorate the 'greet' resource to accept POST requests.
//curl -v http://localhost:9090/sleep/time/2000
  @http:ResourceConfig{
    path: "/time/{count}",
    methods: ["GET"]
  }
  sleepGet (endpoint caller, http:Request request, int count) {
    http:Response response = new;

    io:println("Sleeping..." + count);
    runtime:sleep(count);
    xml xmlMessage = xml `<msg>Hello</msg>`;
    response.setXmlPayload(xmlMessage);
    _ = caller -> respond(response);
  }

}

