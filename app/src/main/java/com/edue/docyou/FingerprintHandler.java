package com.edue.docyou;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import androidx.core.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by Fosu on 9/16/2017.
 */

class FingerprintHandler extends FingerprintManager.AuthenticationCallback{
    private Context context;

    public FingerprintHandler(Context context) {
        this.context = context;
    }
    public void startAuthentication(FingerprintManager fingerprintManager, FingerprintManager.CryptoObject cryptoObject) {
        CancellationSignal cenCancellationSignal = new CancellationSignal();
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED)
            return;
        fingerprintManager.authenticate(cryptoObject, cenCancellationSignal,0,this,null);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        Toast.makeText(context, "FingerPrint Authentication failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        context.startActivity(new Intent(context, MainActivity.class));

    }
}
