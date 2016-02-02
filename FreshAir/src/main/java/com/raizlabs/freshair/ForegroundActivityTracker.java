package com.raizlabs.freshair;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import java.lang.ref.WeakReference;

class ForegroundActivityTracker {

    private WeakReference<Activity> foregroundActivity;

    public ForegroundActivityTracker(Context context) {
        ((Application)context.getApplicationContext()).registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
        foregroundActivity = new WeakReference<>(null);
    }

    Activity getForegroundActivity() {
        synchronized (this) {
            if (foregroundActivity != null) {
                return foregroundActivity.get();
            }

            return null;
        }
    }

    private void setForegroundActivity(Activity activity) {
        synchronized (this) {
            foregroundActivity = new WeakReference<>(activity);
        }
    }

    private void unforegroundActivity(Activity activity) {
        synchronized (this) {
            if (activity != null && activity.equals(getForegroundActivity())) {
                foregroundActivity = null;
            }
        }
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            FreshAirLog.w("Created: " + activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            FreshAirLog.w("Started: " + activity);
        }

        @Override
        public void onActivityResumed(Activity activity) {
            FreshAirLog.w("Resumed: " + activity);
            setForegroundActivity(activity);
            FreshAirLog.i("Foreground: " + getForegroundActivity());
        }

        @Override
        public void onActivityPaused(Activity activity) {
            FreshAirLog.w("Paused: " + activity);
            unforegroundActivity(activity);
            FreshAirLog.i("Foreground: " + getForegroundActivity());
        }

        @Override
        public void onActivityStopped(Activity activity) {
            FreshAirLog.w("Stopped: " + activity);
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            FreshAirLog.w("Destroyed: " + activity);
        }
    };
}
