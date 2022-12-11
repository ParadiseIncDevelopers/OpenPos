package com.utilities.tokenizers;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wallet.WalletLogs;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;

public class RegisteredLoginTokenizer {
    private String email;

    public RegisteredLoginTokenizer(String Email)
    {
        this.email = Email;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createLoginToken()
    {
        Supplier<String> valueCreator = () ->
        {
            Random random = new Random();
            StringBuilder string = new StringBuilder(16);
            char[] Hexa = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
            for(int i = 0; i < 16; i++)
            {
                int n = random.nextInt(16);
                string.append(Hexa[n]);
            }

            return string.toString();
        };

        Map<String, Object> token = new HashMap<>();
        token.put("TokenValue", valueCreator.get());
        token.put("TokenDate", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        FirebaseDatabase.getInstance("https://openpos-logintoken.europe-west1.firebasedatabase.app/")
                .getReference().child(this.email).push().setValue(token);
    }
}
