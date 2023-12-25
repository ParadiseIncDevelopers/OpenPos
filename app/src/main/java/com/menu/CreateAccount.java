package com.menu;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.google.android.material.textfield.TextInputEditText;
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
import java.util.function.Supplier;
import java.util.regex.Pattern;

import static android.view.Window.FEATURE_NO_TITLE;
import static com.utilities.classes.LoginFactoryClass.userCurrency;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.userMoneyCase;
import static com.utilities.classes.LoginFactoryClass.userWalletLogs;
import static com.utilities.classes.LoginFactoryClass.userWallets;
import static com.utilities.classes.LoginFactoryClass.walletTaken;


public class CreateAccount extends AppCompatActivity
{
    private Toolbar create_account_toolbar;
    private TextInputLayout create_account_text_account_type, create_account_text_account_name;
    private AutoCompleteTextView create_account_text_account_type_auto;
    private TextInputEditText create_account_text_account_name_field;
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
        create_account_text_account_name = findViewById(R.id.create_account_text_account_name);
        create_account_text_account_name_field = findViewById(R.id.create_account_text_account_name_field);
        create_account_submit_button = findViewById(R.id.create_account_submit_button);

        create_account_submit_button.setEnabled(false);

        ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));

        Supplier<Boolean> allIsTrue = () ->
                create_account_text_account_name.getHintTextColor() == greenColor &&
                        create_account_text_account_type.getHintTextColor() == greenColor;

        create_account_text_account_name_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(editable.toString()).matches())
            {
                create_account_text_account_name.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                if(allIsTrue.get())
                {
                    create_account_submit_button.setEnabled(true);
                }
            }
            else{
                    create_account_text_account_name.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    create_account_submit_button.setEnabled(false);
            }

            }
        });

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
                if(UtilityValues.Currencies.stream().anyMatch(x -> x.equals(s.toString())))
                {
                    create_account_text_account_type.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        create_account_submit_button.setEnabled(true);
                    }
                }
                else{
                    create_account_text_account_type.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    create_account_submit_button.setEnabled(false);
                }

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
                            String Type = create_account_text_account_type_auto.getText().toString();
                            String AccountName = create_account_text_account_name_field.getText().toString();

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

                            }
                            else{

                                //walletEncryption.put("WalletKey", EncryptorClass.Encrypt(theWalletKey[0]));
                                //walletEncryption.put("PaymentKey", EncryptorClass.Encrypt(thePaymentKey[0]));


                            }

                            Wallet newWallet = new Wallet.Builder()
                                    //.setPaymentKey(EncryptorClass.Encrypt(thePaymentKey[0]))
                                    .setMoneyCase(0.0)
                                    .setEmail(userEmail)
                                    .setCurrency(Type)
                                    //.setWalletKey(EncryptorClass.Encrypt(theWalletKey[0]))
                                    .Build();

                            //TODO: buradan devam.

                            /*FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference().push()
                                    .setValue(newWallet.toJsonIObject());

                            walletTaken = EncryptorClass.setSecurePassword(theWalletKey[0]);

                            ArrayList<Log> newLog = new ArrayList<>();
                            newLog.add(zeroLog);*/
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