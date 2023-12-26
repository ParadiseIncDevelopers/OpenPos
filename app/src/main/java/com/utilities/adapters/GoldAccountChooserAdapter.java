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
import com.free.MainPage;
import com.free.R;
import com.utilities.classes.EncryptorClass;
import com.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class GoldAccountChooserAdapter extends RecyclerView.Adapter<GoldAccountChooserAdapter.GoldAccountChooserHolder>
{
    private ArrayList<Wallet> wallets;
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private boolean no_element;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public GoldAccountChooserAdapter(ArrayList<Wallet> wallets, Context context, LinearLayout layout)
    {
        if(wallets.stream().anyMatch(x -> x.getCurrency().equals("XAU")))
        {
            this.wallets = wallets;
            no_element = false;
        }
        else{
            no_element = true;
            layout.setVisibility(View.VISIBLE);
        }

        GoldAccountChooserAdapter.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public GoldAccountChooserHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_chooser_recycler_view, parent, false);
        return new GoldAccountChooserAdapter.GoldAccountChooserHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull GoldAccountChooserAdapter.GoldAccountChooserHolder holder, int position)
    {


        if(!no_element)
        {
            Wallet thePositionElement = wallets.get(position);

            if(thePositionElement.getCurrency().equals("XAU"))
            {
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

                String link = "";
                link = userAccountImageLinksList.stream().filter(x -> x.contains("turkey.png")).collect(Collectors.joining());
                Glide.with(context).load(link).into(holder.account_chooser_currency_image);
            }
        }
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class GoldAccountChooserHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        private TextView account_chooser_key;
        private TextView account_chooser_email;
        private TextView account_chooser_currency;
        private TextView account_chooser_payment_key;
        private TextView account_chooser_in_wallet;
        private LinearLayout account_chooser_main;
        private ImageView account_chooser_currency_image;

        public GoldAccountChooserHolder(@NonNull @NotNull View itemView)
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
