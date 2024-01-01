package com.abstr.interfaces.retrievers;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IRetrieverFactory
{
    CompletableFuture<List<IContainer>> returnData(String... childElements);

}
