package com.lauzy.freedom.lyricview.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lauzy.freedom.lyricview.Utils.MyDividerItemDecoration;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.adapter.CommentAdapter;
import com.lauzy.freedom.lyricview.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentsFragment extends Fragment implements CommentAdapter.CommentAdapterListener {
    private View view;
    private Context mContext;
    private int mId;
    private RecyclerView recyclerView;
    private List<Comment> mMusicList;
    private CommentAdapter mAdapter;

    public CommentsFragment() {
    }

    public CommentsFragment(Context context) {
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        mMusicList = new ArrayList<>();
        for (int i = 1; i < 10; ++i) {
            Comment c = new Comment();
            c.setId(i);
            c.setName("نام:‌ " + i);
            c.setDate("تاریخ:‌ ۰۷/۱۰/۱۳۹۸");
            c.setRate(i % 5 + 1);
            c.setComment("در بخش نظر کاربر قرار میگیرد. نظر کاربران برای انتشار");
            mMusicList.add(c);
        }
        mAdapter = new CommentAdapter(mContext, mMusicList, this);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onContactSelected(Comment contact) {
        // Replay to comment
    }
}
