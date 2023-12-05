package com.free;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.free.login.Login;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.menu.MainMenu;
import com.utilities.adapters.AvatarChooserMenuAdapter;
import com.utilities.classes.LoginFactoryClass;
import com.utilities.adapters.WalletLogsAdapter;
import com.utilities.classes.UtilityValues;
import com.wallet.AddMoney;
import com.wallet.ReceiveMoney;
import com.wallet.SendMoney;
import com.wallet.TransferMoney;
import com.wallet.Models.Log;
import com.wallet.WithdrawMoney;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.LoginFactoryClass.Logout;
import static com.utilities.classes.LoginFactoryClass.userCurrency;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.userImageUri;
import static com.utilities.classes.LoginFactoryClass.userMoneyCase;
import static com.utilities.classes.LoginFactoryClass.userNameAndSurname;
import static com.utilities.classes.LoginFactoryClass.userWalletLogs;
import static com.utilities.classes.LoginFactoryClass.walletTaken;

public class MainPage extends AppCompatActivity
{
    private TextView main_page_wallet, main_page_name_and_surname, main_page_email, main_page_transactions_text;
    private FloatingActionButton main_page_give_money_button, main_page_receive_money_button,
            main_page_withdraw_money_button, main_page_add_money_button, main_page_transactions_menu,
            main_page_transfer_money_button, main_page_transactions_filter_button;
    private RecyclerView main_page_transactions_logs;
    private WalletLogsAdapter adapter;
    private AvatarChooserMenuAdapter adapter2;
    private TextInputLayout transaction_minimum_amount_filters_1, transaction_minimum_amount_filters_2, transaction_maximum_amount_filters_1,
            transaction_maximum_amount_filters_2, transaction_start_date_filters_1, transaction_start_date_filters_2,
            transaction_start_date_filters_3, transaction_final_date_filters_1, transaction_final_date_filters_2,
            transaction_final_date_filters_3;
    private TextInputEditText transaction_minimum_amount_filters_text_1, transaction_minimum_amount_filters_text_2,
            transaction_maximum_amount_filters_text_1,transaction_maximum_amount_filters_text_2;
    private MaterialAutoCompleteTextView transaction_start_date_filters_text_1, transaction_start_date_filters_text_2, transaction_start_date_filters_text_3,
            transaction_final_date_filters_text_1, transaction_final_date_filters_text_2, transaction_final_date_filters_text_3;
    private Toolbar transaction_filters_toolbar;
    private Button transaction_filters_submit_button, transaction_filters_reset_button, transaction_filters_get_default_list;
    private CheckBox transaction_received_amounts_filters_checkbox, transaction_sent_amounts_filters_checkbox;
    @SuppressLint("StaticFieldLeak")
    public static ImageView main_page_transactions_profile_image;

    private static boolean filter_minimum_1 = true, filter_minimum_2 = true, filter_maximum_1 = true, filter_maximum_2 = true,
    filter_start_1 = true, filter_start_2 = true, filter_start_3 = true, filter_final_1 = true, filter_final_2 = true, filter_final_3 = true;

    private static ArrayList<Log> allLogs;

    @SuppressLint("ClickableViewAccessibility")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*NetworkCallback(this, () -> {
            main_page_transactions_profile_image = findViewById(R.id.main_page_transactions_profile_image);

            main_page_wallet = findViewById(R.id.main_page_wallet);
            main_page_name_and_surname = findViewById(R.id.main_page_name_and_surname);
            main_page_email = findViewById(R.id.main_page_email);
            main_page_transactions_text = findViewById(R.id.main_page_transactions_text);
            main_page_transactions_logs = findViewById(R.id.main_page_transactions_logs);

            main_page_receive_money_button = findViewById(R.id.main_page_receive_money_button);
            main_page_give_money_button = findViewById(R.id.main_page_give_money_button);
            main_page_withdraw_money_button = findViewById(R.id.main_page_withdraw_money_button);
            main_page_add_money_button = findViewById(R.id.main_page_add_money_button);
            main_page_transfer_money_button = findViewById(R.id.main_page_transfer_money_button);
            main_page_transactions_filter_button = findViewById(R.id.main_page_transactions_filter_button);

            main_page_transactions_menu = findViewById(R.id.main_page_transactions_menu);

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            if (Looper.myLooper() == null)
            {
                Looper.prepare();
            }

            if(getIntent().getExtras() != null)
            {
                LoginFactoryClass.userEmail = getIntent().getExtras().getString("Email");
            }

            main_page_email.setText(userEmail);
            main_page_name_and_surname.setText(userNameAndSurname);

            runOnUiThread(() -> {
                try
                {
                    main_page_transactions_text.setText("Last transactions");
                    Glide.with(MainPage.this)
                            .load(new URL(userImageUri.toString()).toString())
                            .into(main_page_transactions_profile_image);
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            });

            runOnUiThread(() -> {
                allLogs = userWalletLogs.get(walletTaken);
                ArrayList<Log> logs = allLogs;
                adapter = new WalletLogsAdapter(logs);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                manager.setReverseLayout(true);
                manager.setStackFromEnd(true);
                main_page_transactions_logs.setLayoutManager(manager);
                main_page_transactions_logs.setAdapter(adapter);
                main_page_wallet.setText(String.format("%s %s", userMoneyCase.get(walletTaken), userCurrency.get(walletTaken)));
            });

            main_page_give_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, SendMoney.class);
                startActivity(intent);
            });
            main_page_receive_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, ReceiveMoney.class);
                startActivity(intent);
            });
            main_page_add_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, AddMoney.class);
                startActivity(intent);
            });
            main_page_withdraw_money_button.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, WithdrawMoney.class);
                startActivity(intent);
            });

            main_page_transactions_menu.setOnClickListener(view ->
            {
                Intent intent = new Intent(MainPage.this, MainMenu.class);
                startActivity(intent);
            });

            main_page_transfer_money_button.setOnClickListener(view -> {
                Intent intent = new Intent(MainPage.this, TransferMoney.class);
                startActivity(intent);
            });

            main_page_transactions_filter_button.setOnClickListener(view ->
            {
                openTransactionFilters();
            });

            main_page_transactions_profile_image.setOnClickListener(view -> openUserImageAvatarList());
        });*/
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void openTransactionFilters()
    {
        final Dialog transactionFilter = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        transactionFilter.requestWindowFeature(Window.FEATURE_NO_TITLE);
        transactionFilter.setContentView(R.layout.dialog_transaction_filters);
        transactionFilter.setCanceledOnTouchOutside(false);

        transaction_filters_toolbar = transactionFilter.findViewById(R.id.transaction_filters_toolbar);
        transaction_filters_toolbar.setNavigationOnClickListener(view -> transactionFilter.dismiss());
        transaction_minimum_amount_filters_1 = transactionFilter.findViewById(R.id.transaction_minimum_amount_filters_1);
        transaction_minimum_amount_filters_2 = transactionFilter.findViewById(R.id.transaction_minimum_amount_filters_2);
        transaction_maximum_amount_filters_1 = transactionFilter.findViewById(R.id.transaction_maximum_amount_filters_1);
        transaction_maximum_amount_filters_2 = transactionFilter.findViewById(R.id.transaction_maximum_amount_filters_2);
        transaction_start_date_filters_1 = transactionFilter.findViewById(R.id.transaction_start_date_filters_1);
        transaction_start_date_filters_2 = transactionFilter.findViewById(R.id.transaction_start_date_filters_2);
        transaction_start_date_filters_3 = transactionFilter.findViewById(R.id.transaction_start_date_filters_3);
        transaction_final_date_filters_1 = transactionFilter.findViewById(R.id.transaction_final_date_filters_1);
        transaction_final_date_filters_2 = transactionFilter.findViewById(R.id.transaction_final_date_filters_2);
        transaction_final_date_filters_3 = transactionFilter.findViewById(R.id.transaction_final_date_filters_3);
        transaction_minimum_amount_filters_text_1 = transactionFilter.findViewById(R.id.transaction_minimum_amount_filters_text_1);
        transaction_minimum_amount_filters_text_2 = transactionFilter.findViewById(R.id.transaction_minimum_amount_filters_text_2);
        transaction_maximum_amount_filters_text_1 = transactionFilter.findViewById(R.id.transaction_maximum_amount_filters_text_1);
        transaction_maximum_amount_filters_text_2 = transactionFilter.findViewById(R.id.transaction_maximum_amount_filters_text_2);
        transaction_start_date_filters_text_1 = transactionFilter.findViewById(R.id.transaction_start_date_filters_text_1);
        transaction_start_date_filters_text_2 = transactionFilter.findViewById(R.id.transaction_start_date_filters_text_2);
        transaction_start_date_filters_text_3 = transactionFilter.findViewById(R.id.transaction_start_date_filters_text_3);
        transaction_final_date_filters_text_1 = transactionFilter.findViewById(R.id.transaction_final_date_filters_text_1);
        transaction_final_date_filters_text_2 = transactionFilter.findViewById(R.id.transaction_final_date_filters_text_2);
        transaction_final_date_filters_text_3 = transactionFilter.findViewById(R.id.transaction_final_date_filters_text_3);
        transaction_filters_submit_button = transactionFilter.findViewById(R.id.transaction_filters_submit_button);
        transaction_filters_reset_button = transactionFilter.findViewById(R.id.transaction_filters_reset_button);
        transaction_received_amounts_filters_checkbox = transactionFilter.findViewById(R.id.transaction_received_amounts_filters_checkbox);
        transaction_sent_amounts_filters_checkbox = transactionFilter.findViewById(R.id.transaction_sent_amounts_filters_checkbox);

        List<Integer> getYears = UtilityValues.Years();

        int spinnerItem = R.layout.support_simple_spinner_dropdown_item;

        ArrayAdapter<Integer> days = new ArrayAdapter<>(this, spinnerItem, UtilityValues.Days);
        ArrayAdapter<Integer> months = new ArrayAdapter<>(this, spinnerItem, UtilityValues.Months);
        ArrayAdapter<Integer> years = new ArrayAdapter<>(this, spinnerItem, getYears);
        Supplier<Boolean> isAllTrue = () -> filter_minimum_1 && filter_minimum_2 && filter_maximum_1 && filter_maximum_2
                && filter_start_1 && filter_start_2 && filter_start_3 && filter_final_1 && filter_final_2 && filter_final_3;

        transaction_received_amounts_filters_checkbox.setChecked(true);
        transaction_sent_amounts_filters_checkbox.setChecked(true);
        transaction_start_date_filters_text_1.setAdapter(days);
        transaction_final_date_filters_text_1.setAdapter(days);
        transaction_start_date_filters_text_2.setAdapter(months);
        transaction_final_date_filters_text_2.setAdapter(months);
        transaction_start_date_filters_text_3.setAdapter(years);
        transaction_final_date_filters_text_3.setAdapter(years);
        transaction_minimum_amount_filters_text_1.setText("0");
        transaction_minimum_amount_filters_text_2.setText("00");
        transaction_maximum_amount_filters_text_1.setText("0");
        transaction_maximum_amount_filters_text_2.setText("00");
        transaction_start_date_filters_text_1.setText(String.valueOf(days.getItem(0)));
        transaction_start_date_filters_text_2.setText(String.valueOf(months.getItem(0)));
        transaction_start_date_filters_text_3.setText(String.valueOf(years.getItem(0)));
        transaction_final_date_filters_text_1.setText(String.valueOf(days.getItem(0)));
        transaction_final_date_filters_text_2.setText(String.valueOf(months.getItem(0)));
        transaction_final_date_filters_text_3.setText(String.valueOf(years.getItem(0)));

        transaction_filters_submit_button.setEnabled(true);

        if(!transaction_minimum_amount_filters_text_1.getText().toString().isEmpty())
        {
            transaction_minimum_amount_filters_1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_minimum_amount_filters_text_2.getText().toString().isEmpty())
        {
            transaction_minimum_amount_filters_2.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_maximum_amount_filters_text_1.getText().toString().isEmpty())
        {
            transaction_maximum_amount_filters_1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_maximum_amount_filters_text_2.getText().toString().isEmpty())
        {
            transaction_maximum_amount_filters_2.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_start_date_filters_text_1.getText().toString().isEmpty())
        {
            transaction_start_date_filters_1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_start_date_filters_text_2.getText().toString().isEmpty())
        {
            transaction_start_date_filters_2.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_start_date_filters_text_3.getText().toString().isEmpty())
        {
            transaction_start_date_filters_3.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_final_date_filters_text_1.getText().toString().isEmpty())
        {
            transaction_final_date_filters_1.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_final_date_filters_text_2.getText().toString().isEmpty())
        {
            transaction_final_date_filters_2.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        if(!transaction_final_date_filters_text_3.getText().toString().isEmpty())
        {
            transaction_final_date_filters_3.setDefaultHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
        }

        transaction_minimum_amount_filters_text_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_minimum_1 = !s.toString().isEmpty() || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches();

                if(filter_minimum_1)
                {
                    transaction_minimum_amount_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {
                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_minimum_amount_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_minimum_amount_filters_text_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_minimum_2 = !s.toString().isEmpty() || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches();

                if(filter_minimum_2)
                {
                    transaction_minimum_amount_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {
                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_minimum_amount_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_maximum_amount_filters_text_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_maximum_1 = !s.toString().isEmpty() || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches();

                if(filter_maximum_1)
                {
                    transaction_maximum_amount_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {
                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_maximum_amount_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_maximum_amount_filters_text_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_maximum_2 = !s.toString().isEmpty() || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches();

                if(filter_maximum_2)
                {
                    transaction_maximum_amount_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {
                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_maximum_amount_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_start_date_filters_text_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_start_1 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && UtilityValues.Days.contains(Integer.valueOf(s.toString()));

                if(filter_start_1)
                {
                    transaction_start_date_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {

                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_start_date_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_start_date_filters_text_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_start_2 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && UtilityValues.Months.contains(Integer.valueOf(s.toString()));

                if(filter_start_2)
                {
                    transaction_start_date_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {

                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_start_date_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_start_date_filters_text_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_start_3 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && getYears.contains(Integer.valueOf(s.toString()));

                if(filter_start_3)
                {
                    transaction_start_date_filters_3.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {

                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_start_date_filters_3.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_final_date_filters_text_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_final_1 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && UtilityValues.Days.contains(Integer.valueOf(s.toString()));

                if(filter_final_1)
                {
                    transaction_final_date_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {

                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_final_date_filters_1.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_final_date_filters_text_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_final_2 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && UtilityValues.Months.contains(Integer.valueOf(s.toString()));

                if(filter_final_2)
                {
                    transaction_final_date_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {

                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_final_date_filters_2.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });
        transaction_final_date_filters_text_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s)
            {
                filter_final_3 = (!s.toString().isEmpty()
                        || Pattern.compile("^(\\d){1,}$").matcher(s.toString()).matches())
                        && getYears.contains(Integer.valueOf(s.toString()));

                if(filter_final_3)
                {
                    transaction_final_date_filters_3.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                    if(isAllTrue.get())
                    {
                        transaction_filters_submit_button.setEnabled(true);
                    }
                }
                else{
                    transaction_final_date_filters_3.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                    transaction_filters_submit_button.setEnabled(false);
                }
            }
        });

        transaction_filters_get_default_list.setOnClickListener(view ->
        {
            main_page_transactions_text.setText("Last transactions");
            adapter = new WalletLogsAdapter(allLogs);
            main_page_transactions_logs.setAdapter(adapter);
            main_page_wallet.setText(String.format("%s %s", userMoneyCase.get(walletTaken), userCurrency.get(walletTaken)));

            transactionFilter.dismiss();
        });

        transaction_filters_reset_button.setOnClickListener(view ->
        {
            transaction_start_date_filters_text_1.setText(String.valueOf(days.getItem(0)));
            transaction_start_date_filters_text_2.setText(String.valueOf(months.getItem(0)));
            transaction_start_date_filters_text_3.setText(String.valueOf(years.getItem(0)));
            transaction_final_date_filters_text_1.setText(String.valueOf(days.getItem(0)));
            transaction_final_date_filters_text_2.setText(String.valueOf(months.getItem(0)));
            transaction_final_date_filters_text_3.setText(String.valueOf(years.getItem(0)));
            transaction_minimum_amount_filters_text_1.setText("0");
            transaction_minimum_amount_filters_text_2.setText("00");
            transaction_maximum_amount_filters_text_1.setText("0");
            transaction_maximum_amount_filters_text_2.setText("00");
            transaction_received_amounts_filters_checkbox.setChecked(true);
            transaction_received_amounts_filters_checkbox.setChecked(true);
        });

        transaction_filters_submit_button.setOnClickListener(view ->
        {
            boolean getReceivedAmounts = transaction_received_amounts_filters_checkbox.isChecked();
            boolean getSentAmounts = transaction_sent_amounts_filters_checkbox.isChecked();
            double minAmount = Double.parseDouble(
                    String.format("%s.%s",
                            transaction_minimum_amount_filters_text_1.getText().toString(),
                            transaction_minimum_amount_filters_text_2.getText().toString()));
            double maxAmount = Double.parseDouble(
                    String.format("%s.%s",
                            transaction_maximum_amount_filters_text_1.getText().toString(),
                            transaction_maximum_amount_filters_text_2.getText().toString()));
            int startDay = Integer.parseInt(transaction_start_date_filters_text_1.getText().toString());
            int startMonth = Integer.parseInt(transaction_start_date_filters_text_2.getText().toString());
            int startYear = Integer.parseInt(transaction_start_date_filters_text_3.getText().toString());
            int finalDay = Integer.parseInt(transaction_final_date_filters_text_1.getText().toString());
            int finalMonth = Integer.parseInt(transaction_final_date_filters_text_2.getText().toString());
            int finalYear = Integer.parseInt(transaction_final_date_filters_text_3.getText().toString());
            LocalDateTime startDate = LocalDateTime.of(startYear, startMonth, startDay, 0, 0, 0);
            LocalDateTime finalDate = LocalDateTime.of(finalYear, finalMonth, finalDay, 0, 0, 0);

            if(startDate.isAfter(finalDate))
            {
                Toast.makeText(this, "The start date cannot be after final date", Toast.LENGTH_SHORT).show();
            }
            else if(finalDate.isBefore(startDate))
            {
                Toast.makeText(this, "The final date cannot be before start date.", Toast.LENGTH_SHORT).show();
            }
            else if (maxAmount < minAmount)
            {
                Toast.makeText(this, "Maximum amount cannot have less value than minimum amount or " +
                        "minimum amount cannot have more value than maximum amount.", Toast.LENGTH_SHORT).show();
            }
            else {
                ArrayList<Log> logs = new ArrayList<>();

                String[] headerArray = new String[] { "Content Description", "Rest", "Commission", "Date", "Email", "Spend" };
                String[][] subArray = allLogs.stream().map(u ->
                {
                    String[] newArray = new String[] {"", "", "", "", "", ""};
                    newArray[0] = u.getContentDescription();
                    newArray[1] = u.getRest();
                    newArray[2] = u.getCommission();
                    newArray[3] = u.getDate();
                    newArray[4] = u.getEmail();
                    newArray[5] = u.getSpend();
                    return newArray;
                }).toArray(String[][]::new);

                String[][] allArrays = new String[subArray.length][];
                allArrays[0] = headerArray;
                for(int i = 1; i < subArray.length; i++)
                {
                    allArrays[i] = subArray[i];
                }

                List<Log> getReceivedAmountsList = new ArrayList<>();
                List<Log> getSentAmountsList = new ArrayList<>();

                for(int i = 1; i < allArrays.length; i++)
                {
                    Log.Builder log = new Log.Builder();

                    if(i - 1 != 0)
                    {
                        double getBeforeValue = Double.parseDouble(allLogs.get(i - 1).getRest().replace(",","."));
                        double getAfterValue = Double.parseDouble(allLogs.get(i).getRest().replace(",","."));

                        if(getBeforeValue < getAfterValue && getReceivedAmounts)
                        {
                            log.SetCommission(allArrays[i][2])
                                    .SetContentDescription(allArrays[i][0])
                                    .SetDate(allArrays[i][3])
                                    .SetEmail(allArrays[i][4])
                                    .SetRest(allArrays[i][1])
                                    .SetSpend(allArrays[i][5])
                                    .Build();
                            getReceivedAmountsList.add(log.Build());
                        }
                        else if(getBeforeValue > getAfterValue && getSentAmounts)
                        {
                            log.SetCommission(allArrays[i][2])
                                    .SetContentDescription(allArrays[i][0])
                                    .SetDate(allArrays[i][3])
                                    .SetEmail(allArrays[i][4])
                                    .SetRest(allArrays[i][1])
                                    .SetSpend(allArrays[i][5])
                                    .Build();
                            getSentAmountsList.add(log.Build());
                        }
                    }
                    else {
                        log.SetCommission(allArrays[i][2])
                                .SetContentDescription(allArrays[i][0])
                                .SetDate(allArrays[i][3])
                                .SetEmail(allArrays[i][4])
                                .SetRest(allArrays[i][1])
                                .SetSpend(allArrays[i][5])
                                .Build();
                        getReceivedAmountsList.add(log.Build());
                    }
                }

                getReceivedAmountsList.addAll(getSentAmountsList);

                Stream<Log> getMinAndMaxAmountStream = getReceivedAmountsList.stream().filter(x ->
                        Double.parseDouble(x.getRest().replace(",", ".")) >= minAmount &&
                                Double.parseDouble(x.getRest().replace(",", ".")) <= maxAmount
                );
                Stream<Log> getStartAndFinalDateStream = getMinAndMaxAmountStream.filter(x -> {
                   LocalDateTime getDate = LocalDateTime.parse(x.getDate(), DateTimeFormatter.ISO_DATE_TIME);

                   LocalDateTime getStartDateConverted = LocalDateTime.of(
                           getDate.getYear(),
                           getDate.getMonthValue(),
                           getDate.getDayOfMonth(), 0, 0, 0);

                    LocalDateTime getFinalDateConverted = LocalDateTime.of(
                            getDate.getYear(),
                            getDate.getMonthValue(),
                            getDate.getDayOfMonth(), 0, 0, 0);
                    return getFinalDateConverted.isBefore(finalDate) && getStartDateConverted.isAfter(startDate);
                });

                getStartAndFinalDateStream
                        .distinct()
                        .collect(Collectors.toList())
                        .forEach(x ->
                {
                    Log theLog = new Log.Builder()
                            .SetSpend(x.getSpend())
                            .SetRest(x.getRest())
                            .SetEmail(x.getEmail())
                            .SetDate(x.getDate())
                            .SetContentDescription(x.getContentDescription())
                            .SetCommission(x.getCommission())
                            .Build();
                    logs.add(theLog);
                });

                main_page_transactions_text.setText(String.format("Last transactions, %d of results", logs.size()));
                adapter = new WalletLogsAdapter(logs);
                main_page_transactions_logs.setAdapter(adapter);
                main_page_wallet.setText(String.format("%s %s", userMoneyCase.get(walletTaken), userCurrency.get(walletTaken)));

                transactionFilter.dismiss();
            }
        });

        transactionFilter.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void openUserImageAvatarList()
    {
        final Dialog imageChange = new Dialog(this);
        imageChange.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageChange.setContentView(R.layout.dialog_user_image_change);

        imageChange.setCanceledOnTouchOutside(false);

        RecyclerView dialog_user_avatar_image_list = imageChange.findViewById(R.id.dialog_user_avatar_image_list);
        LinearLayout dialog_user_image_change_loading_container = imageChange.findViewById(R.id.dialog_user_image_change_loading_container);
        LinearLayout dialog_user_image_change_loaded_container = imageChange.findViewById(R.id.dialog_user_image_change_loaded_container);
        ArrayList<AccountImages> paths = new ArrayList<>();

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            Task<ListResult> listResult = FirebaseStorage.getInstance().getReference().child("AvatarImages").listAll()
                    .addOnCompleteListener(task -> {

                    })
                    .addOnFailureListener(Throwable::printStackTrace)
                    .addOnCanceledListener(() -> {

                    })
                    .addOnSuccessListener(images ->
                    {
                        List<StorageReference> ref = images.getItems();
                        Thread tr = new Thread(() ->
                        {
                            ref.forEach(x ->
                            {
                                Task<Uri> loadImage = x.getDownloadUrl();

                                final Uri[] uri = {null};
                                try
                                {
                                    uri[0] = Tasks.await(loadImage);
                                }
                                catch (ExecutionException | InterruptedException e)
                                {
                                    e.printStackTrace();
                                }

                                AccountImages acc = new AccountImages();
                                acc.setImagePath(uri[0]);
                                paths.add(acc);
                            });

                            runOnUiThread(() -> {
                                adapter2 = new AvatarChooserMenuAdapter(this, paths, imageChange,
                                        main_page_transactions_profile_image);
                                GridLayoutManager manager = new GridLayoutManager(imageChange.getContext(), 3);
                                dialog_user_avatar_image_list.setLayoutManager(manager);
                                dialog_user_avatar_image_list.setAdapter(adapter2);
                                dialog_user_image_change_loading_container.setVisibility(View.GONE);
                                dialog_user_image_change_loaded_container.setVisibility(View.VISIBLE);
                            });
                        });

                        tr.start();
                        while(tr.isAlive())
                        {
                            System.out.println("Waiting...");
                        }
                    });

            Tasks.whenAll(listResult).addOnCompleteListener(res ->
            {

            });
        }
        else {
            Task<AuthResult> auth =
            mAuth.signInAnonymously()
                    .addOnSuccessListener(this, authResult ->
                    {
                        Task<ListResult> listResult = FirebaseStorage.getInstance().getReference().child("AvatarImages").listAll()
                                .addOnCompleteListener(task -> {

                                })
                                .addOnFailureListener(Throwable::printStackTrace)
                                .addOnCanceledListener(() -> {

                                })
                                .addOnSuccessListener(images ->
                                {
                                    List<StorageReference> ref = images.getItems();
                                    Thread tr = new Thread(() ->
                                    {
                                        ref.forEach(x ->
                                        {
                                            Task<Uri> loadImage = x.getDownloadUrl();

                                            final Uri[] uri = {null};
                                            try
                                            {
                                                uri[0] = Tasks.await(loadImage);
                                            }
                                            catch (ExecutionException | InterruptedException e)
                                            {
                                                e.printStackTrace();
                                            }

                                            AccountImages acc = new AccountImages();
                                            acc.setImagePath(uri[0]);
                                            paths.add(acc);
                                        });

                                        runOnUiThread(() -> {
                                            adapter2 = new AvatarChooserMenuAdapter(this, paths, imageChange,
                                                    main_page_transactions_profile_image);
                                            GridLayoutManager manager = new GridLayoutManager(imageChange.getContext(), 3);
                                            dialog_user_avatar_image_list.setLayoutManager(manager);
                                            dialog_user_avatar_image_list.setAdapter(adapter2);
                                            dialog_user_image_change_loading_container.setVisibility(View.GONE);
                                            dialog_user_image_change_loaded_container.setVisibility(View.VISIBLE);
                                        });
                                    });

                                    tr.start();
                                    while(tr.isAlive())
                                    {
                                        System.out.println("Waiting...");
                                    }
                                });

                        Tasks.whenAll(listResult).addOnCompleteListener(res ->
                        {

                        });
                    })
                    .addOnFailureListener(this, Throwable::printStackTrace);
            Tasks.whenAll(auth).addOnSuccessListener(v ->
            {

            });
        }
        dialog_user_image_change_loading_container.setVisibility(View.VISIBLE);
        dialog_user_image_change_loaded_container.setVisibility(View.GONE);
        imageChange.show();
    }
}