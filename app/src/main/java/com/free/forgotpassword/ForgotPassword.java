package com.free.forgotpassword;

import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;
import androidx.appcompat.app.AppCompatActivity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.Toast;
import com.free.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import java.util.function.Supplier;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    private TextInputLayout forgot_password_email_text;
    private TextInputEditText forgot_password_email_text_field;
    private Button forgot_password_submit_button, forgot_password_register_account_page;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        NetworkCallback(this, () ->
        {
            forgot_password_email_text = findViewById(R.id.forgot_password_email_text);
            forgot_password_email_text_field = findViewById(R.id.forgot_password_email_text_field);
            forgot_password_submit_button = findViewById(R.id.forgot_password_submit_button);
            forgot_password_register_account_page = findViewById(R.id.forgot_password_register_account_page);

            ColorStateList greenColor = ColorStateList.valueOf(Color.parseColor("#558B2F"));

            Supplier<Boolean> allIsTrue = () -> forgot_password_email_text.getHintTextColor() == greenColor;
            forgot_password_email_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do something before text changes
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do something while text changes
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if(Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(s.toString()).matches())
                    {
                        forgot_password_email_text.setHintTextColor(ColorStateList.valueOf(Color
                                .parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            forgot_password_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        forgot_password_email_text.setHintTextColor(ColorStateList.valueOf(Color
                                .parseColor("#E64A19")));
                    }
                }
            });

            // Set onClickListener for the submit button
            forgot_password_submit_button.setOnClickListener(view -> {
                // Get the email from the input field
                String email = forgot_password_email_text_field.getText().toString().trim();

                // Check if the email is valid (you might want to add your own validation here)

                // Call Firebase method to send password reset email
                FirebaseAuth.getInstance()
                        .sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(ForgotPassword.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(ForgotPassword.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                            }
                        });
            });

            forgot_password_register_account_page.setOnClickListener(view -> {
                // Handle clicking on the register account button (if needed)
            });
        });
    }
}