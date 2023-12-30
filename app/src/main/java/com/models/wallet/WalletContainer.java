package com.models.wallet;

import com.models.logs.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WalletContainer {
    private final double moneyCase;
    private final String currency;
    private final List<Log> logs;
    private final WalletEncryption encryption;

    private WalletContainer(double moneyCase, String currency, List<Log> logs, WalletEncryption encryption) {
        this.moneyCase = moneyCase;
        this.currency = currency;
        this.logs = logs;
        this.encryption = encryption;
    }

    public double getMoneyCase() {
        return moneyCase;
    }

    public String getCurrency() {
        return currency;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public WalletEncryption getEncryption() {
        return encryption;
    }

    public JSONObject toJsonObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MoneyCase", this.moneyCase);
        jsonObject.put("Currency", this.currency);
        jsonObject.put("Logs", this.logs);
        jsonObject.put("WalletEncryption", this.encryption.toJsonObject());
        return jsonObject;
    }

    public static class Builder {
        private double moneyCase;
        private String currency;
        private List<Log> logs;
        private WalletEncryption encryption;

        public Builder setMoneyCase(double moneyCase) {
            this.moneyCase = moneyCase;
            return this;
        }

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setLogs(List<Log> logs) {
            this.logs = logs;
            return this;
        }

        public Builder setEncryption(WalletEncryption encryption) {
            this.encryption = encryption;
            return this;
        }

        public WalletContainer build() {
            return new WalletContainer(moneyCase, currency, logs, encryption);
        }
    }
}
