package com.free.main.adapter.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.abstr.concrete.retrievers.DataSnapshotFactory;
import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.bumptech.glide.Glide;
import com.free.R;
import com.free.main.adapter.WalletLogs;
import com.free.main.adapter.credit.CreditAccountEditor;
import com.free.main.adapter.debit.DebitAccountEditor;
import com.models.wallet.Wallet;
import com.utilities.RetrieverFactoryEnums;
import com.utilities.classes.ContainerConverter;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.utilities.UserUtility.userLogs;
import static com.utilities.UserUtility.userWalletKeyIds;
import static com.utilities.UserUtility.userWallets;

public class AccountLogsAdapter extends RecyclerView.Adapter<AccountLogsAdapter.AccountLogsAdapterHolder>
{
    private static List<Wallet> wallets;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String pageName = null;

    public AccountLogsAdapter(@NonNull List<Wallet> wallets, Context context, String pageName)
    {
        AccountLogsAdapter.wallets = wallets;
        AccountLogsAdapter.pageName = pageName;
        AccountLogsAdapter.context = context;
    }
    
    @NonNull
    @NotNull
    @Override
    public AccountLogsAdapterHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_chooser_recycler_view, parent, false);
        return new AccountLogsAdapter.AccountLogsAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull AccountLogsAdapter.AccountLogsAdapterHolder holder, int position)
    {
        //Glide.with(context).load(link).into(holder.account_logs_currency_image);
        Wallet index = wallets.get(position);
        holder.account_logs_id_key.setText("Key: " + index.getId());
        holder.account_logs_name.setText("Account Name: " + index.getAccountName());
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class AccountLogsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView account_logs_currency_image;
        private final TextView account_logs_id_key;
        private final TextView account_logs_name;
        private final Context context;

        @SuppressLint("UseCompatLoadingForDrawables")
        public AccountLogsAdapterHolder(@NonNull View itemView)
        {
            super(itemView);
            account_logs_currency_image = itemView.findViewById(R.id.account_logs_currency_image);
            account_logs_id_key = itemView.findViewById(R.id.account_logs_id_key);
            account_logs_name = itemView.findViewById(R.id.account_logs_name);

            context = itemView.getContext();
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {
                String pageName = AccountLogsAdapter.pageName;
                switch (pageName) {
                    case "MainPage": {
                        Intent intent = new Intent(context, WalletLogs.class);

                        Wallet wallet = AccountLogsAdapter.wallets.get(position);

                        userLogs = wallet.getWalletLogs();

                        DataSnapshotFactory snap = new DataSnapshotFactory();
                        IRetrieverFactory factory = snap.getRetriever(RetrieverFactoryEnums.LOGS);
                        factory.returnData().thenAccept(then -> {
                            userLogs = ContainerConverter.toLogList(then);
                            intent.putExtra("id", account_logs_id_key.getText().toString().replace("Key: ", ""));
                            intent.putExtra("currency", wallet.getCurrency());
                            intent.putExtra("wallet_id", wallet.getId());
                            intent.putExtra("moneyCase", wallet.getMoneyCase());
                            context.startActivity(intent);
                        }).exceptionally(ex -> null);
                        break;
                    }
                    case "CreditAccount": {
                        Intent intent = new Intent(context, CreditAccountEditor.class);
                        intent.putExtra("id", account_logs_id_key.getText().toString().replace("Key: ", ""));
                        context.startActivity(intent);
                        break;
                    }
                    case "DebitAccount": {
                        Intent intent = new Intent(context, DebitAccountEditor.class);
                        intent.putExtra("id", account_logs_id_key.getText().toString().replace("Key: ", ""));
                        context.startActivity(intent);
                        break;
                    }
                    default:

                        break;
                }
            }


        }
    }
}
