package com.free;

import static com.utilities.UserUtility.userAccountImageLinksList;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.WindowManager;
import com.free.login.Login;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        accountImageLinksList();
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();

    }

    public void accountImageLinksList()
    {
        FirebaseStorage storage = FirebaseStorage.getInstance("gs://openpos-3e0d3-accountflags");
        StorageReference storageRef = storage.getReference();

        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    userAccountImageLinksList = new ArrayList<>();
                    List<StorageReference> items = listResult.getItems();

                    for (StorageReference item : items) {
                        item.getDownloadUrl().addOnSuccessListener(uri -> {
                            String downloadUrl = uri.toString();
                            userAccountImageLinksList.add(downloadUrl);
                        });
                    }
                })
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnCanceledListener(() -> {

                });
    }
}