package com.models.logs;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.abstr.interfaces.retrievers.IContainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Log implements IContainer {
    private String id;
    private String email;
    private String contentDescription;
    private LocalDateTime date;
    private double debit;
    private double credit;

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<String, Object> toJsonObject()
    {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("id", id);
        jsonObject.put("email", email);
        jsonObject.put("contentDescription", contentDescription);
        jsonObject.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // Format date to string
        jsonObject.put("debit", debit);
        jsonObject.put("credit", credit);
        return jsonObject;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getId() {
        return id;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public static class Builder {
        private String id;
        private String email;
        private String contentDescription;
        private LocalDateTime date;
        private double debit;
        private double credit;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public CompletableFuture<Builder> setId(Context context)
        {
            CompletableFuture<Builder> future = new CompletableFuture<>();

            Function<String, String> hexCharRandom = (s) -> {
                int randomIndex = (int) (Math.random() * s.length());
                return String.valueOf(s.charAt(randomIndex));
            } ;
            BiFunction<String, Integer, String> generateRandomHexString = (s, t) -> {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < t; i++) {
                    int randomIndex = (int) (Math.random() * s.length());
                    stringBuilder.append(s.charAt(randomIndex));
                }
                return stringBuilder.toString();
            };
            DatabaseReference databaseReference =
                    FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                            .getReference();

            databaseReference.get()
                    .addOnCompleteListener(task ->
                    {
                        boolean keyExists = false;

                        if(task.isSuccessful())
                        {
                            DataSnapshot snap = task.getResult();
                            if(snap.child("walletLogs").exists())
                            {
                                do
                                {
                                    String hexChars = "0123456789ABCDEF";
                                    StringBuilder keyBuilder = new StringBuilder();
                                    keyBuilder.append(hexCharRandom.apply(hexChars)).append("-");

                                    for (int i = 0; i < 3; i++) {
                                        keyBuilder.append(generateRandomHexString.apply(hexChars, 5)).append("-");
                                    }

                                    keyBuilder.append(generateRandomHexString.apply(hexChars, 5));
                                    id = keyBuilder.toString();

                                    if (snap.child(id).exists())
                                    {
                                        keyExists = true;
                                    }else{
                                        id = keyBuilder.toString();
                                    }
                                } while (keyExists);
                            }
                            else{
                                String hexChars = "0123456789ABCDEF";
                                StringBuilder keyBuilder = new StringBuilder();
                                keyBuilder.append(hexCharRandom.apply(hexChars)).append("-");

                                for (int i = 0; i < 3; i++) {
                                    keyBuilder.append(generateRandomHexString.apply(hexChars, 5)).append("-");
                                }

                                keyBuilder.append(generateRandomHexString.apply(hexChars, 5));
                                id = keyBuilder.toString();

                            }
                            future.complete(this);
                        }
                        else{
                            Toast.makeText(context, "Task is not successful.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(ex -> {
                        Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        future.completeExceptionally(ex);
                    }).addOnCanceledListener(() -> {

                    });
            return future;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder setDebit(double debit) {
            this.debit = debit;
            return this;
        }

        public Builder setCredit(double credit) {
            this.credit = credit;
            return this;
        }

        public Log Build() {
            Log log = new Log();
            log.id = this.id;
            log.email = this.email;
            log.contentDescription = this.contentDescription;
            log.date = this.date;
            log.debit = this.debit;
            log.credit = this.credit;
            return log;
        }
    }

    private Log() {
        super();
        // Private constructor
    }
}
