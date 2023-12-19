package com.user;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Base64;

public class UserRegistrar {
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
        return jsonObject;
    }

    public static class Builder
    {
        private String id;
        private String email;
        private String nameSurname;

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

        public UserRegistrar Build()
        {
            UserRegistrar credentials = new UserRegistrar();
            credentials.id = this.id;
            credentials.email = this.email;
            credentials.nameSurname = this.nameSurname;
            return credentials;
        }
    }

    private UserRegistrar()
    {

    }
}
