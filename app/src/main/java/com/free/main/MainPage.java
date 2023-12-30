package com.free.main;

import static com.utilities.UserUtility.userAccountImageLinksList;
import static com.utilities.UserUtility.userLoginId;
import static com.utilities.UserUtility.userWallets;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.free.R;
import com.free.main.adapter.credit.CreditAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.logs.Log;
import com.models.wallet.Wallet;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPage extends AppCompatActivity
{
    private ConstraintLayout main_page_ConstraintLayout1_Exit;
    private FloatingActionButton main_page_FAB1_Close;
    private FloatingActionButton main_page_FAB2_Menu;
    private ShapeableImageView main_page_Logo1_Acoount;
    private TextView main_page_TextView1_Header;
    private ConstraintLayout main_page_ConstraintLayout2_Account, main_page_menu_button_1,
            main_page_menu_button_2, main_page_menu_button_3, main_page_menu_button_4;
    private RecyclerView main_page_RecyclerView_Accounts;
    private ConstraintLayout main_page_no_account_layout;
    private ImageView main_page_ImageView_NoAccount;
    private TextView main_page_TextView_NoAccount;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        main_page_ConstraintLayout1_Exit = findViewById(R.id.main_page_ConstraintLayout1_Exit);
        main_page_FAB1_Close = findViewById(R.id.main_page_FAB1_Close);
        main_page_FAB2_Menu = findViewById(R.id.main_page_FAB2_Menu);
        main_page_Logo1_Acoount = findViewById(R.id.main_page_Logo1_Acoount);
        main_page_TextView1_Header = findViewById(R.id.main_page_TextView1_Header);
        main_page_ConstraintLayout2_Account = findViewById(R.id.main_page_ConstraintLayout2_Account);
        main_page_RecyclerView_Accounts = findViewById(R.id.main_page_RecyclerView_Accounts);
        main_page_no_account_layout = findViewById(R.id.main_page_no_account_layout);
        main_page_ImageView_NoAccount = findViewById(R.id.main_page_ImageView_NoAccount);
        main_page_TextView_NoAccount = findViewById(R.id.main_page_TextView_NoAccount);

        DatabaseReference uniqueKeysRef = FirebaseDatabase
                .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                .getReference("UniqueKeys");

        uniqueKeysRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                userAccountImageLinksList = StreamSupport.stream(iterable.spliterator(), false)
                        .filter(keySnapshot -> keySnapshot.getValue(String.class).equals(userLoginId))
                        .map(DataSnapshot::getKey)
                        .collect(Collectors.toList());

                FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                        .getReference()
                        .child("Wallets")
                        .get()
                        .addOnCanceledListener(() -> {

                        })
                        .addOnFailureListener(x -> {

                        })
                        .addOnSuccessListener(task -> {
                            Iterable<DataSnapshot> iterable2 = dataSnapshot.getChildren();
                            userWallets = StreamSupport.stream(iterable2.spliterator(), false)
                                    .filter(keySnapshot -> keySnapshot.getValue(String.class).equals(userLoginId))
                                    .map((q) ->
                                    {
                                        Wallet.Builder walletBuilder = new Wallet.Builder()
                                                .setEmail(q.child("email").getValue().toString())
                                                .setCurrency(q.child("currency").toString())
                                                .setMoneyCase(Double.parseDouble(q.child("moneyCase").toString()));
                                        if(q.child("walletLogs").exists())
                                        {
                                            walletBuilder.setWalletLogs((List<Log>)q.child("walletLogs").getValue());
                                        }
                                        else{
                                            walletBuilder.setWalletLogs(new ArrayList<>());
                                        }
                                        Wallet theWallet = walletBuilder.Build();
                                        return theWallet;
                                    })
                                    .collect(Collectors.toList());
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        main_page_FAB2_Menu.setOnClickListener(view -> {
            final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wallet_logs_printer_screen);
            dialog.setCanceledOnTouchOutside(false);

            main_page_menu_button_1 = dialog.findViewById(R.id.main_page_menu_button_1);
            main_page_menu_button_2 = dialog.findViewById(R.id.main_page_menu_button_2);
            main_page_menu_button_3 = dialog.findViewById(R.id.main_page_menu_button_3);
            main_page_menu_button_4 = dialog.findViewById(R.id.main_page_menu_button_4);

            main_page_menu_button_1.setOnClickListener(view1 -> {

            });

            main_page_menu_button_2.setOnClickListener(view1 -> {
                Intent intent = new Intent(MainPage.this, CreditAccount.class);
                startActivity(intent);
            });

            main_page_menu_button_3.setOnClickListener(view1 -> {

            });

            main_page_menu_button_4.setOnClickListener(view1 -> {

            });
        });

        /*NetworkCallback(this, () -> {
            main_page_transactions_profile_image = findViewById(R.id.main_page_transactions_profile_image);

            main_page_wallet = findViewById(R.id.main_page_wallet);
            main_page_name_and_surname = findViewById(R.id.main_page_name_and_surname);
            main_page_email = findViewById(R.id.main_page_email);
            main_page_transactions_text = findViewById(R.id.main_page_transactions_text);
            main_page_transactions_logs = findViewById(R.id.main_page_transactions_logs);

            main_page_receive_money_button = findViewById(R.id.main_page_receive_money_button);
            main_page_give_money_button = findViewById(R.id.main_page_give_money_button);
            main_page_withdraw_money_button = findViewById(R.id.main_page_withdraw_money_button);
            main_page_add_money_button = findViewById(R.id.main_page_add_money_button);
            main_page_transfer_money_button = findViewById(R.id.main_page_transfer_money_button);
            main_page_transactions_filter_button = findViewById(R.id.main_page_transactions_filter_button);

            main_page_transactions_menu = findViewById(R.id.main_page_transactions_menu);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }

            if(getIntent().getExtras() != null)
            {
                UserUtility.userEmail = getIntent().getExtras().getString("Email");
            }

            main_page_email.setText(userEmail);
            main_page_name_and_surname.setText(userNameAndSurname);

            runOnUiThread(() -> {
                try
                {
                    main_page_transactions_text.setText("Last transactions");
                    Glide.with(MainPage.this)
                            .load(new URL(userImageUri.toString()).toString())
                            .into(main_page_transactions_profile_image);
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            });

            runOnUiThread(() -> {
                allLogs = userWalletLogs.get(walletTaken);
                ArrayList<Log> logs = allLogs;
                adapter = new WalletLogsAdapter(logs);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setReverseLayout(true);
                manager.setStackFromEnd(true);
                main_page_transactions_logs.setLayoutManager(manager);
                main_page_transactions_logs.setAdapter(adapter);
                main_page_wallet.setText(String.format("%s %s", userMoneyCase.get(walletTaken), userCurrency.get(walletTaken)));
            });

            main_page_give_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, SendMoney.class);
                startActivity(intent);
            });
            main_page_receive_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, DebitAccount.class);
                startActivity(intent);
            });
            main_page_add_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, CreditAccount.class);
                startActivity(intent);
            });
            main_page_withdraw_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, TransferAccount.class);
                startActivity(intent);
            });

            main_page_transactions_menu.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, MainMenu.class);
                startActivity(intent);
            });

            main_page_transfer_money_button.setOnClickListener(view -> {
                Intent intent = new Intent(MainPage.this, FindOrSaveUser.class);
                startActivity(intent);
            });

            main_page_transactions_filter_button.setOnClickListener(view ->
            {
                openTransactionFilters();
            });

            main_page_transactions_profile_image.setOnClickListener(view -> openUserImageAvatarList());
        });*/
    }
}