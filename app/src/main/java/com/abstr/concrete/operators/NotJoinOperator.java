package com.abstr.concrete.operators;

import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.operators.IJoinOperator;

import java.util.List;

public class NotJoinOperator implements IJoinOperator
{
    private IJoinOperator operator;

    public NotJoinOperator(IJoinOperator operator)
    {
        this.operator = operator;
    }

    @Override
    public List<IContainer> executeFilter(List<IContainer> listElements)
    {
        List<IContainer> firstOperator = operator.executeFilter(listElements);

        for (IContainer container1 : firstOperator)
        {
            if(!firstOperator.contains(container1))
            {
                firstOperator.add(container1);
            }
        }
        return firstOperator;
    }
}
