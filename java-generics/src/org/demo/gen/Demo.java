package org.demo.gen;

import org.demo.gen.operation.Addition;

public class Demo {

    public static void main(String[] args) {
        Calculator<Number> cal = new Calculator<>();
        System.out.println(cal.add(10, 20));
        System.out.println(cal.add(Integer.parseInt("10"), 20));
        System.out.println(cal.divide(100.5, 20.5));
        System.out.println(cal.multiply(100.5, 20.5));
        System.out.println(cal.subtract(100.5, 20.5));
        System.out.println(cal.calculate(100.5, 20.5, new Addition()));
    }
}
