package com.utilities.classes;

public class EncryptorKeyContainer
{
    private String tokenValue;
    private String tokenDate;

    public String getTokenValue() {
        return tokenValue;
    }

    public String getTokenDate() {
        return tokenDate;
    }

    private EncryptorKeyContainer()
    {

    }

    public static class Builder
    {
        private String tokenValue;
        private String tokenDate;

        public Builder SetTokenValue(String tokenValue)
        {
            this.tokenValue = tokenValue;
            return this;
        }

        public Builder SetTokenDate(String tokenDate)
        {
            this.tokenDate = tokenDate;
            return this;
        }

        public EncryptorKeyContainer Build()
        {
            EncryptorKeyContainer container = new EncryptorKeyContainer();
            container.tokenDate = tokenDate;
            container.tokenValue = tokenValue;
            return container;
        }
    }
}
