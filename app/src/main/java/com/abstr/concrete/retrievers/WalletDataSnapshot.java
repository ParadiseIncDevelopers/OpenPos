package com.abstr.concrete.retrievers;

import static com.utilities.UserUtility.userWalletKeyIds;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.abstr.concrete.operators.ByDateOperator;
import com.abstr.interfaces.operators.IOrderOperator;
import com.abstr.interfaces.retrievers.IContainer;
import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.google.firebase.database.FirebaseDatabase;
import com.models.wallet.Wallet;
import com.utilities.OrderTypeEnums;
import org.jetbrains.annotations.Contract;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WalletDataSnapshot implements IRetrieverFactory
{
    private WalletDataSnapshot()
    {

    }

    @NonNull
    @Contract(value = " -> new", pure = true)
    public static WalletDataSnapshot getInstance()
    {
        return new WalletDataSnapshot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public CompletableFuture<List<IContainer>> returnData(@NonNull String... snapshots)
    {
        CompletableFuture<List<IContainer>> future = new CompletableFuture<>();


        FirebaseDatabase.getInstance(snapshots[0])
                .getReference()
                .child(snapshots[1])
                .get()
                .addOnCanceledListener(() -> {

                })
                .addOnFailureListener(x -> {

                })
                .addOnSuccessListener(task -> {
                    Stream<IContainer> theWallet = userWalletKeyIds.stream().map(y -> {
                        Wallet.Builder walletBuilder = new Wallet.Builder()
                                .setAccountName(task.child(y).child("accountName").getValue().toString())
                                .setCreationDate(LocalDateTime.parse(task.child(y).child("creationDate").getValue().toString()))
                                .setId(task.child(y).getKey())
                                .setEmail(task.child(y).child("email").getValue().toString())
                                .setCurrency(task.child(y).child("currency").getValue().toString())
                                .setMoneyCase(Double.parseDouble(task.child(y).child("moneyCase").getValue().toString()));
                        if(task.child(y).child("walletLogs").exists())
                        {
                            walletBuilder.setWalletLogs((Map<String, Map<String, Object>>) task.child(y).child("walletLogs").getValue());
                        }
                        else{
                            walletBuilder.setWalletLogs(new HashMap<>());
                        }

                        return walletBuilder.Build();
                    });

                    IOrderOperator operator = new ByDateOperator(theWallet);
                    List<IContainer> collectedWallet = operator.executeOrder(OrderTypeEnums.DESC).collect(Collectors.toList());

                    future.complete(collectedWallet);
                });
        return future;
    }
}
