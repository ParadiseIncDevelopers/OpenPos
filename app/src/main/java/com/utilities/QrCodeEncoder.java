package com.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import com.google.zxing.WriterException;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import static android.content.Context.WINDOW_SERVICE;

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

    public void SetQrCode(ImageView imageView, WindowManager manager)
    {
        Display display = manager.getDefaultDisplay();
        Point point = new Point();
        display.getSize(point);
        int width = point.x;
        int height = point.y;
        int dimen = Math.min(width, height);
        dimen = dimen * 3 / 4;

        QRGEncoder qrgEncoder = new QRGEncoder(getEncoderString(), null, QRGContents.Type.TEXT, dimen);
        try
        {
            setTheBitmap(qrgEncoder.encodeAsBitmap());
            imageView.setImageBitmap(getTheBitmap());
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }

    private QrCodeEncoder()
    {

    }
}
