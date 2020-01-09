package com.lauzy.freedom.lyricview.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.lauzy.freedom.lyricview.api.Api;
import com.lauzy.freedom.lyricview.api.ServiceGenerator;
import com.lauzy.freedom.lyricview.model.Album;
import com.lauzy.freedom.lyricview.model.DatumAlbum;
import com.lauzy.freedom.lyricview.model.PaginateAlbum;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumsFragment extends Fragment implements MusicAdapter.MusicAdapterListener {
    private static final int PAGE_START = 1;
    View view;
    private Activity mActivity;
    private int mId, mSID;
    private String mSinger;
    private RecyclerView recyclerView;
    private List<Album> mMusicList;
    private MusicAdapter mAdapter;
    private int currentPage = PAGE_START;
    private boolean loading = true;
    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    public AlbumsFragment() {
    }

    AlbumsFragment(Activity activity,
                   int id,
                   String name) {
        this.mActivity = activity;
        this.mId = id;
        this.mSinger = name;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_album, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        mMusicList = new ArrayList<>();
        mAdapter = new MusicAdapter(mActivity.getBaseContext(),
                mId,
                mMusicList,
                this);

        // white background notification bar
        // whiteNotificationBar(recyclerView);

        LinearLayoutManager mLayoutManager =
                new LinearLayoutManager(mActivity.getBaseContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(mActivity.getBaseContext(),
                DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            ++currentPage;
                            loadNextPage();
                        }
                    }
                }
            }
        });
        loadFirstPage();
        return view;
    }

    private void loadNextPage() {
        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Call<DatumAlbum> call = service.getAlbum(mId, currentPage);
        call.enqueue(new Callback<DatumAlbum>() {
            @Override
            public void onResponse(Call<DatumAlbum> call,
                                   Response<DatumAlbum> response) {
                try {
                    DatumAlbum results = response.body();
                    if (results != null) {
                        mAdapter.addAll(results.getData());
                    } else loading = false;
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<DatumAlbum> call, Throwable t) {
            }
        });
    }

    private void loadFirstPage() {
        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        Call<DatumAlbum> call = service.getAlbum(mId, currentPage);
        call.enqueue(new Callback<DatumAlbum>() {
            @Override
            public void onResponse(Call<DatumAlbum> call, Response<DatumAlbum> response) {
                try {
                    DatumAlbum results = response.body();
                    if (results != null) {
                        mAdapter.addAll(results.getData());
                    } else loading = false;
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<DatumAlbum> call, Throwable t) {
            }
        });
    }

    @Override
    public void onContactSelected(Album contact) {
        Intent intent = new Intent(mActivity, LyricActivity.class);
        intent.putExtra("ID", contact.getId());
        intent.putExtra("SID", mId);
        intent.putExtra("NAME", contact.getName());
        intent.putExtra("SCORE", contact.getRate());
        intent.putExtra("SINGER", mSinger);
        startActivity(intent);
        mActivity.overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        mActivity.finish();
    }
}
