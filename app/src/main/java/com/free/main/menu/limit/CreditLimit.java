package com.free.main.menu.limit;

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

public class CreditLimit extends AppCompatActivity {
    private FloatingActionButton credit_limit_menu_button;
    private ConstraintLayout credit_limit_constraintlayout_1;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_limit);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);



        NetworkCallback(this, () ->
        {
            credit_limit_constraintlayout_1 = findViewById(R.id.credit_limit_constraintlayout_1);
            credit_limit_menu_button = findViewById(R.id.credit_limit_menu_button);
            credit_limit_menu_button.setOnClickListener(view -> finish());


        });
    }
}