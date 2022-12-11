package com.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.free.mainPage.MainPage;
import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.classes.EncryptorClass;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.LoginFactoryClass.userCurrency;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.walletTaken;

public class WithdrawMoney extends AppCompatActivity
{
    private LinearLayout withdraw_money_taker_resume_container;
    private TextView withdraw_money_up_to_text, withdraw_money_taker_resume_header, withdraw_money_withdraw_text,
            withdraw_money_commission_fee_text, withdraw_money_total_text;
    private TextInputLayout withdraw_money_page_text, withdraw_money_decimal_text;
    private TextInputEditText withdraw_money_page_text_field, withdraw_money_decimal_text_field;
    private FloatingActionButton withdraw_money_page_resume_button;
    private Button withdraw_money_submit_button;

    private double money;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw_money);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        withdraw_money_taker_resume_container = findViewById(R.id.withdraw_money_taker_resume_container);
        withdraw_money_up_to_text = findViewById(R.id.withdraw_money_up_to_text);
        withdraw_money_page_text = findViewById(R.id.withdraw_money_page_text);
        withdraw_money_decimal_text = findViewById(R.id.withdraw_money_decimal_text);
        withdraw_money_page_text_field = findViewById(R.id.withdraw_money_page_text_field);
        withdraw_money_decimal_text_field = findViewById(R.id.withdraw_money_decimal_text_field);
        withdraw_money_taker_resume_header = findViewById(R.id.withdraw_money_taker_resume_header);
        withdraw_money_withdraw_text = findViewById(R.id.withdraw_money_withdraw_text);
        withdraw_money_commission_fee_text = findViewById(R.id.withdraw_money_commission_fee_text);
        withdraw_money_total_text = findViewById(R.id.withdraw_money_total_text);
        withdraw_money_page_resume_button = findViewById(R.id.withdraw_money_page_resume_button);
        withdraw_money_submit_button = findViewById(R.id.withdraw_money_submit_button);

        NetworkCallback(this, () ->
        {
            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .child(EncryptorClass.setSecurePassword(userEmail))
                    .child(walletTaken)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            withdraw_money_up_to_text.setText(String.format("You can withdraw up to : %s %s", snapshot.child("MoneyCase").getValue().toString(), snapshot.child("Currency").getValue().toString()));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

            withdraw_money_page_resume_button.setOnClickListener(view -> EncryptorClass.BiometricClass.checkEncryption(this, () ->
            {

                if(withdraw_money_decimal_text_field.getText().toString().isEmpty())
                {
                    money = Double.parseDouble(withdraw_money_page_text_field.getText().toString());
                }
                else{

                    double q1 = Double.parseDouble(withdraw_money_page_text_field.getText().toString());
                    double q2 = Double.parseDouble(withdraw_money_decimal_text_field.getText().toString()) / 100.0;
                    money = q1 + q2;
                }

                runOnUiThread(() -> {
                    withdraw_money_taker_resume_header.setVisibility(View.VISIBLE);
                    withdraw_money_taker_resume_container.setVisibility(View.VISIBLE);
                    withdraw_money_withdraw_text.setText(String.format("You are withdrawing : %s %s", new DecimalFormat("#.##").format(money).replace(",","."), userCurrency.get(walletTaken)));
                    withdraw_money_commission_fee_text.setText(String.format("The commission is : %s", new DecimalFormat("#.##").format(Wallet.returnCommission(money)).replace(",",".")));
                    withdraw_money_total_text.setText(String.format("The total is : %s",
                            Double.parseDouble(new DecimalFormat("#.##").format(money).replace(",",".")) +
                            Double.parseDouble(new DecimalFormat("#.##").format(Wallet.returnCommission(money)).replace(",","."))
                            ));
                    withdraw_money_submit_button.setEnabled(true);
                });
            }));

            withdraw_money_submit_button.setOnClickListener(view ->
            {
                EncryptorClass.BiometricClass.checkEncryption(WithdrawMoney.this, () ->
                {
                    money = Double.parseDouble(withdraw_money_page_text_field.getText().toString());

                    Wallet.withdrawMoneyFromWallet(WithdrawMoney.this, userEmail, money);



                    Intent intent = new Intent(WithdrawMoney.this, MainPage.class);
                    intent.putExtra("Email", userEmail);
                    startActivity(intent);
                    finish();
                });
            });
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WithdrawMoney.this, MainPage.class);
        intent.putExtra("Email", userEmail);
        startActivity(intent);
        finish();
    }
}