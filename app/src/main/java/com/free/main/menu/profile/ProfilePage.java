package com.free.main.menu.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.abstr.concrete.singletons.ApiUsageSingleton;
import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.utilities.UserUtility;

import static com.utilities.UserUtility.userApiCounts;
import static com.utilities.UserUtility.userLoginId;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;

public class ProfilePage extends AppCompatActivity
{
    private FloatingActionButton profile_page_back_button;
    private TextView profile_page_id_text, profile_page_api_counting_text, profile_page_email_text, profile_page_name_surname_header;
    private TextInputLayout profile_page_name_surname_text;
    private TextInputEditText profile_page_name_surname_text_field;
    private CheckBox profile_page_remember_box;
    private Button login_page_submit_button;

    @SuppressLint("DefaultLocale")
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        NetworkCallback(this, () ->
        {
            profile_page_back_button = findViewById(R.id.profile_page_back_button);
            profile_page_id_text = findViewById(R.id.profile_page_id_text);
            profile_page_api_counting_text = findViewById(R.id.profile_page_api_counting_text);
            profile_page_email_text = findViewById(R.id.profile_page_email_text);
            profile_page_name_surname_header = findViewById(R.id.profile_page_name_surname_header);
            profile_page_name_surname_text = findViewById(R.id.profile_page_name_surname_text);
            profile_page_name_surname_text_field = findViewById(R.id.profile_page_name_surname_text_field);
            profile_page_remember_box = findViewById(R.id.profile_page_remember_box);
            login_page_submit_button = findViewById(R.id.login_page_submit_button);

            profile_page_back_button.setOnClickListener(view -> finish());
            profile_page_id_text.setText(userLoginId);
            ApiUsageSingleton.GetInstance().getApiCount(profile_page_api_counting_text);
            profile_page_name_surname_text_field.setText(UserUtility.userNameAndSurname);
            profile_page_email_text.setText(UserUtility.userEmail);

            login_page_submit_button.setOnClickListener(view ->
            {

            });
        });
    }
}