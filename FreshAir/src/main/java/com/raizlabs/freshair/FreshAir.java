package com.raizlabs.freshair;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
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
            public void execute(final Activity activity) {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                        .setTitle(forced ? updateInfo.getForcedTitle(activity) : updateInfo.getTitle(activity))
                        .setMessage(forced ? updateInfo.getForcedDescription(activity) : updateInfo.getDescription(activity))
                        .setCancelable(!forced)
                        .setPositiveButton(updateInfo.getAcceptString(activity), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FreshAir.goToUpdatePage(activity);

                                // If we're forcing an update, reshow this dialog so it stays on top
                                if (forced) {
                                    execute(activity);
                                }
                            }
                        });

                // Only show the decline button if we aren't forcing the update
                if (!forced) {
                    dialogBuilder.setNegativeButton(updateInfo.getDeclineString(activity), null);
                }

                dialogBuilder.show();
            }
        }, forced);
    }

    public static boolean goToUpdatePage(Context context) {
        final Uri playStoreUri = Uri.parse("market://details?id=" + context.getPackageName());
        FreshAirLog.i("Attempting to open Play Store update page at URI: " + playStoreUri.toString());

        final Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, playStoreUri);
        final PackageManager packageManager = context.getPackageManager();

        for (ResolveInfo resolveInfo : packageManager.queryIntentActivities(playStoreIntent, 0)) {
            final ActivityInfo resolveActivity = resolveInfo.activityInfo;
            final String resolvePackageName = resolveActivity.packageName;
            if (resolvePackageName.equals("com.android.vending")) {
                FreshAirLog.i("Opening Play Store update page...");
                playStoreIntent.setComponent(new ComponentName(resolvePackageName, resolveActivity.name));
                context.startActivity(playStoreIntent);
                return true;
            }
        }

        FreshAirLog.e("Failed to find the Play Store as a candidate for the resolution of update URI: " + playStoreUri.toString());
        return false;
    }

    public static void showOnboarding(FragmentActivity activity) {

        MainPreferences preferences = new MainPreferences(activity);
        final int lastPromptVersion = preferences.getLastOnboardingPromptVersion();
        final int releaseInfoVersion = releaseInfo.getVersionCode();

        FreshAirLog.v("Last Onboarding Prompt: " + lastPromptVersion + " Attempting to display release: " + releaseInfoVersion);

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
