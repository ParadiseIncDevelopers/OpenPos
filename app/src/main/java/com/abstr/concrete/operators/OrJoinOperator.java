package com.abstr.concrete.operators;

import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.operators.IJoinOperator;
import java.util.List;

public class OrJoinOperator implements IJoinOperator
{
    private IJoinOperator operator;
    private IJoinOperator operator2;

    public OrJoinOperator(IJoinOperator operator, IJoinOperator operator2)
    {
        this.operator = operator;
        this.operator2 = operator2;
    }

    @Override
    public List<IContainer> executeFilter(List<IContainer> listElements)
    {
        List<IContainer> firstOperator = operator.executeFilter(listElements);
        List<IContainer> secondOperator = operator2.executeFilter(listElements);

        for (IContainer container1 : secondOperator)
        {
            if(!firstOperator.contains(container1))
            {
                firstOperator.add(container1);
            }
        }
        return firstOperator;
    }
}
