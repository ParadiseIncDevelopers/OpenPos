package com.user;

import com.utilities.interfaces.IEncryptedObject;

public class EncryptedName implements IEncryptedObject {
    private final String text;
    private final String salt;
    private final String secret;
    private final String code;

    private EncryptedName(String text, String salt, String secret, String code) {
        this.text = text;
        this.salt = salt;
        this.secret = secret;
        this.code = code;
    }

    public String getText() {
        return text;
    }

    public String getSalt() {
        return salt;
    }

    public String getSecret() {
        return secret;
    }

    @Override
    public String get6code() {
        return code;
    }

    public static class Builder {
        private String text;
        private String salt;
        private String secret;
        private String code;

        public Builder setText(String text) {
            this.text = text;
            return this;
        }

        public Builder setSalt(String salt) {
            this.salt = salt;
            return this;
        }

        public Builder setSecret(String secret) {
            this.secret = secret;
            return this;
        }

        public Builder setCode(String code) {
            this.code = code;
            return this;
        }

        public EncryptedName build() {
            return new EncryptedName(text, salt, secret, code);
        }
    }
}

