package com.utilities.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.free.mainPage.MainPage;
import com.free.R;
import com.utilities.classes.EncryptorClass;
import com.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import static com.utilities.classes.LoginFactoryClass.userAccountImageLinksList;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.walletTaken;

public class NormalAccountChooserAdapter extends RecyclerView.Adapter<NormalAccountChooserAdapter.NormalAccountChooserHolder>
{
    private final ArrayList<Wallet> wallets;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public NormalAccountChooserAdapter(ArrayList<Wallet> wallets, Context context)
    {
        ArrayList<Wallet> wall = new ArrayList<>();
        wallets.stream().filter(x -> !x.getCurrency().equals("XAU")).forEach(wall::add);
        this.wallets = wall;

        NormalAccountChooserAdapter.context = context;
    }
    
    @NonNull
    @NotNull
    @Override
    public NormalAccountChooserHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) 
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_chooser_recycler_view, parent, false);
        return new NormalAccountChooserAdapter.NormalAccountChooserHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull NormalAccountChooserAdapter.NormalAccountChooserHolder holder, int position) 
    {
        Wallet thePositionElement = wallets.get(position);

        if(thePositionElement.getWalletKey().equals("MainWallet"))
        {
            holder.account_chooser_key.setText("MainWallet");
        }
        else {
            String key = thePositionElement.getWalletKey();
            holder.account_chooser_key.setText(key);
        }

        holder.account_chooser_email.setText(thePositionElement.getEmail());
        holder.account_chooser_currency.setText(thePositionElement.getCurrency());
        holder.account_chooser_payment_key.setText(thePositionElement.getPaymentKey());
        holder.account_chooser_in_wallet.setText(String.valueOf(thePositionElement.getMoneyCase()));

        Consumer<String> setImage = (s) ->
        {
            String link = "";
            switch (Objects.requireNonNull(s))
            {
                case "TRY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("turkey.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "USD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("united-states.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "EUR":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("european.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "CNY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("china.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "JPY":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("japan.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "CHF":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("switzerland.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "CAD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("canada.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "MAD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("morocco.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "GBP":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("united-kingdom.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "RUB":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("russia.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "AUD":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("australia.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
                case "QAR":
                    link = userAccountImageLinksList.stream().filter(x -> x.contains("qatar.png")).collect(Collectors.joining());
                    Glide.with(context).load(link).into(holder.account_chooser_currency_image);
                    break;
            }
        };

        setImage.accept(thePositionElement.getCurrency());
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class NormalAccountChooserHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView account_chooser_key;
        private TextView account_chooser_email;
        private TextView account_chooser_currency;
        private TextView account_chooser_payment_key;
        private TextView account_chooser_in_wallet;
        private LinearLayout account_chooser_main;
        private ImageView account_chooser_currency_image;

        public NormalAccountChooserHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            account_chooser_key = itemView.findViewById(R.id.account_chooser_key);
            account_chooser_email = itemView.findViewById(R.id.account_chooser_email);
            account_chooser_currency = itemView.findViewById(R.id.account_chooser_currency);
            account_chooser_payment_key = itemView.findViewById(R.id.account_chooser_payment_key);
            account_chooser_in_wallet = itemView.findViewById(R.id.account_chooser_in_wallet);
            account_chooser_main = itemView.findViewById(R.id.account_chooser_main);
            account_chooser_currency_image = itemView.findViewById(R.id.account_chooser_currency_image);

            account_chooser_main.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view)
        {
            if(!account_chooser_key.getText().toString().equals("MainWallet"))
            {
                walletTaken = EncryptorClass.setSecurePassword(account_chooser_key.getText().toString());
            }
            else {
                walletTaken = account_chooser_key.getText().toString();
            }

            Intent intent = new Intent(context, MainPage.class);
            intent.putExtra("Email", userEmail);
            context.startActivity(intent);
        }
    }
}
