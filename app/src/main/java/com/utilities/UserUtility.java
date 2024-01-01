package com.utilities;

import android.net.Uri;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.utilities.classes.EncryptorKeyContainer;
import com.models.wallet.Wallet;
import com.models.logs.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class UserUtility
{
    public static String userLoginId;

    private final String email;
    public static String LoginType = "";
    public static String userNameAndSurname;
    public static List<Log> userLogs = new ArrayList<>();
    public static String walletLogId = "";
    public static double walletMoneyCase = 0.0;
    public static List<Wallet> userWallets = new ArrayList<>();
    public static String userEmail;
    public static List<String> userWalletKeyIds;

    public UserUtility(String Email)
    {
        this.email = Email;
    }

    public static class ProgramObjectsUtilityClass
    {
        public static JSONObject Users = new JSONObject();
        public static JSONObject UserStatus = new JSONObject();
        public static JSONObject UsersAllWallets = new JSONObject();

        public static BiConsumer<JSONArray, JSONObject> objectGetter = (a, o) ->
        {
            for (int i = 0; i < a.length(); i++)
            {
                try
                {
                    Map<String, Object> obj = (Map<String, Object>) a.get(i);
                    o.put(obj.keySet().toArray()[0].toString(), obj.get(obj.keySet().toArray()[0].toString()));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
        };

        @RequiresApi(api = Build.VERSION_CODES.N)
        public static void jsonArrayToJsonObjects(JSONObject object, JSONArray array)
        {
            objectGetter.accept(array, object);
        }

        @NonNull
        public static JSONObject mapToJsonObject(@NonNull Map<String, Object> objects)
        {
            JSONObject obj = new JSONObject();
            for (int i = 0; i < objects.size(); i++)
            {
                try
                {
                    String key = objects.keySet().toArray()[i].toString();
                    obj.put(key ,objects.get(key));
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
            }
            return obj;
        }

        @NonNull
        public static JSONArray mapToJsonArray(@NonNull ArrayList<Object> objects)
        {
            JSONArray obj = new JSONArray();
            for (int i = 0; i < objects.size(); i++)
            {
                obj.put(objects.get(i));
            }
            return obj;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int LoginUser(boolean accountApprovalIsInProgress, boolean accountBlocked, boolean actionsBlocked)
    {
        if(LoginType.equals(UtilityValues.REGISTERED))
        {
            if(accountApprovalIsInProgress)
            {
               return 0;
            }
            else{
                if(accountBlocked)
                {
                    return 1;
                }
                else{
                    if(actionsBlocked)
                    {
                        return 2;
                    }
                    else{
                        return 3;
                    }
                }
            }
        }
        else {
            return 3;
        }
    }

    public static void Logout()
    {

    }
}