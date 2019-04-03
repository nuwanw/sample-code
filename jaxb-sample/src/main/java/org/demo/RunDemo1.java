package org.demo;

import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class RunDemo1 {
    public static void main(String[] args) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employees.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Employees employees = (Employees) jaxbUnmarshaller.unmarshal(Employee.class.getResourceAsStream("/employees.xml"));
        List<Employee> empArray = employees.getEmployee();
        for(Employee employee : empArray) {
            System.out.println(employee.toString());
            System.out.println("location " + employee.getDepartment().getLocation());
        }
    }
}
