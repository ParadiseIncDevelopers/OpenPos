package com.free.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.free.MainPage;
import com.free.R;
import com.free.register.Register;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.utilities.classes.EncryptorClass;
import com.utilities.classes.LoginFactoryClass;
import com.utilities.tokenizers.LoginTokenizer;
import com.wallet.Wallet;
import com.wallet.Models.Log;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static android.view.Window.FEATURE_NO_TITLE;
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.Users;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.UsersAllWallets;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.mapToJsonArray;
import static com.utilities.classes.LoginFactoryClass.ProgramObjectsUtilityClass.mapToJsonObject;
import static com.utilities.classes.LoginFactoryClass.userAccountImageLinksList;
import static com.utilities.classes.LoginFactoryClass.userCurrency;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.userMoneyCase;
import static com.utilities.classes.LoginFactoryClass.userNameAndSurname;
import static com.utilities.classes.LoginFactoryClass.userWalletLogs;
import static com.utilities.classes.LoginFactoryClass.userWallets;
import static com.utilities.classes.LoginFactoryClass.walletTaken;

public class Login extends AppCompatActivity
{
    private TextInputLayout login_page_email_text, login_page_password_text;
    private TextInputEditText login_page_email_text_field, login_page_password_text_field;
    private CheckBox login_page_remember_box;
    private Button login_page_submit_button, login_page_register_account_page;
    private TextView login_page_error_text, login_page_forgot_password_text;
    private Dialog dialog;

    private final Supplier<String> getMacAddress = () -> {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return "02:00:00:00:00:00";
    };

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            login_page_error_text = findViewById(R.id.login_page_error_text);
            login_page_forgot_password_text = findViewById(R.id.login_page_forgot_password_text);
            login_page_email_text = findViewById(R.id.login_page_email_text);
            login_page_password_text = findViewById(R.id.login_page_password_text);
            login_page_email_text_field = findViewById(R.id.login_page_email_text_field);
            login_page_password_text_field = findViewById(R.id.login_page_password_text_field);
            login_page_remember_box = findViewById(R.id.login_page_remember_box);
            login_page_submit_button = findViewById(R.id.login_page_submit_button);
            login_page_register_account_page = findViewById(R.id.login_page_register_account_page);

            int greenColor = Color.parseColor("#558B2F");
            Supplier<Boolean> allIsTrue = () ->
                    login_page_email_text.getBoxStrokeColor() == greenColor &&
                            login_page_password_text.getBoxStrokeColor() == greenColor;

            login_page_email_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$").matcher(s.toString()).matches())
                    {
                        login_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            login_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        login_page_email_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });
            login_page_password_text_field.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void afterTextChanged(Editable s)
                {
                    if(Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$").matcher(s.toString()).matches())
                    {
                        login_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        if(allIsTrue.get())
                        {
                            login_page_submit_button.setEnabled(true);
                        }
                    }
                    else{
                        login_page_password_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    }
                }
            });

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            login_page_submit_button.setOnClickListener(view ->
            {
                String textEmail = login_page_email_text_field.getText().toString();
                String textPassword = login_page_password_text_field.getText().toString();
                String Email = EncryptorClass.setSecurePassword(textEmail);
                String Password = EncryptorClass.setSecurePassword(textPassword);

                if(login_page_remember_box.isChecked())
                {
                    //rememberTheUser();
                }

                LoginTokenizer tokenizer = new LoginTokenizer(Email);
                tokenizer.createLoginToken();

                FirebaseDatabase.getInstance("https://openpos-userstatus.europe-west1.firebasedatabase.app/")
                        .getReference()
                        .child("Users")
                        .child(Email)
                        .addListenerForSingleValueEvent(new ValueEventListener()
                        {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                            {
                                if(Stream.of(Users).anyMatch(x -> x.isNull(Email)))
                                {
                                    try
                                    {
                                        if(Stream.of(Users.getJSONObject(Email)).anyMatch(x ->
                                        {
                                            try
                                            {
                                                return x.get("EncryptedObject").toString().equals(Password);
                                            }
                                            catch (JSONException e)
                                            {
                                                e.printStackTrace();
                                            }
                                            return false;
                                        }))
                                        {

                                            EncryptorClass.BiometricClass.checkEncryption(Login.this, () ->
                                            {
                                                runOnUiThread(() -> {
                                                    dialog = new Dialog(Login.this);
                                                    dialog.requestWindowFeature(FEATURE_NO_TITLE);
                                                    dialog.setCancelable(false);
                                                    dialog.setContentView(R.layout.dialog_main_page_loading);
                                                    dialog.show();
                                                });
                                                Intent intent = new Intent(Login.this, MainPage.class);

                                                /*
                                                *
                                                * userEmail = textEmail;
                                                walletTaken = "MainWallet";

                                                JSONObject obj = new JSONObject();
                                                try
                                                {
                                                    obj = mapToJsonObject((Map<String, Object>) UsersAllWallets.get(Email));
                                                }
                                                catch (JSONException e)
                                                {
                                                    e.printStackTrace();
                                                }

                                                userWalletLogs = new HashMap<>();
                                                JSONObject finalObj = obj;
                                                obj.keys().forEachRemaining(x ->
                                                {
                                                    try
                                                    {
                                                        JSONObject theX = mapToJsonObject((Map<String, Object>) finalObj.get(x));
                                                        JSONArray theLogs = mapToJsonArray((ArrayList<Object>) theX.get("Logs"));
                                                        ArrayList<Log> logs = new ArrayList<>();

                                                        int minLog = Math.min(theLogs.length(), 50);

                                                        for (int i = 0; i < minLog; i++)
                                                        {
                                                            try
                                                            {
                                                                JSONObject jsonObject = mapToJsonObject((Map<String, Object>) theLogs.get(i));

                                                                Log log = new Log.Builder()
                                                                        .SetSpend(jsonObject.get("spend").toString())
                                                                        .SetRest(jsonObject.get("rest").toString())
                                                                        .SetEmail(jsonObject.get("email").toString())
                                                                        .SetDate(jsonObject.get("date").toString())
                                                                        .SetContentDescription(jsonObject.get("contentDescription").toString())
                                                                        .SetCommission(jsonObject.get("commission").toString())
                                                                        .Build();
                                                                logs.add(log);
                                                            }
                                                            catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }

                                                        double moneyCase = Double.parseDouble(mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString().replace(",", "."));
                                                        String currency = mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString();
                                                        String paymentKey = mapToJsonObject((Map<String, Object>) mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("EncryptionKeys")).get("PaymentKey").toString();
                                                        String walletKey = mapToJsonObject((Map<String, Object>) mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("EncryptionKeys")).get("WalletKey").toString();
                                                        if (Objects.requireNonNull(x).equals("MainWallet"))
                                                        {
                                                            Wallet theWallet = new Wallet.Builder()
                                                                    .setPaymentKey(EncryptorClass.Decrypt(paymentKey))
                                                                    .setWalletKey("MainWallet")
                                                                    .setCurrency(currency)
                                                                    .setEmail(userEmail)
                                                                    .setMoneyCase(moneyCase)
                                                                    .Build();
                                                            userWallets.add(theWallet);
                                                            userMoneyCase.put("MainWallet", mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString());
                                                            userCurrency.put("MainWallet", mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString());
                                                            userWalletLogs.put("MainWallet", logs);
                                                        }
                                                        else {
                                                            Wallet theWallet = new Wallet.Builder()
                                                                    .setPaymentKey(EncryptorClass.Decrypt(paymentKey))
                                                                    .setWalletKey(EncryptorClass.Decrypt(walletKey))
                                                                    .setCurrency(currency)
                                                                    .setEmail(userEmail)
                                                                    .setMoneyCase(moneyCase)
                                                                    .Build();
                                                            userWallets.add(theWallet);
                                                            userMoneyCase.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString());
                                                            userCurrency.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString());
                                                            userWalletLogs.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), logs);
                                                        }

                                                    }
                                                    catch (JSONException e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                });


                                                try
                                                {
                                                    userNameAndSurname = EncryptorClass.Decrypt(mapToJsonObject((Map<String, Object>) Users.get(Email)).get("NameSurname").toString());
                                                }
                                                catch (JSONException e) {
                                                    e.printStackTrace();
                                                }

                                                userAccountImageLinksList = new ArrayList<>();
                                                FirebaseStorage.getInstance("gs://openpos-3e0d3-accountflags")
                                                        .getReference().listAll()
                                                        .addOnCompleteListener(task -> {

                                                        })
                                                        .addOnFailureListener(Throwable::printStackTrace)
                                                        .addOnCanceledListener(() -> {

                                                        })
                                                        .addOnSuccessListener(images ->
                                                        {
                                                            List<StorageReference> ref = images.getItems();
                                                            Thread tr1 = new Thread(() ->
                                                            {
                                                                ref.forEach(x ->
                                                                {
                                                                    Task<Uri> loadImage = x.getDownloadUrl();

                                                                    final Uri[] uri = {null};
                                                                    try {
                                                                        uri[0] = Tasks.await(loadImage);
                                                                    } catch (ExecutionException | InterruptedException e) {
                                                                        e.printStackTrace();
                                                                    }

                                                                    userAccountImageLinksList.add(uri[0].toString());
                                                                });
                                                            });

                                                            tr1.start();
                                                            while (tr1.isAlive()) {
                                                                System.out.println("Waiting...");
                                                            }
                                                        });

                                                try {
                                                    Task<Uri> uploadedImage = FirebaseStorage.getInstance().
                                                            getReference(Email)
                                                            .child("ProfileImage")
                                                            .getDownloadUrl();
                                                    LoginFactoryClass.userImageUri = Tasks.await(uploadedImage);
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                * */

                                                startActivity(intent);
                                                finish();
                                            });
                                        }
                                        else {
                                            login_page_error_text.setText("You entered wrong password. Please try again.");
                                        }
                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                                else {
                                    login_page_error_text.setText("This account does not exist. Please try again.");
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error)
                            {

                            }
                        });
            });

            login_page_forgot_password_text.setOnClickListener(view -> {

            });

            login_page_register_account_page.setOnClickListener(view ->
            {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
                finish();
            });
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.O)
    private void rememberTheUser()
    {
        String saltKey = EncryptorClass.generateSaltKey(32);
        String secretKey = EncryptorClass.generateSecretKey(32);
        String EncryptedMacAddress = EncryptorClass.Encrypt(EncryptorClass.setSecurePassword(getMacAddress.get()), secretKey, saltKey);
        RememberContainer.Builder container = new RememberContainer.Builder().setMacAddress(EncryptedMacAddress);

        userRememberContainer.put("Email", EmailNotEncrypted);

        FirebaseDatabase.getInstance()
                .getReference("Users")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {


                        userRememberContainer.put("NameSurname", EncryptorClass.Decrypt(snapshot.child("NameSurname").getValue().toString()));

                        FirebaseDatabase.getInstance()
                                .getReference("Users")
                                .child("RememberUser")
                                .child(email)
                                .setValue(userRememberContainer);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }*/

    /*FirebaseDatabase.getInstance()
                    .getReference("Users")
                    .child("RememberUser")
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                        {
                            List<DataSnapshot> snap = StreamSupport.stream(snapshot.getChildren().spliterator(), true).collect(Collectors.toList());
                            Supplier<Stream<DataSnapshot>> streamSnap = snap::stream;

                            if(streamSnap.get().anyMatch(x -> x.child("MacAddress").getValue().toString().equals(EncryptorClass.Encrypt(getMacAddress.get()))))
                            {
                                Stream<DataSnapshot> macFilter = streamSnap.get().filter(x -> x.child("MacAddress").getValue().toString().equals(EncryptorClass.Encrypt(getMacAddress.get())));
                                String emailConverter = macFilter.collect(Collectors.toList()).get(0).child("Email").getValue().toString();
                                String Email = EncryptorClass.setSecurePassword(emailConverter);

                                LoginTokenizer tokenizer = new LoginTokenizer(Email);
                                tokenizer.createLoginToken();

                                FirebaseDatabase.getInstance("https://openpos-userstatus.europe-west1.firebasedatabase.app/")
                                        .getReference()
                                        .child("Users")
                                        .child(Email)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                                if (!snapshot.hasChild("isUserActionsBlocked")) {
                                                    actionsBlocked[0] = true;
                                                } else {
                                                    actionsBlocked[0] = Boolean.parseBoolean(snapshot.child("isUserActionsBlocked").getValue().toString());
                                                }

                                                if (!snapshot.hasChild("isUserAccountBlocked")) {
                                                    accountBlocked[0] = true;
                                                } else {
                                                    accountBlocked[0] = Boolean.parseBoolean(snapshot.child("isUserAccountBlocked").getValue().toString());
                                                }

                                                if (!snapshot.hasChild("isUserApprovalInProgress"))
                                                {
                                                    userApproval[0] = true;
                                                }
                                                else {
                                                    userApproval[0] = Boolean.parseBoolean(snapshot.child("isUserApprovalInProgress").getValue().toString());
                                                }

                                                if(Stream.of(Users).anyMatch(x -> !x.isNull(Email)))
                                                {
                                                    EncryptorClass.BiometricClass.checkEncryption(Login.this, () ->
                                                    {
                                                        runOnUiThread(() -> {
                                                            dialog = new Dialog(Login.this);
                                                            dialog.requestWindowFeature(FEATURE_NO_TITLE);
                                                            dialog.setCancelable(false);
                                                            dialog.setContentView(R.layout.dialog_main_page_loading);
                                                            dialog.show();
                                                        });
                                                        Intent intent = new Intent(Login.this, MainPage.class);

                                                        userEmail = emailConverter;
                                                        walletTaken = "MainWallet";

                                                        JSONObject obj = new JSONObject();
                                                        try
                                                        {
                                                            obj = mapToJsonObject((Map<String, Object>) UsersAllWallets.get(Email));
                                                        }
                                                        catch (JSONException e)
                                                        {
                                                            e.printStackTrace();
                                                        }

                                                        userWalletLogs = new HashMap<>();
                                                        JSONObject finalObj = obj;
                                                        obj.keys().forEachRemaining(x ->
                                                        {
                                                            try
                                                            {
                                                                JSONObject theX = mapToJsonObject((Map<String, Object>) finalObj.get(x));
                                                                JSONArray theLogs = mapToJsonArray((ArrayList<Object>) theX.get("Logs"));
                                                                ArrayList<Log> logs = new ArrayList<>();

                                                                for (int i = 0; i < theLogs.length(); i++)
                                                                {
                                                                    try
                                                                    {
                                                                        JSONObject jsonObject = mapToJsonObject((Map<String, Object>) theLogs.get(i));

                                                                        Log log = new Log.Builder()
                                                                                .SetSpend(jsonObject.get("spend").toString())
                                                                                .SetRest(jsonObject.get("rest").toString())
                                                                                .SetEmail(jsonObject.get("email").toString())
                                                                                .SetDate(jsonObject.get("date").toString())
                                                                                .SetContentDescription(jsonObject.get("contentDescription").toString())
                                                                                .SetCommission(jsonObject.get("commission").toString())
                                                                                .Build();
                                                                        logs.add(log);
                                                                    }
                                                                    catch (JSONException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                }

                                                                double moneyCase = Double.parseDouble(mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString().replace(",", "."));
                                                                String currency = mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString();
                                                                String paymentKey = mapToJsonObject((Map<String, Object>) mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("EncryptionKeys")).get("PaymentKey").toString();
                                                                String walletKey = mapToJsonObject((Map<String, Object>) mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("EncryptionKeys")).get("WalletKey").toString();
                                                                if (Objects.requireNonNull(x).equals("MainWallet"))
                                                                {
                                                                    Wallet theWallet = new Wallet.Builder()
                                                                            .setPaymentKey(EncryptorClass.Decrypt(paymentKey))
                                                                            .setWalletKey("MainWallet")
                                                                            .setCurrency(currency)
                                                                            .setEmail(userEmail)
                                                                            .setMoneyCase(moneyCase)
                                                                            .Build();
                                                                    userWallets.add(theWallet);
                                                                    userMoneyCase.put("MainWallet", mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString());
                                                                    userCurrency.put("MainWallet", mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString());
                                                                    userWalletLogs.put("MainWallet", logs);
                                                                }
                                                                else {
                                                                    Wallet theWallet = new Wallet.Builder()
                                                                            .setPaymentKey(EncryptorClass.Decrypt(paymentKey))
                                                                            .setWalletKey(EncryptorClass.Decrypt(walletKey))
                                                                            .setCurrency(currency)
                                                                            .setEmail(userEmail)
                                                                            .setMoneyCase(moneyCase)
                                                                            .Build();
                                                                    userWallets.add(theWallet);
                                                                    userMoneyCase.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("MoneyCase").toString());
                                                                    userCurrency.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), mapToJsonObject((Map<String, Object>) finalObj.get(x)).get("Currency").toString());
                                                                    userWalletLogs.put(EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(walletKey)), logs);
                                                                }

                                                            }
                                                            catch (JSONException e)
                                                            {
                                                                e.printStackTrace();
                                                            }
                                                        });

                                                        try
                                                        {
                                                            userNameAndSurname = EncryptorClass.Decrypt(mapToJsonObject((Map<String, Object>) Users.get(Email)).get("NameSurname").toString());
                                                        }
                                                        catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                        userAccountImageLinksList = new ArrayList<>();
                                                        FirebaseStorage.getInstance("gs://openpos-3e0d3-accountflags")
                                                                .getReference().listAll()
                                                                .addOnCompleteListener(task -> {

                                                                })
                                                                .addOnFailureListener(Throwable::printStackTrace)
                                                                .addOnCanceledListener(() -> {

                                                                })
                                                                .addOnSuccessListener(images ->
                                                                {
                                                                    List<StorageReference> ref = images.getItems();
                                                                    Thread tr1 = new Thread(() ->
                                                                    {
                                                                        ref.forEach(x ->
                                                                        {
                                                                            Task<Uri> loadImage = x.getDownloadUrl();

                                                                            final Uri[] uri = {null};
                                                                            try {
                                                                                uri[0] = Tasks.await(loadImage);
                                                                            } catch (ExecutionException | InterruptedException e) {
                                                                                e.printStackTrace();
                                                                            }

                                                                            userAccountImageLinksList.add(uri[0].toString());
                                                                        });
                                                                    });

                                                                    tr1.start();
                                                                    while (tr1.isAlive()) {
                                                                        System.out.println("Waiting...");
                                                                    }
                                                                });

                                                        try {
                                                            Task<Uri> uploadedImage = FirebaseStorage.getInstance().
                                                                    getReference(Email)
                                                                    .child("ProfileImage")
                                                                    .getDownloadUrl();
                                                            LoginFactoryClass.userImageUri = Tasks.await(uploadedImage);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }

                                                        startActivity(intent);
                                                        finish();
                                                    });

                                                    try
                                                    {
                                                        LoginFactoryClass factory = new LoginFactoryClass(Email);
                                                        LoginFactoryClass.LoginType = UtilityValues.REGISTERED;
                                                        int loginId = factory.LoginUser(userApproval[0], accountBlocked[0], actionsBlocked[0]);
                                                        if (loginId == 0)
                                                        {
                                                            login_page_error_text.setText("Your account is not approved.");
                                                        }
                                                        else if (loginId == 1)
                                                        {
                                                            login_page_error_text.setText("Your account is completely blocked. Please call us to unblock your account.");
                                                        }
                                                        else if (loginId == 2)
                                                        {
                                                            login_page_error_text.setText("Your actions are blocked. You have to wait.");
                                                        }
                                                        else if (loginId == 3)
                                                        {

                                                        }

                                                    }
                                                    catch (Exception e)
                                                    {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                else {
                                                    login_page_error_text.setText("This account does not exist. Please try again.");
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                            }
                                        });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });*/
}