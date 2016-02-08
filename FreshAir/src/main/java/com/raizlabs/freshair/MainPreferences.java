package com.raizlabs.freshair;

import android.content.Context;
import android.content.SharedPreferences;

public class MainPreferences {

    private static final String PREFS_NAME = "FreshAir:Main";

    private static final String KEY_ONBOARDING_PROMPT_VERSION = "LastOnboardingPromptVersionCode";
    private static final String KEY_UPDATE_PROMPT_VERSION = "LastUpdatePromptVersionCode";
    private static final String KEY_FORCED_UPDATE_VERSION = "ForcedUpdateVersionCode";
    private static final String KEY_APP_DISABLED = "AppIsDisabled";

    private SharedPreferences preferences;

    public MainPreferences(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public int getLastOnboardingPromptVersion() {
        return preferences.getInt(KEY_ONBOARDING_PROMPT_VERSION, -1);
    }

    public void setLastOnboardingPromptVersion(int version) {
        if (version > getLastOnboardingPromptVersion()) {
            preferences.edit().putInt(KEY_ONBOARDING_PROMPT_VERSION, version).apply();
        }
    }

    public void clearLastOnboardingPromptVersion() {
        preferences.edit().remove(KEY_ONBOARDING_PROMPT_VERSION).apply();
    }

    public int getLastUpdatePromptVersion() {
        return preferences.getInt(KEY_UPDATE_PROMPT_VERSION, -1);
    }

    public void setLastUpdatePromptVersion(int version) {
        if (version > getLastUpdatePromptVersion()) {
            preferences.edit().putInt(KEY_UPDATE_PROMPT_VERSION, version).apply();
        }
    }

    public void clearLastUpdatePromptVersion() {
        preferences.edit().remove(KEY_UPDATE_PROMPT_VERSION).apply();
    }

    public int getForcedUpdateVersion() {
        return preferences.getInt(KEY_FORCED_UPDATE_VERSION, -1);
    }

    public void setForcedUpdateVersion(int version) {
        preferences.edit().putInt(KEY_FORCED_UPDATE_VERSION, version).apply();
    }

    public void clearForcedUpdateVersion() {
        preferences.edit().remove(KEY_FORCED_UPDATE_VERSION).apply();
    }

    public boolean isAppDisabled() {
        return preferences.getBoolean(KEY_APP_DISABLED, false);
    }

    public void setAppDisabled() {
        preferences.edit().putBoolean(KEY_APP_DISABLED, true).apply();
    }

    public void clearAppDisabled() {
        preferences.edit().remove(KEY_APP_DISABLED).apply();
    }
}
