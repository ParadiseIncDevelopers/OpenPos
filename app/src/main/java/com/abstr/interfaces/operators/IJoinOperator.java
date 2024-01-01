package com.abstr.interfaces.operators;

import com.abstr.interfaces.retrievers.IContainer;

import java.util.List;

public interface IJoinOperator extends IOperator
{
    List<IContainer> executeFilter(List<IContainer> listElements);
}
