package com.ques.copytodownload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.ques.copytodownload.services.ClipboardMonitorService;
import com.ques.copytodownload.utils.Logger;

public class MainActivity extends AppCompatActivity {

    private static final int RC_PERMISSIONS_WRITE_EXTERNAL_STORAGE = 1000;
    private static final String PERMISSION_WRITE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startClipboardMonitorServiceIfPossible();
    }

    private void startClipboardMonitorServiceIfPossible() {
        if (isPermissionGranted(PERMISSION_WRITE)) {
            ClipboardMonitorService.start(getApplicationContext());
        } else {
            requestWritePermission();
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_WRITE)) {
            Logger.showLongToast(this, "Placeholder");
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{ PERMISSION_WRITE },
                    RC_PERMISSIONS_WRITE_EXTERNAL_STORAGE);
        }
    }

    private boolean isPermissionGranted(String permission) {
        int writePermission = ContextCompat.checkSelfPermission(this, permission);
        return writePermission == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case RC_PERMISSIONS_WRITE_EXTERNAL_STORAGE: {
                if (isPermissionGranted(grantResults)) {
                    startClipboardMonitorServiceIfPossible();
                } else {
                    Logger.showLongToast(this, "This app requires this permission.");
                }
            }
        }
    }

    private boolean isPermissionGranted(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
