package com.menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.free.R;
import com.utilities.adapters.GoldAccountChooserAdapter;

import static com.utilities.classes.LoginFactoryClass.userWallets;

public class ChooseGoldAccount extends AppCompatActivity {

    private RecyclerView choose_gold_account_recycler_view;
    private GoldAccountChooserAdapter adapter;
    private LinearLayout choose_gold_account_no_recycler_view;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_gold_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        choose_gold_account_recycler_view = findViewById(R.id.choose_gold_account_recycler_view);
        choose_gold_account_no_recycler_view = findViewById(R.id.choose_gold_account_no_recycler_content);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        adapter = new GoldAccountChooserAdapter(userWallets, ChooseGoldAccount.this, choose_gold_account_no_recycler_view);
        choose_gold_account_recycler_view.setLayoutManager(manager);
        choose_gold_account_recycler_view.setAdapter(adapter);

    }
}