package com.udacity.kavya.admissionbox.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.kavya.admissionbox.R;

import java.util.List;


public class RootFragment extends Fragment {

    private static final String TAG = "RootFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /* Inflate the layout for this fragment */
        View view = inflater.inflate(R.layout.root_fragment, container, false);

        FragmentTransaction transaction = getFragmentManager()
                .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
        transaction.replace(R.id.root_frame, new HomeFragment(), "HOME FRAG");
        transaction.addToBackStack(null);

        transaction.commit();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> frags = getActivity().getSupportFragmentManager().getFragments();
        if (frags != null) {
            for (Fragment f : frags) {
                if (f != null)
                    handleResult(f, requestCode, resultCode, data);
            }
        }
    }

    private void handleResult(Fragment frag, int requestCode, int resultCode, Intent data) {
        if (frag instanceof OrgDetailFragment) { // custom interface with no signitures
            FragmentTransaction transaction = getFragmentManager()
                    .beginTransaction();
		/*
		 * When this container fragment is created, we fill it with our first
		 * "real" fragment
		 */
            transaction.replace(R.id.root_frame, new OrgDetailFragment(), "ORG FRAG");
            transaction.addToBackStack(null);

            transaction.commit();
        }
    }

}
