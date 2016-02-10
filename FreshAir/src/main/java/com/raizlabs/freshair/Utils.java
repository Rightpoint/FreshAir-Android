package com.raizlabs.freshair;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

class Utils {

    public static void initArguments(Fragment fragment) {
        if (fragment.getArguments() == null) {
            fragment.setArguments(new Bundle());
        }
    }

    public static int getApplicationVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            FreshAirLog.e("Failed to obtain Application Version Code", e);
        }

        return -1;
    }

    /**
     * Utility method for pulling plain text from an InputStream object
     */
    public static String readStream(InputStream in) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            FreshAirLog.e("Error reading stream", e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }

            try {
                reader.close();
            } catch (IOException e) {
            }
        }
        return sb.toString();
    }

    /**
     * Converts the given value in density-independent pixels into raw pixels
     * with respect to the given {@link DisplayMetrics}
     *
     * @param dips    The value in density-independent pixels
     * @param metrics The {@link DisplayMetrics} to use to do the conversion
     * @return The value in raw pixels
     */
    public static float dipsToPixels(float dips, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dips, metrics);
    }
}
