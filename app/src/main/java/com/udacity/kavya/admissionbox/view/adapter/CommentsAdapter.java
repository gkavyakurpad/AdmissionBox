package com.udacity.kavya.admissionbox.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.kavya.admissionbox.R;
import com.udacity.kavya.admissionbox.model.Comments;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by 310243577 on 1/22/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.CommentViewHolder> {

    private List<Comments> mresultListModel;
    private Context mContext;

    public CommentsAdapter(Context context, List<Comments> model) {
        this.mresultListModel = model;
        this.mContext = context;
    }


    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comments, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        if (mresultListModel != null && mresultListModel.size() > 0) {
            holder.textviewUserName.setText(mresultListModel.get(position).getEmail());
            holder.textviewComment.setText(mresultListModel.get(position).getComment());
        }
    }

    @Override
    public int getItemCount() {
        if (mresultListModel != null)
            return mresultListModel.size();
        else
            return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.textview_userName)
        TextView textviewUserName;
        @Bind(R.id.textview_comment)
        TextView textviewComment;
        @Bind(R.id.moviecard)
        CardView moviecard;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
