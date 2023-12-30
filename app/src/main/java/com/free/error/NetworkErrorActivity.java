package com.free.error;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;

import com.free.R;

public class NetworkErrorActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_error);

        new Handler().postDelayed(() -> System.exit(0), 1000);
    }
}