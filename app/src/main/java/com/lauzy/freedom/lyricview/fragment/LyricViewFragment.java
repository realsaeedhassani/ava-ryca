package com.lauzy.freedom.lyricview.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.CONSTANT;
import com.lauzy.freedom.lyricview.ViewLyric.Lrc;
import com.lauzy.freedom.lyricview.ViewLyric.LrcHelper;
import com.lauzy.freedom.lyricview.ViewLyric.LrcView;
import com.lauzy.freedom.lyricview.api.Api;
import com.lauzy.freedom.lyricview.api.ServiceGenerator;

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
    private String SINGER, ALBUM;
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

    public LyricViewFragment(Context context, String name, int mAid, int mSid) {
        SINGER = "2";//String.valueOf(mSid);
        ALBUM = "1";// String.valueOf(mAid);
        mContext = context;
        serviceGenerator = ServiceGenerator.getInstance(mContext);
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

    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        try {
            mSeekBar.setProgress((int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100));
            // This math construction give a percentage of "was playing"/"song length"
            mLrcView.updateTime(mMediaPlayer.getCurrentPosition());
            mTvStart.setText(LrcHelper.formatTime(mMediaPlayer.getCurrentPosition()));
            if (mMediaPlayer.isPlaying()) {
                Runnable notification = () -> primarySeekBarProgressUpdater();
                handler.postDelayed(notification, 80);
            }
        } catch (Exception ignored) {
        }
    }

//    private void play() {
//
//        try {
//            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                clearMediaPlayer();
//                mSeekBar.setProgress(0);
//                wasPlaying = true;
//                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
//                        android.R.drawable.ic_media_play));
//            }
//            if (!wasPlaying) {
//                if (mMediaPlayer == null) {
//                    mMediaPlayer = new MediaPlayer();
//                }
//                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
//                        android.R.drawable.ic_media_pause));
//
//                new AudioStreamWorkerTask(mContext, new OnCacheCallback() {
//                    @Override
//                    public void onSuccess(FileInputStream fileInputStream) {
//                        if (fileInputStream != null) {
//                            mMediaPlayer = new MediaPlayer();
//                            try {
//                                mMediaPlayer.setDataSource(fileInputStream.getFD());
//                                mMediaPlayer.prepare();
//                                mMediaPlayer.setVolume(1f, 1f);
//                                mMediaPlayer.setLooping(false);
//                                mMediaPlayer.start();
//                                mSeekBar.setMax(mMediaPlayer.getDuration());
//                                mHandler.post(mRunnable);
//                                fileInputStream.close();
//                            } catch (IOException | IllegalStateException e) {
//                            }
//                        } else {
//                        }
//                    }
//
//                    @Override
//                    public void onError() {
//                    }
//                }).execute(CONSTANT.BASE_URL + "/files/"
//                        + SINGER + "/"
//                        + ALBUM + "/"
//                        + ALBUM + ".mp3");
//            }
//            wasPlaying = false;
//        } catch (Exception e) {
//        }
//    }

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
                    mMediaPlayer.seekTo(seekBar.getProgress());
                }
            });

        } catch (Exception ex) {
        }
    }

    private void clearMediaPlayer() {
        mMediaPlayer.stop();
        mMediaPlayer.release();
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

        fab.setOnClickListener(v -> {
            try {
                mMediaPlayer.setDataSource(
                        CONSTANT.BASE_URL + "/files/"
                                + SINGER + "/"
                                + ALBUM + "/"
                                + ALBUM + ".mp3"
                );
                mMediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mMediaPlayer.getDuration();

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
