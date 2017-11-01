/*
*  Copyright (c) 2017, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
*
*  WSO2 Inc. licenses this file to you under the Apache License,
*  Version 2.0 (the "License"); you may not use this file except
*  in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing,
*  software distributed under the License is distributed on an
*  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
*  KIND, either express or implied.  See the License for the
*  specific language governing permissions and limitations
*  under the License.
*/
package org.mock.demo;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.any;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WireMockTest {

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8079);

    @Test()
    public void testAnyResource() throws Exception {
        wireMockRule.stubFor(any(urlPathEqualTo("/api/books")).willReturn(
                aResponse().withHeader("Content-Type", "application/json").withStatus(200).withBody("{'a':'b'}")));
        try {
            wireMockRule.start();
            HttpResponse httpResponse = doPost(new URL("http://localhost:8079/api/books"), "hello");
            Assert.assertEquals("{'a':'b'}", httpResponse.getData());
        } finally {
            wireMockRule.stop();
        }
    }

    @Test()
    public void testPostResource() throws Exception {
        wireMockRule.stubFor(post(urlPathEqualTo("/api/books"))
                .willReturn(aResponse().withHeader("Content-Type", "application/json").withStatus(200).withBody("{'b':'b'}")));
        try {
            wireMockRule.start();
            HttpResponse httpResponse = doPost(new URL("http://localhost:8079/api/books"), "hello");
            Assert.assertEquals("{'b':'b'}", httpResponse.getData());
        } finally {
            wireMockRule.stop();
        }
    }

    private static HttpResponse doPost(URL endpoint, String body) throws Exception {
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) endpoint.openConnection();
            try {
                urlConnection.setRequestMethod("POST");
            } catch (ProtocolException e) {
                throw new Exception("Shouldn't happen: HttpURLConnection doesn't support POST?? " + e.getMessage(), e);
            }
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.setAllowUserInteraction(false);
            urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            OutputStream out = urlConnection.getOutputStream();
            try {
                Writer writer = new OutputStreamWriter(out, "UTF-8");
                writer.write(body);
                writer.close();
            } catch (IOException e) {
                throw new Exception("IOException while posting data " + e.getMessage(), e);
            } finally {
                if (out != null) {
                    out.close();
                }
            }
            // Get the response
            StringBuilder sb = new StringBuilder();
            BufferedReader rd = null;
            try {
                rd = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream(), Charset.defaultCharset()));
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
            } catch (FileNotFoundException ignored) {
            } finally {
                if (rd != null) {
                    rd.close();
                }
            }
            Iterator<String> itr = urlConnection.getHeaderFields().keySet().iterator();
            Map<String, String> headers = new HashMap();
            while (itr.hasNext()) {
                String key = itr.next();
                if (key != null) {
                    headers.put(key, urlConnection.getHeaderField(key));
                }
            }
            return new HttpResponse(sb.toString(), urlConnection.getResponseCode(), headers);
        } catch (IOException e) {
            throw new Exception("Connection error (is server running at " + endpoint + " ?): " + e.getMessage(), e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    private static class HttpResponse {
        private String data;
        private int responseCode;
        private String responseMessage;
        private Map<String, String> headers;

        public HttpResponse(String data, int responseCode) {
            this.data = data;
            this.responseCode = responseCode;
        }

        public HttpResponse(String data, int responseCode, Map<String, String> headers) {
            this.data = data;
            this.responseCode = responseCode;
            this.headers = headers;
        }

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public int getResponseCode() {
            return responseCode;
        }

        public void setResponseCode(int responseCode) {
            this.responseCode = responseCode;
        }

        public void setHeaders(Map<String, String> headers) {
            this.headers = headers;
        }

        public Map<String, String> getHeaders() {
            return headers;
        }

        public String getResponseMessage() {
            return responseMessage;
        }

        public void setResponseMessage(String responseMessage) {
            this.responseMessage = responseMessage;
        }
    }
}
