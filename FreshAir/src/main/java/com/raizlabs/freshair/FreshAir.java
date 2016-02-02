package com.raizlabs.freshair;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class FreshAir {

    public static void init(Context context) {
    }

    public static void showUpdatePrompt() {

    }

    public static void showOnboarding(FragmentActivity activity, ReleaseInfo releaseInfo) {

        MainPreferences preferences = new MainPreferences(activity);
        final int lastPromptVersion = preferences.getLastOnboardingPromptVersion();
        final int releaseInfoVersion = releaseInfo.getVersionCode();

        FreshAirLog.v( "Last Onboarding Prompt: " + lastPromptVersion + " Attempting to display release: " + releaseInfoVersion);

        if (lastPromptVersion < releaseInfo.getVersionCode()) {
            OnboardingFragment onboardingFragment = OnboardingFragment.newInstance(releaseInfo);
            onboardingFragment.show(activity.getSupportFragmentManager(), null);

            preferences.setLastOnboardingPromptVersion(Utils.getApplicationVersion(activity));
        }
    }

    public static void clearOnboardingVersion(Context context) {
        MainPreferences preferences = new MainPreferences(context);
        preferences.clearLastOnboardingPromptVersion();
    }
}
