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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class UserUtility
{
    public static String userLoginId = "";
    public static int userApiCounts = -1;
    public static String LoginType = "";
    public static String userNameAndSurname;
    public static List<Log> userLogs = new ArrayList<>();
    public static String walletLogId = "";
    public static double walletMoneyCase = 0.0;
    public static List<Wallet> userWallets = new ArrayList<>();

    public static String userEmail;
    public static List<String> userWalletKeyIds;

    public static double DoubleFormatter(double theDouble)
    {
        if(theDouble == 0)
        {
            return 0.0;
        }

        if (theDouble == (long) theDouble) {
            DecimalFormat df = new DecimalFormat("#.0");
            return Double.parseDouble(df.format(theDouble).replace(",","."));
        }
        return theDouble;
    }
}