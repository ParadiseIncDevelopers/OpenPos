package com.wallet;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.money.Money;
import com.user.UserRegistrar;
import com.utilities.classes.EncryptorClass;
import com.wallet.Models.Log;
import com.wallet.Models.WalletContainer;
import com.wallet.Models.WalletEncryption;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;

import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static com.utilities.classes.CurrencyConverter.ConvertMoneyByAny;
import static com.utilities.classes.CurrencyConverter.ConvertMoneyByDollar;
import static com.utilities.classes.LoginFactoryClass.userMoneyCase;
import static com.utilities.classes.LoginFactoryClass.userWalletLogs;
import static com.utilities.classes.LoginFactoryClass.walletTaken;

public class Wallet
{
    private String actionCode;
    private String email;
    private String currency;
    private double moneyCase;
    private String walletKey;
    private String paymentKey;

    private WalletContainer container;

    public static Supplier<String> walletKeyCreator = () ->
    {
        Random random = new Random();
        StringBuilder string = new StringBuilder();
        char[] Hexa = new char[] {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 5; j++)
            {
                int n = random.nextInt(16);
                string.append(Hexa[n]);
            }

            if(i != 5)
            {
                string.append("-");
            }
        }

        return string.toString();
    };

    public static Supplier<String> paymentKeyCreator = () ->
    {
        Random random = new Random();
        StringBuilder string = new StringBuilder();
        char[] Hexa = new char[] {'0','1','2','3','4','5','6','7','8','9'};
        for(int i = 0; i < 6; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                int n = random.nextInt(10);
                string.append(Hexa[n]);
            }

            if(i != 5)
            {
                string.append("-");
            }
        }

        return string.toString();
    };

    private Wallet()
    {

    }

    public Wallet(String actionCode)
    {
        this.actionCode = actionCode;
    }


    public String getEmail() {
        return email;
    }

    public String getCurrency() {
        return currency;
    }

    public double getMoneyCase() {
        return moneyCase;
    }

    public String getWalletKey() {
        return walletKey;
    }

    public String getPaymentKey() {
        return paymentKey;
    }

    public static class Builder
    {
        private String email;
        private String currency;
        private double moneyCase;
        private String walletKey;
        private String paymentKey;

        public Builder setCurrency(String currency)
        {
            this.currency = currency;
            return this;
        }

        public Builder setMoneyCase(double moneyCase)
        {
            this.moneyCase = moneyCase;
            return this;
        }

        public Builder setWalletKey(String walletKey)
        {
            this.walletKey = walletKey;
            return this;
        }

        public Builder setPaymentKey(String paymentKey)
        {
            this.paymentKey = paymentKey;
            return this;
        }

        public Builder setEmail(String email)
        {
            this.email = email;
            return this;
        }

        public Wallet Build()
        {
            Wallet wallet = new Wallet();
            wallet.email = this.email;
            wallet.paymentKey = this.paymentKey;
            wallet.walletKey = this.walletKey;
            wallet.moneyCase = this.moneyCase;
            wallet.currency = this.currency;
            return wallet;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void transactToWallet(Context context, String email, String paymentKey, String walletKey, String destinationEmail, double money, double commission, String description)
    {
        Map<String, Object> elements = new HashMap<>();
        Function<String, String> descriptionText = (s) ->
        {
            if(s.isEmpty())
            {
                return "NO_DESCRIPTION";
            }
            else{
                return s;
            }
        };

        double finalMoney = money + commission;

        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                .getReference()
                .child(EncryptorClass.setSecurePassword(email))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {

                        List<DataSnapshot> snap = StreamSupport.stream(snapshot.getChildren().spliterator(), true).collect(Collectors.toList());
                        Supplier<Stream<DataSnapshot>> streamSnap = () -> snap.stream();

                        if(streamSnap.get().anyMatch(x -> x.child("EncryptionKeys").child("PaymentKey").getValue().toString().equals(paymentKey) &&
                                        x.child("EncryptionKeys").child("WalletKey").getValue().toString().equals(walletKey)))
                        {
                            DataSnapshot theSnap = streamSnap.get().filter(x -> x.child("EncryptionKeys").child("PaymentKey").getValue().toString().equals(paymentKey) &&
                                    x.child("EncryptionKeys").child("WalletKey").getValue().toString().equals(walletKey)).collect(Collectors.toList()).get(0);
                            String theWallet = EncryptorClass.setSecurePassword(EncryptorClass.Decrypt(theSnap.child("EncryptionKeys").child("WalletKey").getValue().toString()));

                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(destinationEmail)
                                    .child(walletKey)
                                    .addListenerForSingleValueEvent(new ValueEventListener()
                                    {
                                        @RequiresApi(api = Build.VERSION_CODES.O)
                                        @Override
                                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2)
                                        {
                                            elements.putAll((Map<String, Object>) snapshot2.getValue());
                                            double changeCurrentMoney = Double.parseDouble(Objects.requireNonNull(snapshot2.child("MoneyCase").getValue()).toString().replace(",",".")) - finalMoney;

                                            elements.replace("MoneyCase", new DecimalFormat("#.##").format(changeCurrentMoney));

                                            List<Log> getLogs = (List<Log>) elements.get("Logs");
                                            Log log = new Log.Builder()
                                                    .SetEmail(destinationEmail)
                                                    .SetContentDescription(descriptionText.apply(description))
                                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                                    .SetRest(new DecimalFormat("#.##").format(changeCurrentMoney))
                                                    .SetSpend(new DecimalFormat("#.##").format(money))
                                                    .SetCommission(new DecimalFormat("#.##").format(commission))
                                                    .Build();
                                            getLogs.add(log);

                                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                    .getReference()
                                                    .child(EncryptorClass.setSecurePassword(destinationEmail))
                                                    .child(walletKey).setValue(elements);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull @NotNull DatabaseError error)
                                        {

                                        }
                                    });

                            elements.putAll((Map<String, Object>) snapshot.getValue());
                            double changeCurrentMoney = Double.parseDouble(Objects.requireNonNull(snapshot.child("MoneyCase").getValue()).toString().replace(",",".")) + finalMoney;

                            Money theMoney = new Money();
                            theMoney.setMoneyCase(changeCurrentMoney);
                            theMoney.setCurrency(snapshot.child("Currency").getValue().toString());

                            Money newMoney = ConvertMoneyByAny(snapshot.child("Currency").getValue().toString(), theMoney);

                            elements.replace("MoneyCase", new DecimalFormat("#.##").format(newMoney.getMoneyCase()));

                            List<Log> getLogs = (List<Log>) elements.get("Logs");
                            Log log = new Log.Builder()
                                    .SetEmail(email)
                                    .SetContentDescription(descriptionText.apply(description))
                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                    .SetRest(new DecimalFormat("#.##").format(changeCurrentMoney))
                                    .SetSpend(new DecimalFormat("#.##").format(money))
                                    .SetCommission(new DecimalFormat("#.##").format(commission))
                                    .Build();
                            getLogs.add(log);

                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(email))
                                    .child(theWallet).setValue(elements);

                            getCommission(newMoney.getMoneyCase(), newMoney.getCurrency());
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void transactToWallet(Context context, String email, String destinationEmail, String dataWallet, double money, String description)
    {
        Map<String, Object> elements = new HashMap<>();
        Function<String, String> descriptionText = (s) ->
        {
            if(s.isEmpty())
            {
                return "NO_DESCRIPTION";
            }
            else{
                return s;
            }
        };

        double commission = returnCommission(money);
        double finalMoney = money + commission;

        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                .getReference()
                .child(EncryptorClass.setSecurePassword(email))
                .child(walletTaken)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                .getReference()
                                .child(EncryptorClass.setSecurePassword(destinationEmail))
                                .child(dataWallet)
                                .addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot2)
                                    {
                                        elements.putAll((Map<String, Object>) snapshot2.getValue());
                                        double changeCurrentMoney = Double.parseDouble(Objects.requireNonNull(snapshot2.child("MoneyCase").getValue()).toString().replace(",",".")) - finalMoney;

                                        elements.replace("MoneyCase", new DecimalFormat("#.##").format(changeCurrentMoney));

                                        List<Log> getLogs = (List<Log>) elements.get("Logs");
                                        Log log = new Log.Builder()
                                                .SetEmail(destinationEmail)
                                                .SetContentDescription(descriptionText.apply(description))
                                                .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                                .SetRest(new DecimalFormat("#.##").format(changeCurrentMoney))
                                                .SetSpend(new DecimalFormat("#.##").format(money))
                                                .SetCommission(new DecimalFormat("#.##").format(commission))
                                                .Build();
                                        getLogs.add(log);

                                        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                                .getReference()
                                                .child(EncryptorClass.setSecurePassword(destinationEmail))
                                                .child(dataWallet).setValue(elements);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error)
                                    {

                                    }
                                });

                        elements.putAll((Map<String, Object>) snapshot.getValue());
                        double changeCurrentMoney = Double.parseDouble(Objects.requireNonNull(snapshot.child("MoneyCase").getValue()).toString().replace(",",".")) + finalMoney;

                        Money theMoney = new Money();
                        theMoney.setMoneyCase(changeCurrentMoney);
                        theMoney.setCurrency(snapshot.child("Currency").getValue().toString());

                        Money newMoney = ConvertMoneyByAny(snapshot.child("Currency").getValue().toString(), theMoney);

                        elements.replace("MoneyCase", new DecimalFormat("#.##").format(newMoney.getMoneyCase()));

                        List<Log> getLogs = (List<Log>) elements.get("Logs");
                        Log log = new Log.Builder()
                                .SetEmail(email)
                                .SetContentDescription(descriptionText.apply(description))
                                .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                .SetRest(new DecimalFormat("#.##").format(changeCurrentMoney))
                                .SetSpend(new DecimalFormat("#.##").format(money))
                                .SetCommission(new DecimalFormat("#.##").format(commission))
                                .Build();
                        getLogs.add(log);

                        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                .getReference()
                                .child(EncryptorClass.setSecurePassword(email))
                                .child(dataWallet).setValue(elements);

                        getCommission(newMoney.getMoneyCase(), newMoney.getCurrency());
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error)
                    {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void addMoneyToWallet(Context context, String email, double money)
    {
        Map<String, Object> elements = new HashMap<>();

        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                .getReference()
                .child(EncryptorClass.setSecurePassword(email))
                .child(walletTaken)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        elements.putAll((Map<String, Object>) snapshot.getValue());
                        double changeCurrentMoney = Double.parseDouble(Objects.requireNonNull(snapshot.child("MoneyCase").getValue()).toString().replace(",",".")) + money;

                        Money theMoney = new Money();
                        theMoney.setMoneyCase(changeCurrentMoney);
                        theMoney.setCurrency(snapshot.child("Currency").getValue().toString());

                        Money newMoney = ConvertMoneyByDollar(theMoney);

                        if(newMoney.getMoneyCase() >= 5.0)
                        {
                            elements.replace("MoneyCase", new DecimalFormat("#.##").format(theMoney.getMoneyCase()));
                            userMoneyCase.replace(walletTaken, new DecimalFormat("#.##").format(theMoney.getMoneyCase()));

                            List<Log> getLogs = Log.parseToArrayList((List<Map<String, Object>>) elements.get("Logs"));
                            userMoneyCase.replace(walletTaken, new DecimalFormat("#.##").format(changeCurrentMoney));

                            Log log = new Log.Builder()
                                    .SetEmail(email)
                                    .SetContentDescription("ADDED MONEY")
                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                    .SetRest(new DecimalFormat("#.##").format(theMoney.getMoneyCase()))
                                    .SetSpend(new DecimalFormat("#.##").format(money))
                                    .SetCommission(new DecimalFormat("#.##").format(0.0))
                                    .Build();

                            getLogs.add(log);

                            elements.replace("Logs", getLogs);

                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(email))
                                    .child(walletTaken).setValue(elements);

                            userWalletLogs.replace(walletTaken, (ArrayList<Log>) getLogs);
                        }
                        else{
                            Toast.makeText(context, "You must add more than 5$ or the amount equivalent from your currency.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error)
                    {

                    }
                });
    }

    public static double returnCommission(double money)
    {
        return money / 100 * 0.5;
    }

    public static void getCommission(double commission, String currency)
    {
        FirebaseDatabase.getInstance("https://openpos-adminsection.europe-west1.firebasedatabase.app/")
                .getReference().addListenerForSingleValueEvent(new ValueEventListener()
        {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(!snapshot.hasChild("CommissionsList"))
                {
                    Map<String, Object> totalMoneyContent = new HashMap<>();
                    Map<String, Object> totalMoneyDateSection = new HashMap<>();
                    List<Object> totalMoneyDateSectionCounter = new ArrayList<>();
                    totalMoneyDateSection.put("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    totalMoneyDateSection.put("Sum", new DecimalFormat("#.##").format(commission));
                    totalMoneyDateSection.put("Account", walletTaken);
                    totalMoneyDateSection.put("Currency", currency);
                    totalMoneyDateSectionCounter.add(totalMoneyDateSection);
                    totalMoneyContent.put("CommissionsList", totalMoneyDateSectionCounter);
                    totalMoneyContent.put("TotalMoney", new DecimalFormat("#.##").format(commission));
                    totalMoneyContent.put("Currency", "USD");
                    FirebaseDatabase.getInstance("https://openpos-adminsection.europe-west1.firebasedatabase.app/")
                            .getReference().setValue(totalMoneyContent);
                }
                else{

                    Map<String, Object> totalMoneyContent = (Map<String, Object>) snapshot.getValue();
                    Map<String, Object> totalMoneyDateSection = new HashMap<>();
                    totalMoneyDateSection.put("Date", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    totalMoneyDateSection.put("Sum", new DecimalFormat("#.##").format(commission));
                    totalMoneyDateSection.put("Account", walletTaken);
                    List<Object> totalMoneyDateSectionCounter = (List<Object>) snapshot.child("CommissionsList").getValue();
                    assert totalMoneyDateSectionCounter != null;
                    totalMoneyDateSectionCounter.add(totalMoneyDateSection);
                    assert totalMoneyContent != null;
                    totalMoneyContent.put("CommissionsList", totalMoneyDateSectionCounter);
                    totalMoneyContent.put("TotalMoney", new DecimalFormat("#.##").format(Double.valueOf(snapshot.child("TotalMoney").getValue().toString().replace(",",".")) + commission));
                    FirebaseDatabase.getInstance("https://openpos-adminsection.europe-west1.firebasedatabase.app/")
                            .getReference().setValue(totalMoneyContent);
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void withdrawMoneyFromWallet(Context context, String email, double money)
    {
        Map<String, Object> elements = new HashMap<>();

        double commission = returnCommission(money);
        double finalMoney = money + commission;
        FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                .getReference()
                .child(EncryptorClass.setSecurePassword(email))
                .child(walletTaken)
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                    {
                        elements.putAll((Map<String, Object>) snapshot.getValue());

                        double currentMoney = Double.parseDouble(Objects.requireNonNull(snapshot.child("MoneyCase").getValue()).toString().replace(",","."));

                        if(currentMoney < finalMoney)
                        {
                            Toast.makeText(context, "You can't withdraw more money", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            double changeCurrentMoney = currentMoney - finalMoney;

                            Money theMoney = new Money();
                            theMoney.setMoneyCase(commission);
                            theMoney.setCurrency(snapshot.child("Currency").getValue().toString());

                            Money newMoney = ConvertMoneyByDollar(theMoney);

                            elements.replace("MoneyCase", new DecimalFormat("#.##").format(changeCurrentMoney));
                            userMoneyCase.replace(walletTaken, new DecimalFormat("#.##").format(changeCurrentMoney));

                            List<Log> getLogs = Log.parseToArrayList((List<Map<String, Object>>) elements.get("Logs"));

                            Log log = new Log.Builder()
                                    .SetEmail(email)
                                    .SetContentDescription("WITHDRAW MONEY")
                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                    .SetRest(new DecimalFormat("#.##").format(changeCurrentMoney))
                                    .SetSpend(new DecimalFormat("#.##").format(finalMoney))
                                    .SetCommission(new DecimalFormat("#.##").format(commission))
                                    .Build();

                            getLogs.add(log);

                            elements.replace("Logs", getLogs);

                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(email))
                                    .child(walletTaken).setValue(elements);

                            userWalletLogs.replace(walletTaken, (ArrayList<Log>) getLogs);

                            getCommission(newMoney.getMoneyCase(), newMoney.getCurrency());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error)
                    {

                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String createNormalAccount(String Currency)
    {
        final String[] returnString = {""};

        /*if(this.code.equals("CREATE_NORMAL_WALLET"))
        {
            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                        {
                            Map<String, Object> wallet = new HashMap<>();
                            Map<String, String> walletLogs = new HashMap<>();
                            Map<String, Map<String, String>> logs = new HashMap<>();
                            Map<String, Object> walletEncryption = new HashMap<>();

                            Log zeroLog = new Log.Builder()
                                    .SetEmail(email)
                                    .SetCommission("NO_COMMISSION")
                                    .SetContentDescription("WALLET CREATED.")
                                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                                    .SetRest("0.00")
                                    .SetSpend("0.00")
                                    .Build();

                            walletLogs.put("email", zeroLog.getEmail());
                            walletLogs.put("commission", zeroLog.getCommission());
                            walletLogs.put("contentDescription", zeroLog.getContentDescription());
                            walletLogs.put("date", zeroLog.getDate());
                            walletLogs.put("spend", zeroLog.getSpend());
                            walletLogs.put("rest", zeroLog.getRest());
                            logs.put("0", walletLogs);

                            final String[] thePaymentKey = {paymentKeyCreator.get()};
                            final String[] theWalletKey = {walletKeyCreator.get()};

                            if(snapshot.hasChildren())
                            {
                                for(DataSnapshot snap : snapshot.getChildren())
                                {
                                    if(snap.child("EncryptionKeys").child("WalletKey").getValue().toString().equals(theWalletKey[0]))
                                    {
                                        theWalletKey[0] = walletKeyCreator.get();
                                    }

                                    if(snap.child("EncryptionKeys").child("PaymentKey").getValue().toString().equals(thePaymentKey[0]))
                                    {
                                        thePaymentKey[0] = paymentKeyCreator.get();
                                    }
                                }

                                walletEncryption.put("WalletKey", EncryptorClass.Encrypt(theWalletKey[0]));
                                walletEncryption.put("PaymentKey", EncryptorClass.Encrypt(thePaymentKey[0]));

                                wallet.put("MoneyCase", 0.0);
                                wallet.put("Currency", Currency);
                                wallet.put("Logs", logs);

                            }
                            else{

                                walletEncryption.put("WalletKey", EncryptorClass.Encrypt(theWalletKey[0]));
                                walletEncryption.put("PaymentKey", EncryptorClass.Encrypt(thePaymentKey[0]));

                                wallet.put("MoneyCase", 0.0);
                                wallet.put("Currency", Currency);
                                wallet.put("Logs", walletLogs);

                            }
                            wallet.put("EncryptionKeys", walletEncryption);
                            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(email))
                                    .child(EncryptorClass.setSecurePassword(theWalletKey[0]))
                                    .setValue(wallet);

                            returnString[0] = walletEncryption.get("WalletKey").toString();
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }*/

        return returnString[0];
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void createWallet(@NonNull UserRegistrar user)
    {
        if(this.actionCode.equals("CREATE_WALLET"))
        {
            List<Log> logs = new ArrayList<>();
            Log zeroLog = new Log.Builder()
                    .SetDate(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .SetRest("0.00")
                    .SetSpend("0.00")
                    .Build();
            logs.add(zeroLog);

            String saltKey = EncryptorClass.generateSaltKey(32);
            String secretKey = EncryptorClass.generateSecretKey(32);

            final String[] thePaymentKey = {paymentKeyCreator.get()};
            final String[] theWalletKey = {walletKeyCreator.get()};

            FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                    .getReference()
                    .addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                        {

                            if(snapshot.hasChildren())
                            {
                                for(DataSnapshot snap : snapshot.getChildren())
                                {

                                    if(snap.child("EncryptionKeys").child("WalletKey").getValue().toString().equals(theWalletKey[0]))
                                    {
                                        theWalletKey[0] = walletKeyCreator.get();
                                    }

                                    if(snap.child("EncryptionKeys").child("PaymentKey").getValue().toString().equals(thePaymentKey[0]))
                                    {
                                        thePaymentKey[0] = paymentKeyCreator.get();
                                    }
                                }

                                WalletEncryption walletEncryption = new WalletEncryption.Builder()
                                        .setWalletKey(EncryptorClass.Encrypt(theWalletKey[0], secretKey, saltKey))
                                        .setPaymentKey(EncryptorClass.Encrypt(thePaymentKey[0], secretKey, saltKey))
                                        .build();

                                container = new WalletContainer.Builder()
                                        .setEncryption(walletEncryption)
                                        .setLogs(logs)
                                        .setCurrency(user.getCurrency())
                                        .setMoneyCase(0.0)
                                        .build();
                            }
                            else {
                                WalletEncryption walletEncryption = new WalletEncryption.Builder()
                                        .setWalletKey(EncryptorClass.Encrypt(theWalletKey[0], secretKey, saltKey))
                                        .setPaymentKey(EncryptorClass.Encrypt(thePaymentKey[0], secretKey, saltKey))
                                        .build();

                                container = new WalletContainer.Builder()
                                        .setEncryption(walletEncryption)
                                        .setLogs(logs)
                                        .setCurrency(user.getCurrency())
                                        .setMoneyCase(0.0)
                                        .build();
                                walletTaken = walletKey;
                            }

                            try
                            {
                                FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                                        .getReference()
                                        .child(user.getId())
                                        .child(walletKey)
                                        .setValue(container.toJsonObject());
                            }
                            catch (JSONException e)
                            {
                                throw new RuntimeException(e);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull @NotNull DatabaseError error) {

                        }
                    });
        }
    }
}
