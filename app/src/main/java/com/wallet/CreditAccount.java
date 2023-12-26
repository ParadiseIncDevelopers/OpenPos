package com.wallet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;

import com.free.MainPage;
import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.UserUtility.userEmail;


public class CreditAccount extends AppCompatActivity
{
    private TextInputLayout add_money_page_text, add_money_page_decimal_text;
    private TextInputEditText add_money_page_text_field, add_money_page_decimal_text_field;
    private FloatingActionButton add_money_page_submit_button;

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
            add_money_page_submit_button.setOnClickListener(view ->
            {
                double money;

                if(add_money_page_decimal_text_field.getText().toString().isEmpty())
                {
                    money = Double.parseDouble(add_money_page_text_field.getText().toString());
                }
                else{
                    money = Double.parseDouble(add_money_page_text_field.getText().toString()) +
                            (Double.parseDouble(add_money_page_decimal_text_field.getText().toString()) / 100.0);
                }

                //Wallet.addMoneyToWallet(CreditAccount.this, userEmail, money);
                Intent intent = new Intent(CreditAccount.this, MainPage.class);
                intent.putExtra("Email", userEmail);
                startActivity(intent);
                finish();
            });
        });
    }
}