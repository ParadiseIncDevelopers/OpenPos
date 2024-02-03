package com.free.main.adapter;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.free.R;
import com.models.logs.Log;
import com.models.structs.Stack;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.stream.Collectors;

public class WalletLogsAdapter extends RecyclerView.Adapter<WalletLogsAdapter.WalletLogsHolder>
{
    private static Stack<Log> stack = new Stack<>();
    private static int mainSize;

    public WalletLogsAdapter(List<Log> logs)
    {
        stack.fullyPush(logs);
        mainSize = stack.size();
    }
    
    @NonNull
    @NotNull
    @Override
    public WalletLogsHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wallet_logs_recycler_view, parent, false);
        return new WalletLogsHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull @NotNull WalletLogsAdapter.WalletLogsHolder holder, int position)
    {
        //TODO : 1-stacks to be controlled by checking sizes.

        Log index = stack.pop();

        if(index.getDebit() == 0)
        {
            holder.wallet_logs_recycler_text_1.setText("Credit");
            holder.wallet_logs_recycler_text_2.setText(String.valueOf(index.getCredit()));
        }
        else{
            holder.wallet_logs_recycler_text_1.setText("Debit");
            holder.wallet_logs_recycler_text_2.setText(String.valueOf(index.getDebit()));
        }
        holder.wallet_logs_recycler_text_3.setText(String.format(index.getId()));
        holder.wallet_logs_recycler_component.setOnClickListener(view -> {

        });
    }

    @Override
    public int getItemCount() {
        return mainSize;
    }

    public static class WalletLogsHolder extends RecyclerView.ViewHolder
    {
        private final TextView wallet_logs_recycler_text_1;
        private final TextView wallet_logs_recycler_text_2;
        private final TextView wallet_logs_recycler_text_3;
        private final ConstraintLayout wallet_logs_recycler_component;

        public WalletLogsHolder(@NonNull @NotNull View itemView)
        {
            super(itemView);
            wallet_logs_recycler_component = itemView.findViewById(R.id.wallet_logs_recycler_component);
            wallet_logs_recycler_text_1 = itemView.findViewById(R.id.wallet_logs_recycler_text_1);
            wallet_logs_recycler_text_2 = itemView.findViewById(R.id.wallet_logs_recycler_text_2);
            wallet_logs_recycler_text_3 = itemView.findViewById(R.id.wallet_logs_recycler_text_3);
        }

        /*@RequiresApi(api = Build.VERSION_CODES.N)
        @SuppressLint("SetJavaScriptEnabled")
        @Override
        public boolean onLongClick(View view)
        {
            Dialog dialog = new Dialog(itemView.getContext());
            dialog.requestWindowFeature(FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_wallet_logs_printer_screen);

            WebView dialog_wallet_logs_webpage = new WebView(itemView.getContext());
            LinearLayout dialog_wallet_webview_container = dialog.findViewById(R.id.dialog_wallet_webview_container);
            Button dialog_wallet_logs_send_email_button = dialog.findViewById(R.id.dialog_wallet_logs_send_email_button);

            dialog_wallet_logs_webpage.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return false;
                }
            });

            String html_code =
                    "<html>" +
                    "<head>" +
                    "    <link rel='stylesheet' href='https://www.w3schools.com/w3css/4/w3.css'>" +
                    "    <style>"  +
                    "    </style>" +
                    "</head>" +
                    "<body>" +
                    "    <div class='w3-container w3-padding-16'>" +
                    "        <div class='w3-padding-16 w3-container w3-border w3-border-red'>" +
                    "            <div class='w3-padding'>Email : " + wallet_logs_email.getText().toString() +"</div>" +
                    "            <div class='w3-padding'>Send : " + wallet_logs_spend.getText().toString() + " " + userCurrency.get(walletTaken) + "</div>" +
                    "            <div class='w3-padding'>Rest : " + wallet_logs_rest.getText().toString() + " " + userCurrency.get(walletTaken) +"</div>" +
                    "            <div class='w3-padding'>Date : " + wallet_logs_date.getText().toString() + "</div>" +
                    "            <div class='w3-padding'>Description : " + wallet_logs_description.getText().toString() + "</div>" +
                    "        </div>" +
                    "    </div>" +
                    "</body>" +
                    "</html>";

            dialog_wallet_logs_webpage.loadData(html_code,"text/html; charset=UTF-8", null);

            dialog_wallet_logs_send_email_button.setOnClickListener(v ->
            {
                final String username = "yusufsalimozbek@gmail.com";
                final String password = "kohm xprm rfup ekyp";

                Properties prop = new Properties();
                prop.put("mail.smtp.host", "smtp.gmail.com");
                prop.put("mail.smtp.port", "465");
                prop.put("mail.smtp.auth", "true");
                prop.put("mail.smtp.socketFactory.port", "465");
                prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

                Session session = Session.getInstance(prop,
                        new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(username, password);
                            }
                        });

                String theHtmlCode = "<html>" +
                        "<head>" +
                        "    <link rel='stylesheet' href='https://www.w3schools.com/w3css/4/w3.css'>" +
                        "    <style>"  +
                        "    </style>" +
                        "</head>" +
                        "<body>" +
                        "    <div class='w3-container w3-padding-16'>" +
                        "        <div class='w3-padding-16 w3-container w3-border w3-border-red'>" +
                        "            <div class='w3-padding'>Email : " + wallet_logs_email.getText().toString() +"</div>" +
                        "            <div class='w3-padding'>Send : " + wallet_logs_spend.getText().toString() + " " + userCurrency.get(walletTaken) + "</div>" +
                        "            <div class='w3-padding'>Rest : " + wallet_logs_rest.getText().toString() + " " + userCurrency.get(walletTaken) + "</div>" +
                        "            <div class='w3-padding'>Date : " + wallet_logs_date.getText().toString() + "</div>" +
                        "            <div class='w3-padding'>Description : " + wallet_logs_description.getText().toString() + "</div>" +
                        "        </div>" +
                        "    </div>" +
                        "</body>" +
                        "</html>";

                FirebaseStorage.getInstance()
                        .getReference()
                        .child(EncryptorClass.setSecurePassword(userEmail))
                        .child("Transactions")
                        .child("LastTransactionPrinted.html")
                        .putBytes(theHtmlCode.getBytes())
                        .addOnCompleteListener(x -> {

                        })
                        .addOnFailureListener(Throwable::printStackTrace)
                        .addOnSuccessListener(x ->
                        {
                            Toast.makeText(itemView.getContext(), "Uploaded.", Toast.LENGTH_SHORT).show();

                            FirebaseStorage.getInstance()
                                    .getReference()
                                    .child(EncryptorClass.setSecurePassword(userEmail))
                                    .child("Transactions")
                                    .child("LastTransactionPrinted.html").getDownloadUrl()
                                    .addOnFailureListener(Throwable::printStackTrace)
                                    .addOnCompleteListener(y -> { })
                                    .addOnSuccessListener(y ->
                                    {
                                        try
                                        {
                                            Message message = new MimeMessage(session);
                                            message.setSubject("Here is your transaction from OpenPos.");
                                            message.setFrom(new InternetAddress("yusufsalimozbek@gmail.com"));
                                            message.setRecipients(
                                                    Message.RecipientType.TO,
                                                    InternetAddress.parse(wallet_logs_email.getText().toString())
                                            );
                                            BodyPart messageBodyPart = new MimeBodyPart();
                                            messageBodyPart.setText("Dear User,\n\n\n\nYour transaction is sent to your account via your contact email.\n\n\nBest regards,\nOpenPos");
                                            Multipart multipart = new MimeMultipart();
                                            multipart.addBodyPart(messageBodyPart);
                                            messageBodyPart = new MimeBodyPart();
                                            String filename = "YourTransaction.html";

                                            DataSource source = new ByteArrayDataSource(theHtmlCode.getBytes(), "text/html");
                                            messageBodyPart.setDataHandler(new DataHandler(source));
                                            messageBodyPart.setFileName(filename);
                                            messageBodyPart.setHeader("MyContent", "MyAttachment");
                                            multipart.addBodyPart(messageBodyPart);
                                            message.setContent(multipart);

                                            Thread tr1 = new Thread(() ->
                                            {
                                                try {
                                                    Transport.send(message);
                                                } catch (MessagingException e) {
                                                    e.printStackTrace();
                                                }
                                            });

                                            tr1.start();
                                            try {
                                                tr1.join();
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            dialog.dismiss();
                                        }
                                        catch (MessagingException e) {
                                            e.printStackTrace();
                                        }
                                    }).addOnCanceledListener(() -> {

                            });
                        })
                        .addOnCanceledListener(() -> {

                        });
            });

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog_wallet_webview_container.addView(dialog_wallet_logs_webpage, params);
            dialog.show();
            return false;
        }*/
    }
}