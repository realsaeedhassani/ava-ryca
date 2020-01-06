package com.lauzy.freedom.lyricview.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.model.Music;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Music> contactList;
    private List<Music> contactListFiltered;
    private MusicAdapterListener listener;

    public MusicAdapter(Context context, List<Music> contactList, MusicAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_album, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Music contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getSinger());

        Glide.with(context)
                .load(contact.getUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return contactListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    contactListFiltered = contactList;
                } else {
                    List<Music> filteredList = new ArrayList<>();
                    for (Music row : contactList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or date number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getUrl().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    contactListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = contactListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                contactListFiltered = (ArrayList<Music>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MusicAdapterListener {
        void onContactSelected(Music contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(view1 -> {
                // send selected contact in callback
                listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
            });
        }
    }
}