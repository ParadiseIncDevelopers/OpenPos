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
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class UserRegistrar
{
    private String id;
    private String email;
    private EncryptedPassword code;
    private EncryptedName nameSurname;
    private EncryptedPhoneNumber phoneNumber;
    private EncryptedPassword password;
    private String currency;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createUser()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("Email", this.email);
        data.put("UserCode", this.code);
        data.put("EncryptedObject", this.password);
        data.put("PhoneNumber", this.phoneNumber);
        data.put("NameSurname", this.nameSurname);

        FirebaseDatabase.getInstance()
                .getReference("Users")
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        FirebaseDatabase.getInstance()
                                .getReference()
                                .child("Users")
                                .child(id)
                                .setValue(data);

                        Wallet wallet = new Wallet("CREATE_WALLET", email);
                        wallet.createWallet(currency);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    public String getId()
    {
        return id;
    }

    public static class Builder
    {
        private String id;
        private String email;
        private EncryptedName nameSurname;
        private EncryptedPhoneNumber phoneNumber;
        private EncryptedPassword password;
        private String currency;
        private EncryptedPassword code;

        private String generatedCode;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setEmail(String Email)
        {
            this.email = Email;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setNameSurname(String nameSurname)
        {
            String text = EncryptorClass.setSecurePassword(nameSurname);
            String passwordSecretKey = EncryptorClass.generateSecretKey(24);
            String passwordSaltKey = EncryptorClass.generateSaltKey(24);

            this.nameSurname = new EncryptedName.Builder()
                    .setSalt(passwordSaltKey)
                    .setCode(EncryptorClass.Encrypt(text, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey).build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPhoneNumber(String phoneNumber)
        {
            String text = EncryptorClass.setSecurePassword(phoneNumber);
            String passwordSecretKey = EncryptorClass.generateSecretKey(20);
            String passwordSaltKey = EncryptorClass.generateSaltKey(20);

            this.phoneNumber = new EncryptedPhoneNumber.Builder()
                    .setSalt(passwordSaltKey)
                    .setCode(EncryptorClass.Encrypt(text, passwordSecretKey, passwordSaltKey))
                    .setSecret(passwordSecretKey).build();
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPassword(String Password)
        {
            String passwordHash = EncryptorClass.setSecurePassword(Password);
            String passwordSecretKey = EncryptorClass.generateSecretKey(32);
            String passwordSaltKey = EncryptorClass.generateSaltKey(32);

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
            String passwordSecretKey = EncryptorClass.generateSecretKey(32);
            String passwordSaltKey = EncryptorClass.generateSaltKey(32);

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
