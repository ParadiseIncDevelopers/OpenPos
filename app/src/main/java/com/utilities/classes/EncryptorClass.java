package com.utilities.classes;

import android.Manifest;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import static android.content.Context.KEYGUARD_SERVICE;
import static androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON;

import com.google.firebase.database.DataSnapshot;

import org.jetbrains.annotations.Contract;

public class EncryptorClass
{
    private static final String SECRET_KEY = "0A2B24A5A9B102C500FE532DBD3F5DC8";
    private static final String SALTVALUE = "F8C699DA740449802C4C0B4869A6F4C5";

    @NonNull
    public static String generateSaltKey(int length) {
        if (length <= 0 || length > 32) {
            throw new IllegalArgumentException("Invalid salt key length");
        }

        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[length / 2];
        secureRandom.nextBytes(salt);

        return bytesToHex(salt);
    }

    @NonNull
    public static String generateSecretKey(int length) {
        if (length <= 0 || length > 32) {
            throw new IllegalArgumentException("Invalid secret key length");
        }

        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(length * 8); // Key length in bits
            SecretKey secretKey = keyGen.generateKey();

            byte[] encodedKey = secretKey.getEncoded();
            return bytesToHex(encodedKey);
        } catch (Exception e) {
            throw new RuntimeException("Error generating secret key", e);
        }
    }

    @NonNull
    private static String bytesToHex(@NonNull byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Encrypt(String text, String secretKey, String saltValue)
    {
        byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        IvParameterSpec ivspec = new IvParameterSpec(iv);
        SecretKeyFactory factory = null;
        try {
            factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltValue.getBytes(), 65536, 256);
        SecretKey tmp = null;
        try {
            if (factory != null) {
                tmp = factory.generateSecret(spec);
            }
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        SecretKeySpec secretKeyspec = null;
        if (tmp != null) {
            secretKeyspec = new SecretKeySpec(tmp.getEncoded(), "AES");
        }
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        try {
            if (cipher != null) {
                cipher.init(Cipher.ENCRYPT_MODE, secretKeyspec, ivspec);
            }
        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }

        if (cipher != null) {
            try {
                return Base64.getEncoder()
                        .encodeToString(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)));
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                e.printStackTrace();
            }
        }
        else{
            return null;
        }
        return null;
    }

    @Nullable
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String Decrypt(String text, String secretKey, String saltKey)
    {
        try
        {
            /* Declare a byte array. */
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), saltKey.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec theSecretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, theSecretKey, ivspec);

            return new String(cipher.doFinal(Base64.getDecoder().decode(text)));
        }
        catch (InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException
                | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e)
        {
            e.printStackTrace();
        }
        return null;

    }

    @Contract("_ -> !null")
    public static String getSaltKey(@NonNull DataSnapshot x)
    {
        return x.child("Email").child("Salt").getValue(String.class);
    }

    public static String getSecretKey(@NonNull DataSnapshot x)
    {
        return x.child("Email").child("Secret").getValue(String.class);
    }

    public static class BiometricClass 
    {
        private static BiometricPrompt biometricPrompt = null;
        private static final Executor executor = Executors.newSingleThreadExecutor();
        
        @RequiresApi(api = Build.VERSION_CODES.P)
        public static void checkEncryption(Activity context, Runnable runnable)
        {
            if(biometricPrompt == null)
            {
                BiometricClass.biometricPrompt = new BiometricPrompt((FragmentActivity)context, executor, new BiometricPrompt.AuthenticationCallback()
                {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString)
                    {
                        if(errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                        {
                            biometricPrompt.cancelAuthentication();
                            System.exit(0);
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
                    {
                        runnable.run();
                    }

                    @Override
                    public void onAuthenticationFailed()
                    {
                        authenticate(context);
                    }
                });
                checkBiometricSupport(context);
            }
            else {
                BiometricClass.biometricPrompt = null;
                BiometricClass.biometricPrompt = new BiometricPrompt((FragmentActivity)context, executor, new BiometricPrompt.AuthenticationCallback()
                {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString)
                    {
                        if(errorCode == ERROR_NEGATIVE_BUTTON && biometricPrompt != null)
                        {
                            biometricPrompt.cancelAuthentication();
                            System.exit(0);
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result)
                    {
                        runnable.run();
                    }

                    @Override
                    public void onAuthenticationFailed()
                    {
                        authenticate(context);
                    }
                });
                checkBiometricSupport(context);
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private static SecretKey createKey() throws NoSuchProviderException, NoSuchAlgorithmException, InvalidAlgorithmParameterException
        {
            String algorithm = KeyProperties.KEY_ALGORITHM_AES;
            String provider = "AndroidKeyStore";
            KeyGenerator keyGenerator = KeyGenerator.getInstance(algorithm, provider);
            KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("MY_KEY", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setUserAuthenticationRequired(true)
                    .build();

            keyGenerator.init(keyGenParameterSpec);
            return keyGenerator.generateKey();
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private static Cipher getEncryptCipher(Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException
        {
            String algorithm = KeyProperties.KEY_ALGORITHM_AES;
            String blockMode = KeyProperties.BLOCK_MODE_CBC;
            String padding = KeyProperties.ENCRYPTION_PADDING_PKCS7;
            Cipher cipher = Cipher.getInstance(algorithm + "/" + blockMode + "/" + padding);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher;
        }

        @RequiresApi(api = Build.VERSION_CODES.M)
        private static void authenticate(Activity context)
        {
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("OpenPos Authentication")
                    .setSubtitle("")
                    .setNegativeButtonText("Cancel")
                    .setDescription("Please place your finger on the sensor to unlock.")
                    .build();

            BiometricManager biometricManager = BiometricManager.from(context);
            switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG))
            {
                case BiometricManager.BIOMETRIC_SUCCESS:
                    try
                    {
                        biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(getEncryptCipher(createKey())));
                    }
                    catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | NoSuchProviderException | InvalidAlgorithmParameterException e)
                    {
                        e.printStackTrace();
                    }
                    break;
                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                    break;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.P)
        private static void checkBiometricSupport(Activity context)
        {
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
            PackageManager packageManager = context.getPackageManager();

            if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT))
            {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_BIOMETRIC) != PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(context, new String[] { Manifest.permission.USE_BIOMETRIC }, PackageManager.PERMISSION_GRANTED);
                }
                if (keyguardManager.isKeyguardSecure())
                {
                    authenticate(context);
                }
                else{
                    Toast.makeText(context, "Fingerprint security is not enabled.", Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(context, "This phone has not fingerprint feature.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String setSecurePassword(@NonNull String password)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashtext = new StringBuilder(no.toString(16));
            while (hashtext.length() < 32) {
                hashtext.insert(0, "0");
            }
            return hashtext.toString();
        }
        catch (NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
    }
}
