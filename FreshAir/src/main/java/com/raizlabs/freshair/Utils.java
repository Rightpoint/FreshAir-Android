package com.raizlabs.freshair;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;

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
}
