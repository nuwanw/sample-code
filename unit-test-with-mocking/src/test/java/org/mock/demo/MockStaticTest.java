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

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.Test;

import javax.xml.ws.handler.MessageContext;

import static org.mockito.ArgumentMatchers.any;

@PrepareForTest(Utils.class)
public class MockStaticTest extends PowerMockTestCase {

    @Test()
    public void testStaticVoidMethod() throws Exception {
        PowerMockito.mockStatic(Utils.class);
//        PowerMockito.doNothing().doThrow(new RuntimeException()).when(Utils.class);
        PowerMockito.doNothing().when(Utils.class, "foo", any(String.class));
        Employ employ = new Employ();
        employ.foo("bar");
    }

    @Test()
    public void testDoAnswer() throws Exception {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.doAnswer(new Answer<Void>() {
            public Void answer(InvocationOnMock invocation) {
                System.out.println("DoAnswer works : args " + invocation.getArgument(0));
                return null;
            }
        }).when(Utils.class, "foo", any(String.class));
        Employ employ = new Employ();
        employ.foo("bar");
    }

    @Test()
    public void testDoAnswerReturn() throws Exception {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.doAnswer(new Answer<String>() {
            public String answer(InvocationOnMock invocation) {
                String s = invocation.getArgument(0);
                return s;
            }
        }).when(Utils.class, "foo", any(String.class));
        Employ employ = new Employ();
        employ.foo("bar");
    }
}