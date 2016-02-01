package com.raizlabs.freshair;

import android.support.v4.app.FragmentActivity;

public class FreshAir {

    public static final String LOG_TAG = "FreshAir";

    public static void showUpdatePrompt() {

    }

    public static void showOnboarding(FragmentActivity activity, ReleaseInfo releaseInfo) {
        OnboardingFragment onboardingFragment = OnboardingFragment.newInstance(releaseInfo);
        onboardingFragment.show(activity.getSupportFragmentManager(), null);
    }
}
