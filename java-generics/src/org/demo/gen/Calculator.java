package org.demo.gen;

import org.demo.gen.operation.*;

public class Calculator<T extends Number> implements ArithmeticExpression<T> {

    @Override
    public Number add(T num1, T num2) {
        AbstractArithmeticOperation operator = new Addition();
        return operator.execute(num1, num2);
    }

    @Override
    public  Number subtract(T num1, T num2) {
        ArithmeticOperation operator = new Subtraction();
        return operator.execute(num1, num2);
    }

    @Override
    public Number divide(T num1, T num2) {
        ArithmeticOperation operator = new Division();
        return operator.execute(num1, num2);
    }

    @Override
    public Number multiply(T num1, T num2) {
        ArithmeticOperation operator = new Multiplication();
        return operator.execute(num1, num2);
    }

}
