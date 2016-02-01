package com.raizlabs.freshair;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class OnboardingFragment extends DialogFragment {

    private static final String KEY_RELEASE = "release";

    public static OnboardingFragment newInstance(ReleaseInfo releaseInfo) {
        OnboardingFragment fragment = new OnboardingFragment();
        fragment.setRelease(releaseInfo);
        return fragment;
    }

    private ReleaseInfo releaseInfo;

    public void setRelease(ReleaseInfo release) {
        FreshAirUtils.initArguments(this);
        this.releaseInfo = release;
        getArguments().putParcelable(KEY_RELEASE, release);
    }

    protected ReleaseInfo getRelease() {
        if (releaseInfo == null) {
            releaseInfo = getArguments().getParcelable(KEY_RELEASE);
        }

        return releaseInfo;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_onboarding, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_onboarding_viewPager);
        viewPager.setAdapter(new FeaturePager(getChildFragmentManager(), getRelease()));
    }

    private static class FeaturePager extends FragmentStatePagerAdapter {

        private List<FeatureInfo> features;

        public FeaturePager(FragmentManager fm, ReleaseInfo release) {
            this(fm, release.getFeatures());
        }

        public FeaturePager(FragmentManager fm, List<FeatureInfo> features) {
            super(fm);
            this.features = features;
        }

        @Override
        public Fragment getItem(int position) {
            return FeatureFragment.newInstance(features.get(position));
        }


        @Override
        public int getCount() {
            return features.size();
        }
    }
}
