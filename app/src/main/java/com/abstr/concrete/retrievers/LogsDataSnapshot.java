package com.abstr.concrete.retrievers;

import androidx.annotation.NonNull;

import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.retrievers.IRetrieverFactory;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LogsDataSnapshot implements IRetrieverFactory
{
    private LogsDataSnapshot()
    {

    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public static LogsDataSnapshot getInstance()
    {
        return new LogsDataSnapshot();
    }

    @Override
    public CompletableFuture<List<IContainer>> returnData(String... snapshots)
    {
        return null;
    }
}
