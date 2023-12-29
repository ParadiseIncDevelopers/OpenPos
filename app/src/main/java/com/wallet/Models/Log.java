package com.wallet.Models;

import android.os.Build;
import androidx.annotation.RequiresApi;
import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Log {
    private String senderId;
    private String receiverId;
    private String senderEmail;
    private String receiverEmail;
    private String contentDescription;
    private LocalDateTime date;
    private double debit;
    private double credit;
    private double commission;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public JSONObject toJsonObject()
    {
        JSONObject jsonObject = new JSONObject();
        try
        {
            jsonObject.put("senderId", senderId);
            jsonObject.put("receiverId", receiverId);
            jsonObject.put("senderEmail", senderEmail);
            jsonObject.put("receiverEmail", receiverEmail);
            jsonObject.put("contentDescription", contentDescription);
            jsonObject.put("date", date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)); // Format date to string
            jsonObject.put("debit", debit);
            jsonObject.put("credit", credit);
            jsonObject.put("commission", commission);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public double getCommission() {
        return commission;
    }

    public double getCredit() {
        return credit;
    }

    public double getDebit() {
        return debit;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderId() {
        return senderId;
    }

    public static class Builder {
        private String senderId;
        private String receiverId;
        private String senderEmail;
        private String receiverEmail;
        private String contentDescription;
        private LocalDateTime date;
        private double debit;
        private double credit;
        private double commission;

        public Builder setSenderId(String senderId) {
            this.senderId = senderId;
            return this;
        }

        public Builder setReceiverId(String receiverId) {
            this.receiverId = receiverId;
            return this;
        }

        public Builder setSenderEmail(String senderEmail) {
            this.senderEmail = senderEmail;
            return this;
        }

        public Builder setReceiverEmail(String receiverEmail) {
            this.receiverEmail = receiverEmail;
            return this;
        }

        public Builder setContentDescription(String contentDescription) {
            this.contentDescription = contentDescription;
            return this;
        }

        public Builder setDate(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder setDebit(double debit) {
            this.debit = debit;
            return this;
        }

        public Builder setCredit(double credit) {
            this.credit = credit;
            return this;
        }

        public Builder setCommission(double commission) {
            this.commission = commission;
            return this;
        }

        public Log build() {
            Log log = new Log();
            log.senderId = this.senderId;
            log.receiverId = this.receiverId;
            log.senderEmail = this.senderEmail;
            log.receiverEmail = this.receiverEmail;
            log.contentDescription = this.contentDescription;
            log.date = this.date;
            log.debit = this.debit;
            log.credit = this.credit;
            log.commission = this.commission;
            return log;
        }
    }

    private Log() {
        // Private constructor
    }
}
