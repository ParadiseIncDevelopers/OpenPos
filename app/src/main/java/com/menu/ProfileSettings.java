package com.menu;

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
import android.widget.Button;
import android.widget.CheckBox;
import com.free.MainPage;
import com.free.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.FirebaseDatabase;
import com.menu.profileSettingsPack.PasswordAndSecuritySettings;
import com.utilities.classes.EncryptorClass;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import static com.utilities.classes.UserUtility.userEmail;
import static com.utilities.classes.UserUtility.userNameAndSurname;

public class ProfileSettings extends AppCompatActivity
{
    private CheckBox profile_settings_remember_user_check_box;
    private Button profile_settings_update_submit, profile_settings_password_and_security_settings_button;
    private TextInputLayout profile_settings_name_and_surname_text, profile_settings_phone_number_text;
    private TextInputEditText profile_settings_name_and_surname_text_field,
            profile_settings_phone_number_text_field;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        profile_settings_password_and_security_settings_button = findViewById(R.id.profile_settings_password_and_security_settings_button);

        profile_settings_remember_user_check_box = findViewById(R.id.profile_settings_remember_user_check_box);
        profile_settings_name_and_surname_text = findViewById(R.id.password_and_security_settings_password_1_text);
        profile_settings_phone_number_text = findViewById(R.id.password_and_security_settings_password_2_text);
        profile_settings_name_and_surname_text_field = findViewById(R.id.password_and_security_settings_password_1_text_field);
        profile_settings_phone_number_text_field = findViewById(R.id.profile_settings_phone_number_text_field);
        profile_settings_update_submit = findViewById(R.id.profile_settings_update_submit);

        profile_settings_password_and_security_settings_button.setOnClickListener(view ->
        {
            Intent intent = new Intent(ProfileSettings.this, PasswordAndSecuritySettings.class);
            startActivity(intent);
        });

        Consumer<Boolean> profileIsChecked = (s) ->
        {
            if(s)
            {
                Map<String, Object> rememberElement = new HashMap<>();
                rememberElement.put("Email", userEmail);
                rememberElement.put("MacAddress",getMacAddress.get());
                rememberElement.put("NameSurname", userNameAndSurname);
                FirebaseDatabase.getInstance().getReference("RememberUser").child(EncryptorClass.setSecurePassword(userEmail)).setValue(rememberElement);
            }
            else {
                FirebaseDatabase.getInstance().getReference("RememberUser").child(EncryptorClass.setSecurePassword(userEmail)).setValue(null);
            }
        };

            Supplier<Boolean> allIsTrue = () -> profile_settings_name_and_surname_text.getBoxStrokeColor() == Color.parseColor("#558B2F") &&
                            profile_settings_phone_number_text.getBoxStrokeColor() == Color.parseColor("#558B2F");


        profile_settings_name_and_surname_text_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                if(Pattern.compile("\\b([A-ZÀ-ÿ][-,a-z. ']+[ ]*)+").matcher(s.toString()).matches())
                {
                    profile_settings_name_and_surname_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        profile_settings_update_submit.setEnabled(true);
                    }
                }
                else{
                    profile_settings_name_and_surname_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                }
            }
        });

        profile_settings_phone_number_text_field.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(Pattern.compile("^(\\+\\d{1,2}\\s?)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$").matcher(s.toString()).matches())
                {
                    profile_settings_phone_number_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(allIsTrue.get())
                    {
                        profile_settings_update_submit.setEnabled(true);
                    }
                }
                else{
                    profile_settings_phone_number_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                }
            }
        });

        profile_settings_update_submit.setOnClickListener(view ->
        {
            profileIsChecked.accept(profile_settings_remember_user_check_box.isChecked());
            //String nameAndSurnameEncrypted = EncryptorClass.Encrypt(profile_settings_name_and_surname_text_field.getText().toString());
            //String phoneNumberEncrypted = EncryptorClass.Encrypt(profile_settings_phone_number_text_field.getText().toString());
            //FirebaseDatabase.getInstance().getReference("Users").child(EncryptorClass.setSecurePassword(userEmail)).child("NameAndSurname").setValue(nameAndSurnameEncrypted);
            //FirebaseDatabase.getInstance().getReference("Users").child(EncryptorClass.setSecurePassword(userEmail)).child("PhoneNumber").setValue(phoneNumberEncrypted);

            //userNameAndSurname = EncryptorClass.Decrypt(nameAndSurnameEncrypted);
            //userPhoneNumber = EncryptorClass.Decrypt(phoneNumberEncrypted);

            Intent intent = new Intent(ProfileSettings.this, MainPage.class);
            startActivity(intent);
            finish();
        });
    }
}