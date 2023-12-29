package com.wallet;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.free.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.WriterException;
import com.utilities.classes.EncryptorClass;
import com.utilities.QrCodeEncoder;
import org.jetbrains.annotations.NotNull;
import static com.free.NetworkChangeReceiver.NetworkCallback;
import static com.utilities.classes.UserUtility.userEmail;

public class WalletStatsPrediction extends AppCompatActivity {

    private ImageView send_page_qr_code;
    private TextView send_money_credentials_id, send_money_wallet_case;
    private Bitmap bitmap;
    private String encryptionKey;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_stats_prediction);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            send_page_qr_code = findViewById(R.id.send_page_qr_code);
            send_money_wallet_case = findViewById(R.id.send_money_wallet_case);
            send_money_credentials_id = findViewById(R.id.send_money_credentials_id);

            runOnUiThread(() -> {
                QrCodeEncoder encoder = new QrCodeEncoder.Builder()
                        .setTheBitmap(bitmap)
                        .setContext(WalletStatsPrediction.this)
                        .setEncoderString("wallet_key:" + walletTaken + "<->" + "user_email:" + userEmail)
                        .Build();
                try {
                    encoder.SetQrCode(send_page_qr_code);
                } catch (WriterException e) {
                    throw new RuntimeException(e);
                }

                FirebaseDatabase.getInstance("https://openpos-wallets.europe-west1.firebasedatabase.app/")
                        .getReference(EncryptorClass.setSecurePassword(userEmail))
                        .child(walletTaken)
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot)
                            {
                                send_money_wallet_case.setText(String.format("In your case you have : %s %s", snapshot.child("MoneyCase").getValue().toString(), snapshot.child("Currency").getValue().toString()));
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
            });
            send_money_credentials_id.setText("Please send money from this qr code...");
        });
    }
}