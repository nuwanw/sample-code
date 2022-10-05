package org.demo.gen;

import org.demo.gen.operation.Addition;

public class Demo {

    public static void main(String[] args) {
        Calculator<Number> cal = new Calculator<>();
        System.out.println("Add>> " + cal.add(10, 20));
        System.out.println("Add>> " + cal.add(Integer.parseInt("10"), 20));
        System.out.println("Dev>> " + cal.divide(100.5, 20.5));
        System.out.println("Mul>> " + cal.multiply(100.5, 20.5));
        System.out.println("Sub>> " + cal.subtract(100, 20.5));
        System.out.println("Sub>> " + cal.subtract(100, 25));
        System.out.println("Cal>> " + cal.calculate(100.5, 20.5, new Addition()));
    }
}
