package com.free.main.menu.createAccount;

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

import com.free.main.MainPage;
import com.free.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.utilities.UtilityValues;
import com.models.wallet.Wallet;

import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import static com.utilities.UserUtility.userEmail;

public class CreateAccount extends AppCompatActivity
{
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
                if(Pattern.compile("^.{4,40}$").matcher(editable.toString()).matches())
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
            String Type = create_account_text_account_type_auto.getText().toString();
            String AccountName = create_account_text_account_name_field.getText().toString();

            Wallet.Builder wallet = new Wallet.Builder()
                    .setAccountName(AccountName)
                    .setCreationDate(LocalDateTime.now())
                    .setEmail(userEmail)
                    .setCurrency(Type)
                    .setMoneyCase(0.0);

            wallet.setId(this).thenAccept(task -> {
                Wallet BuildedWallet = task.Build();
                FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                        .getReference()
                        .child("Wallets")
                        .child(BuildedWallet.getId())
                        .setValue(BuildedWallet.toJsonObject());

                Intent intent = new Intent(CreateAccount.this, MainPage.class);
                startActivity(intent);
            }).exceptionally(x -> {
                x.printStackTrace();
                return null;
            });



        });
    }
}