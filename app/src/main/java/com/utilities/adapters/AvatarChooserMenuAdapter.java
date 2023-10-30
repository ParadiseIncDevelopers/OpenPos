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

import static com.free.MainPage.main_page_transactions_profile_image;
import static com.utilities.classes.LoginFactoryClass.userEmail;
import static com.utilities.classes.LoginFactoryClass.userImageUri;

public class AvatarChooserMenuAdapter extends RecyclerView.Adapter<AvatarChooserMenuAdapter.AvatarChooserMenuHolder>
{

    private final ArrayList<AccountImages> images;
    private static Dialog dialog;
    @SuppressLint("StaticFieldLeak")
    private static ImageView profileImage;
    @SuppressLint("StaticFieldLeak")
    private static Activity context;

    public AvatarChooserMenuAdapter(Activity context, ArrayList<AccountImages> images, Dialog dialog, ImageView profileImage)
    {
        AvatarChooserMenuAdapter.context = context;
        this.images = images;
        AvatarChooserMenuAdapter.dialog = dialog;
        AvatarChooserMenuAdapter.profileImage = profileImage;
    }

    @NonNull
    @NotNull
    @Override
    public AvatarChooserMenuAdapter.AvatarChooserMenuHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.avatar_image_chooser_recycler_view, parent, false);
        return new AvatarChooserMenuAdapter.AvatarChooserMenuHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull AvatarChooserMenuAdapter.AvatarChooserMenuHolder holder, int position)
    {
        try
        {
            Glide.with(context)
                    .load(new URL(images.get(position).getImagePath().toString()).toString())
                    .into(holder.avatar_image_chooser_image);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public static class AvatarChooserMenuHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final ImageView avatar_image_chooser_image;

        public AvatarChooserMenuHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            avatar_image_chooser_image = itemView.findViewById(R.id.avatar_image_chooser_image);
            avatar_image_chooser_image.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
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

        }
    }
}
