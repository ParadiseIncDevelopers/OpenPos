package com.models.account;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.abstr.interfaces.IUserContainer;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Base64;

public class UserSavedAccounts implements IUserContainer {
    private boolean isBlocked;
    private boolean isUserOnTheList;
    private double creditLimit;
    private String id;
    private String email;
    private String nameSurname;

    public String getId()
    {
        return id;
    }
    public String getEmail()
    {
        return email;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    public JSONObject toJsonObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("email", email);
        jsonObject.put("nameSurname", nameSurname);
        jsonObject.put("isBlocked", isBlocked);
        jsonObject.put("isUserOnTheList", isUserOnTheList);
        jsonObject.put("creditLimit", creditLimit);

        return jsonObject;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public boolean isUserOnTheList() {
        return isUserOnTheList;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public static class Builder
    {
        private String id;
        private String email;
        private String nameSurname;
        private boolean isUserOnTheList;
        private boolean isBlocked;
        private double creditLimit;


        public Builder setId(String id)
        {
            this.id = id;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setEmail(@NonNull String email)
        {
            //Encoder.
            Base64.Encoder encoder = Base64.getEncoder();
            //encodes the string twice.
            this.email = encoder.encodeToString(encoder.encodeToString(email.getBytes()).getBytes());
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setNameSurname(String nameSurname)
        {
            this.nameSurname = nameSurname;
            return this;
        }

        public UserSavedAccounts Build()
        {
            UserSavedAccounts credentials = new UserSavedAccounts();
            credentials.id = this.id;
            credentials.email = this.email;
            credentials.nameSurname = this.nameSurname;
            credentials.isBlocked = this.isBlocked;
            credentials.isUserOnTheList = this.isUserOnTheList;
            credentials.creditLimit = this.creditLimit;

            return credentials;
        }

        public Builder setIsUserOnTheList(boolean isUserOnTheList) {
            this.isUserOnTheList = isUserOnTheList;
            return this;
        }

        public Builder setIsBlocked(boolean isBlocked) {
            this.isBlocked = isBlocked;
            return this;
        }

        public Builder setCreditLimit(double creditLimit) {
            this.creditLimit = creditLimit;
            return this;
        }
    }

    private UserSavedAccounts()
    {
    }
}
