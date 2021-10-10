import ballerina/http;
import ballerina/log;
import ballerina/runtime;
import ballerina/lang.'int as ints;
//import ballerina/io;

// By default, Ballerina exposes an HTTP service via HTTP/1.1.
// Annotations decorate code.
@http:ServiceConfig { basePath: "/sleep" }
service sleep on new http:Listener(9090) {

    // Resource functions are invoked with the HTTP caller and the incoming request as arguments.
    //curl -v http://localhost:9090/sleep -H "Content-Type:application/xml" -d "<sleep>2000</sleep>"
    @http:ResourceConfig { path: "/", methods: ["POST"] }
    resource function doSleepOnPost(http:Caller caller, http:Request request) {
        http:Response response = new;
        xml|error reqPayload =  request.getXmlPayload();
        xml xmlPayload  = <xml> reqPayload;
//        log:printInfo(xmlPayload/*);
        xml countValueXml = xmlPayload/*;
        string countValue = countValueXml.toString();
        log:printInfo("Sleeping..." + countValue);
       //int|error count = int.convert(countValue);
        int|error count = ints:fromString(countValue);
        if (count is int) {
            runtime:sleep(<int>count);
            xml xmlMessage = xml `<msg>Hello</msg>`;
            response.setXmlPayload(xmlMessage);
            var result = caller->respond(response);
            if (result is error) {
                log:printError("Error sending response", err = result);
            }
        } else {
            log:printError("Error casting value response", err = count);
            xml xmlMessage = xml `<msg>Error</msg>`;
            response.setXmlPayload(xmlMessage);
            var result = caller->respond(response);
        }
    }

    //curl -v http://localhost:9090/sleep/time/2000
    @http:ResourceConfig {
        path: "/time/{count}",
        methods: ["GET"]
    }
    resource function sleepOnGet(http:Caller caller, http:Request request, int count) {
        http:Response response = new;
        //log:printInfo("Sleeping..." + count);
        runtime:sleep(count);
        xml xmlMessage = xml `<msg>Hello</msg>`;
        response.setXmlPayload(xmlMessage);
        var result = caller->respond(response);
        if (result is error) {
            log:printError("Error sending response", err = result);
        }
        return;
    }
}
