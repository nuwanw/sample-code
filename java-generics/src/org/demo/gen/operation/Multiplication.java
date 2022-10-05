package org.demo.gen.operation;

import org.demo.gen.ArithmeticOperation;

public class Multiplication implements ArithmeticOperation {

    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        if (num1 instanceof Integer && num2 instanceof Integer) {
            return multiply(num1.intValue(), num2.intValue());
        } else {
            return multiply(num1.doubleValue(), num2.doubleValue());
        }
    }


    private int multiply(int num1, int num2) {
        return num1 * num2;
    }


    private double multiply(double num1, double num2) {
        return num1 * num2;
    }

}
