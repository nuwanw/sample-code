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

public class Employ {
    private String name;
    private int offset;

    public Employ(String name) {
        this.name = name;
    }

    public Employ(String name, int age) {
        this.name = name;
        this.offset = age;
    }

    public Employ() {
    }

    public double calculateSalary(double basic, double allowances) {
        double salary = Utils.sum(basic, allowances);
        if(offset > 1) {
            return salary * offset;
        }
        return salary;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
