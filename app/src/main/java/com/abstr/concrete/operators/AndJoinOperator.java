package com.abstr.concrete.operators;

import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.operators.IJoinOperator;
import java.util.List;

public class AndJoinOperator implements IJoinOperator
{
    private IJoinOperator operator;
    private IJoinOperator operator2;

    public AndJoinOperator(IJoinOperator operator, IJoinOperator operator2)
    {
        this.operator = operator;
        this.operator2 = operator2;
    }

    @Override
    public List<IContainer> executeFilter(List<IContainer> listElements)
    {
        List<IContainer> firstOperator = operator.executeFilter(listElements);
        return operator2.executeFilter(firstOperator);
    }
}
