package com.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.free.MainPage;
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
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.UserUtility.userCurrency;
import static com.utilities.classes.UserUtility.userEmail;
import static com.utilities.classes.UserUtility.walletTaken;

public class TransferMoney extends AppCompatActivity {

    private TextInputLayout transfer_money_page_text, transfer_money_decimal_text,
            transfer_money_page_payment_key_text, transfer_money_page_wallet_key_text,
            transfer_money_page_description_text, transfer_money_page_destination_email_text;
    private TextInputEditText transfer_money_page_text_field, transfer_money_decimal_text_field,
            transfer_money_page_payment_key_text_field, transfer_money_page_wallet_key_text_field,
            transfer_money_page_description_text_field, transfer_money_page_destination_email_text_field;
    private FloatingActionButton transfer_money_page_resume_button;
    private TextView transfer_money_taker_resume_header, transfer_money_transfer_text,
            transfer_money_commission_fee_text, transfer_money_total_text, transfer_money_up_to_text;
    private LinearLayout transfer_money_taker_resume_container;
    private Button transfer_money_submit_button;

    private double money = -1;
    private double commission = -1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer_money);

        transfer_money_page_text = findViewById(R.id.transfer_money_page_text);
        transfer_money_decimal_text = findViewById(R.id.transfer_money_decimal_text);
        transfer_money_page_payment_key_text = findViewById(R.id.transfer_money_page_payment_key_text);
        transfer_money_page_wallet_key_text = findViewById(R.id.transfer_money_page_wallet_key_text);
        transfer_money_page_description_text = findViewById(R.id.transfer_money_page_description_text);
        transfer_money_page_destination_email_text = findViewById(R.id.transfer_money_page_destination_email_text);

        transfer_money_page_text_field = findViewById(R.id.transfer_money_page_text_field);
        transfer_money_decimal_text_field = findViewById(R.id.transfer_money_decimal_text_field);
        transfer_money_page_payment_key_text_field = findViewById(R.id.transfer_money_page_payment_key_text_field);
        transfer_money_page_wallet_key_text_field = findViewById(R.id.transfer_money_page_wallet_key_text_field);
        transfer_money_page_description_text_field = findViewById(R.id.transfer_money_page_description_text_field);
        transfer_money_page_destination_email_text_field = findViewById(R.id.transfer_money_page_destination_email_text_field);

        transfer_money_up_to_text = findViewById(R.id.transfer_money_up_to_text);
        transfer_money_taker_resume_container = findViewById(R.id.transfer_money_taker_resume_container);
        transfer_money_taker_resume_header = findViewById(R.id.transfer_money_taker_resume_header);
        transfer_money_transfer_text = findViewById(R.id.transfer_money_transfer_text);
        transfer_money_commission_fee_text = findViewById(R.id.transfer_money_commission_fee_text);
        transfer_money_total_text = findViewById(R.id.transfer_money_total_text);
        transfer_money_page_resume_button = findViewById(R.id.transfer_money_page_resume_button);
        transfer_money_submit_button = findViewById(R.id.transfer_money_submit_button);

        NetworkCallback(this, () ->
        {
            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .child(EncryptorClass.setSecurePassword(userEmail))
                    .child(walletTaken)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            transfer_money_up_to_text.setText(String.format("You can transfer up to : %s %s", snapshot.child("MoneyCase").getValue().toString(), snapshot.child("Currency").getValue().toString()));
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });

            transfer_money_page_resume_button.setOnClickListener(view ->
            {
                String decimal = transfer_money_decimal_text_field.getText().toString();
                String text = transfer_money_page_text_field.getText().toString();
                if(!text.isEmpty())
                {
                    if(decimal.isEmpty())
                    {
                        money = Double.parseDouble(text);
                    }
                    else {
                        double q1 = Double.parseDouble(text);
                        double q2 = Double.parseDouble(decimal) / 100.0;
                        money = q1 + q2;
                    }

                    //commission = Wallet.returnCommission(money);

                    transfer_money_taker_resume_header.setVisibility(View.VISIBLE);
                    transfer_money_taker_resume_container.setVisibility(View.VISIBLE);
                    transfer_money_transfer_text.setText(String.format("You are withdrawing : %s %s", money, userCurrency.get(walletTaken)));
                    transfer_money_commission_fee_text.setText(String.format("The commission is : %s", commission));
                    transfer_money_total_text.setText(String.format("The total is : %s", (money + commission)));
                    transfer_money_submit_button.setEnabled(true);
                }
                else{
                    Toast.makeText(this, "Please write the amount the money you want to transfer.", Toast.LENGTH_SHORT).show();
                }
            });

            /*transfer_money_submit_button.setOnClickListener(view ->
            {
                EncryptorClass.BiometricClass.checkEncryption(this, () -> {
                    Wallet.transactToWallet(this,
                            walletTaken,
                            transfer_money_page_payment_key_text_field.getText().toString(),
                            transfer_money_page_wallet_key_text_field.getText().toString(),
                            transfer_money_page_destination_email_text_field.getText().toString(),
                            money,
                            commission,
                            transfer_money_page_description_text_field.getText().toString());
                });
            });*/
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(TransferMoney.this, MainPage.class);
        intent.putExtra("Email", userEmail);
        startActivity(intent);
        finish();
    }
}