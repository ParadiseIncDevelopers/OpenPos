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

    private Button main_menu_button_1, main_menu_button_2, main_menu_button_3, main_menu_button_4, main_menu_button_5,
            main_menu_button_6, main_menu_button_7, main_menu_button_8, main_menu_button_9, main_menu_button_10,
            main_menu_button_11, main_menu_button_12;

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
        main_menu_button_5 = findViewById(R.id.main_menu_button_5);
        main_menu_button_6 = findViewById(R.id.main_menu_button_6);
        main_menu_button_7 = findViewById(R.id.main_menu_button_7);
        main_menu_button_8 = findViewById(R.id.main_menu_button_8);
        main_menu_button_9 = findViewById(R.id.main_menu_button_9);
        main_menu_button_10 = findViewById(R.id.main_menu_button_10);
        main_menu_button_11 = findViewById(R.id.main_menu_button_11);
        main_menu_button_12 = findViewById(R.id.main_menu_button_12);

        main_menu_button_1.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, ProfileSettings.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_2.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, CreateAccount.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_3.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, CreateGoldAccount.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_4.setOnClickListener(view ->
        {
            Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(MainMenu.this, CreateParticipationAccount.class);
            //startActivity(intent);
            //finish();
        });
        main_menu_button_5.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, CreateLoanAccount.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_6.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, ChooseAccount.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_7.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, ChooseGoldAccount.class);
            startActivity(intent);
            finish();
        });
        main_menu_button_8.setOnClickListener(view ->
        {
            Toast.makeText(this, "Coming soon.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent(MainMenu.this, ChooseParticipationAccount.class);
            //startActivity(intent);
            //finish();
        });
        main_menu_button_9.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, ChooseLoanAccount.class);
            startActivity(intent);
            finish();
        });

        main_menu_button_10.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, AutomatedPayments.class);
            startActivity(intent);
            finish();
        });

        main_menu_button_11.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, Offers.class);
            startActivity(intent);
            finish();
        });

        main_menu_button_12.setOnClickListener(view ->
        {
            Intent intent = new Intent(MainMenu.this, Integrations.class);
            startActivity(intent);
            finish();
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