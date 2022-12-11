package com.free;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.exifinterface.media.ExifInterface;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.utilities.classes.EncryptorClass;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.free.NetworkChangeReceiver.NetworkCallback;

public class UserApprovalRegister extends AppCompatActivity
{
    private ImageView user_approval_image_id_front, user_approval_image_id_back;
    private Uri frontUri, backUri;
    private Button user_approval_submit_button;

    private final ActivityResultLauncher<Intent> frontGalleryLauncher = registerForActivityResult(
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
            });

    private final ActivityResultLauncher<Intent> backGalleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null)
                {
                    backUri = result.getData().getData();

                    InputStream input = null;
                    try {
                        input = this.getContentResolver().openInputStream(backUri);
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
                            ei = new ExifInterface(backUri.getPath());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
                    Matrix matrix = new Matrix();
                    Bitmap img = null;
                    try {
                        img = MediaStore.Images.Media.getBitmap(UserApprovalRegister.this.getApplicationContext().getContentResolver(), backUri);
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

                    user_approval_image_id_back.setImageBitmap(rotatedImg);
                }
            });

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_approval_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        NetworkCallback(this, () -> {
            user_approval_image_id_front = findViewById(R.id.user_approval_image_id_front);
            user_approval_image_id_back = findViewById(R.id.user_approval_image_id_back);
            user_approval_submit_button = findViewById(R.id.user_approval_submit_button);

            user_approval_image_id_front.setOnClickListener(view ->
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                frontGalleryLauncher.launch(intent);
            });

            user_approval_image_id_back.setOnClickListener(view ->
            {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                backGalleryLauncher.launch(intent);
            });

            user_approval_submit_button.setOnClickListener(view ->
            {
                FirebaseStorage storage = FirebaseStorage.getInstance("gs://openpos-3e0d3-approval/");
                StorageReference reference = storage.getReference();

                StorageReference ref1 = reference.child(getIntent().getExtras().get("Email").toString()).child("Approval").child("Front");
                StorageReference ref2 = reference.child(getIntent().getExtras().get("Email").toString()).child("Approval").child("Back");

                try
                {

                    if(frontUri != null || backUri != null)
                    {
                        StorageTask<UploadTask.TaskSnapshot> frontUpload = ref1.putFile(frontUri).addOnSuccessListener(taskSnapshot -> Toast.makeText(this, "Front image is loaded.", Toast.LENGTH_SHORT).show())
                                .addOnProgressListener(snapshot -> Toast.makeText(this, "Front image is loading...", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Front image is not loaded.", Toast.LENGTH_SHORT).show());
                        StorageTask<UploadTask.TaskSnapshot> backUpload = ref2.putFile(backUri).addOnSuccessListener(taskSnapshot -> Toast.makeText(this, "Back image is loaded.", Toast.LENGTH_SHORT).show())
                                .addOnProgressListener(snapshot -> Toast.makeText(this, "Back image is loading...", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e -> Toast.makeText(this, "Back image is not loaded.", Toast.LENGTH_SHORT).show());

                        Map<String, Object> approved = new HashMap<>();
                        Map<String, Map<String, Object>> AccountApproved = new HashMap<>();
                        approved.put("isUserAccountBlocked", true);
                        approved.put("isUserActionsBlocked", true);
                        approved.put("isUserApprovalInProgress", true);
                        AccountApproved.put(EncryptorClass.setSecurePassword(getIntent().getExtras().get("Email").toString()), approved);

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
                }
            });
        });
    }
}