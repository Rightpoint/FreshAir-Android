package com.raizlabs.freshair;

public class FreshAirUninitializedException extends IllegalStateException {

    public FreshAirUninitializedException() {
        super("Please call FreshAir.initialize(Context) before using.");
    }

}
