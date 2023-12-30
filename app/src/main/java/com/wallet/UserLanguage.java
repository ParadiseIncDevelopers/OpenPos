package com.wallet;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import com.free.R;
import static com.free.NetworkChangeReceiver.NetworkCallback;

public class UserLanguage extends AppCompatActivity
{
    private ConstraintLayout user_languages_button_1,user_languages_button_2,user_languages_button_3,user_languages_button_4,user_languages_button_5,user_languages_button_6;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_languages);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            user_languages_button_1 = findViewById(R.id.user_languages_button_1);
            user_languages_button_2 = findViewById(R.id.user_languages_button_2);
            user_languages_button_3 = findViewById(R.id.user_languages_button_3);
            user_languages_button_4 = findViewById(R.id.user_languages_button_4);
            user_languages_button_5 = findViewById(R.id.user_languages_button_5);
            user_languages_button_6 = findViewById(R.id.user_languages_button_6);

            user_languages_button_1.setOnClickListener(view -> {});
            user_languages_button_2.setOnClickListener(view -> {});
            user_languages_button_3.setOnClickListener(view -> {});
            user_languages_button_4.setOnClickListener(view -> {});
            user_languages_button_5.setOnClickListener(view -> {});
            user_languages_button_6.setOnClickListener(view -> {});
        });
    }
}