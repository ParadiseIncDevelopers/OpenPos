package com.models.wallet;

import static com.utilities.UserUtility.userLoginId;
import android.content.Context;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.abstr.interfaces.retrievers.IContainer;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.models.logs.Log;
import org.json.JSONArray;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Wallet implements IContainer
{
    private String accountName;
    private String email;
    private String currency;
    private double moneyCase;
    private String id;
    private LocalDateTime creationDate;
    private List<Log> walletLogs;

    private Wallet() {
    }

    public String getEmail() {
        return email;
    }

    public String getCurrency() {
        return currency;
    }

    public double getMoneyCase() {
        return moneyCase;
    }

    public String getAccountName() {
        return accountName;
    }

    public List<Log> getWalletLogs()
    {
        return walletLogs;
    }

    public String getId() {
        return id;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public static class Builder {
        private String accountName;
        private String email;
        private String currency;
        private double moneyCase;
        private String id;
        private List<Log> walletLogs;
        private LocalDateTime creationDate;

        public Builder setAccountName(String accountName) {
            this.accountName = accountName;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setMoneyCase(double moneyCase) {
            this.moneyCase = moneyCase;
            return this;
        }

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
                            if(snap.child("UniqueKeys").exists())
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
                                        databaseReference.child("UniqueKeys").child(id).setValue(userLoginId);
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
                                databaseReference.child("UniqueKeys").child(id).setValue(userLoginId);
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

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setWalletLogs(@NonNull Map<String, Map<String, Object>> walletLogs) {
            List<Map<String,Object>> elements = new ArrayList<>(walletLogs.values());
            List<Log> logs = new ArrayList<>();
            elements.forEach(x -> logs.add(new Log.Builder()
                    .setDate(LocalDateTime.parse(x.get("date").toString()))
                    .setContentDescription(x.get("contentDescription").toString())
                    .setId(x.get("id").toString())
                    .setDebit(Double.parseDouble(x.get("debit").toString()))
                    .setCredit(Double.parseDouble(x.get("credit").toString()))
                    .setEmail(x.get("email").toString())
                    .build()));
            this.walletLogs = logs;
            return this;
        }

        public Wallet Build() {
            Wallet wallet = new Wallet();
            wallet.accountName = this.accountName;
            wallet.email = this.email;
            wallet.id = this.id;
            wallet.creationDate = this.creationDate;
            wallet.moneyCase = this.moneyCase;
            wallet.currency = this.currency;
            wallet.walletLogs = this.walletLogs;
            return wallet;
        }

        public Builder setCreationDate(LocalDateTime creationDate)
        {
            this.creationDate = creationDate;
            return this;
        }
    }

    @Override
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Map<String, Object> toJsonObject() {
        Map<String, Object> jsonObject = new HashMap<>();
        jsonObject.put("accountName", accountName);
        jsonObject.put("email", email);
        jsonObject.put("currency", currency);
        jsonObject.put("moneyCase", moneyCase);
        jsonObject.put("creationDate", creationDate.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        if (walletLogs != null && !walletLogs.isEmpty())
        {
            JSONArray logsArray = new JSONArray();
            for (Log log : walletLogs)
            {
                logsArray.put(log.toJsonObject());
            }
            jsonObject.put("walletLogs", logsArray);
        }
        return jsonObject;
    }
}