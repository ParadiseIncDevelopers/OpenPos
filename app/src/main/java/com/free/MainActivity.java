package com.free;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.free.login.Login;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import java.util.Map;
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.UserStatus;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.Users;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.UsersAllWallets;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.jsonArrayToJsonObjects;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();

        /*NetworkCallback(this, () -> new Handler().postDelayed(() ->
        {
            FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Map<String, Object> theSnapshot = (Map<String, Object>) snapshot.getValue();
                    JSONArray arr = new JSONArray();
                    arr.put(theSnapshot);
                    jsonArrayToJsonObjects(Users, arr);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance("https://openpos-userstatus.europe-west1.firebasedatabase.app/")
                    .getReference("Users")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    Map<String, Object> theSnapshot = (Map<String, Object>) snapshot.getValue();
                    JSONArray arr = new JSONArray();
                    arr.put(theSnapshot);
                    jsonArrayToJsonObjects(UserStatus, arr);
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                            Map<String, Object> theSnapshot = (Map<String, Object>) snapshot.getValue();
                            JSONArray arr = new JSONArray();
                            arr.put(theSnapshot);
                            jsonArrayToJsonObjects(UsersAllWallets, arr);
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });


        }, 5000));*/
    }
}