package org.demo.gen.operation;

import org.demo.gen.ArithmeticOperation;

public class Division implements ArithmeticOperation {

    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        return num1.doubleValue() / num2.doubleValue();
    }

}
