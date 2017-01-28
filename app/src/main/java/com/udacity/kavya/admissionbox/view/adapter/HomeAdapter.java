package com.udacity.kavya.admissionbox.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
import com.udacity.kavya.admissionbox.model.Org;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.OrgViewHolder> {

    private List<Org> mresultListModel;
    private Context mContext;
    private OrgClickHandler orgOnClickHandler;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    public HomeAdapter(Context context, List<Org> model, OrgClickHandler clickHandler) {
        this.mresultListModel = model;
        this.mContext = context;
        this.orgOnClickHandler = clickHandler;
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReferenceFromUrl("gs://admissionbox-81008.appspot.com").child("school1.jpeg");
    }

    @Override
    public OrgViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_home, parent, false);
        return new OrgViewHolder(view);
    }

    public void updateList(Context c, List<Org> movies) {
        this.mContext = c;
        this.mresultListModel = movies;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(OrgViewHolder holder, int position) {
        holder.orgName.setText(mresultListModel.get(position).getName());
        holder.orgAdress.setText(mresultListModel.get(position).getShortDesc());
        // Load the image using Glide
        Glide.with(mContext)
                .using(new FirebaseImageLoader())
                .load(storageReference)
                .into(holder.imageviewPoster);
    }

    @Override
    public int getItemCount() {
        if (mresultListModel != null)
            return mresultListModel.size();
        else
            return 0;
    }

    public interface OrgClickHandler {
        void onClick(OrgViewHolder orgViewHolder, Org model);
    }


    public class OrgViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.imageview_poster)
        ImageView imageviewPoster;
        @Bind(R.id.orgName)
        TextView orgName;
        @Bind(R.id.orgAdress)
        TextView orgAdress;
        @Bind(R.id.moviecard)
        CardView moviecard;

        public OrgViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (orgOnClickHandler != null) {
                Org resultModel = mresultListModel.get(getAdapterPosition());
                orgOnClickHandler.onClick(this, resultModel);
            }
        }
    }
}
