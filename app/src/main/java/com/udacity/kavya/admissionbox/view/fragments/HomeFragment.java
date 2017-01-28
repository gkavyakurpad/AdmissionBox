package com.udacity.kavya.admissionbox.view.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.udacity.kavya.admissionbox.R;
import com.udacity.kavya.admissionbox.constants.AppConstants;
import com.udacity.kavya.admissionbox.databse.DataAccessImpl;
import com.udacity.kavya.admissionbox.model.Org;
import com.udacity.kavya.admissionbox.view.activites.OrgranizationActivity;
import com.udacity.kavya.admissionbox.view.adapter.HomeAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 310243577 on 12/27/2016.
 */

public class HomeFragment extends Fragment implements HomeAdapter.OrgClickHandler, FragmentManager.OnBackStackChangedListener {

    @Bind(R.id.recycler_view_movie)
    RecyclerView recyclerView;
    @Bind(R.id.tvNodata)
    TextView tvNodata;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabase;
    private ChildEventListener mChildEventListener;
    private HomeAdapter mHomeAdapter;
    private ArrayList<Org> mOrgList = new ArrayList<>();
    private ActionBar mActionBar;

    private GridLayoutManager mlayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().supportInvalidateOptionsMenu();
        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (mActionBar != null)
            mActionBar.setTitle("AdmissionBox");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().getSupportFragmentManager().addOnBackStackChangedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        ((OrgranizationActivity) getActivity()).hideUpButton();

        mlayoutManager = new GridLayoutManager(getActivity(), 1);
        //Setup layout manager to Recycler View
        recyclerView.setLayoutManager(mlayoutManager);
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabase = mFirebaseDatabase.getReference().child("org");

        mHomeAdapter = new HomeAdapter(getActivity().getApplicationContext(), mOrgList, this);
        recyclerView.setAdapter(mHomeAdapter);

        //Restore saved instance
        if (savedInstanceState != null && savedInstanceState.containsKey(AppConstants.EXTRA_ORG)) {
            mOrgList = savedInstanceState.getParcelableArrayList(AppConstants.EXTRA_ORG);
            updateOrgList(mOrgList);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        attachDatabaseReadListener();
    }

    private void attachDatabaseReadListener() {
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Org org = dataSnapshot.getValue(Org.class);
                    mOrgList.add(org);
                    mHomeAdapter.notifyDataSetChanged();
                }

                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                }

                public void onChildRemoved(DataSnapshot dataSnapshot) {
                }

                public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                }

                public void onCancelled(DatabaseError databaseError) {
                }
            };
            mDatabase.addChildEventListener(mChildEventListener);
        }
    }

    public void updateOrgList(ArrayList<Org> orgList) {
        if (orgList != null && orgList.size() > 0) {
            this.mOrgList = orgList;
            mHomeAdapter.updateList(getActivity(), orgList);
            //TODO
            //updating sqlLite
            DataAccessImpl sql = new DataAccessImpl();
            sql.insertSqlLite(this.mOrgList);


            recyclerView.setVisibility(View.VISIBLE);
            tvNodata.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.GONE);
            tvNodata.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(AppConstants.EXTRA_ORG, (ArrayList<? extends Parcelable>) mOrgList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(HomeAdapter.OrgViewHolder orgViewHolder, Org model) {

        Fragment fragment = new OrgDetailFragment();
        Bundle arg = new Bundle();
        arg.putParcelable("RESULT_MODEL", model);
        fragment.setArguments(arg);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.root_frame, fragment, "DETAIL FRAG");
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }


    @Override
    public void onBackStackChanged() {
        // enable Up button only  if there are entries on the backstack
        if (getActivity().getSupportFragmentManager().getBackStackEntryCount() < 1) {
            ((OrgranizationActivity) getActivity()).hideUpButton();
        }
    }
}
