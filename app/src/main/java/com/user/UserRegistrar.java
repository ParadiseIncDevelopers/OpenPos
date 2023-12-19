package com.user;

import android.os.Build;
import androidx.annotation.RequiresApi;
import com.wallet.Wallet;
import org.json.JSONException;
import org.json.JSONObject;

public class UserRegistrar {
    //Kullanıcı Id
    private String id;
    //Adı soyadı
    private String nameSurname;

    public void setid(String id) {
        this.id = id;
    }

    public String getid() {
        return id;
    }

    public void setnameSurname(String nameSurname) {
        this.nameSurname = nameSurname;
    }

    public String getnameSurname() {
        return nameSurname;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createUser() {
        Wallet wallet = new Wallet("CREATE_WALLET");
        //wallet.createWallet(UserRegistrar.this);
    }

    public String getId() {
        return id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    //Verileri json object e çeviren helper method.
    public JSONObject toJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("nameSurname", nameSurname);

        return jsonObject;
    }
}



