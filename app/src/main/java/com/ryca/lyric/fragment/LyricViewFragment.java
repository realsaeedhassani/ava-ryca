package com.ryca.lyric.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ryca.lyric.AppController;
import com.ryca.lyric.R;
import com.ryca.lyric.Utils.CONSTANT;
import com.ryca.lyric.ViewLyric.Lrc;
import com.ryca.lyric.ViewLyric.LrcHelper;
import com.ryca.lyric.ViewLyric.LrcView;
import com.ryca.lyric.api.Api;
import com.ryca.lyric.api.ServiceGenerator;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricViewFragment extends Fragment
        implements
        View.OnTouchListener,
        MediaPlayer.OnCompletionListener,
        MediaPlayer.OnBufferingUpdateListener {

    private static final String TAG = LyricViewFragment.class.getName();
    private final Handler handler = new Handler();
    private int mediaFileLengthInMilliseconds;
    private View view;
    private boolean repeate = false;
    private FloatingActionButton fab, fabRepeat;
    private Context mContext;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private LrcView mLrcView;
    private SeekBar mSeekBar;
    private TextView mTvStart;
    private List<Lrc> mLRC;
    private String SINGER, ALBUM;

    public LyricViewFragment(Context context, String name, int mAid, int mSid) {
        SINGER = String.valueOf(mSid);
        ALBUM = String.valueOf(mAid);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lyric, container, false);
        initView();
        getData();
        return view;
    }

    private void getData() {
        Api service = null;
        try {
            service = ServiceGenerator.createService(Api.class);
        } catch (KeyManagementException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        primarySeekBarProgressUpdater();

        Call<ResponseBody> call = service.getLrc(SINGER, ALBUM, ALBUM + ".lrc");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    mLRC = LrcHelper.parseInputStream(response.body().byteStream());
                    mLrcView.setLrcData(mLRC);
                    mLrcView.setOnPlayIndicatorLineListener((time, content) -> mMediaPlayer.seekTo((int) time));
                } catch (Exception ignored) {
                    Toasty.info(mContext, mContext.getString(R.string.not_done), Toasty.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void primarySeekBarProgressUpdater() {
        try {
            mSeekBar.setProgress((int) (((float)
                    mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
            mLrcView.updateTime(mMediaPlayer.getCurrentPosition());
            mTvStart.setText(LrcHelper.formatTime(mMediaPlayer.getCurrentPosition()));
            if (mMediaPlayer.isPlaying()) {
                Runnable notification = this::primarySeekBarProgressUpdater;
                handler.postDelayed(notification, 80);
            }
        } catch (Exception ignored) {
        }
    }

    private void initView() {
        fab = view.findViewById(R.id.fab);
        fabRepeat = view.findViewById(R.id.fab_repeat);
        mTvStart = view.findViewById(R.id.tv_start);
        mSeekBar = view.findViewById(R.id.seek_play);
        mSeekBar.setMax(99);
        mSeekBar.setOnTouchListener(this);

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);
        HttpProxyCacheServer proxy = AppController.getProxy(mContext);
        String proxyUrl = proxy.getProxyUrl(CONSTANT.BASE_URL + "/files/"
                + SINGER + "/"
                + ALBUM + "/"
                + ALBUM + ".mp3");
        try {
            mMediaPlayer.setDataSource(proxyUrl);
            mMediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaFileLengthInMilliseconds = mMediaPlayer.getDuration();
        fab.setOnClickListener(v -> {
            try {


                if (!mMediaPlayer.isPlaying()) {
                    mMediaPlayer.start();
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                            android.R.drawable.ic_media_pause));

                } else {
                    mMediaPlayer.pause();
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                            android.R.drawable.ic_media_play));
                }
                primarySeekBarProgressUpdater();
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        fabRepeat.setOnClickListener(v -> {
            if (repeate) {
                mMediaPlayer.setLooping(false);
                fabRepeat.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.ic_repeat));
                repeate = false;
            } else {
                mMediaPlayer.setLooping(true);
                fabRepeat.setImageDrawable(ContextCompat.getDrawable(mContext,
                        R.drawable.ic_repeat_active));
                repeate = true;
            }
        });
        mLrcView = view.findViewById(R.id.lrc_view);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.seek_play) {
            if (mMediaPlayer.isPlaying()) {
                SeekBar sb = (SeekBar) v;
                int playPositionInMillisecconds = (mediaFileLengthInMilliseconds / 100) * sb.getProgress();
                mMediaPlayer.seekTo(playPositionInMillisecconds);
            }
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                android.R.drawable.ic_media_play));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        mSeekBar.setSecondaryProgress(percent);
    }
}
