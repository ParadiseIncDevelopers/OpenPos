package com.free;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class LoginTypes extends AppCompatActivity
{
    private FloatingActionButton login_types_registered_user_button, login_types_anonymous_user_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_types);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        login_types_registered_user_button = findViewById(R.id.login_types_registered_user_button);
        login_types_anonymous_user_button = findViewById(R.id.login_types_anonymous_user_button);

        login_types_registered_user_button.setOnClickListener(view -> {
            Intent intent = new Intent(LoginTypes.this, AnonymousLogin.class);
            startActivity(intent);
            finish();
        });

        login_types_anonymous_user_button.setOnClickListener(view -> {
            Intent intent = new Intent(LoginTypes.this, Login.class);
            startActivity(intent);
            finish();
        });
    }
}