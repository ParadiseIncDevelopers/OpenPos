package com.wallet;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.utilities.classes.EncryptorClass;
import org.jetbrains.annotations.NotNull;
import java.util.HashMap;
import java.util.Map;

public class UserRegistrar
{
    private String email;
    private String nameSurname;
    private String phoneNumber;
    private String password;
    private String currency;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createUser()
    {
        Map<String, Object> data = new HashMap<>();
        data.put("Email", EncryptorClass.Encrypt(this.email));
        data.put("Password", this.password);
        data.put("PhoneNumber", this.phoneNumber);
        data.put("NameSurname", this.nameSurname);

        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
            {
                FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(EncryptorClass.setSecurePassword(email)).setValue(data);

                Wallet wallet = new Wallet("CREATE_WALLET", email);
                wallet.createWallet(currency);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    public static class Builder
    {
        private String email;
        private String nameSurname;
        private String phoneNumber;
        private String password;
        private String currency;

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setEmail(String Email)
        {
            this.email = Email;
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setNameSurname(String nameSurname)
        {
            this.nameSurname = EncryptorClass.Encrypt(nameSurname);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPhoneNumber(String phoneNumber)
        {
            this.phoneNumber = EncryptorClass.Encrypt(phoneNumber);
            return this;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public Builder setPassword(String Password)
        {
            this.password = EncryptorClass.Encrypt(EncryptorClass.setSecurePassword(Password));
            return this;
        }

        public Builder setCurrency(@NotNull String Currency)
        {
            this.currency = Currency;
            return this;
        }

        public UserRegistrar Build()
        {
            UserRegistrar credentials = new UserRegistrar();
            credentials.email = this.email;
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
