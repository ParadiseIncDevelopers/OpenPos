package com.free.main.menu.findSaveUser;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Build;
import android.os.Bundle;
import com.free.R;
import static com.utilities.NetworkChangeReceiver.NetworkCallback;

public class FindOrSaveUser extends AppCompatActivity {


    private double money = -1;
    private double commission = -1;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_or_save_user);

        NetworkCallback(this, () ->
        {

        });
    }
}