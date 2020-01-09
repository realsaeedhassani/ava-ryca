package com.ryca.lyric.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.eloyzone.jalalicalendar.DateConverter;
import com.github.eloyzone.jalalicalendar.JalaliDate;
import com.github.eloyzone.jalalicalendar.JalaliDateFormatter;
import com.ryca.lyric.R;
import com.ryca.lyric.model.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.content.Context.MODE_PRIVATE;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private static final String PREFS_NAME = "CLIENT";
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

    public void addAll(List<Comment> moveResults) {
        for (Comment result : moveResults) {
            add(result);
        }
    }

    private void add(Comment movie) {
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

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_comment, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Comment contact = contactListFiltered.get(position);

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String city = prefs.getString("email", null);

        if (contact.getCity() != null) {
            holder.name.setText(contact.getName()
                    + " " + context.getString(R.string.of)
                    + " " + contact.getCity());
        } else
            holder.name.setText(contact.getName());
        holder.rate.setText(String.valueOf(contact.getRate()));
        if (contact.getRate()<3)
            holder.rate.setBackgroundResource(R.drawable.rb_rate_0);
        else if (contact.getRate()>4)
            holder.rate.setBackgroundResource(R.drawable.rb_rate_2);
        else
            holder.rate.setBackgroundResource(R.drawable.rb_rate_1);
        holder.comment.setText(contact.getComment());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        long timeStamp = 0;
        try {
            timeStamp = sdf.parse( contact.getCreatedAt()).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat currentDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String time =currentDateFormat.format(timeStamp);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date2 = null;
        try {
            date2 = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        df.setTimeZone(TimeZone.getDefault());
        String mDate = df.format(date2);

        String[] date = mDate.split(" ")[0].split("-");
        DateConverter dateConverter = new DateConverter();
        JalaliDate jalaliDate = dateConverter.gregorianToJalali
                (Integer.parseInt(date[0])
                        , Integer.parseInt(date[1])
                        , Integer.parseInt(date[2]));

        holder.date.setText(jalaliDate.format
                (new JalaliDateFormatter("yyyy- M dd",
                        JalaliDateFormatter.FORMAT_IN_PERSIAN)) + " " +
                mDate.split(" ")[1]);
    }

    public interface CommentAdapterListener {
        void onContactSelected(Comment contact);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, date, comment, rate;

        MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            date = view.findViewById(R.id.date);
            comment = view.findViewById(R.id.comment);
            rate = view.findViewById(R.id.rate);

            view.setOnClickListener(view1 -> {
                listener.onContactSelected(contactListFiltered.get(getAdapterPosition()));
            });
        }
    }
}