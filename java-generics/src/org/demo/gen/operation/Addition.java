package org.demo.gen.operation;

public class Addition extends AbstractArithmeticOperation {

    @Override
    protected <T extends Number> Number doExecute(T num1, T num2) {
        return num1.doubleValue() + num2.doubleValue();
    }

}
