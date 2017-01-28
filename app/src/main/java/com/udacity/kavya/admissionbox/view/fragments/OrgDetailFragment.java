package com.udacity.kavya.admissionbox.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.udacity.kavya.admissionbox.R;
import com.udacity.kavya.admissionbox.databse.Preference;
import com.udacity.kavya.admissionbox.model.Comments;
import com.udacity.kavya.admissionbox.model.Likes;
import com.udacity.kavya.admissionbox.model.Org;
import com.udacity.kavya.admissionbox.view.activites.ChatsActivity;
import com.udacity.kavya.admissionbox.view.activites.OrgranizationActivity;
import com.udacity.kavya.admissionbox.view.adapter.CommentsAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class OrgDetailFragment extends Fragment implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    @Bind(R.id.toolbar_image_backdrop)
    ImageView toolbarImageBackdrop;

    @Bind(R.id.appbar)
    AppBarLayout appbar;
    @Bind(R.id.txtOrgdesc)
    TextView txtOrgdesc;
    @Bind(R.id.btLikes)
    Button btLikes;
    @Bind(R.id.btComments)
    Button btComments;
    @Bind(R.id.commentList)
    RecyclerView commentList;

    private Org mOrgModel;
    private String orgId;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseDatabase mFirebaseDatabase;
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "OrgDetailFragment";
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    public static final String ANONYMOUS = "anonymous";
    private GoogleApiClient mGoogleApiClient;
    private String mUsername;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private DatabaseReference mMessagesDatabaseReference, mCommentsReference;
    private ChildEventListener likesDatabaseReference;
    int likescount = 0;

    public OrgDetailFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mOrgModel.getName());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://admissionbox-81008.appspot.com").child("school1.jpeg");
        Glide.with(getActivity())
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(toolbarImageBackdrop);
        orgId = mOrgModel.getOrgId();
        txtOrgdesc.setText(mOrgModel.getDesc());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();

        mMessagesDatabaseReference = mFirebaseDatabase.getReference().child("likes").child(orgId);
        mCommentsReference = mFirebaseDatabase.getReference().child("comments").child(orgId);
        attachDatabaseReadListener();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((OrgranizationActivity) getActivity()).showUpButton();
        // Initialize Firebase components

    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orgdetail, container, false);
        ButterKnife.bind(this, view);
        mFirebaseAuth = FirebaseAuth.getInstance();
        GridLayoutManager mlayoutManager = new GridLayoutManager(getActivity(), 1);
        //Setup layout manager to Recycler View
        commentList.setLayoutManager(mlayoutManager);

        Bundle b = this.getArguments();
        if (b != null) {
            mOrgModel = b.getParcelable("RESULT_MODEL");
        }

        if(mOrgModel != null && mOrgModel.getComments().size() > 0) {
            CommentsAdapter commentsAdapter = new CommentsAdapter(getActivity().getApplicationContext(), mOrgModel.getComments());
            commentList.setAdapter(commentsAdapter);
        }

        mUsername = ANONYMOUS;
        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API)
                    .build();
        } else {
            mGoogleApiClient.stopAutoManage(getActivity());
        }

        btLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivityForResult(new Intent(getActivity(), SignInActivity.class), 1001);
                mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                setLikes();
            }
        });

        btComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseAuth.addAuthStateListener(mAuthStateListener);
                setComments();
            }
        });
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    onSignedInInitialize(user.getEmail());
                    if (getActivity() != null) {
                        Preference.getInstance(getActivity()).setuserEmail(user.getEmail());
                        Preference.getInstance(getActivity()).setuserName(user.getDisplayName());
                    }
                    mUsername = user.getEmail();
                } else {
                    // User is signed out
                    onSignedOutCleanup();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(
                                            AuthUI.EMAIL_PROVIDER,
                                            AuthUI.GOOGLE_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getActivity(), ChatsActivity.class);
                intent.putExtra("username", mUsername);
                intent.putExtra("orgID", orgId);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    private void setComments() {
        comment("Comment");
    }

    private void setLikes() {
        Likes likes = new Likes();
        likes.setUsername(mUsername);
        mMessagesDatabaseReference.push().setValue(likes);
//        if (mOrgModel.getLikes() != null) {
//            btLikes.setText(mOrgModel.getLikes().size() + likescount++ + " " + "likes");
//        } else {
//            btLikes.setText(likescount++ + " " + "like");
//        }
    }

    private void onSignedInInitialize(String username) {
        mUsername = username;

    }

    private void onSignedOutCleanup() {
        mUsername = ANONYMOUS;
    }


    void comment(String title) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.layout_commentbox);
        dialog.setTitle(title);
        final EditText comments = (EditText) dialog.findViewById(R.id.editTextComments);
        Button dialogButton = (Button) dialog.findViewById(R.id.buttonSave);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == comments.getText() || comments.getText().toString().isEmpty()) {

                } else {
                    Comments c = new Comments();
                    c.setComment(comments.getText().toString());
                    c.setEmail(mUsername);
                    mCommentsReference.push().setValue(c);
                    ArrayList<Comments> commentses = mOrgModel.getComments();
                    commentses.add(c);
                    CommentsAdapter commentsAdapter = new CommentsAdapter(getActivity().getApplicationContext(), commentses);
                    commentList.setAdapter(commentsAdapter);
                    commentsAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });

        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(getActivity(), "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(getActivity(), "Sign in canceled", Toast.LENGTH_SHORT).show();
                getActivity().finish();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    private void attachDatabaseReadListener() {
        if (likesDatabaseReference == null) {
            likesDatabaseReference = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Long likes = dataSnapshot.getChildrenCount();
                    likescount++;
                    if (btLikes != null)
                        btLikes.setText(likescount + " " + "likes");
                    //mHomeAdapter.notifyDataSetChanged();
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
            mMessagesDatabaseReference.addChildEventListener(likesDatabaseReference);
        }
    }

}
