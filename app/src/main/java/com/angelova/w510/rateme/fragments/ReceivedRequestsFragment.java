package com.angelova.w510.rateme.fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angelova.w510.rateme.R;

/**
 * Created by W510 on 11.8.2018 г..
 */

public class ReceivedRequestsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_received_requests, container, false);

        return rootView;
    }
}
