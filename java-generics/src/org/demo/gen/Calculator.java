package org.demo.gen;

import org.demo.gen.operation.Addition;
import org.demo.gen.operation.Division;
import org.demo.gen.operation.Multiplication;
import org.demo.gen.operation.Subtraction;

public class Calculator<T extends Number> implements ArithmeticExpression<T> {

    @Override
    public Number add(T num1, T num2) {
        ArithmeticOperation operator = new Addition();
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
