package org.demo;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class RunDemo {
    public static void main(String[] args) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Employee.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        Employee employee = (Employee) jaxbUnmarshaller.unmarshal(Employee.class.getResourceAsStream("/employee.xml"));
        System.out.println(employee.toString());
    }
}
