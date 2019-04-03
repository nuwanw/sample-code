package org.demo;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "employee")
public class Employee {

    private String name;
    private String age;
    private Department department;
    private double salary;
    private int id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public double getSalary() {
        return salary;
    }

    @XmlElement(name = "salary")
    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    @XmlAttribute(name = "id")
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String empInfo = this.getId() + " " + this.getName() + " " + this.getAge() + " " + this.getSalary();
        empInfo = empInfo + "\n" + "Department " + this.getDepartment().getName();
        return empInfo;
    }

    public static class Department {
        private String name;
        private String location;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocation() {
            return location;
        }

        @XmlElement(name = "location")
        public void setLocation(String location) {
            this.location = location;
        }

    }

}
