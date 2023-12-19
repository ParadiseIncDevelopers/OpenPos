package com.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toolbar;
import com.free.MainPage;
import com.free.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.classes.EncryptorClass;
import com.utilities.classes.UtilityValues;
import com.wallet.Wallet;
import com.wallet.Models.Log;
import org.jetbrains.annotations.NotNull;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.utilities.classes.LoginFactoryClass.userCurrency;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.userMoneyCase;
import static com.utilities.classes.LoginFactoryClass.userWalletLogs;
import static com.utilities.classes.LoginFactoryClass.userWallets;
import static com.utilities.classes.LoginFactoryClass.walletTaken;
import static com.wallet.Wallet.paymentKeyCreator;
import static com.wallet.Wallet.walletKeyCreator;

public class CreateAccount extends AppCompatActivity
{
    private Toolbar create_account_toolbar;
    private TextInputLayout create_account_text_account_type;
    private AutoCompleteTextView create_account_text_account_type_auto;
    private Button create_account_submit_button;
    private Dialog dialog;


    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        create_account_toolbar = findViewById(R.id.create_account_toolbar);
        create_account_text_account_type = findViewById(R.id.create_account_text_account_type);
        create_account_text_account_type_auto = findViewById(R.id.create_account_text_account_type_auto);
        create_account_submit_button = findViewById(R.id.create_account_submit_button);

        create_account_submit_button.setEnabled(false);

        ArrayAdapter<String> currency = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, UtilityValues.Currencies);
        create_account_text_account_type_auto.setAdapter(currency);

        create_account_text_account_type_auto.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                create_account_submit_button.setEnabled(UtilityValues.Currencies.stream().anyMatch(x -> x.equals(s.toString())));
            }
        });

        create_account_submit_button.setOnClickListener(view ->
        {
            runOnUiThread(() -> {
                dialog = new Dialog(CreateAccount.this);
                dialog.requestWindowFeature(FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.dialog_main_page_loading);
                dialog.show();
            });

            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.O)
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                        {
                            String Currency = create_account_text_account_type_auto.getText().toString();

                            Map<String, Object> wallet = new HashMap<>();
                            Map<String, String> walletLogs = new HashMap<>();
                            Map<String, Map<String, String>> logs = new HashMap<>();
                            Map<String, Object> walletEncryption = new HashMap<>();

                            Log zeroLog = new Log.Builder()
                                    .SetEmail(userEmail)
                                    .SetCommission("NO_COMMISSION")
                                    .SetContentDescription("WALLET CREATED.")
                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                    .SetRest("0.00")
                                    .SetSpend("0.00")
                                    .Build();

                            walletLogs.put("email", zeroLog.getEmail());
                            walletLogs.put("commission", zeroLog.getCommission());
                            walletLogs.put("contentDescription", zeroLog.getContentDescription());
                            walletLogs.put("date", zeroLog.getDate());
                            walletLogs.put("spend", zeroLog.getSpend());
                            walletLogs.put("rest", zeroLog.getRest());
                            logs.put("0", walletLogs);

                            final String[] thePaymentKey = {paymentKeyCreator.get()};
                            final String[] theWalletKey = {walletKeyCreator.get()};

                            if(snapshot.hasChildren())
                            {
                                for(DataSnapshot snap : snapshot.child(EncryptorClass.setSecurePassword(userEmail)).getChildren())
                                {
                                    /*if(snap.child("EncryptionKeys").child("WalletKey").getValue().toString().equals(EncryptorClass.Encrypt(theWalletKey[0])))
                                    {
                                        theWalletKey[0] = walletKeyCreator.get();
                                    }

                                    if(snap.child("EncryptionKeys").child("PaymentKey").getValue().toString().equals(EncryptorClass.Encrypt(thePaymentKey[0])))
                                    {
                                        thePaymentKey[0] = paymentKeyCreator.get();
                                    }*/
                                }

                                //walletEncryption.put("WalletKey", EncryptorClass.Encrypt(theWalletKey[0]));
                                //walletEncryption.put("PaymentKey", EncryptorClass.Encrypt(thePaymentKey[0]));

                                wallet.put("MoneyCase", 0.0);
                                wallet.put("Currency", Currency);
                                wallet.put("Logs", logs);

                            }
                            else{

                                //walletEncryption.put("WalletKey", EncryptorClass.Encrypt(theWalletKey[0]));
                                //walletEncryption.put("PaymentKey", EncryptorClass.Encrypt(thePaymentKey[0]));

                                wallet.put("MoneyCase", 0.0);
                                wallet.put("Currency", Currency);
                                wallet.put("Logs", walletLogs);

                            }
                            wallet.put("EncryptionKeys", walletEncryption);

                            Wallet newWallet = new Wallet.Builder()
                                    //.setPaymentKey(EncryptorClass.Encrypt(thePaymentKey[0]))
                                    .setMoneyCase(0.0)
                                    .setEmail(userEmail)
                                    .setCurrency(Currency)
                                    //.setWalletKey(EncryptorClass.Encrypt(theWalletKey[0]))
                                    .Build();

                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(userEmail))
                                    .child(EncryptorClass.setSecurePassword(theWalletKey[0]))
                                    .setValue(wallet);

                            walletTaken = EncryptorClass.setSecurePassword(theWalletKey[0]);

                            ArrayList<Log> newLog = new ArrayList<>();
                            newLog.add(zeroLog);

                            userWallets.add(newWallet);
                            userMoneyCase.put(walletTaken, String.valueOf(newWallet.getMoneyCase()));
                            userCurrency.put(walletTaken, newWallet.getCurrency());
                            userWalletLogs.put(walletTaken, newLog);

                            dialog.dismiss();
                            Intent intent = new Intent(CreateAccount.this, MainPage.class);
                            intent.putExtra("Email", userEmail);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        });
    }
}