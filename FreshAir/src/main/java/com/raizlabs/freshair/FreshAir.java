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

    /**
     * Initializes the FreshAir SDK.
     * @param context A {@link Context} of the calling app.
     */
    public static void initialize(Context context) {
        foregroundActivityTracker = new ForegroundActivityTracker(context);
    }

    /**
     * Sets the {@link UpdatePromptInfo} to be used by {@link #showUpdatePrompt(boolean)}.
     * @param updatePrompt The {@link UpdatePromptInfo} to display.
     */
    public static void setUpdatePrompt(UpdatePromptInfo updatePrompt) {
        FreshAir.updatePromptInfo = updatePrompt;
    }

    /**
     * Sets the {@link ReleaseInfo} to be used by {@link #showOnboarding(FragmentActivity)}.
     * @param releaseInfo The {@link ReleaseInfo} to display.
     */
    public static void setReleaseInfo(ReleaseInfo releaseInfo) {
        FreshAir.releaseInfo = releaseInfo;
    }

    /**
     * Shows the update prompt, which prompts the user to go to the Play Store and update the app. This prompt will be
     * shown over the current foreground Activity, or the next Activity to be foregrounded if one is not yet available.
     * Data is presented based on the prompt information set via {@link #setUpdatePrompt(UpdatePromptInfo)}.
     * <br/><br/>
     * If {@code forced} is set to true, this prompt will block the user from using the current Activity, and any
     * subsequently foregrounded Activity, even if they back out to previous Activities.
     * @param forced True to force the update prompt over all Activities, false to just display as a normal
     *               dismissible dialog.
     */
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

    /**
     * Attempts to start the Play Store Activity for the update page of this app, and returns success or failure.
     * @param context The {@link Context} to use to start the Activity.
     * @return True if the Play Store was started, false if the Play Store could not be found or was unresolved.
     */
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

    /**
     * Shows the onboarding dialog in the given Activity using the data indicated in {@link #setReleaseInfo(ReleaseInfo)}.
     * This will only show once per version indicated in the {@link ReleaseInfo} - if we've already shown the indicated
     * version or higher, this call will do nothing.
     * @param activity The {@link FragmentActivity} to show the dialog in.
     */
    public static void showOnboarding(FragmentActivity activity) {
        MainPreferences preferences = new MainPreferences(activity);
        final int lastPromptVersion = preferences.getLastOnboardingPromptVersion();
        final int releaseInfoVersion = releaseInfo.getVersionCode();

        FreshAirLog.v("Last version displayed: " + lastPromptVersion + " Attempting to display release: " + releaseInfoVersion);

        if (lastPromptVersion < releaseInfo.getVersionCode()) {
            OnboardingFragment onboardingFragment = OnboardingFragment.newInstance(releaseInfo);
            onboardingFragment.show(activity.getSupportFragmentManager(), null);

            preferences.setLastOnboardingPromptVersion(Utils.getApplicationVersion(activity));
        }
    }

    /**
     * Clears the history of which onboarding versions have been shown.
     * @see #showOnboarding(FragmentActivity)
     * @param context The {@link Context} to use to access resources.
     */
    public static void clearOnboardingVersion(Context context) {
        MainPreferences preferences = new MainPreferences(context);
        preferences.clearLastOnboardingPromptVersion();
    }
}
