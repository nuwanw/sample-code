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

import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

@PrepareForTest(Utils.class)
public class EmployTest extends PowerMockTestCase {

    @Test()
    public void testCalculateSalary() {
        PowerMockito.mockStatic(Utils.class);
        PowerMockito.when(Utils.sum(1000, 500)).thenReturn(2000.0);
        Employ employ = new Employ();
        Assert.assertEquals(employ.calculateSalary(1000, 500), 2000.0);
        //this will verify whether the mok object is executed
        PowerMockito.verifyStatic(Utils.class);
    }
}
