package org.demo.gen;

public interface ArithmeticOperation {
    public <T extends Number> Number execute(T num1, T num2);
}
