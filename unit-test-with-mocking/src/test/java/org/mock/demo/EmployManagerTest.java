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

//import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
//import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import static org.mockito.ArgumentMatchers.any;

//@RunWith(PowerMockRunner.class)
@PrepareForTest(value = {EmployManager.class})
public class EmployManagerTest extends PowerMockTestCase {

    @Test()
    public void testGetInfoMockObject() throws Exception {
        Employ employ = new Employ();
        employ.setName("MockName");
        PowerMockito.whenNew(Employ.class).withNoArguments().thenReturn(employ);
        EmployManager employManager = new EmployManager();
        Assert.assertEquals(employManager.getEmpInfo("0"), "MockName 1200.0");
    }

    @Test()
    public void testGetInfoMockMethod() throws Exception {
        Employ employ = PowerMockito.mock(Employ.class);
        PowerMockito.when(employ.getName()).thenReturn("MockName");
        PowerMockito.when(employ.calculateSalary(1000, 200)).thenReturn(1500.0);
        PowerMockito.whenNew(Employ.class).withNoArguments().thenReturn(employ);
        EmployManager employManager = new EmployManager();
        Assert.assertEquals(employManager.getEmpInfo("0"), "MockName 1500.0");
    }

    @Test()
    public void testGetInfoMockObjectArgument() throws Exception {
        Employ employ = new Employ("Nuwan");
        PowerMockito.whenNew(Employ.class).withArguments(any(String.class)).thenReturn(employ);
        EmployManager employManager = new EmployManager();
        Assert.assertEquals(employManager.getEmpInfo("1"), "Nuwan 1200.0");
    }

    @Test()
    public void testGetInfoMockObjectTwoArgument() throws Exception {
        Employ employ = new Employ("Nuwan", 30);
        PowerMockito.whenNew(Employ.class).withArguments(any(String.class), any(Integer.class)).thenReturn(employ);
        EmployManager employManager = new EmployManager();
        Assert.assertEquals(employManager.getEmpInfo("2"), "Nuwan 36000.0");
    }
}
