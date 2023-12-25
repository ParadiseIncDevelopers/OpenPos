package com.wallet;
import java.util.Random;


public class Wallet {

    private String actionCode;
    private String email;
    private String currency;
    private double moneyCase;
    private String walletKey;
    private String paymentKey;
    private String id; // New id field

    private Wallet() {
        // Private constructor
    }

    public Wallet(String actionCode, String id) {
        this.actionCode = actionCode;
        this.id = id;
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

    public String getWalletKey() {
        return walletKey;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public String getId() { // Getter for id
        return id;
    }

    public static class Builder {
        private String email;
        private String currency;
        private double moneyCase;
        private String walletKey;
        private String paymentKey;
        private String id; // New id field in the Builder

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setMoneyCase(double moneyCase) {
            this.moneyCase = moneyCase;
            return this;
        }

        public Builder setWalletKey(String walletKey) {
            this.walletKey = walletKey;
            return this;
        }

        public Builder setPaymentKey(String paymentKey) {
            this.paymentKey = paymentKey;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder setId(String id) { // Setter for id in the Builder
            this.id = id;
            return this;
        }

        public Wallet Build() { // Renamed Build() to build() conventionally
            Wallet wallet = new Wallet(this.currency, this.id); // Create Wallet instance with actionCode and id
            wallet.email = this.email;
            wallet.paymentKey = this.paymentKey;
            wallet.walletKey = this.walletKey;
            wallet.moneyCase = this.moneyCase;
            wallet.currency = this.currency;
            return wallet;
        }
    }

    // Supplier methods...
    // (Remaining code for walletKeyCreator and paymentKeyCreator methods)
}