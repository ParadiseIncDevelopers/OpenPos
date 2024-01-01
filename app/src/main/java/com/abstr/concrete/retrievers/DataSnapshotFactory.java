package com.abstr.concrete.retrievers;

import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.utilities.RetrieverFactoryEnums;

public class DataSnapshotFactory
{
    public IRetrieverFactory getRetriever(RetrieverFactoryEnums factoryEnum)
    {
        if (factoryEnum == RetrieverFactoryEnums.LOGS)
        {
            return LogsDataSnapshot.getInstance();
        }
        else{
            return WalletDataSnapshot.getInstance();
        }
    }
}
