package com.raizlabs.freshair;

import android.os.Bundle;
import android.support.v4.app.Fragment;

class FreshAirUtils {

    public static void initArguments(Fragment fragment) {
        if (fragment.getArguments() == null) {
            fragment.setArguments(new Bundle());
        }
    }
}
