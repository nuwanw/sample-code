package org.demo.gen;

import org.demo.gen.operation.*;

public class Calculator<T extends Number> implements ArithmeticExpression<T> {

    @Override
    public Number add(T num1, T num2) {
        ArithmeticOperation operator = ArithmeticOperationFactory.getOperator(ArithmeticOperationFactory.OPERATOR.ADD);
        return operator.execute(num1, num2);
    }

    @Override
    public Number subtract(T num1, T num2) {
        ArithmeticOperation operator = ArithmeticOperationFactory.getOperator(ArithmeticOperationFactory.OPERATOR.SUBTRACT);
        return operator.execute(num1, num2);
    }

    @Override
    public Number divide(T num1, T num2) {
        ArithmeticOperation operator = ArithmeticOperationFactory.getOperator(ArithmeticOperationFactory.OPERATOR.DIVIDE);
        return operator.execute(num1, num2);
    }

    @Override
    public Number multiply(T num1, T num2) {
        ArithmeticOperation operator = ArithmeticOperationFactory.getOperator(ArithmeticOperationFactory.OPERATOR.MULTIPLY);
        return operator.execute(num1, num2);
    }

    public Number calculate(T num1, T num2, ArithmeticOperation operator) {
        return operator.execute(num1, num2);
    }

}
