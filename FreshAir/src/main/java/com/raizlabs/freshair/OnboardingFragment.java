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

    private static final String KEY_ONBOARDING = "onboardingInfo";

    public static OnboardingFragment newInstance(OnboardingInfo onboardingInfo) {
        OnboardingFragment fragment = new OnboardingFragment();
        fragment.setOnboardingInfo(onboardingInfo);
        return fragment;
    }

    private OnboardingInfo onboardingInfo;

    public void setOnboardingInfo(OnboardingInfo release) {
        Utils.initArguments(this);
        this.onboardingInfo = release;
        getArguments().putParcelable(KEY_ONBOARDING, release);
    }

    protected OnboardingInfo getRelease() {
        if (onboardingInfo == null) {
            onboardingInfo = getArguments().getParcelable(KEY_ONBOARDING);
        }

        return onboardingInfo;
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

        public FeaturePager(FragmentManager fm, OnboardingInfo onboardingInfo) {
            this(fm, onboardingInfo.getFeatures());
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
