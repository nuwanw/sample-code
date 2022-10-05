package org.demo.gen;

public interface ArithmeticExpression<T extends Number> {
    public Number add(T num1, T num2);

    public Number subtract(T num1, T num2);

    public Number divide(T num1, T num2);

    public Number multiply(T num1, T num2);
}
