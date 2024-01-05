package com.abstr.concrete.operators;

import com.abstr.interfaces.operators.IOrderOperator;
import com.abstr.interfaces.retrievers.IContainer;
import com.models.logs.Log;
import com.models.wallet.Wallet;
import com.utilities.OrderTypeEnums;
import java.util.Comparator;
import java.util.stream.Stream;

public class ByDateOperator implements IOrderOperator
{
    private Stream<IContainer> elements;

    public ByDateOperator(Stream<IContainer> elements)
    {
        this.elements = elements;
    }

    @Override
    public Stream<IContainer> executeOrder(OrderTypeEnums orderType) {

        if(orderType == OrderTypeEnums.ASC)
        {
            return elements.sorted(Comparator.comparing(s ->
            {
                if(s instanceof Wallet)
                {
                    return ((Wallet) s).getCreationDate();
                }
                else {
                    return ((Log) s).getDate();
                }
            }));
        }
        else{
            return elements.sorted(Comparator.comparing(s ->
            {
                if(s instanceof Wallet)
                {
                    return ((Wallet) s).getCreationDate();
                }
                else {
                    return ((Log) s).getDate();
                }
            }).reversed());
        }
    }
}
