package com.models.wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class WalletEncryption
{
    private final String paymentKey;
    private final String walletKey;

    private WalletEncryption(String paymentKey, String walletKey) {
        this.paymentKey = paymentKey;
        this.walletKey = walletKey;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public JSONObject toJsonObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("MoneyCase", this.paymentKey);
        jsonObject.put("Currency", this.walletKey);
        return jsonObject;
    }

    public static class Builder {
        private String paymentKey;
        private String walletKey;

        public Builder setPaymentKey(String paymentKey) {
            this.paymentKey = paymentKey;
            return this;
        }

        public Builder setWalletKey(String walletKey) {
            this.walletKey = walletKey;
            return this;
        }

        public WalletEncryption build() {
            return new WalletEncryption(paymentKey, walletKey);
        }
    }
}



