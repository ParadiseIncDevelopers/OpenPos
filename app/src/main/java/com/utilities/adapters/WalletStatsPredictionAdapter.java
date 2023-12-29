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

public class WalletStatsPredictionAdapter extends RecyclerView.Adapter<WalletStatsPredictionAdapter.WalletStatsPredictionAdapterHolder>
{
    private ArrayList<Wallet> wallets;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public WalletStatsPredictionAdapter(@NonNull ArrayList<Wallet> wallets, Context context, LinearLayout layout)
    {

    }

    @NonNull
    @NotNull
    @Override
    public WalletStatsPredictionAdapter.WalletStatsPredictionAdapterHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.account_chooser_recycler_view, parent, false);
        return new WalletStatsPredictionAdapter.WalletStatsPredictionAdapterHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull WalletStatsPredictionAdapter.WalletStatsPredictionAdapterHolder holder, int position)
    {
    }

    @Override
    public int getItemCount() {
        return wallets.size();
    }

    public static class WalletStatsPredictionAdapterHolder extends RecyclerView.ViewHolder
    {

        public WalletStatsPredictionAdapterHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
        }
    }
}
