package org.demo;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employees")
public class Employees {

    private List<Employee> employees;

    public List<Employee> getEmployee() {
        return employees;
    }

    public void setEmployee(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getEmployee(int id) {
        Employee employee = null;
        for(Employee emp : employees) {
            if(emp.getId() == id) {
                employee =  emp;
                break;
            }
        }
        return employee;
    }
}
