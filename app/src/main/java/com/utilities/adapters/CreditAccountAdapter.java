package com.utilities.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.free.AccountImages;
import com.free.R;
import com.google.firebase.storage.FirebaseStorage;
import com.utilities.classes.EncryptorClass;
import org.jetbrains.annotations.NotNull;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;
import static com.utilities.classes.UserUtility.userEmail;
import static com.utilities.classes.UserUtility.userImageUri;

public class CreditAccountAdapter extends RecyclerView.Adapter<CreditAccountAdapter.CreditAccountHolder>
{
    ArrayList<AccountImages> images;

    public CreditAccountAdapter(Activity context, ArrayList<AccountImages> images, Dialog dialog, ImageView profileImage)
    {
    }

    @NonNull
    @NotNull
    @Override
    public CreditAccountAdapter.CreditAccountHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avatar_image_chooser_recycler_view, parent, false);
        return new CreditAccountAdapter.CreditAccountHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull CreditAccountAdapter.CreditAccountHolder holder, int position)
    {

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class CreditAccountHolder extends RecyclerView.ViewHolder
    {
        public CreditAccountHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
        }

        /*@RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View view)
        {
            Bitmap image = ((BitmapDrawable) ((ImageView) view).getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            try
            {
                FirebaseStorage.getInstance().
                        getReference(EncryptorClass.setSecurePassword(userEmail))
                        .child("ProfileImage")
                        .putBytes(byteArray)
                        .addOnCompleteListener(task ->
                        {
                            dialog.dismiss();
                            Activity currentAct = context;
                            Intent intent = currentAct.getIntent();
                            intent.putExtra("Email", userEmail);

                                FirebaseStorage.getInstance()
                                        .getReference(EncryptorClass.setSecurePassword(userEmail))
                                        .child("ProfileImage")
                                        .getDownloadUrl()
                                        .addOnFailureListener(Throwable::printStackTrace)
                                        .addOnCompleteListener(task1 ->
                                        {
                                            Glide.with(context).load(userImageUri).into(main_page_transactions_profile_image);
                                        })
                                        .addOnSuccessListener(ts ->
                                        {
                                            userImageUri = ts;
                                        });

                            context.startActivity(intent);
                        })
                        .addOnProgressListener(task ->
                        {

                        })
                        .addOnSuccessListener(task -> {

                        })
                        .addOnFailureListener(Throwable::printStackTrace);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

        }*/
    }
}
