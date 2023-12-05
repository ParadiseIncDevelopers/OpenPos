package com.user;

import com.utilities.interfaces.IEncryptedObject;

import org.json.JSONException;
import org.json.JSONObject;

public class EncryptedPassword implements IEncryptedObject
{
    private final String text;
    private final String salt;
    private final String secret;

    private EncryptedPassword(String text, String salt, String secret) {
        this.text = text;
        this.salt = salt;
        this.secret = secret;
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
        return null;
    }

    @Override
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", this.text);
        jsonObject.put("salt", this.salt);
        jsonObject.put("secret", this.secret);

        return jsonObject;
    }

    public static class Builder {
        private String passwordText;
        private String salt;
        private String secret;

        public Builder setPasswordText(String passwordText) {
            this.passwordText = passwordText;
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

        public EncryptedPassword build() {
            return new EncryptedPassword(passwordText, salt, secret);
        }
    }
}
