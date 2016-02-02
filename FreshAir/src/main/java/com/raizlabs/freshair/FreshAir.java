package com.raizlabs.freshair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

public class FreshAir {

    private static ForegroundActivityTracker foregroundActivityTracker;

    private static UpdatePromptInfo updatePromptInfo = new UpdatePromptInfo.Builder();
    private static ReleaseInfo releaseInfo;

    public static void init(Context context) {
        foregroundActivityTracker = new ForegroundActivityTracker(context);
    }

    public static void setUpdatePrompt(UpdatePromptInfo updatePrompt) {
        FreshAir.updatePromptInfo = updatePrompt;
    }

    public static void setReleaseInfo(ReleaseInfo releaseInfo) {
        FreshAir.releaseInfo = releaseInfo;
    }

    public static void showUpdatePrompt(final boolean forced) {
        final UpdatePromptInfo updateInfo = updatePromptInfo;

        foregroundActivityTracker.postToForegroundActivity(new ForegroundActivityTracker.ActivityAction() {
            @Override
            public void execute(Activity activity) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                        .setTitle(forced ? updateInfo.getForcedTitle(activity) : updateInfo.getTitle(activity))
                        .setMessage(forced ? updateInfo.getForcedDescription(activity) : updateInfo.getDescription(activity))
                        .setPositiveButton(updateInfo.getAcceptString(activity), null)
                        .setCancelable(!forced);

                if (!forced) {
                    dialogBuilder.setNegativeButton(updateInfo.getDeclineString(activity), null);
                }

                dialogBuilder.show();
            }
        }, forced);
    }

    public static void showOnboarding(FragmentActivity activity) {

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
