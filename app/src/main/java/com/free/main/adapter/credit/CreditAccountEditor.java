package com.free.main.adapter.credit;

import static com.utilities.UserUtility.userLoginId;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.abstr.concrete.singletons.ApiUsageSingleton;
import com.free.R;
import com.free.main.MainPage;
import com.free.main.adapter.debit.DebitAccountEditor;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.logs.Log;
import com.utilities.UserUtility;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class CreditAccountEditor extends AppCompatActivity {

    private FloatingActionButton credit_account_menu_button;
    private TextView credit_account_id;
    private TextInputLayout credit_account_credit_amount,
            credit_account_credit_amount_description;
    private TextInputEditText credit_account_credit_amount_field,
            credit_account_credit_amount_description_field;
    private Button credit_account_submit_button;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_account_editor);

        credit_account_menu_button = findViewById(R.id.credit_account_menu_button);

        credit_account_id = findViewById(R.id.credit_account_id);
        credit_account_credit_amount = findViewById(R.id.credit_account_credit_amount);
        credit_account_credit_amount_field = findViewById(R.id.credit_account_credit_amount_field);
        credit_account_credit_amount_description = findViewById(R.id.credit_account_credit_amount_description);
        credit_account_credit_amount_description_field = findViewById(R.id.credit_account_credit_amount_description_field);
        credit_account_submit_button = findViewById(R.id.credit_account_submit_button);

        Bundle getExtras = getIntent().getExtras();
        assert getExtras != null;
        String walletId = getExtras.getString("id").toString();

        ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));
        Supplier<Boolean> allIsTrue = () -> credit_account_credit_amount.getHintTextColor() == greenColor;

        credit_account_id.setText(String.format("%s : %s", credit_account_id.getText().toString().trim(), walletId));
        credit_account_submit_button.setEnabled(false);
        credit_account_menu_button.setOnClickListener(view -> {
            Intent intent = new Intent(CreditAccountEditor.this, MainPage.class);
            startActivity(intent);
            finish();
        });
        credit_account_credit_amount_field.addTextChangedListener(new TextWatcher() {
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
                    credit_account_credit_amount
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        credit_account_submit_button.setEnabled(true);
                    }
                }
                else{
                    credit_account_credit_amount
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    credit_account_submit_button.setEnabled(false);
                }
            }
        });

        credit_account_submit_button.setOnClickListener(view ->
        {
            //Creates the log.
            Log.Builder log = new Log.Builder()
                    .setContentDescription(credit_account_credit_amount_description_field.getText().toString())
                    .setDate(LocalDateTime.now())
                    .setEmail(UserUtility.userEmail)
                    .setCredit(Double.parseDouble(credit_account_credit_amount_field.getText().toString()));

            //Set the id.
            log.setId(CreditAccountEditor.this).thenAccept(then ->
            {
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userLoginId).get()
                        .addOnSuccessListener(snapshot -> {

                            int apiCounting = Integer.parseInt(snapshot.child("apiCounting").getValue().toString());
                            if (apiCounting != 0)
                            {
                                //Find the wallet
                                FirebaseDatabase
                                        .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                        .getReference()
                                        .child("Wallets")
                                        .child(walletId)
                                        .addListenerForSingleValueEvent(new ValueEventListener()
                                        {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot)
                                            {
                                                //Then Build the log.
                                                Log buildedLog = log.Build();

                                                Consumer<Double> creditAccountAction = (s) ->
                                                {
                                                    FirebaseDatabase
                                                            .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                            .getReference()
                                                            .child("Wallets")
                                                            .child(walletId)
                                                            .child("moneyCase")
                                                            .setValue(s);

                                                    //create data.
                                                    FirebaseDatabase
                                                            .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                            .getReference()
                                                            .child("Wallets")
                                                            .child(walletId)
                                                            .child("walletLogs")
                                                            .child(buildedLog.getId())
                                                            .setValue(buildedLog.toJsonObject());
                                                };

                                                if(!snapshot.child("creditLimit").exists())
                                                {
                                                    double moneyCase =
                                                            Double.parseDouble(Objects.requireNonNull(snapshot.child("moneyCase")
                                                                    .getValue().toString()));
                                                    moneyCase = UserUtility.DoubleFormatter(moneyCase - buildedLog.getCredit());

                                                    creditAccountAction.accept(moneyCase);
                                                    ApiUsageSingleton.GetInstance(1).consumeApi();
                                                }
                                                else {
                                                    double creditLimit =
                                                            Double.parseDouble(Objects.requireNonNull(snapshot.child("creditLimit")
                                                                    .getValue().toString()));
                                                    double moneyCase =
                                                            Double.parseDouble(Objects.requireNonNull(snapshot.child("moneyCase")
                                                                    .getValue().toString()));
                                                    moneyCase = UserUtility.DoubleFormatter(moneyCase - buildedLog.getCredit());

                                                    if(moneyCase < creditLimit)
                                                    {
                                                        creditAccountAction.accept(moneyCase);
                                                    }
                                                    else{
                                                        //error
                                                        Toast.makeText(CreditAccountEditor.this,
                                                                        "You shall not credit this account more than your limit.",
                                                                        Toast.LENGTH_SHORT)
                                                                .show();
                                                    }
                                                    ApiUsageSingleton.GetInstance(1).consumeApi();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                Intent intent = new Intent(CreditAccountEditor.this, MainPage.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(CreditAccountEditor.this, "You have no api usage tickets. Please contact us.", Toast.LENGTH_SHORT).show();
                            }


                        })
                        .addOnCanceledListener(() -> {})
                        .addOnFailureListener(Throwable::printStackTrace);


            }).exceptionally(x -> {
                x.printStackTrace();
                return null;
            });
        });
    }
}