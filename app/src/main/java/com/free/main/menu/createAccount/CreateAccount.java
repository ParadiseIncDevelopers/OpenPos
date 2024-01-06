package com.free.main.menu.createAccount;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
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
import android.widget.Toast;
import com.abstr.concrete.singletons.ApiUsageSingleton;
import com.free.main.MainPage;
import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.UserUtility;
import com.utilities.UtilityValues;
import com.models.wallet.Wallet;
import java.time.LocalDateTime;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import static com.utilities.UserUtility.userEmail;
import static com.utilities.UserUtility.userLoginId;

public class CreateAccount extends AppCompatActivity
{
    private TextInputLayout create_account_text_account_type, create_account_text_account_name, create_account_credit_limit;
    private AutoCompleteTextView create_account_text_account_type_auto;
    private TextInputEditText create_account_text_account_name_field, create_account_credit_limit_field;
    private Button create_account_submit_button;
    private FloatingActionButton create_account_menu_button;

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        create_account_menu_button = findViewById(R.id.create_account_menu_button);
        create_account_credit_limit = findViewById(R.id.create_account_credit_limit);
        create_account_credit_limit_field = findViewById(R.id.create_account_credit_limit_field);
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
        create_account_menu_button.setOnClickListener(view -> finish());

        create_account_credit_limit_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(Pattern.compile("^(\\d){1,4}(\\.)(\\d{1,2})$").matcher(s.toString()).matches())
                {
                    create_account_credit_limit
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        create_account_submit_button.setEnabled(true);
                    }
                }
                else{
                    create_account_credit_limit
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    create_account_submit_button.setEnabled(false);
                }

            }
        });

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
            double creditLimit;
            try
            {
                creditLimit = UserUtility.DoubleFormatter(Double.parseDouble(create_account_credit_limit_field.getText().toString()));
            }
            catch (Exception e)
            {
                creditLimit = 150000.0;
            }

            Wallet.Builder wallet = new Wallet.Builder()
                    .setAccountName(AccountName)
                    .setCreationDate(LocalDateTime.now())
                    .setEmail(userEmail)
                    .setCreditLimit(creditLimit)
                    .setCurrency(Type)
                    .setMoneyCase(0.0);

            wallet.setId(this).thenAccept(task -> {
                Wallet BuildedWallet = task.Build();

                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userLoginId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot)
                            {
                                int apiCounting = Integer.parseInt(snapshot.child("apiCounting").getValue().toString());
                                if(apiCounting > 5)
                                {
                                    FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                            .getReference()
                                            .child("Wallets")
                                            .child(BuildedWallet.getId())
                                            .setValue(BuildedWallet.toJsonObject());

                                    ApiUsageSingleton.GetInstance(5).consumeApi();

                                    Intent intent = new Intent(CreateAccount.this, MainPage.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else{
                                    Toast.makeText(CreateAccount.this, "You have no api usage tickets. Please contact us.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }).exceptionally(x -> {
                x.printStackTrace();
                return null;
            });



        });
    }
}