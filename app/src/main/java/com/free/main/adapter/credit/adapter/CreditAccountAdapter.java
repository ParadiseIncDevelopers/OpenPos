package com.free.main.adapter.credit.adapter;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.models.account.UserSavedAccounts;
import com.models.user.AccountImages;
import com.free.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CreditAccountAdapter extends RecyclerView.Adapter<CreditAccountAdapter.CreditAccountHolder>
{
    private final ArrayList<UserSavedAccounts> users;

    public CreditAccountAdapter(ArrayList<UserSavedAccounts> users)
    {
        this.users = users;

    }

    @NonNull
    @NotNull
    @Override
    public CreditAccountAdapter.CreditAccountHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_account_recycler_view_adapter, parent, false);
        return new CreditAccountAdapter.CreditAccountHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull @NotNull CreditAccountAdapter.CreditAccountHolder holder, int position)
    {
        holder.credit_account_RecyclerView_text_1.setText(users.get(position).getEmail());
        holder.credit_account_RecyclerView_text_2.setText(users.get(position).getNameSurname());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class CreditAccountHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final ConstraintLayout credit_account_RecyclerView_button_1;
        private final TextView credit_account_RecyclerView_text_1,credit_account_RecyclerView_text_2;
        private final ShapeableImageView credit_account_RecyclerView_logo_1;
        public CreditAccountHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            credit_account_RecyclerView_button_1 = itemView.findViewById(R.id.credit_account_RecyclerView_button_1) ;
            credit_account_RecyclerView_text_1 = itemView.findViewById(R.id.credit_account_RecyclerView_text_1);
            credit_account_RecyclerView_logo_1 = itemView.findViewById(R.id.credit_account_RecyclerView_logo_1);
            credit_account_RecyclerView_text_2 = itemView.findViewById(R.id.credit_account_RecyclerView_text_2);
        }

        @Override
        public void onClick(View view) {

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
