package com.models.money;

public class Money
{
    private String currency;
    private double moneyCase;

    public static class Builder
    {
        private String currency;
        private double moneyCase;

        public Builder setCurrency(String currency) {
            this.currency = currency;
            return this;
        }

        public Builder setMoneyCase(double moneyCase) {
            this.moneyCase = moneyCase;
            return this;
        }

        public Money Build()
        {
            Money money = new Money();
            money.moneyCase = this.moneyCase;
            money.currency = this.currency;
            return money;
        }
    }

    private Money()
    {

    }

    public String getCurrency() {
        return currency;
    }

    public double getMoneyCase() {
        return moneyCase;
    }
}
