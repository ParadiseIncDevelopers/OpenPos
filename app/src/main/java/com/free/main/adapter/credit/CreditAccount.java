package com.free.main.adapter.credit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.free.R;
import com.free.main.adapter.credit.adapter.CreditAccountAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.utilities.NetworkChangeReceiver.NetworkCallback;


public class CreditAccount extends AppCompatActivity
{
    private ConstraintLayout credit_account_ConstraintLayout1_Exit,credit_account_ConstraintLayout2_Account;
    private RecyclerView credit_account_RecyclerView_Accounts;
    private FloatingActionButton credit_account_FAB2_Menu,credit_account_FAB2_add_anonymous;
    private CreditAccountAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            credit_account_ConstraintLayout1_Exit = findViewById(R.id.credit_account_ConstraintLayout1_Exit);
            credit_account_ConstraintLayout2_Account = findViewById(R.id.credit_account_ConstraintLayout2_Account);
            credit_account_RecyclerView_Accounts = findViewById(R.id.credit_account_RecyclerView_Accounts);
            credit_account_FAB2_Menu = findViewById(R.id.credit_account_FAB2_Menu);
            credit_account_FAB2_add_anonymous = findViewById(R.id.credit_account_FAB2_add_anonymous);

            credit_account_FAB2_add_anonymous.setOnClickListener(view -> {

            });

            credit_account_FAB2_Menu.setOnClickListener(view -> {

            });

            adapter = new CreditAccountAdapter();

            credit_account_RecyclerView_Accounts.setLayoutManager(new LinearLayoutManager(CreditAccount.this));
            credit_account_RecyclerView_Accounts.setAdapter(adapter);

        });
    }
}