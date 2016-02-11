package com.raizlabs.freshair;

import android.content.Context;

/**
 * Thrown when Fresh Air actions are attempted but hasn't been initialized via {@link FreshAir#initialize(Context)}.
 */
public class FreshAirUninitializedException extends IllegalStateException {

    public FreshAirUninitializedException() {
        super("Please call FreshAir.initialize(Context) before using.");
    }

}
