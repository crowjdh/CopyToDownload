package com.ques.copytodownload.utils;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

@SuppressWarnings("unused")
public class Logger {
    private static final String TAG = Logger.class.getSimpleName();

    private Logger() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static void iOrShortToast(@Nullable Context context, String message) {
        iOrToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void iOrLongToast(@Nullable Context context, String message) {
        iOrToast(context, message, Toast.LENGTH_LONG);
    }

    public static void dOrShortToast(@Nullable Context context, String message) {
        dOrToast(context, message, Toast.LENGTH_SHORT);
    }

    public static void dOrLongToast(@Nullable Context context, String message) {
        dOrToast(context, message, Toast.LENGTH_LONG);
    }

    private static void iOrToast(@Nullable Context context, String message, int toastLength) {
        if (context == null) {
            i(message);
        } else {
            showToast(context, message, toastLength);
        }
    }

    private static void dOrToast(@Nullable Context context, String message, int toastLength) {
        if (context == null) {
            d(message);
        } else {
            showToast(context, message, toastLength);
        }
    }

    public static void i(String message) {
        Log.i(TAG, message);
    }

    public static void d(String message) {
        Log.d(TAG, message);
    }

    public static void showLongToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_LONG);
    }

    public static void showShortToast(Context context, String message) {
        showToast(context, message, Toast.LENGTH_SHORT);
    }

    private static void showToast(Context context, String message, int toastLength) {
        Toast.makeText(context, message, toastLength).show();
    }
}
