package com.lauzy.freedom.lyricview.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.MyDividerItemDecoration;
import com.lauzy.freedom.lyricview.acitivity.LyricActivity;
import com.lauzy.freedom.lyricview.adapter.MusicAdapter;
import com.lauzy.freedom.lyricview.model.Music;

import java.util.ArrayList;
import java.util.List;

public class AlbumsFragment extends Fragment implements MusicAdapter.MusicAdapterListener {
    View view;
    private Activity mActivity;
    private int mId;
    private RecyclerView recyclerView;
    private List<Music> mMusicList;
    private MusicAdapter mAdapter;

    public AlbumsFragment() {
    }

    public AlbumsFragment(Activity activity, int id) {
        this.mActivity = activity;
        this.mId = id;
        Log.e(">> ID: ", String.valueOf(id));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        mMusicList = new ArrayList<>();
        for (int i = 1; i < 3; ++i) {
            Music c = new Music();
            c.setId(i);
            if (i == 1)
                c.setName("d");
            else
                c.setName("d" + i);
            c.setSinger("آهنگ شماره " + i);
            c.setUrl("https://static.cdn.asset.aparat.com/profile-photo/506723-s.jpg");
            mMusicList.add(c);
        }
        mAdapter = new MusicAdapter(mActivity.getBaseContext(), mMusicList, this);

        // white background notification bar
//        whiteNotificationBar(recyclerView);

        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(mActivity.getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(mActivity.getBaseContext(),
                DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onContactSelected(Music contact) {
        Intent intent = new Intent(mActivity, LyricActivity.class);
        intent.putExtra("ID", contact.getId());
        intent.putExtra("NAME", contact.getName());
        startActivity(intent);
        mActivity.finish();
    }
}
