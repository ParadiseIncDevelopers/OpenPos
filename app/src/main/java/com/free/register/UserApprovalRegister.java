package com.free.register;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.free.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import static com.free.NetworkChangeReceiver.NetworkCallback;

import java.util.regex.Pattern;

public class UserApprovalRegister extends AppCompatActivity
{
    //private Uri frontUri, backUri;
    private Button user_approval_submit_button, user_approval_phone_submit_button;
    private TextInputLayout user_approval_phone_text, user_approval_8digit_text;
    private TextInputEditText user_approval_phone_text_field, user_approval_8digit_text_field;
    private LinearLayout user_approval_8digit_container;

    private String eightDigitText;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_approval_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () ->
        {
            user_approval_submit_button = findViewById(R.id.user_approval_submit_button);
            user_approval_phone_submit_button = findViewById(R.id.user_approval_phone_submit_button);
            user_approval_phone_text = findViewById(R.id.user_approval_phone_text);
            user_approval_8digit_text = findViewById(R.id.user_approval_8digit_text);
            user_approval_phone_text_field = findViewById(R.id.user_approval_phone_text_field);
            user_approval_8digit_text_field = findViewById(R.id.user_approval_8digit_text_field);
            user_approval_8digit_container = findViewById(R.id.user_approval_8digit_container);

            user_approval_phone_text_field.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void afterTextChanged(Editable s)
                {
                    if (Pattern.compile("^\\+[0-9]+").matcher(s.toString()).matches())
                    {
                        user_approval_phone_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#558B2F")));
                        user_approval_phone_submit_button.setEnabled(true);
                    }
                    else {
                        user_approval_phone_text.setHintTextColor(ColorStateList.valueOf(Color.parseColor("#E64A19")));
                        user_approval_phone_submit_button.setEnabled(false);
                    }

                    eightDigitText = s.toString();
                }
            });

            user_approval_phone_submit_button.setOnClickListener(view ->
            {
                String phoneNumber = user_approval_phone_text_field.getText().toString();
                sendRandom8DigitCode(phoneNumber);
            });
        });
    }

    private void sendRandom8DigitCode(String phoneNumber) {

    }
}
/*private final ActivityResultLauncher<Intent> frontGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                {
                    frontUri = result.getData().getData();

                    InputStream input = null;
                    try {
                        input = this.getContentResolver().openInputStream(frontUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    ExifInterface ei = null;
                    if (Build.VERSION.SDK_INT > 23) {
                        try {
                            ei = new ExifInterface(input);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            ei = new ExifInterface(frontUri.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Matrix matrix = new Matrix();
                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(UserApprovalRegister.this.getApplicationContext().getContentResolver(), frontUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Bitmap rotatedImg = null;
                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            matrix.postRotate(90);
                            rotatedImg = Bitmap.createBitmap(img, 0, 0,
                                    img.getWidth(),
                                    img.getHeight(),
                                    matrix, true);
                            img.recycle();
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            matrix.postRotate(180);
                            rotatedImg = Bitmap.createBitmap(img, 0, 0,
                                    img.getWidth(),
                                    img.getHeight(),
                                    matrix, true);
                            img.recycle();
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_270:
                            matrix.postRotate(270);
                            rotatedImg = Bitmap.createBitmap(img, 0, 0,
                                    img.getWidth(),
                                    img.getHeight(),
                                    matrix, true);
                            img.recycle();
                            break;
                    }

                    user_approval_image_id_front.setImageBitmap(rotatedImg);
                }
            });*/
/*user_approval_image_id_front.setOnClickListener(view ->
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                frontGalleryLauncher.launch(intent);
            });*/
/*FirebaseStorage storage = FirebaseStorage.getInstance("gs://openpos-3e0d3-approval/");
                StorageReference reference = storage.getReference();
                Object id = getIntent().getExtras().get("id");
                StorageReference ref1 = reference.child(id.toString()).child("Approval").child("Front");
                StorageReference ref2 = reference.child(id.toString()).child("Approval").child("Back");*/
/*try
                {

                    if(frontUri != null || backUri != null)
                    {
                        ref1.putFile(frontUri)
                                .addOnSuccessListener(taskSnapshot -> Toast.makeText(this, "Front image is loaded.", Toast.LENGTH_SHORT).show())
                                .addOnProgressListener(snapshot -> Toast.makeText(this, "Front image is loading...", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Front image is not loaded.", Toast.LENGTH_SHORT).show());
                        ref2.putFile(backUri).addOnSuccessListener(taskSnapshot -> Toast.makeText(this, "Back image is loaded.", Toast.LENGTH_SHORT).show())
                                .addOnProgressListener(snapshot -> Toast.makeText(this, "Back image is loading...", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Back image is not loaded.", Toast.LENGTH_SHORT).show());

                        Map<String, Object> approved = new HashMap<>();
                        Map<String, Map<String, Object>> AccountApproved = new HashMap<>();
                        approved.put("isUserAccountBlocked", true);
                        approved.put("isUserActionsBlocked", true);
                        approved.put("isUserApprovalInProgress", true);
                        AccountApproved.put(id.toString(), approved);

                        FirebaseDatabase.getInstance("https://openpos-userstatus.europe-west1.firebasedatabase.app/")
                                .getReference()
                                .child("Users")
                                .setValue(AccountApproved);

                        Intent intent = new Intent(UserApprovalRegister.this, ApprovalInProgress.class);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(this, "Uri cannot be null.", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }*/