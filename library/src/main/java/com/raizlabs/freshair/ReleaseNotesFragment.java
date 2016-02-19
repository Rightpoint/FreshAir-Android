package com.raizlabs.freshair;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import java.util.List;

public class ReleaseNotesFragment extends DialogFragment {

    private static final String KEY_RELEASE_NOTES = "releaseNotes";

    public static ReleaseNotesFragment newInstance(ReleaseNotes releaseNotes) {
        ReleaseNotesFragment fragment = new ReleaseNotesFragment();
        fragment.setReleaseNotes(releaseNotes);
        return fragment;
    }

    private ReleaseNotes releaseNotes;

    public void setReleaseNotes(ReleaseNotes release) {
        Utils.initArguments(this);
        this.releaseNotes = release;
        getArguments().putParcelable(KEY_RELEASE_NOTES, release);
    }

    protected ReleaseNotes getRelease() {
        if (releaseNotes == null) {
            releaseNotes = getArguments().getParcelable(KEY_RELEASE_NOTES);
        }

        return releaseNotes;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_release_notes, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.fragment_release_notes_viewPager);
        viewPager.setAdapter(new FeaturePager(getChildFragmentManager(), getRelease()));
        viewPager.setOffscreenPageLimit(getRelease().getFeatures().size());

        LinePageIndicator pageIndicator = (LinePageIndicator) view.findViewById(R.id.fragment_release_notes_pageIndicator);
        pageIndicator.setViewPager(viewPager);

        view.findViewById(R.id.fragment_release_notes_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private static class FeaturePager extends FragmentPagerAdapter {

        private List<FeatureInfo> features;

        public FeaturePager(FragmentManager fm, ReleaseNotes releaseNotes) {
            this(fm, releaseNotes.getFeatures());
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
