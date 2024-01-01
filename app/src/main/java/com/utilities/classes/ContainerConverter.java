package com.utilities.classes;

import androidx.annotation.NonNull;
import com.abstr.interfaces.retrievers.IContainer;
import com.models.logs.Log;
import com.models.wallet.Wallet;
import java.util.ArrayList;
import java.util.List;

public class ContainerConverter
{
    private ContainerConverter()
    {

    }

    @NonNull
    public static List<Wallet> toWalletList(@NonNull List<IContainer> container)
    {
        List<Wallet> wallets = new ArrayList<>();

        for (int i = 0; i < container.size(); i++) {
            Wallet wallet = (Wallet) container.get(i);
            wallets.add(wallet);
        }

        return wallets;
    }

    @NonNull
    public static List<Log> toLogList(@NonNull List<IContainer> container)
    {
        List<Log> logs = new ArrayList<>();

        for (int i = 0; i < container.size(); i++) {
            Log wallet = (Log) container.get(i);
            logs.add(wallet);
        }

        return logs;
    }
}
