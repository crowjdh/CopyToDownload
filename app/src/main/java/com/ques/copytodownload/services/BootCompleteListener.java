package com.ques.copytodownload.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jeong on 13/05/2018.
 */

public class BootCompleteListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ClipboardMonitorService.start(context);
    }
}
