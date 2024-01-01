package com.free.main.menu.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import com.free.R;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;

public class ProfilePage extends AppCompatActivity
{
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        NetworkCallback(this, () ->
        {

        });
    }
}