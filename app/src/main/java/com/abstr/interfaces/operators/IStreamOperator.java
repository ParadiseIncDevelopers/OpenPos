package com.abstr.interfaces.operators;

import com.abstr.interfaces.retrievers.IContainer;

import java.util.List;
import java.util.function.Predicate;

public interface IStreamOperator extends IOperator
{
    boolean executeFilter(List<IContainer> listElements, Predicate<IContainer> predicate);


}
