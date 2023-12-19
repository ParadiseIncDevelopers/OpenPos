package com.free.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.free.login.Login;
import com.free.R;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.functions.FirebaseFunctions;
import com.utilities.classes.UtilityValues;
import com.user.UserRegistrar;

import java.util.function.Supplier;
import java.util.regex.Pattern;
import static com.free.NetworkChangeReceiver.NetworkCallback;

import org.json.JSONException;

public class Register extends AppCompatActivity {

    private TextInputLayout register_page_email_text, register_page_name_and_surname_text, register_page_phone_number_text,
            register_page_password_text, register_page_currency_autocomplete, register_page_code_text;
    private TextInputEditText register_page_email_text_field, register_page_name_and_surname_text_field,
            register_page_phone_number_text_field, register_page_password_text_field, register_page_code_text_field;
    private TextView register_error_text;
    private MaterialAutoCompleteTextView register_page_currency_autocomplete_field;
    private Button register_page_submit_button, register_page_login_account_page;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () -> {
            register_error_text = findViewById(R.id.register_error_text);
            register_page_email_text = findViewById(R.id.register_page_email_text);
            register_page_name_and_surname_text = findViewById(R.id.register_page_name_and_surname_text);
            register_page_phone_number_text = findViewById(R.id.register_page_phone_number_text);
            register_page_password_text = findViewById(R.id.register_page_password_text);
            register_page_currency_autocomplete = findViewById(R.id.register_page_currency_autocomplete);
            register_page_email_text_field = findViewById(R.id.register_page_email_text_field);
            register_page_name_and_surname_text_field = findViewById(R.id.register_page_name_and_surname_text_field);
            register_page_phone_number_text_field = findViewById(R.id.register_page_phone_number_text_field);
            register_page_password_text_field = findViewById(R.id.register_page_password_text_field);
            register_page_currency_autocomplete_field = findViewById(R.id.register_page_currency_autocomplete_field);
            register_page_code_text = findViewById(R.id.register_page_code_text);
            register_page_code_text_field = findViewById(R.id.register_page_code_text_field);

            register_page_login_account_page = findViewById(R.id.register_page_login_account_page);
            register_page_submit_button = findViewById(R.id.register_page_submit_button);

            ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));

            Supplier<Boolean> allIsTrue = () ->
                    register_page_email_text.getHintTextColor() == greenColor &&
                            register_page_name_and_surname_text.getHintTextColor() == greenColor &&
                            register_page_phone_number_text.getHintTextColor() == greenColor &&
                            register_page_password_text.getHintTextColor() == greenColor &&
                            register_page_code_text.getHintTextColor() == greenColor &&
                            register_page_currency_autocomplete.getHintTextColor() == greenColor;

            ArrayAdapter<String> currency = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, UtilityValues.Currencies);
            register_page_currency_autocomplete_field.setAdapter(currency);

            register_page_email_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(s.toString()).matches())
                    {
                        register_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        register_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            register_page_name_and_surname_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+").matcher(s.toString()).matches())
                    {
                        register_page_name_and_surname_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        register_page_name_and_surname_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            register_page_phone_number_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$").matcher(s.toString()).matches())
                    {
                        register_page_phone_number_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        register_page_phone_number_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            register_page_password_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$").matcher(s.toString()).matches())
                    {
                        register_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        register_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });

            register_page_code_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if (Pattern.compile("^[0-9]{6}$").matcher(s.toString()).matches())
                    {
                        register_page_code_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if (allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else {
                        register_page_code_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            register_page_currency_autocomplete_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if(!s.toString().isEmpty() && UtilityValues.Currencies.stream().anyMatch(x -> x.equals(s.toString())))
                    {
                        register_page_currency_autocomplete.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            register_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        register_page_currency_autocomplete.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });

            register_page_login_account_page.setOnClickListener(view -> {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
                finish();
            });

            register_page_submit_button.setOnClickListener(view ->
            {
                String Email = register_page_email_text_field.getText().toString().toLowerCase();
                String NameAndSurname = register_page_name_and_surname_text_field.getText().toString().trim();
                String Password = register_page_password_text_field.getText().toString();

                UserRegistrar user = new UserRegistrar.Builder()///
                        .createSaltAndSecret(32)
                        .createId()
                        .setEmail(Email)
                        .setNameSurname(NameAndSurname)
                        .setPhoneNumber(PhoneNumber)
                        .setPassword(Password)
                        .setCurrency(Currency)
                        .setCode(Code)
                        .Build();

                try
                {
                    FirebaseFunctions.getInstance("https://us-central1-openpos-3e0d3.cloudfunctions.net/")
                            .getHttpsCallable("createUser")
                            .call(user.toJsonObject())
                            .addOnSuccessListener(task -> {
                                String result = (String) task.getData();
                                assert result != null;
                                if (result.equals("Your account is in the approval process. Please try again later."))
                                {
                                    Toast.makeText(Register.this, result, Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    if(result.equals("User data added to the database. User is not in the approval process."))
                                    {
                                        FirebaseAuth.getInstance().createUserWithEmailAndPassword(Email, Password)
                                                .addOnSuccessListener(x -> {
                                                    Intent intent = new Intent(Register.this, Login.class);
                                                    //user.createUser();
                                                    startActivity(intent);
                                                    finish();
                                                })
                                                .addOnCanceledListener(() -> {
                                                    Toast.makeText(this, "Operation canceled.", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(x -> {
                                                    Toast.makeText(this, x.getMessage(), Toast.LENGTH_SHORT).show();
                                                });


                                    }
                                    else {
                                        Toast.makeText(Register.this, result, Toast.LENGTH_SHORT).show();
                                    }

                                }
                            })
                            .addOnFailureListener(e ->
                            {
                                Toast.makeText(Register.this, "Function call failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            });
                }
                catch (JSONException e)
                {
                    throw new RuntimeException(e);
                }
            });
        });
    }
}