package com.user;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.classes.EncryptorClass;
import com.wallet.Wallet;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class UserRegistrar {
    //Kullanıcı Id
    private String id;
    //Adı soyadı
    private String nameSurname;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createUser()
    {
        Wallet wallet = new Wallet("CREATE_WALLET");
        //wallet.createWallet(UserRegistrar.this);
    }

    public String getId()
    {
        return id;
    }

    public String getNameSurname() {
        return nameSurname;
    }

    //Verileri json object e çeviren helper method.
    public JSONObject toJsonObject() throws JSONException
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id);
        jsonObject.put("nameSurname", nameSurname);

        return jsonObject;
    }

    public static class Builder
    {
        private String id;
        private String nameSurname;

        public Builder createSaltAndSecret(int length)
        {
            this.keys = new ArrayList<>();
            keys.add(EncryptorClass.generateSecretKey(length));
            keys.add(EncryptorClass.generateSaltKey(length));
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setEmail(String email)
        {
            String passwordSecretKey = this.keys.get(0);
            String passwordSaltKey = this.keys.get(1);

            this.email = new EncryptedEmail.Builder()
                    .setSalt(passwordSaltKey)
                    .setText(EncryptorClass.Encrypt(email, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey).build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setNameSurname(String nameSurname)
        {
            String passwordSecretKey = this.keys.get(0);
            String passwordSaltKey = this.keys.get(1);

            this.nameSurname = new EncryptedName.Builder()
                    .setSalt(passwordSaltKey)
                    .setText(EncryptorClass.Encrypt(nameSurname, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey).build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPhoneNumber(String phoneNumber)
        {
            String passwordSecretKey = this.keys.get(0);
            String passwordSaltKey = this.keys.get(1);

            this.phoneNumber = new EncryptedPhoneNumber.Builder()
                    .setSalt(passwordSaltKey)
                    .setText(EncryptorClass.Encrypt(phoneNumber, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey).build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPassword(String Password)
        {
            String passwordHash = EncryptorClass.setSecurePassword(Password);
            String passwordSecretKey = this.keys.get(0);
            String passwordSaltKey = this.keys.get(1);

            this.password = new EncryptedPassword.Builder()
                    .setSalt(passwordSaltKey)
                    .setPasswordText(EncryptorClass.Encrypt(passwordHash, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey)
                    .build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setCode(@NotNull String code)
        {
            String text = EncryptorClass.setSecurePassword(code);
            String passwordSecretKey = this.keys.get(0);
            String passwordSaltKey = this.keys.get(1);

            this.code = new EncryptedPassword.Builder()
                    .setSalt(passwordSaltKey)
                    .setPasswordText(EncryptorClass.Encrypt(text, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey)
                    .build();
            return this;
        }

        public Builder setCurrency(@NotNull String Currency)
        {
            this.currency = Currency;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        public Builder createId()
        {
            Supplier<String> generateUniqueID = () ->
            {
                String alphanumeric = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
                int len = 5;
                int segm = 3;

                StringBuilder idBuilder = new StringBuilder();
                SecureRandom random = new SecureRandom();

                for (int i = 0; i < segm; i++) {
                    for (int j = 0; j < len; j++) {
                        int index = random.nextInt(alphanumeric.length());
                        char randomChar = alphanumeric.charAt(index);
                        idBuilder.append(randomChar);
                    }
                    if (i < segm - 1) {
                        idBuilder.append('-');
                    }
                }

                return idBuilder.toString();
            };

            DatabaseReference usersRef = FirebaseDatabase.getInstance("https://your-firebase-project-id.firebaseio.com")
                    .getReference("Users");

            generatedCode = generateUniqueID.get();

            usersRef.child(generatedCode)
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists())
                            {
                                generatedCode = generateUniqueID.get();
                                usersRef.child(generatedCode).addListenerForSingleValueEvent(this);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error)
                        {

                        }
                    });

            this.id = generatedCode;
            return this;
        }

        public UserRegistrar Build()
        {
            UserRegistrar credentials = new UserRegistrar();
            credentials.id = this.id;
            credentials.email = this.email;
            credentials.code = this.code;
            credentials.nameSurname = this.nameSurname;
            credentials.phoneNumber = this.phoneNumber;
            credentials.password = this.password;
            credentials.currency = this.currency;
            return credentials;
        }
    }

    private UserRegistrar()
    {

    }
}
