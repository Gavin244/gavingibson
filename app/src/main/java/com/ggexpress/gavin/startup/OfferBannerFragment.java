package com.ggexpress.gavin.startup;


import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import 	androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggexpress.gavin.R;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * A simple {@link Fragment} subclass.
 */
public class OfferBannerFragment extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section-icon";
    private static final String ARG_SECTION_COLOR = "section-color";

    public static OfferBannerFragment newInstance(int color, String icon) {
        OfferBannerFragment fragment = new OfferBannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_NUMBER, icon);
        args.putInt(ARG_SECTION_COLOR, color);
        fragment.setArguments(args);
        return fragment;
    }

    public OfferBannerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_slider_image, container, false);
        rootView.setBackgroundColor(ContextCompat.getColor(getContext(), getArguments().getInt(ARG_SECTION_COLOR)));
        SimpleDraweeView image =  rootView.findViewById(R.id.iv_icon);
        Uri uri = Uri.parse(getArguments().getString(ARG_SECTION_NUMBER));
        image.setImageURI(uri);
//        Glide.with(getActivity())
//                .load(getArguments().getString(ARG_SECTION_NUMBER))
//                .into(image);
        return rootView;

    }

}
