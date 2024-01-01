package com.models.account;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.abstr.interfaces.IUserContainer;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Base64;

public class UserSavedAccounts implements IUserContainer {
    private double creditLimit;
    private String id;

    public String getId()
    {
        return id;
    }

    public JSONObject toJsonObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("creditLimit", creditLimit);

        return jsonObject;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public static class Builder
    {
        private String id;
        private double creditLimit;


        public Builder setId(String id)
        {
            this.id = id;
            return this;
        }

        public Builder setCreditLimit(double creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }

        public UserSavedAccounts Build()
        {
            UserSavedAccounts credentials = new UserSavedAccounts();
            credentials.id = this.id;
            credentials.creditLimit = this.creditLimit;

            return credentials;
        }
    }

    private UserSavedAccounts()
    {
    }
}
