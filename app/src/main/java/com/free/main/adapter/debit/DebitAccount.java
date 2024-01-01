package com.free.main.adapter.debit;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;


public class DebitAccount extends AppCompatActivity
{
    private FloatingActionButton activity_debit_account_FAB_back;
    private ConstraintLayout activity_debit_account_constraint_layout_1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        activity_debit_account_FAB_back = findViewById(R.id.activity_debit_account_FAB_back);
        activity_debit_account_constraint_layout_1 = findViewById(R.id.activity_debit_account_constraint_layout_1);
        activity_debit_account_FAB_back.setOnClickListener(view -> finish());

        NetworkCallback(this, () ->
        {


        });
    }
}