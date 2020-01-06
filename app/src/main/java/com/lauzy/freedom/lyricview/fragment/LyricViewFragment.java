package com.lauzy.freedom.lyricview.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.AudioStreamWorkerTask;
import com.lauzy.freedom.lyricview.Utils.OnCacheCallback;
import com.lauzy.freedom.lyricview.ViewLyric.Lrc;
import com.lauzy.freedom.lyricview.ViewLyric.LrcHelper;
import com.lauzy.freedom.lyricview.ViewLyric.LrcView;
import com.lauzy.freedom.lyricview.api.Api;
import com.lauzy.freedom.lyricview.api.ServiceGenerator;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricViewFragment extends Fragment {
    private static final String TAG = LyricViewFragment.class.getName();
    private ServiceGenerator serviceGenerator;
    private View view;
    private boolean wasPlaying = false, repeate = false;
    private FloatingActionButton fab, fabRepeat;
    private Context mContext;
    private MediaPlayer mMediaPlayer = new MediaPlayer();
    private LrcView mLrcView;
    private Handler mHandler = new Handler();
    private SeekBar mSeekBar;
    private TextView mTvStart;
    private List<Lrc> mLRC;
    private String mName;
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                int currentPosition = mMediaPlayer.getCurrentPosition();
                mLrcView.updateTime(currentPosition);
                mSeekBar.setProgress(currentPosition);
                mTvStart.setText(LrcHelper.formatTime(currentPosition));
                mHandler.postDelayed(this, 100);
            } catch (IllegalStateException ignored) {
            }
        }
    };

    public LyricViewFragment(Context context, String name) {
        mName = name;
        mContext = context;
        serviceGenerator = ServiceGenerator.getInstance(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lyric, container, false);
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
        Call<ResponseBody> call = service.getLrc(mName + ".lrc");
        Log.e(">> URL: ", call.request().url() + " ");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    mLRC = LrcHelper.parseInputStream(response.body().byteStream());
                    init();
                    play();
                } catch (Exception ignored) {
                    Log.e("ERROR_", " " + ignored.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });
    }

    private void play() {

        try {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                clearMediaPlayer();
                mSeekBar.setProgress(0);
                wasPlaying = true;
                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        android.R.drawable.ic_media_play));
            }
            if (!wasPlaying) {
                if (mMediaPlayer == null) {
                    mMediaPlayer = new MediaPlayer();
                }
                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                        android.R.drawable.ic_media_pause));

                new AudioStreamWorkerTask(mContext, new OnCacheCallback() {
                    @Override
                    public void onSuccess(FileInputStream fileInputStream) {
                        if (fileInputStream != null) {
                            mMediaPlayer = new MediaPlayer();
                            try {
                                mMediaPlayer.setDataSource(fileInputStream.getFD());
                                mMediaPlayer.prepare();
                                mMediaPlayer.setVolume(1f, 1f);
                                mMediaPlayer.setLooping(false);
                                mMediaPlayer.start();
                                mSeekBar.setMax(mMediaPlayer.getDuration());
                                mHandler.post(mRunnable);
                                fileInputStream.close();
                            } catch (IOException | IllegalStateException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Log.e(getClass().getSimpleName() + ".MediaPlayer", "fileDescriptor is not valid");
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(getClass().getSimpleName() + ".MediaPlayer", "Can't play audio file");
                    }
                }).execute("http://3d4.ir/files/d.mp3");
            }
            wasPlaying = false;
        } catch (Exception e) {
            Log.e("ERROR_", " " + e.getMessage());
        }
    }

    private void init() {
        try {
            fab = view.findViewById(R.id.fab);
            fabRepeat = view.findViewById(R.id.fab_repeat);
            mSeekBar = view.findViewById(R.id.seek_play);
            mTvStart = view.findViewById(R.id.tv_start);
            fab.setOnClickListener(v -> {
                if (wasPlaying) {
                    mMediaPlayer.pause();
                    mLrcView.pause();
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                            android.R.drawable.ic_media_play));
                    wasPlaying = false;
                } else {
                    if (mMediaPlayer != null) {
                        mMediaPlayer.start();
                        mLrcView.resume();
                    }
                    fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                            android.R.drawable.ic_media_pause));
                    wasPlaying = true;
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
            mLrcView.setLrcData(mLRC);
            mLrcView.setOnPlayIndicatorLineListener((time, content) -> mMediaPlayer.seekTo((int) time));
            mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.e(">> PROGRESS: " , String.valueOf(progress));
                    if (fromUser) {
                        mHandler.removeCallbacks(mRunnable);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    mHandler.post(mRunnable);
                    Log.e(">> TrackingTouch: " , String.valueOf(seekBar.getProgress()));
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            });

        } catch (Exception ex) {
            Log.e(">> Error: ", ex.getMessage() + " ");
        }
    }

    private void clearMediaPlayer() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
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
}
