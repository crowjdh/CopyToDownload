package com.ques.copytodownload.utils;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.net.ConnectivityManager;

class NetworkUtils {
    private NetworkUtils() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    static boolean isConnectedToMetered(Context context) throws NetworkErrorException {
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connManager == null) {
            throw new NetworkErrorException("No connectivity service found");
        }
        return connManager.isActiveNetworkMetered();
    }
}
