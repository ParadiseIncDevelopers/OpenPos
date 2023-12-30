package com.models.wallet;

import static com.utilities.UserUtility.userLoginId;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.models.logs.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Wallet
{
    private String accountName;
    private String email;
    private String currency;
    private double moneyCase;
    private String id;
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

    public static class Builder {
        private String accountName;
        private String email;
        private String currency;
        private double moneyCase;
        private String id;
        private List<Log> walletLogs;

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

        @RequiresApi(api = Build.VERSION_CODES.N)
        public Builder setId()
        {

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
                    .getReference("UniqueKeys");

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    boolean keyExists = false;

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

                        if (dataSnapshot.child(id).exists())
                        {
                            keyExists = true;
                        }else{
                            databaseReference.child(id).setValue(userLoginId);
                        }
                    } while (keyExists);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            return this;
        }

        public Builder setWalletLogs(List<Log> walletLogs) {
            this.walletLogs = walletLogs;
            return this;
        }

        public Wallet Build() {
            Wallet wallet = new Wallet();
            wallet.accountName = this.accountName;
            wallet.email = this.email;
            wallet.id = this.id;
            wallet.moneyCase = this.moneyCase;
            wallet.currency = this.currency;
            wallet.walletLogs = this.walletLogs;
            return wallet;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject toJsonObject() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("accountName", accountName);
            jsonObject.put("email", email);
            jsonObject.put("currency", currency);
            jsonObject.put("moneyCase", moneyCase);

            if (walletLogs != null && !walletLogs.isEmpty())
            {
                JSONArray logsArray = new JSONArray();
                for (Log log : walletLogs)
                {
                    logsArray.put(log.toJsonObject());
                }
                jsonObject.put("walletLogs", logsArray);
            }
            else
            {
                jsonObject.put("walletLogs", new JSONArray());
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }
}