package com.free.main.menu.profile;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.free.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;

import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;

public class ProfilePage extends AppCompatActivity
{
    private FloatingActionButton profile_page_FAB2_Menu,credit_account_FAB2_add_anonymous;
    private ConstraintLayout profile_page_ConstraintLayout1_Exit;
    private ShapeableImageView profile_page_Logo1_Acoount;
    private TextView profile_page_TextView1_Header;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        NetworkCallback(this, () ->
        {
            profile_page_FAB2_Menu = findViewById(R.id.profile_page_FAB2_Menu);
            credit_account_FAB2_add_anonymous = findViewById(R.id.credit_account_FAB2_add_anonymous);
            profile_page_FAB2_Menu.setOnClickListener(view -> finish());




        });
    }
}