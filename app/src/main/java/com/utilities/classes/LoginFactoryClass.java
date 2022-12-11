package com.utilities.classes;

import android.net.Uri;
import android.os.Build;
import androidx.annotation.RequiresApi;
import com.wallet.Wallet;
import com.wallet.WalletLogs;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class LoginFactoryClass
{
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

        public static JSONObject mapToJsonObject(Map<String, Object> objects)
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

        public static JSONArray mapToJsonArray(ArrayList<Object> objects)
        {
            JSONArray obj = new JSONArray();
            for (int i = 0; i < objects.size(); i++)
            {
                obj.put(objects.get(i));
            }
            return obj;
        }
    }

    public static Uri userImageUri;
    private final String email;
    public static String LoginType = "";
    public static Map<String, ArrayList<WalletLogs>> userWalletLogs = new HashMap<>();
    public static Map<String, String> userCurrency = new HashMap<>();
    public static Map<String, String> userMoneyCase = new HashMap<>();
    public static String userNameAndSurname;
    public static String userPhoneNumber;
    public static ArrayList<Wallet> userWallets = new ArrayList<>();
    public static String userEmail;
    public static List<String> userAccountImageLinksList;
    public static String walletTaken = "MainWallet";
    public static int getUserWalletsSize = 0;

    public LoginFactoryClass(String Email)
    {
        this.email = Email;
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
        userWalletLogs = new HashMap<>();
        userImageUri = null;
        userAccountImageLinksList = new ArrayList<>();
        LoginType = "";
        userCurrency = new HashMap<>();
        userMoneyCase = new HashMap<>();
        userNameAndSurname = "";
        userPhoneNumber = "";
        userEmail = "";
        walletTaken = "MainWallet";
    }
}