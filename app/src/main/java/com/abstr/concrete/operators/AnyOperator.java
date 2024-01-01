package com.abstr.concrete.operators;

import androidx.annotation.NonNull;
import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.operators.IStreamOperator;
import java.util.List;
import java.util.function.Predicate;

public class AnyOperator implements IStreamOperator
{
    private Predicate<IContainer> operator;

    public AnyOperator(Predicate<IContainer> operator)
    {
        this.operator = operator;
    }

    @Override
    public boolean executeFilter(@NonNull List<IContainer> listElements, Predicate<IContainer> predicate)
    {
        return listElements.stream().anyMatch(operator);
    }
}
