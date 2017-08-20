/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 0.1-SNAPSHOT
 */
@Path("/service")
public class HelloService {

    @GET
    @Path("/")
    public Response get() {
        System.out.println("GET invoked");
        ResponseMessage responseMessage = new ResponseMessage("hello ");
        return Response.status(Response.Status.OK)
                .header("set-cookie", "a2")
                .header("set-cookie", "a1")
                .entity(responseMessage)
                .type(MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/hello")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response post(RequestMessage request) {
        System.out.println("POST invoked");
        ResponseMessage responseMessage = new ResponseMessage("hello POST " + request.getName());
        return Response.status(Response.Status.OK).header("a", "a2").header("a", "a1").entity(responseMessage)
                .type(MediaType.APPLICATION_JSON).build();
    }

    @PUT
    @Path("/")
    public Response put(RequestMessage request) {
        // TODO: Implementation for HTTP PUT request
        System.out.println("PUT invoked");
        ResponseMessage responseMessage = new ResponseMessage("hello put" + request.getName());
        return Response.status(Response.Status.NO_CONTENT).header("Content-Length", "0").build();
    }

    @DELETE
    @Path("/")
    public void delete() {
        // TODO: Implementation for HTTP DELETE request
        System.out.println("DELETE invoked");
    }
}
