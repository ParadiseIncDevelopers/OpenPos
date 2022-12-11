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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.utilities.classes.CapturerClass;
import com.utilities.classes.EncryptorClass;

import org.jetbrains.annotations.NotNull;
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.LoginFactoryClass.userEmail;


public class ReceiveMoney extends AppCompatActivity {

    private FloatingActionButton receive_money_page_submit_button;
    private TextInputLayout receive_money_page_text, receive_money_page_decimal_text, receive_money_page_description_text;
    private TextInputEditText receive_money_page_text_field, receive_money_page_decimal_text_field, receive_money_page_description_text_field;
    private TextView receive_money_page_error_text;

    private String description;
    private double theMoney;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_money);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () -> {
            receive_money_page_error_text = findViewById(R.id.receive_money_page_error_text);
            receive_money_page_text = findViewById(R.id.receive_money_page_text);
            receive_money_page_decimal_text = findViewById(R.id.receive_money_page_decimal_text);
            receive_money_page_description_text = findViewById(R.id.password_and_security_settings_password_1_text);
            receive_money_page_text_field = findViewById(R.id.receive_money_page_text_field);
            receive_money_page_decimal_text_field = findViewById(R.id.receive_money_page_decimal_text_field);
            receive_money_page_description_text_field = findViewById(R.id.password_and_security_settings_password_1_text_field);
            receive_money_page_submit_button = findViewById(R.id.receive_money_page_submit_button);

            receive_money_page_submit_button.setOnClickListener(view -> EncryptorClass.BiometricClass.checkEncryption(ReceiveMoney.this, () -> {
                        try {
                            if (receive_money_page_decimal_text_field.getText().toString().isEmpty()) {
                                theMoney = Double.parseDouble(receive_money_page_text_field.getText().toString().replace(".", ""));
                            } else {
                                theMoney = Double.parseDouble(receive_money_page_text_field.getText().toString().replace(".", "")) +
                                        (Double.parseDouble(receive_money_page_decimal_text_field.getText().toString()) / 100.0);
                            }

                            description = receive_money_page_description_text_field.getText().toString();

                            IntentIntegrator intent = new IntentIntegrator(this);
                            intent.setBeepEnabled(true);
                            intent.setOrientationLocked(true);
                            intent.setCaptureActivity(CapturerClass.class);
                            intent.initiateScan();
                        }
                        catch (NumberFormatException e)
                        {
                            receive_money_page_error_text.setText("Please write the bill that you will receive.");
                        }
                    }));
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ReceiveMoney.this, MainPage.class);
        intent.putExtra("Email", userEmail);
        startActivity(intent);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        NetworkCallback(this, () -> {
            if(result.getContents() != null)
            {
                if(!result.getContents().contains(userEmail))
                {
                    String destinationEmail = result.getContents().split("user_email:")[1];
                    String dataWallet = result.getContents().split("<->")[0].split(":")[1];

                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(EncryptorClass.setSecurePassword(result.getContents())))
                                    {
                                        Wallet.transactToWallet(ReceiveMoney.this,
                                                userEmail,
                                                destinationEmail,
                                                dataWallet,
                                                theMoney,
                                                description);
                                        Intent intent = new Intent(ReceiveMoney.this, MainPage.class);
                                        intent.putExtra("Email", userEmail);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        receive_money_page_error_text.setVisibility(View.VISIBLE);
                                        receive_money_page_error_text.setText("This account does not exist.");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                }
                else{
                    receive_money_page_error_text.setVisibility(View.VISIBLE);
                    receive_money_page_error_text.setText("You can't send money to yourself.");
                }
            }
            else{
                receive_money_page_error_text.setVisibility(View.VISIBLE);
                receive_money_page_error_text.setText("Please try again.");
            }
        });
    }
}