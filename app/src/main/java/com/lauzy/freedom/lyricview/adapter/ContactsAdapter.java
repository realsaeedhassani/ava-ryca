package com.lauzy.freedom.lyricview.adapter;

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
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.CONSTANT;
import com.lauzy.freedom.lyricview.model.Singer;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Singer> contactList;
    private List<Singer> contactListFiltered;
    private ContactsAdapterListener listener;

    public ContactsAdapter(Context context, List<Singer> contactList, ContactsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.contactList = contactList;
        this.contactListFiltered = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_singer, parent, false);

        return new MyViewHolder(itemView);
    }

    public void addAll(List<Singer> moveResults) {
        for (Singer result : moveResults) {
            add(result);
        }
    }

    private void add(Singer movie) {
        contactList.add(movie);
        notifyItemInserted(contactList.size() - 1);
    }

    @Override
    public int getItemCount() {
        return contactList == null ? 0 : contactList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Singer contact = contactListFiltered.get(position);
        holder.name.setText(contact.getName());
        holder.phone.setText(context.getString(R.string.num_track) + " "
                + contact.getCount());
        Glide.with(context)
                .load(CONSTANT.BASE_URL
                        + "/files/"
                        + contact.getId() + "/"
                        + contact.getId() + ".png")
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
                    contactListFiltered = contactList;
                } else {
                    List<Singer> filteredList = new ArrayList<>();
                    for (Singer row : contactList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or date number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
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
                contactListFiltered = (ArrayList<Singer>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ContactsAdapterListener {
        void onContactSelected(Singer contact);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            phone = view.findViewById(R.id.phone);
            thumbnail = view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
}