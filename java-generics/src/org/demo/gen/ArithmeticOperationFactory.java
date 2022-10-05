package org.demo.gen;

import org.demo.gen.operation.Addition;
import org.demo.gen.operation.Division;
import org.demo.gen.operation.Multiplication;
import org.demo.gen.operation.Subtraction;

public class ArithmeticOperationFactory {

    public static enum OPERATOR{ADD, SUBTRACT, DIVIDE, MULTIPLY};

    public static ArithmeticOperation getOperator(OPERATOR operator) {
        switch (operator){
            case ADD : return new Addition();
            case SUBTRACT:return new Subtraction();
            case DIVIDE: return new Division();
            case MULTIPLY:return new Multiplication();
        };
        return null;
    }
}
