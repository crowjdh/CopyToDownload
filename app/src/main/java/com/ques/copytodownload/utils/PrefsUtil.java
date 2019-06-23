package com.ques.copytodownload.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jeong on 03/03/2019.
 */

public class PrefsUtil {
    private static String IS_TURNED_ON = "IS_TURNED_ON";

    private interface Action {
        void edit(SharedPreferences.Editor editor);
    }

    private PrefsUtil() {
        throw new AssertionError("You MUST NOT create the instance of this class!!");
    }

    public static boolean isPoweredOn(Context context) {
        return getPrefs(context).getBoolean(IS_TURNED_ON, false);
    }

    public static void updatePowerSwitch(Context context, boolean isTurnedOn) {
        editPrefs(context, editor -> editor.putBoolean(IS_TURNED_ON, isTurnedOn));
    }

    private static void editPrefs(Context context, Action action) {
        SharedPreferences.Editor editor = getPrefs(context).edit();
        action.edit(editor);
        editor.apply();
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
    }
}
