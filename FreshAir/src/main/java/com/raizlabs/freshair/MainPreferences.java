package com.raizlabs.freshair;

import android.content.Context;
import android.content.SharedPreferences;

public class MainPreferences {

    private static final String PREFS_NAME = "FreshAir:Main";

    private static final String KEY_ONBOARDING_PROMPT_VERSION = "LastOnboardingPromptVersionCode";
    private static final String KEY_UPDATE_PROMPT_VERSION = "LastUpdatePromptVersionCode";

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
}
