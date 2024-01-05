package com.free.main;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.utilities.UserUtility.userWalletKeyIds;
import static com.utilities.UserUtility.userLoginId;
import static com.utilities.UserUtility.userWallets;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.abstr.concrete.retrievers.DataSnapshotFactory;
import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.free.R;
import com.free.main.adapter.account.AccountLogsAdapter;
import com.free.main.adapter.credit.CreditAccount;
import com.free.main.adapter.debit.DebitAccount;
import com.free.main.menu.createAccount.CreateAccount;
import com.free.main.menu.language.UserLanguage;
import com.free.main.menu.profile.ProfilePage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.RetrieverFactoryEnums;
import com.utilities.UserUtility;
import com.utilities.classes.ContainerConverter;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class MainPage extends AppCompatActivity
{
    private ConstraintLayout main_page_toolbar;
    private FloatingActionButton main_page_logout_button;
    private FloatingActionButton main_page_menu_button;
    private FloatingActionButton main_page_menu_back;
    private ShapeableImageView main_page_logo;
    private TextView main_page_header_text;
    private ConstraintLayout main_page_recyclerView_container, main_page_menu_button_1,
            main_page_menu_button_2, main_page_menu_button_3, main_page_menu_button_4, main_page_menu_button_5;
    private RecyclerView main_page_RecyclerView_Accounts;
    private ConstraintLayout main_page_no_account_layout;
    private ImageView main_page_ImageView_NoAccount;
    private TextView main_page_TextView_NoAccount;

    private AccountLogsAdapter adapter;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            main_page_toolbar = findViewById(R.id.main_page_toolbar);
            main_page_logout_button = findViewById(R.id.main_page_logout_button);
            main_page_menu_button = findViewById(R.id.main_page_menu_button);
            main_page_logo = findViewById(R.id.main_page_logo);
            main_page_header_text = findViewById(R.id.main_page_header_text);
            main_page_recyclerView_container = findViewById(R.id.main_page_recyclerView_container);
            main_page_RecyclerView_Accounts = findViewById(R.id.main_page_RecyclerView_Accounts);
            main_page_no_account_layout = findViewById(R.id.main_page_no_account_layout);
            main_page_ImageView_NoAccount = findViewById(R.id.main_page_ImageView_NoAccount);
            main_page_TextView_NoAccount = findViewById(R.id.main_page_TextView_NoAccount);

            DatabaseReference uniqueKeysRef = FirebaseDatabase
                    .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .child("UniqueKeys");

            uniqueKeysRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    userWalletKeyIds = StreamSupport.stream(iterable.spliterator(), false)
                            .filter(keySnapshot -> keySnapshot.getValue(String.class).equals(userLoginId))
                            .map(DataSnapshot::getKey)
                            .collect(Collectors.toList());

                    if(userWalletKeyIds.size() == 0)
                    {
                        main_page_no_account_layout.setVisibility(VISIBLE);
                        main_page_recyclerView_container.setVisibility(GONE);
                    }
                    else{
                        main_page_no_account_layout.setVisibility(GONE);
                        main_page_recyclerView_container.setVisibility(VISIBLE);
                        DataSnapshotFactory snap = new DataSnapshotFactory();
                        IRetrieverFactory factory = snap.getRetriever(RetrieverFactoryEnums.WALLET);
                        factory.returnData("https://openpos-wallets.europe-west1.firebasedatabase.app/", "Wallets").thenAccept(then -> {
                            userWallets = ContainerConverter.toWalletList(then);
                            adapter = new AccountLogsAdapter(userWallets, MainPage.this, "MainPage");
                            main_page_RecyclerView_Accounts.setLayoutManager(new LinearLayoutManager(MainPage.this));
                            main_page_RecyclerView_Accounts.setAdapter(adapter);
                        }).exceptionally((ex) -> null);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {
                }
            });

            main_page_logout_button.setOnClickListener(view -> {
                UserUtility.userLoginId = "";
                UserUtility.LoginType = "";
                UserUtility.userNameAndSurname = "";
                UserUtility.userLogs = new ArrayList<>();
                UserUtility.walletLogId = "";
                UserUtility.walletMoneyCase = 0.0;
                UserUtility.userWallets = new ArrayList<>();
                UserUtility.userEmail = "";
                UserUtility.userWalletKeyIds = new ArrayList<>();
                FirebaseAuth.getInstance().signOut();
                finish();
            });
            main_page_menu_button.setOnClickListener(view -> {
                final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_main_page_menu);
                dialog.setCanceledOnTouchOutside(false);

                main_page_menu_back = dialog.findViewById(R.id.main_page_menu_back);
                main_page_menu_button_1 = dialog.findViewById(R.id.main_page_menu_button_1);
                main_page_menu_button_2 = dialog.findViewById(R.id.main_page_menu_button_2);
                main_page_menu_button_3 = dialog.findViewById(R.id.main_page_menu_button_3);
                main_page_menu_button_4 = dialog.findViewById(R.id.main_page_menu_button_4);
                main_page_menu_button_5 = dialog.findViewById(R.id.main_page_menu_button_5);

                if(userWalletKeyIds.size() == 0)
                {
                    main_page_menu_button_3.setVisibility(GONE);
                    main_page_menu_button_4.setVisibility(GONE);
                }
                else{
                    main_page_menu_button_3.setVisibility(VISIBLE);
                    main_page_menu_button_4.setVisibility(VISIBLE);
                }

                main_page_menu_back.setOnClickListener(view1 -> dialog.dismiss());

                main_page_menu_button_1.setOnClickListener(view1 -> {
                    Intent intent = new Intent(MainPage.this, ProfilePage.class);
                    startActivity(intent);
                });

                main_page_menu_button_2.setOnClickListener(view1 -> {
                    Intent intent = new Intent(MainPage.this, CreateAccount.class);
                    startActivity(intent);
                });

                main_page_menu_button_3.setOnClickListener(view1 -> {
                    Intent intent = new Intent(MainPage.this, DebitAccount.class);
                    startActivity(intent);
                });

                main_page_menu_button_4.setOnClickListener(view1 -> {
                    Intent intent = new Intent(MainPage.this, CreditAccount.class);
                    startActivity(intent);
                });

                main_page_menu_button_5.setOnClickListener(view1 -> {
                    Intent intent = new Intent(MainPage.this, UserLanguage.class);
                    startActivity(intent);
                });

                dialog.show();
            });
        });
    }
}