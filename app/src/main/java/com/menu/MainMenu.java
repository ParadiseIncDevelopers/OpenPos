package com.menu;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import com.free.MainPage;
import com.free.R;
import static com.utilities.classes.LoginFactoryClass.userEmail;

public class MainMenu extends AppCompatActivity {

    private Button main_menu_button_1, main_menu_button_2, main_menu_button_3, main_menu_button_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        main_menu_button_1 = findViewById(R.id.main_menu_button_1);
        main_menu_button_2 = findViewById(R.id.main_menu_button_2);
        main_menu_button_3 = findViewById(R.id.main_menu_button_3);
        main_menu_button_4 = findViewById(R.id.main_menu_button_4);

        main_menu_button_1.setOnClickListener(view ->
        {
        });
        main_menu_button_2.setOnClickListener(view ->
        {
        });
        main_menu_button_3.setOnClickListener(view -> {
        });
        main_menu_button_4.setOnClickListener(view ->
        {

        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MainMenu.this, MainPage.class);
        intent.putExtra("Email", userEmail);
        startActivity(intent);
        finish();
    }
}