package com.free.main.adapter;

import static com.utilities.UserUtility.userLogs;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class WalletLogs extends AppCompatActivity
{
    private FloatingActionButton wallet_logs_back_menu_button;
    private RecyclerView wallet_logs_RecyclerView_Accounts;
    private TextView wallet_logs_id_text, wallet_logs_money_case_text;
    private WalletLogsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_logs);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () -> {

            wallet_logs_back_menu_button = findViewById(R.id.wallet_logs_back_menu_button);
            wallet_logs_id_text = findViewById(R.id.wallet_logs_id_text);
            wallet_logs_money_case_text = findViewById(R.id.wallet_logs_money_case_text);
            wallet_logs_RecyclerView_Accounts = findViewById(R.id.wallet_logs_RecyclerView_Accounts);

            Bundle extras = getIntent().getExtras();

            wallet_logs_id_text.setText(extras.getString("wallet_id"));
            wallet_logs_money_case_text.setText(String.format("%s %s", extras.getDouble("moneyCase"), extras.getString("currency")));
            adapter = new WalletLogsAdapter(userLogs);
            wallet_logs_RecyclerView_Accounts.setLayoutManager(new LinearLayoutManager(WalletLogs.this));
            wallet_logs_RecyclerView_Accounts.setAdapter(adapter);

            wallet_logs_back_menu_button.setOnClickListener(view -> {
                finish();
            });
        });


    }
}