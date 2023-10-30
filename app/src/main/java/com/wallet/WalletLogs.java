package com.wallet;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WalletLogs
{
    private String email;
    private String contentDescription;
    private String date;
    private String spend;
    private String rest;
    private String commission;

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<WalletLogs> parseToArrayList(@NonNull List<Map<String, Object>> logs)
    {
        List<WalletLogs> allLogs = new ArrayList<>();

        logs.forEach(x -> {
            WalletLogs theLog = new WalletLogs.Builder()
                    .SetCommission(x.get("commission").toString())
                    .SetContentDescription(x.get("contentDescription").toString())
                    .SetDate(x.get("date").toString())
                    .SetEmail(x.get("email").toString())
                    .SetRest(x.get("rest").toString())
                    .SetSpend(x.get("spend").toString())
                    .Build();
            allLogs.add(theLog);
        });

        return allLogs;
    }

    public String getEmail() {
        return email;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getDate() {
        return date;
    }

    public String getSpend() {
        return spend;
    }

    public String getRest() {
        return rest;
    }

    public String getCommission() {
        return commission;
    }

    public static class Builder
    {
        private String email;
        private String contentDescription;
        private String date;
        private String spend;
        private String rest;
        private String commission;

        public Builder SetEmail(String email)
        {
            this.email = email;
            return this;
        }

        public Builder SetCommission(String commission)
        {
            this.commission = commission;
            return this;
        }

        public Builder SetContentDescription(String contentDescription)
        {
            this.contentDescription = contentDescription;
            return this;
        }

        public Builder SetDate(String date)
        {
            this.date = date;
            return this;
        }

        public Builder SetSpend(String spend)
        {
            this.spend = spend;
            return this;
        }

        public Builder SetRest(String rest)
        {
            this.rest = rest;
            return this;
        }

        public WalletLogs Build()
        {
            WalletLogs logs = new WalletLogs();
            logs.contentDescription = this.contentDescription;
            logs.date = this.date;
            logs.email = this.email;
            logs.rest = this.rest;
            logs.spend = this.spend;
            logs.commission = commission;
            return logs;
        }
    }

    private WalletLogs()
    {

    }
}
