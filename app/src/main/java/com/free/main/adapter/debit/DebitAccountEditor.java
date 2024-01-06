package com.free.main.adapter.debit;

import static com.utilities.UserUtility.userLoginId;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class DebitAccountEditor extends AppCompatActivity {

    private FloatingActionButton debit_account_menu_button;
    private TextView debit_account_id;
    private TextInputLayout debit_account_credit_amount,
            debit_account_credit_amount_description;
    private TextInputEditText debit_account_credit_amount_field,
            debit_account_credit_amount_description_field;
    private Button debit_account_submit_button;
    
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_account_editor);

        debit_account_menu_button = findViewById(R.id.debit_account_menu_button);

        debit_account_id = findViewById(R.id.debit_account_id);
        debit_account_credit_amount = findViewById(R.id.debit_account_credit_amount);
        debit_account_credit_amount_field = findViewById(R.id.debit_account_credit_amount_field);
        debit_account_credit_amount_description = findViewById(R.id.debit_account_credit_amount_description);
        debit_account_credit_amount_description_field = findViewById(R.id.debit_account_credit_amount_description_field);
        debit_account_submit_button = findViewById(R.id.debit_account_submit_button);

        Bundle getExtras = getIntent().getExtras();
        assert getExtras != null;
        String walletId = getExtras.getString("id").toString();

        ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));
        Supplier<Boolean> allIsTrue = () -> debit_account_credit_amount.getHintTextColor() == greenColor;

        debit_account_id.setText(String.format("%s : %s", debit_account_id.getText().toString().trim(), walletId));
        debit_account_submit_button.setEnabled(false);
        debit_account_menu_button.setOnClickListener(view -> {
            Intent intent = new Intent(DebitAccountEditor.this, MainPage.class);
            startActivity(intent);
            finish();
        });
        debit_account_credit_amount_field.addTextChangedListener(new TextWatcher() {
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
                    debit_account_credit_amount
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        debit_account_submit_button.setEnabled(true);
                    }
                }
                else{
                    debit_account_credit_amount
                            .setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    debit_account_submit_button.setEnabled(false);
                }
            }
        });

        debit_account_submit_button.setOnClickListener(view ->
        {
            Log.Builder log = new Log.Builder()
                    .setContentDescription(debit_account_credit_amount_description_field.getText().toString())
                    .setDate(LocalDateTime.now())
                    .setEmail(UserUtility.userEmail)
                    .setDebit(Double.parseDouble(debit_account_credit_amount_field.getText().toString()));

            log.setId(DebitAccountEditor.this).thenAccept(then ->
            {
                FirebaseDatabase.getInstance()
                        .getReference("Users")
                        .child(userLoginId)
                        .get()
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
                                                Log buildedLog = log.Build();
                                                double moneyCase =
                                                        Double.parseDouble(Objects.requireNonNull(snapshot.child("moneyCase")
                                                                .getValue().toString()));
                                                moneyCase = UserUtility.DoubleFormatter(moneyCase + buildedLog.getDebit());

                                                if(moneyCase <= 0)
                                                {
                                                    FirebaseDatabase
                                                            .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                            .getReference()
                                                            .child("Wallets")
                                                            .child(walletId)
                                                            .child("moneyCase")
                                                            .setValue(moneyCase);

                                                    FirebaseDatabase
                                                            .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                            .getReference()
                                                            .child("Wallets")
                                                            .child(walletId)
                                                            .child("walletLogs")
                                                            .child(buildedLog.getId())
                                                            .setValue(buildedLog.toJsonObject());
                                                    ApiUsageSingleton.GetInstance(1).consumeApi();
                                                }
                                                else{
                                                    Toast.makeText(DebitAccountEditor.this,
                                                                    "Your credit should not pass more than initial wallet case.",
                                                                    Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                Intent intent = new Intent(DebitAccountEditor.this, MainPage.class);
                                startActivity(intent);
                                finish();
                            }
                            else{
                                Toast.makeText(DebitAccountEditor.this, "You have no api usage tickets. Please contact us.", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(Throwable::printStackTrace)
                        .addOnCanceledListener(() -> {

                        });


            }).exceptionally(x -> {
                x.printStackTrace();
                return null;
            });
        });
    }
}