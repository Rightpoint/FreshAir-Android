package com.raizlabs.freshair;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FeatureFragment extends Fragment {

    private static final String KEY_FEATURE = "feature";

    public static FeatureFragment newInstance(FeatureInfo featureInfo) {
        FeatureFragment fragment = new FeatureFragment();
        fragment.setFeature(featureInfo);
        return fragment;
    }

    private FeatureInfo feature;

    public void setFeature(FeatureInfo feature) {
        FreshAirUtils.initArguments(this);
        this.feature = feature;
        getArguments().putParcelable(KEY_FEATURE, feature);
    }

    protected FeatureInfo getFeature() {
        if (feature == null) {
            feature = getArguments().getParcelable(KEY_FEATURE);
        }

        return feature;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feature, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Context context = view.getContext();
        final FeatureInfo feature = getFeature();

        ((TextView) view.findViewById(R.id.fragment_feature_title))
                .setText(feature.getTitle(context));
        ((TextView) view.findViewById(R.id.fragment_feature_description))
                .setText(feature.getDescription(context));

        feature.populateImage((ImageView) view.findViewById(R.id.fragment_feature_image));
    }

}
