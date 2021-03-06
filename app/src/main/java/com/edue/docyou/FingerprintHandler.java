package com.edue.docyou;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import android.widget.TextView;

import render.animations.Attention;
import render.animations.Render;

/**
 * Created by Fosu on 9/16/2017.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
    //The CancellationSignal method should be used when ever the app can no longer process user input, for example when the app goes
    //into the background. If you don't use this method, then other apps will be unable to access the touch sensor, including the lock screen

    private Context context;
    private TextView textView;
    Render render;

    public FingerprintHandler(Context context, TextView textView) {
        this.render = new Render(context);
        this.context = context;
        this.textView = textView;
    }

    //implementing the startAuthentication method, which is responsible for starting the fingerprint authentication process
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject, cenCancellationSignal, 0, this, null);
    }

    //onAuthenticationFailed is called when the fingerprint does not match any of the fingerprints registered
    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        //start animation on text view
        render.setAnimation(Attention.Shake(textView));
        render.start();
        //changing text to red color
        textView.setTextColor(Color.parseColor("#FF0000"));
        textView.setText("Try Again!");
        float radius =  (float) 0.3;
        textView.setShadowLayer(radius,radius,0, Color.parseColor("#FFFFFF"));
//        Toast.makeText(context, "FingerPrint Authentication failed", Toast.LENGTH_SHORT).show();
    }

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints registered on the device.
    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context, MainActivity.class));

    }

}
