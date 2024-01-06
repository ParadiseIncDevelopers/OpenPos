package com.free.login;

import static android.view.Window.FEATURE_NO_TITLE;

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
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.free.forgotpassword.ForgotPassword;
import com.free.main.MainPage;
import com.free.R;
import com.free.register.Register;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.UserUtility;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;

public class Login extends AppCompatActivity
{
    private TextInputLayout login_page_email_text, login_page_password_text;
    private TextInputEditText login_page_email_text_field, login_page_password_text_field;
    private CheckBox login_page_remember_box;
    private Button login_page_submit_button, login_page_register_account_page;
    private TextView login_page_error_text, login_page_forgot_password_text;
    private Dialog dialog;

    private final Supplier<String> getMacAddress = () -> {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    };

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            login_page_error_text = findViewById(R.id.login_page_error_text);
            login_page_forgot_password_text = findViewById(R.id.login_page_forgot_password_text);
            login_page_email_text = findViewById(R.id.login_page_email_text);
            login_page_password_text = findViewById(R.id.login_page_password_text);
            login_page_email_text_field = findViewById(R.id.login_page_email_text_field);
            login_page_password_text_field = findViewById(R.id.login_page_password_text_field);
            login_page_remember_box = findViewById(R.id.login_page_remember_box);
            login_page_submit_button = findViewById(R.id.login_page_submit_button);
            login_page_register_account_page = findViewById(R.id.login_page_register_account_page);

            ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));
            Supplier<Boolean> allIsTrue = () ->
                    login_page_email_text.getHintTextColor() == greenColor &&
                            login_page_password_text.getHintTextColor() == greenColor;

            login_page_email_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(s.toString()).matches())
                    {
                        login_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            login_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        login_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            login_page_password_text_field.addTextChangedListener(new TextWatcher() {
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
                    if(Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")
                            .matcher(s.toString()).matches())
                    {
                        login_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            login_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        login_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Looper.getMainLooper();

            login_page_submit_button.setOnClickListener(view ->
            {
                runOnUiThread(() -> {
                    dialog = new Dialog(Login.this);
                    dialog.requestWindowFeature(FEATURE_NO_TITLE);
                    dialog.setCancelable(false);
                    dialog.setContentView(R.layout.dialog_main_page_loading);
                    dialog.show();
                });

                String textEmail = login_page_email_text_field.getText().toString();
                String textPassword = login_page_password_text_field.getText().toString();

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signInWithEmailAndPassword(textEmail, textPassword)
                        .addOnSuccessListener(authResult ->
                        {
                            UserUtility.userEmail = Objects.requireNonNull(authResult.getUser()).getEmail();
                            UserUtility.userLoginId = authResult.getUser().getUid();

                            if(login_page_remember_box.isChecked())
                            {
                                FirebaseDatabase.getInstance()
                                        .getReference()
                                        .child("Users")
                                        .child(UserUtility.userLoginId).child("rememberUser")
                                        .setValue(true);
                            }

                            FirebaseDatabase.getInstance()
                                    .getReference()
                                    .child("Users")
                                    .child(UserUtility.userLoginId)
                                    .addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot)
                                        {
                                            UserUtility.userNameAndSurname = snapshot.child("nameSurname")
                                                    .getValue().toString();

                                            Intent intent = new Intent(Login.this, MainPage.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        })
                        .addOnFailureListener(exception ->
                        {
                            login_page_error_text.setText(exception.getMessage());
                            new Handler().postDelayed(() -> {
                                dialog.dismiss();
                            }, 500);
                        })
                        .addOnCanceledListener(() -> login_page_error_text.setText("Operation canceled."));
            });

            login_page_forgot_password_text.setOnClickListener(view -> {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
                finish();
            });

            login_page_register_account_page.setOnClickListener(view ->
            {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}