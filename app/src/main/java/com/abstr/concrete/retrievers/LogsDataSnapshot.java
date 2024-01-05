package com.abstr.concrete.retrievers;

import static com.utilities.UserUtility.userLogs;

import androidx.annotation.NonNull;

import com.abstr.concrete.operators.ByDateOperator;
import com.abstr.interfaces.operators.IOrderOperator;
import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.models.logs.Log;
import com.utilities.OrderTypeEnums;

import org.jetbrains.annotations.Contract;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        CompletableFuture<List<IContainer>> future = new CompletableFuture<>();

        Stream<IContainer> container = userLogs.stream()
                .map(x -> new Log.Builder()
                        .setId(x.getId())
                        .setDebit(x.getDebit())
                        .setDate(x.getDate())
                        .setCredit(x.getCredit())
                        .Build());

        IOrderOperator operator = new ByDateOperator(container);
        List<IContainer> collectedWallet = operator.executeOrder(OrderTypeEnums.ASC).collect(Collectors.toList());

        future.complete(collectedWallet);

        return future;
    }
}
