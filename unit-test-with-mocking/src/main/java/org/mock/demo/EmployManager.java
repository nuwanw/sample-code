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

import java.util.HashMap;
import java.util.Map;

public class EmployManager {
    private Map<String,Employ> employMap = new HashMap<String, Employ>();

    public EmployManager() {
        employMap.put("0", new Employ());
        employMap.put("1", new Employ("Nuwanw"));
        employMap.put("2", new Employ("Nuwanw", 2));
    }

    public String getEmpInfo(String name) {
        return employMap.get(name).getName() + " " + employMap.get(name).calculateSalary(1000, 200);
    }
}
