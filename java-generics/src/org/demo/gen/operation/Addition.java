package org.demo.gen.operation;

public class Addition extends AbstractArithmeticOperation {

    @Override
    protected <T extends Number> Number doExecute(T num1, T num2) {
        if(num1 instanceof Integer && num2 instanceof Integer) {
            return add(num1.intValue(), num2.intValue());
        } else {
            return add(num1.doubleValue(), num2.doubleValue());
        }
    }

    private int add(int num1, int num2) {
        return num1 + num2;
    }

    private double add(double num1, double num2) {
        return num1 + num2;
    }


}
