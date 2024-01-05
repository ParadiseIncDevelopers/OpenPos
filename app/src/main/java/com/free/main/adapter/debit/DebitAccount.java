package com.free.main.adapter.debit;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.abstr.concrete.retrievers.DataSnapshotFactory;
import com.abstr.interfaces.retrievers.IRetrieverFactory;
import com.free.R;
import com.free.main.adapter.account.AccountLogsAdapter;
import com.free.main.adapter.credit.CreditAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.RetrieverFactoryEnums;
import com.utilities.classes.ContainerConverter;

import static com.utilities.UserUtility.userLoginId;
import static com.utilities.UserUtility.userWalletKeyIds;
import static com.utilities.UserUtility.userWallets;
import static com.utilities.classes.NetworkChangeReceiver.NetworkCallback;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


public class DebitAccount extends AppCompatActivity
{
    private ConstraintLayout debit_account_toolbar;
    private FloatingActionButton debit_account_back_menu;
    private ShapeableImageView debit_account_logo;
    private TextView debit_account_header;
    private RecyclerView debit_account_RecyclerView_Accounts;

    private AccountLogsAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debit_account);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            debit_account_toolbar = findViewById(R.id.debit_account_toolbar);
            debit_account_back_menu = findViewById(R.id.debit_account_back_menu);
            debit_account_logo = findViewById(R.id.debit_account_logo);
            debit_account_header = findViewById(R.id.debit_account_header);
            debit_account_RecyclerView_Accounts = findViewById(R.id.debit_account_RecyclerView_Accounts);

            debit_account_back_menu.setOnClickListener(view -> finish());

            DatabaseReference uniqueKeysRef = FirebaseDatabase
                    .getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference("UniqueKeys");

            uniqueKeysRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    userWalletKeyIds = StreamSupport.stream(iterable.spliterator(), false)
                            .filter(keySnapshot -> keySnapshot.getValue(String.class).equals(userLoginId))
                            .map(DataSnapshot::getKey)
                            .collect(Collectors.toList());

                    DataSnapshotFactory snap = new DataSnapshotFactory();

                    IRetrieverFactory factory = snap.getRetriever(RetrieverFactoryEnums.WALLET);

                    factory.returnData("https://openpos-wallets.europe-west1.firebasedatabase.app/", "Wallets").thenAccept(then -> {
                        userWallets = ContainerConverter.toWalletList(then);
                        adapter = new AccountLogsAdapter(userWallets, DebitAccount.this, "DebitAccount");
                        debit_account_RecyclerView_Accounts.setLayoutManager(new LinearLayoutManager(DebitAccount.this));
                        debit_account_RecyclerView_Accounts.setAdapter(adapter);
                    }).exceptionally((ex) -> null);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        });
    }
}