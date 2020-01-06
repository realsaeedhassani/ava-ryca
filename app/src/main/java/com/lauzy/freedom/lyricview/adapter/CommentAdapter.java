package com.lauzy.freedom.lyricview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.SvgRatingBar;
import com.lauzy.freedom.lyricview.model.Comment;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private Context context;
    private List<Comment> contactList;
    private List<Comment> contactListFiltered;
    private CommentAdapterListener listener;

    public CommentAdapter(Context context, List<Comment> contactList, CommentAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Comment contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.date.setText(contact.getDate());
        holder.svgRatingBar.setRating(contact.getRate());
        holder.comment.setText(contact.getComment());
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }


    public interface CommentAdapterListener {
        void onContactSelected(Comment contact);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, comment;
        private SvgRatingBar svgRatingBar;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            comment = view.findViewById(R.id.comment);
            svgRatingBar = view.findViewById(R.id.rate);

            view.setOnClickListener(view1 -> {
                listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
            });
        }
    }
}