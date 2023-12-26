package com.utilities.adapters;

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
import com.bumptech.glide.Glide;
import com.free.R;
import com.menu.WalletLogs;
import com.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static com.utilities.classes.UserUtility.userAccountImageLinksList;

public class AccountLogsAdapter extends RecyclerView.Adapter<AccountLogsAdapter.AccountLogsAdapterHolder>
{
    private final List<Wallet> wallets;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public AccountLogsAdapter(@NonNull List<Wallet> wallets, Context context)
    {
        this.wallets = wallets;
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
        Consumer<String> setImage = (s) -> {
            String link;
            switch (Objects.requireNonNull(s))
            {
                case "TRY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("turkey.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "USD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("united-states.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "EUR":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("european.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "CNY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("china.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "JPY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("japan.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "CHF":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("switzerland.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "CAD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("canada.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "MAD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("morocco.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "GBP":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("united-kingdom.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "RUB":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("russia.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "AUD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("australia.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
                case "QAR":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("qatar.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_logs_currency_image);
                    break;
            }
        };
        Wallet index = wallets.get(position);
        holder.account_logs_id_key.setText("Key: " + index.getId());
        holder.account_logs_name.setText("Account Name: " + index.getAccountName());
        setImage.accept(index.getCurrency());
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class AccountLogsAdapterHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView account_logs_currency_image;
        private TextView account_logs_id_key;
        private TextView account_logs_name;
        private Context context;

        public AccountLogsAdapterHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();

            account_logs_currency_image = itemView.findViewById(R.id.account_logs_currency_image);
            account_logs_id_key = itemView.findViewById(R.id.account_logs_id_key);
            account_logs_name = itemView.findViewById(R.id.account_logs_name);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION)
            {
                Intent intent = new Intent(context, WalletLogs.class);
                intent.putExtra("ID", account_logs_id_key.getText().toString());
                intent.putExtra("AccountName", account_logs_name.getText().toString());
                context.startActivity(intent);
            }
        }
    }
}
