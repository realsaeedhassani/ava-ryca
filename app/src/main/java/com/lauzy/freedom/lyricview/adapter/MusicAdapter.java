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
    private List<Music> musicList;
    private List<Music> musicListFilter;
    private MusicAdapterListener listener;

    public MusicAdapter(Context context, List<Music> contactList, MusicAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.musicList = contactList;
        this.musicListFilter = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_album, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Music contact = musicListFilter.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(contact.getSinger());

        Glide.with(context)
                .load(contact.getUrl())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return musicListFilter.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    musicListFilter = musicList;
                } else {
                    List<Music> filteredList = new ArrayList<>();
                    for (Music row : musicList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getUrl().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    musicListFilter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = musicListFilter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                musicListFilter = (ArrayList<Music>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MusicAdapterListener {
        void onContactSelected(Music contact);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(view1 -> {
                // send selected contact in callback
                listener.onContactSelected(musicListFilter.get(getAdapterPosition()));
            });
        }
    }
}