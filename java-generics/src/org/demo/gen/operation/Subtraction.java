package org.demo.gen.operation;

import org.demo.gen.ArithmeticOperation;

public class Subtraction implements ArithmeticOperation {

    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        if (num1 instanceof Integer && num2 instanceof Integer) {
            return subtract(num1.intValue(), num2.intValue());
        } else {
            return subtract(num1.doubleValue(), num2.doubleValue());
        }
    }

    private int subtract(int num1, int num2) {
        return num1 - num2;
    }


    private double subtract(double num1, double num2) {
        return num1 - num2;
    }

}
