package com.ques.copytodownload.services;

import android.app.ActivityManager;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.ques.copytodownload.model.ClipboardURLHandler;
import com.ques.copytodownload.utils.Logger;

/**
 * Created by jeong on 13/05/2018.
 */

public class ClipboardMonitorService extends Service
        implements ClipboardManager.OnPrimaryClipChangedListener {
    private static final String TAG = ClipboardMonitorService.class.getSimpleName();
    private ClipboardManager mClipboardManager;

    @Override
    public void onCreate() {
        super.onCreate();

        InitClipboardManager();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mClipboardManager != null) {
            mClipboardManager.removePrimaryClipChangedListener(this);
        }
    }

    private void InitClipboardManager() {
        mClipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        if (mClipboardManager != null) {
            mClipboardManager.addPrimaryClipChangedListener(this);
        }
    }

    @Override
    public void onPrimaryClipChanged() {
        Logger.d("onPrimaryClipChanged:\n" + mClipboardManager.getPrimaryClip());
        String instagramUrl = mClipboardManager.getPrimaryClip().getItemAt(0).getText().toString();
        ClipboardURLHandler.downloadInstagramImage(getApplicationContext(), instagramUrl);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void start(Context context) {
        if (isServiceRunning(context)) {
            Logger.i("ClipboardMonitorService already started");
            return;
        }

        context.startService(new Intent(context, ClipboardMonitorService.class));
        Logger.i("ClipboardMonitorService started");
    }

    private static boolean isServiceRunning(Context context) {
        final String serviceName = ClipboardMonitorService.class.getName();
        ActivityManager manager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        if (manager == null) {
            return false;
        }

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if(serviceName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}