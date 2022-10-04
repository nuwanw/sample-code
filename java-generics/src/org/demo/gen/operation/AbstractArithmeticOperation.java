package org.demo.gen.operation;

import org.demo.gen.ArithmeticOperation;

public abstract class AbstractArithmeticOperation implements ArithmeticOperation {
    @Override
    public <T extends Number> Number execute(T num1, T num2) {
        System.out.println("Do Pre Actions");
        return doExecute(num1, num2);
    }

    protected abstract <T extends Number> Number doExecute(T num1, T num2);
}
