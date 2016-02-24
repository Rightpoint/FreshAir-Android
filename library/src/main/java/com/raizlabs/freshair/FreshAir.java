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
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

/**
 * Central class for the Fresh Air SDK. Most actions are performed through here.
 */
public class FreshAir {

    private static ForegroundActivityTracker foregroundActivityTracker;

    private static int currentApplicationVersion;
    private static UpdatePromptInfo updatePromptInfo = new UpdatePromptInfo.Builder();
    private static ReleaseNotes releaseNotes;
    private static MainPreferences preferences;

    /**
     * Initializes the FreshAir SDK.
     *
     * @param context A {@link Context} of the calling app.
     */
    public static void initialize(Context context) {
        context = context.getApplicationContext();

        foregroundActivityTracker = new ForegroundActivityTracker(context);
        preferences = new MainPreferences(context);

        final int currentOsVersion = Build.VERSION.SDK_INT;
        // If the user upgraded, we need to reapply the rules, so clear the disabling of the app
        if (currentOsVersion > preferences.getLastOsVersion()) {
            preferences.clearAppDisabled();
        }
        preferences.setLastOsVersion(currentOsVersion);

        if (preferences.isAppDisabled()) {
            disableApp();
        } else {
            currentApplicationVersion = Utils.getApplicationVersion(context);
            if (currentApplicationVersion < preferences.getForcedUpdateVersion()) {
                showUpdatePrompt(preferences.getForcedUpdateVersion(), true);
            }
        }
    }

    /**
     * @return True if {@link #initialize(Context)} has been called.
     */
    public static boolean isInitialized() {
        return (foregroundActivityTracker != null);
    }

    /**
     * Sets the {@link UpdatePromptInfo} to be used by {@link #showUpdatePrompt(int, boolean)}.
     *
     * @param updatePrompt The {@link UpdatePromptInfo} to display.
     */
    public static void setUpdatePrompt(UpdatePromptInfo updatePrompt) {
        FreshAir.updatePromptInfo = updatePrompt;
    }

    /**
     * Sets the {@link ReleaseNotes} to be used by {@link #showReleaseNotes(FragmentActivity)}.
     *
     * @param releaseNotes The {@link ReleaseNotes} to display.
     */
    public static void setReleaseNotes(ReleaseNotes releaseNotes) {
        FreshAir.releaseNotes = releaseNotes;
    }

    /**
     * Requests updates from the given URL to determine the currently available release information. When
     * the request finishes, the user is shown update prompts based on the returned info. See
     * {@link #showUpdatePrompt(ReleaseInfo)} for more details.
     *
     * @param url The URL to fetch {@link ReleaseInfo} from.
     */
    public static void checkForUpdates(final String url) {
        if (ensureInitialized()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ReleaseInfoRequest request = new ReleaseInfoRequest(url);
                        final ReleaseInfo info = request.run();

                        if (info != null) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    showUpdatePrompt(info);
                                }
                            });
                        } else {
                            FreshAirLog.e("Error checking for updates from: " + url + ", data was not parsed properly.");
                        }
                    } catch (Exception e) {
                        FreshAirLog.e("Error checking for updates from: " + url, e);
                    }
                }
            }).start();
        }
    }

    /**
     * Shows the update prompt, which prompts the user to go to the Play Store and update the app. This prompt will be
     * shown over the current foreground Activity, or the next Activity to be foregrounded if one is not yet available.
     * Data is presented based on the prompt information set via {@link #setUpdatePrompt(UpdatePromptInfo)}.
     * <br/><br/>
     * The user will only be prompted once for each version. If they have already acknowledged a prompt for this
     * version, or any higher version code than the one specified, the user will not be prompted again unless
     * they are being forced to update.
     * <br/><br/>
     * If the update is set to be forced, the user will be prompted no matter the version, and this prompt will block
     * the user from using the current Activity, and any subsequently foregrounded Activity, even if they manage to
     * back out to previous Activities or switch tasks. This will persist across app launches (reapplied when
     * {@link #initialize(Context)} is called), but will need to be called again if the user clears application data.
     *
     * @param releaseInfo A set of release info which contains information about the available releases and forced
     *                    updates.
     */
    public static void showUpdatePrompt(ReleaseInfo releaseInfo) {
        if (ensureInitialized()) {
            int newestAvailableVersion = currentApplicationVersion;
            for (Release release : releaseInfo.getReleases()) {
                if (release.isApplicable()) {
                    newestAvailableVersion = Math.max(newestAvailableVersion, release.getVersionCode());
                }
            }

            clearForcedUpdateVersion();
            clearAppDisabled();

            // If the newest we support is less than the min, we must disable the app
            if (newestAvailableVersion < releaseInfo.getForcedMinimumVersion()) {
                disableApp();
            } else {
                if (newestAvailableVersion > currentApplicationVersion) {
                    // Update available
                    // Forced if the current version is less than the forced minimum
                    final boolean forced = (currentApplicationVersion < releaseInfo.getForcedMinimumVersion());
                    showUpdatePrompt(newestAvailableVersion, forced);
                }
            }
        }
    }

    /**
     * Clears the forced update state.
     */
    public static void clearForcedUpdateVersion() {
        if (ensureInitialized()) {
            preferences.clearForcedUpdateVersion();
        }
    }

    /**
     * Clears the app disabled state.
     */
    public static void clearAppDisabled() {
        if (ensureInitialized()) {
            preferences.clearAppDisabled();
        }
    }

    /**
     * Disables the app, preventing it from being used. This persists across app launches until
     * {@link #clearAppDisabled()} is called, or {@link ReleaseInfo} is processed which allows it again.
     */
    public static void disableApp() {
        if (ensureInitialized()) {
            preferences.setAppDisabled();

            final UpdatePromptInfo promptInfo = updatePromptInfo;

            foregroundActivityTracker.postToForegroundActivity(new ForegroundActivityTracker.ActivityAction() {
                @Override
                public void execute(Activity activity) {
                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                            .setTitle(promptInfo.getDisabledTitle(activity))
                            .setMessage(promptInfo.getDisabledDescription(activity))
                            .setCancelable(false);

                    dialogBuilder.show();
                }
            }, true);
        }
    }

    /**
     * Shows the update prompt, which prompts the user to go to the Play Store and update the app. This prompt will be
     * shown over the current foreground Activity, or the next Activity to be foregrounded if one is not yet available.
     * Data is presented based on the prompt information set via {@link #setUpdatePrompt(UpdatePromptInfo)}.
     * <br/><br/>
     * The user will only be prompted once for each version. If they have already acknowledged a prompt for this
     * version, or any higher version code than the one specified, the user will not be prompted again unless
     * {@code forced} is set to true.
     * <br/><br/>
     * If {@code forced} is set to true, the user will be prompted no matter the version, and this prompt will block
     * the user from using the current Activity, and any subsequently foregrounded Activity, even if they manage to
     * back out to previous Activities or switch tasks. This will persist across app launches (reapplied when
     * {@link #initialize(Context)} is called), but will need to be called again if the user clears application data.
     *
     * @param newVersionCode The version code to prompt an update for.
     * @param forced         True to force the update prompt over all Activities, false to just display as a normal
     *                       dismissible dialog.
     */
    public static void showUpdatePrompt(final int newVersionCode, final boolean forced) {
        if (ensureInitialized()) {

            if (forced) {
                preferences.setForcedUpdateVersion(newVersionCode);
            }

            final int lastVersionPrompt = preferences.getLastUpdatePromptVersion();
            final boolean hasPromptedThisVersion = lastVersionPrompt >= newVersionCode;

            FreshAirLog.d(String.format("Last update version displayed: %d Attempting to display update: %d Forced = %b",
                    lastVersionPrompt, newVersionCode, forced));

            if (forced || !hasPromptedThisVersion) {

                final UpdatePromptInfo updateInfo = updatePromptInfo;

                foregroundActivityTracker.postToForegroundActivity(new ForegroundActivityTracker.ActivityAction() {
                    @Override
                    public void execute(final Activity activity) {
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity)
                                .setTitle(forced ? updateInfo.getForcedTitle(activity) : updateInfo.getTitle(activity))
                                .setMessage(forced ? updateInfo.getForcedDescription(activity) : updateInfo.getDescription(activity))
                                .setCancelable(!forced)
                                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        onUserAcknowledgedVersionUpdate(newVersionCode);
                                    }
                                })
                                .setPositiveButton(updateInfo.getAcceptString(activity), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FreshAirLog.v("User accepted update version: " + newVersionCode);
                                        FreshAir.goToUpdatePage(activity);

                                        // If we're forcing an update, reshow this dialog so it stays on top
                                        if (forced) {
                                            execute(activity);
                                        }

                                        onUserAcknowledgedVersionUpdate(newVersionCode);
                                    }
                                });

                        // Only show the decline button if we aren't forcing the update
                        if (!forced) {
                            dialogBuilder.setNegativeButton(updateInfo.getDeclineString(activity), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    onUserAcknowledgedVersionUpdate(newVersionCode);
                                }
                            });
                        }

                        dialogBuilder.show();
                    }
                }, forced);
            }
        }
    }

    /**
     * Call to mark the given version update as acknowledged so we don't reprompt the user for this version or any
     * previous version.
     * @param versionCode The version code to mark as acknowledged.
     */
    public static void onUserAcknowledgedVersionUpdate(int versionCode) {
        if (ensureInitialized()) {
            FreshAirLog.v("User acknowledged version update: " + versionCode);
            preferences.setLastUpdatePromptVersion(versionCode);
        }
    }

    /**
     * Clears the history of which update versions have been shown.
     *
     * @see #showUpdatePrompt(int, boolean)
     */
    public static void clearUpdatePromptVersion() {
        if (ensureInitialized()) {
            FreshAirLog.v("Clearing update prompt version history");
            preferences.clearLastUpdatePromptVersion();
        }
    }

    /**
     * Attempts to start the Play Store Activity for the update page of this app, and returns success or failure.
     *
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
     * Shows the release notes dialog in the given Activity using the data indicated in {@link #setReleaseNotes(ReleaseNotes)}.
     * This will only show once per version indicated in the {@link ReleaseNotes} - if we've already shown the indicated
     * version or higher, this call will do nothing.
     *
     * @param activity The {@link FragmentActivity} to show the dialog in.
     */
    public static void showReleaseNotes(FragmentActivity activity) {
        if (ensureInitialized()) {
            final int lastPromptVersion = preferences.getLastReleaseNotesPromptVersion();
            final int releaseInfoVersion = releaseNotes.getVersionCode();

            FreshAirLog.d(String.format("Last release notes version displayed: %d Attempting to display release: %d",
                    lastPromptVersion, releaseInfoVersion));

            if (lastPromptVersion < releaseNotes.getVersionCode()) {
                ReleaseNotesFragment releaseNotesFragment = ReleaseNotesFragment.newInstance(releaseNotes);
                releaseNotesFragment.show(activity.getSupportFragmentManager(), null);

                preferences.setLastReleaseNotesPromptVersion(Utils.getApplicationVersion(activity));
            }
        }
    }

    /**
     * Clears the history of which release notes versions have been shown.
     *
     * @see #showReleaseNotes(FragmentActivity)
     */
    public static void clearReleaseNotesVersion() {
        if (ensureInitialized()) {
            FreshAirLog.v("Clearing release notes version history");
            preferences.clearLastReleaseNotesPromptVersion();
        }
    }

    /**
     * Sets the log levels which will be logged to the Android {@link Log}.
     *
     * @param logLevel A bitmask of the desired levels, using values defined in
     *                 {@link LogLevel}.
     */
    public static void setLogLevel(int logLevel) {
        FreshAirLog.setLogLevel(logLevel);
    }

    /**
     * @return The app's current version code, or -1 if the SDK hasn't been initialized.
     */
    static int getCurrentApplicationVersion() {
        if (ensureInitialized()) {
            return currentApplicationVersion;
        } else {
            return -1;
        }
    }

    /**
     * Checks if the SDK is initialized, and logs errors if not.
     *
     * @return True if the SDK is initialized, false if it is not.
     */
    static boolean ensureInitialized() {
        if (!isInitialized()) {
            FreshAirLog.e("Attempted to use FreshAir before it was initialized.", new FreshAirUninitializedException());
            return false;
        }

        return true;
    }
}
