package com.abstr.interfaces.operators;

import com.abstr.interfaces.retrievers.IContainer;
import com.utilities.OrderTypeEnums;
import java.util.stream.Stream;

public interface IOrderOperator
{
    Stream<IContainer> executeOrder(OrderTypeEnums orderType);
}
