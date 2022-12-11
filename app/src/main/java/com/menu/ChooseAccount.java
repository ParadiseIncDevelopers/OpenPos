package com.menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import com.free.R;
import com.utilities.adapters.NormalAccountChooserAdapter;
import static com.utilities.classes.LoginFactoryClass.userWallets;

public class ChooseAccount extends AppCompatActivity
{
    private RecyclerView choose_account_recycler_view;
    private NormalAccountChooserAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        choose_account_recycler_view = findViewById(R.id.choose_account_recycler_view);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter = new NormalAccountChooserAdapter(userWallets, ChooseAccount.this);
        choose_account_recycler_view.setLayoutManager(manager);
        choose_account_recycler_view.setAdapter(adapter);
    }
}