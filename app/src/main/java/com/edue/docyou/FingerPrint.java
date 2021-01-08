package com.edue.docyou;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
//import android.support.annotation.Nullable;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;


/**
 * Created by Fosu on 9/16/2017.
 */

public class FingerPrint extends AppCompatActivity {
    private static final String TAG = FingerPrint.class.getSimpleName();
    private KeyStore keyStore;
    private static final String KEY_NAME = "EDUE";
    private Cipher cipher;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fingerprint);

        //If you've set your app's minSdkVersion to anything lower than 23, then you'll need to verify that the device is running Marshmallow and above
        //before executing any fingerprint related code
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            TextView textView = findViewById(R.id.fingerprint_tv);
            KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
            FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);

            //checking if the device has a fingerprint sensor
            if (!Objects.requireNonNull(fingerprintManager).isHardwareDetected()) {
                //if a fingerprint sensor isn't available, then inform the user that they'll be unable to use the app's fingerprint feature
                textView.setText("Your device does not support fingerprint authentication");
            }

            //checking if the user has granted the app the USE_FINGERPRINT permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                //If the app doesn't have this permission, then display the following text
                textView.setText("Kindly enable the fingerprint permission.");
            }

            //checking that the user has registered at least one fingerprint
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                //If the user hasn't configured any fingerprints, then display the below text
                textView.setText("No fingerprint has been configured. Kindly register at least one fingerprint.");
            }

            //checking that the lock screen is secured
            if (!Objects.requireNonNull(keyguardManager).isDeviceSecure()) {
                //If the user hasn't secured their lock screen with a PIN OR PASSWORD OR PATTERN, then display this message
                textView.setText("Kindly enable lock screen security in your device's settings");
            }   else{
                try {
                    genKey();
                }   catch (Exception e){
                    Log.e(TAG, "onCreate: ", e);
                }

                if (cipherInit()){
                    //If the cipher is initialized successfully, then create a CryptoObject instance
                    FingerprintManager.CryptoObject cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    //Here, we are referencing  the FingerprintHandler class that we will create in the next section.
                    //This class will be responsible for starting the authentication process (via te startAuth method) and processing the authentication process events
                    FingerprintHandler handler = new FingerprintHandler(this);
                    handler.startAuthentication(fingerprintManager, cryptoObject);
                }
            }
        }

    }

    //creating a new method that we will use to initialize our cipher
    private boolean cipherInit() {
        try {
            //obtaining a cipher instance and configure it with the properties required for fingerprint authentication
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/" +KeyProperties.BLOCK_MODE_CBC+"/"+ KeyProperties.ENCRYPTION_PADDING_PKCS7);
            }
        } catch (NoSuchAlgorithmException
                | NoSuchPaddingException e) {
            Log.e(TAG, "cipherInit: ", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //return true if the cipher has been initialized successfully
            return true;

            //return false if cipher initialization failed
        } catch (IOException
                | KeyStoreException
                | CertificateException
                | UnrecoverableKeyException
                | NoSuchAlgorithmException
                | InvalidKeyException e1) {

            Log.e(TAG, "cipherInit: ", e1);
            return false;
        }

    }

    private void genKey(){
        try {
            //obtain a reference to the keystore using the standard android keystore container identifier ("AndroidKeystore")
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            KeyGenerator keyGenerator = null;

            //Generating the key
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

                //initializing an empty keystore
                keyStore.load(null);

                keyGenerator.init(new
                        //specifying the operations this key can be used for
                        KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        //configuring this key so that the user has to confirm their identity with a fingerprint each time they want to use it
                        .setUserAuthenticationRequired(true)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build()
                );
                //generating the key
                keyGenerator.generateKey();
            }



        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException e) {

            Log.e(TAG, "genKey: ", e);
        }
    }

    // making the fingerprint screen close after verification unless the app is opened afresh
    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
