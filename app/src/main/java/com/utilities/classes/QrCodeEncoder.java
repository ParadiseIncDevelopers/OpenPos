package com.utilities.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QrCodeEncoder
{
    private String encoderString;
    private Bitmap theBitmap;
    private Context context;

    public String getEncoderString() {
        return encoderString;
    }
    public void setEncoderString(String encoderString) {
        this.encoderString = encoderString;
    }

    public Bitmap getTheBitmap() {
        return theBitmap;
    }
    public void setTheBitmap(Bitmap theBitmap) {
        this.theBitmap = theBitmap;
    }

    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public static class Builder
    {
        private String encoderString;
        private Bitmap theBitmap;
        private Context context;

        public Builder setEncoderString(String encoderString)
        {
            this.encoderString = encoderString;
            return this;
        }

        public Builder setTheBitmap(Bitmap theBitmap)
        {
            this.theBitmap = theBitmap;
            return this;
        }

        public Builder setContext(Context context)
        {
            this.context = context;
            return this;
        }

        public QrCodeEncoder Build()
        {
            QrCodeEncoder encoder = new QrCodeEncoder();
            encoder.encoderString = this.encoderString;
            encoder.theBitmap = this.theBitmap;
            encoder.context = this.context;
            return encoder;
        }
    }

    public void SetQrCode(@NonNull ImageView imageView) throws WriterException
    {
        MultiFormatWriter writer = new MultiFormatWriter();

        BitMatrix bm = writer.encode(encoderString, BarcodeFormat.CODE_128,150, 150);
        Bitmap ImageBitmap = Bitmap.createBitmap(180, 40, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < 180; i++) {//width
            for (int j = 0; j < 40; j++) {//height
                ImageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK: Color.WHITE);
            }
        }

        imageView.setImageBitmap(ImageBitmap);
    }

    private QrCodeEncoder()
    {

    }
}
