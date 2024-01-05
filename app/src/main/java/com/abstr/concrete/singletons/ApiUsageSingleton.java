package com.abstr.concrete.singletons;

import android.widget.TextView;

import androidx.annotation.NonNull;
import com.google.firebase.database.FirebaseDatabase;
import com.utilities.UserUtility;
import org.jetbrains.annotations.Contract;

public class ApiUsageSingleton
{
    private int usageCount;

    private ApiUsageSingleton(int usageCount)
    {
        this.usageCount = usageCount;
    }

    private ApiUsageSingleton()
    {
        this.usageCount = 0;
    }

    @NonNull
    @Contract("_ -> new")
    public static ApiUsageSingleton GetInstance(int usageCount)
    {
        return new ApiUsageSingleton(usageCount);
    }

    public static ApiUsageSingleton GetInstance()
    {
        return new ApiUsageSingleton();
    }

    public void getApiCount(TextView textView)
    {
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(UserUtility.userLoginId)
                .get()
                .addOnCanceledListener(() -> {})
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(task -> {
                    UserUtility.userApiCounts = Integer.parseInt(task.child("apiCounting").getValue().toString());
                    textView.setText(String.format("Tickets : %d", UserUtility.userApiCounts));
                });
    }

    public void consumeApi()
    {
        FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(UserUtility.userLoginId)
                .get()
                .addOnCanceledListener(() -> {})
                .addOnFailureListener(Throwable::printStackTrace)
                .addOnSuccessListener(task -> {
                    int apiNumber = Integer.parseInt(task.child("apiCounting").getValue().toString());
                    apiNumber = apiNumber - usageCount;

                    FirebaseDatabase.getInstance()
                            .getReference("Users")
                            .child(UserUtility.userLoginId)
                            .child("apiCounting")
                            .setValue(apiNumber);
                });
    }
}
