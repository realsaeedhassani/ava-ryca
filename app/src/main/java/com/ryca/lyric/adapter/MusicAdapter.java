package com.ryca.lyric.adapter;

import android.annotation.SuppressLint;
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
import com.ryca.lyric.R;
import com.ryca.lyric.Utils.CONSTANT;
import com.ryca.lyric.model.Album;

import java.util.ArrayList;
import java.util.List;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private int mSid;
    private List<Album> musicList;
    private List<Album> musicListFilter;
    private MusicAdapterListener listener;

    public MusicAdapter(Context context,
                        int sid,
                        List<Album> contactList,
                        MusicAdapterListener listener) {
        this.mSid = sid;
        this.context = context;
        this.listener = listener;
        this.musicList = contactList;
        this.musicListFilter = contactList;
    }

    public void addAll(List<Album> moveResults) {
        for (Album result : moveResults) {
            add(result);
        }
    }

    private void add(Album movie) {
        musicList.add(movie);
        notifyItemInserted(musicList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return musicList == null ? 0 : musicList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_album, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Album contact = musicListFilter.get(position);
        holder.name.setText(contact.getName());
        holder.cc.setText(context.getString(R.string.num_com) + " " + contact.getCc());
        double rate = Double.parseDouble(contact.getRate());
        if (rate < 3)
            holder.score.setBackgroundResource(R.drawable.round_count_0);
        else if (rate > 4)
            holder.score.setBackgroundResource(R.drawable.round_count_2);
        else
            holder.score.setBackgroundResource(R.drawable.round_count_1);

        holder.score.setText(String.format("%.2f", Double.parseDouble(contact.getRate())));

        Glide.with(context)
                .load(
                        CONSTANT.BASE_URL + "/files/"
                                + mSid + "/"
                                + contact.getId() + "/"
                                + contact.getId() + ".png"
                )
                .apply(RequestOptions.circleCropTransform())
                .into(holder.thumbnail);
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
                    List<Album> filteredList = new ArrayList<>();
                    for (Album row : musicList) {
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                musicListFilter = (ArrayList<Album>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MusicAdapterListener {
        void onContactSelected(Album contact);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, score, rate, cc;
        ImageView thumbnail;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            score = view.findViewById(R.id.score);
            cc = view.findViewById(R.id.cc);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(view1 -> {
                // send selected contact in callback
                listener.onContactSelected(musicListFilter.get(getAdapterPosition()));
            });
        }
    }
}