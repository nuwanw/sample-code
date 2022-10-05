package org.demo.gen.operation;

import org.demo.gen.ArithmeticOperation;

public class Division implements ArithmeticOperation {

    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        if (num1 instanceof Integer && num2 instanceof Integer) {
            return divide(num1.intValue(), num2.intValue());
        } else {
            return divide(num1.doubleValue(), num2.doubleValue());
        }
    }

    private int divide(int num1, int num2) {
        return num1 / num2;
    }


    private double divide(double num1, double num2) {
        return num1 / num2;
    }

}
