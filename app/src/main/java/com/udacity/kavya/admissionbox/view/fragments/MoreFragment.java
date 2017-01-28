package com.udacity.kavya.admissionbox.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.kavya.admissionbox.R;
import com.udacity.kavya.admissionbox.databse.Preference;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MoreFragment extends Fragment {

    @Bind(R.id.imageposter)
    ImageView imageposter;
    @Bind(R.id.txtemil)
    TextView txtemil;
    @Bind(R.id.txtname)
    TextView txtname;
    private FirebaseStorage storage;
    private StorageReference storageReference;


    public MoreFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_more, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://admissionbox-81008.appspot.com").child("school1.jpeg");
        // Inflate the layout for this fragment
        ButterKnife.bind(this, view);
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(imageposter);

        txtemil.setText(Preference.getInstance(getActivity()).getUserEmail());
        txtname.setText(Preference.getInstance(getActivity()).getUserName());
        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
