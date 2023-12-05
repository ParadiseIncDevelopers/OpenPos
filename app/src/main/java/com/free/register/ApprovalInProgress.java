package com.free.register;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.free.login.Login;
import com.free.R;

public class ApprovalInProgress extends AppCompatActivity {

    private TextView approval_in_progress_text;
    private int count = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_in_progress);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        approval_in_progress_text = findViewById(R.id.approval_in_progress_text);
        new CountDownTimer(5000, 1000) {

            public void onTick(long millisUntilFinished)
            {
                count--;
                approval_in_progress_text.setText(String.format("Redirection to the login in : %s", count));
            }

            public void onFinish()
            {
                Intent intent = new Intent(ApprovalInProgress.this, Login.class);
                startActivity(intent);
                finish();
            }
        }.start();
    }

    @Override
    public void onBackPressed()
    {
        Toast.makeText(this, "You cannot go back.", Toast.LENGTH_SHORT).show();
    }
}