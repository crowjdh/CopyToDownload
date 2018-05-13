package com.ques.copytodownload;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.ques.copytodownload.model.ClipboardURLHandler;

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

        if (isPermissionGranted(PERMISSION_WRITE)) {
            ClipboardURLHandler.downloadInstagramImage("https://www.instagram.com/p/Bis75J2nvTI");
        } else {
            requestWritePermission();
        }
    }

    private void requestWritePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, PERMISSION_WRITE)) {
            Toast.makeText(this, "Placeholder", Toast.LENGTH_LONG).show();
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
                    ClipboardURLHandler.downloadInstagramImage("https://www.instagram.com/p/Bis75J2nvTI");
                } else {
                    Toast.makeText(this, "This app requires this permission.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    private boolean isPermissionGranted(int[] grantResults) {
        return grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED;
    }
}
