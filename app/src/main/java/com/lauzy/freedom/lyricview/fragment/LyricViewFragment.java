package com.lauzy.freedom.lyricview.fragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.Utils.AudioStreamWorkerTask;
import com.lauzy.freedom.lyricview.Utils.CONSTANT;
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
    // this value contains the song duration in milliseconds.
    // Look at getDuration() method in MediaPlayer class
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
    private LinearLayout mContainer;
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
        mContainer = view.findViewById(R.id.view_container);

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
        Log.e(">> URL: ", call.request().url() + " ");
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    mLRC = LrcHelper.parseInputStream(response.body().source().inputStream());
//                    play();
                } catch (Exception ignored) {
                    Log.e("ERROR_1", " " + ignored.getMessage());
                    Log.e("ERROR_2", " " + response.code());
                    Toasty.error(mContext, mContext.getString(R.string.not_done), Toasty.LENGTH_LONG).show();
                }
//                init();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toasty.error(mContext, mContext.getString(R.string.no_net), Toasty.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Method which updates the SeekBar primary progress by current song playing position
     */
    private void primarySeekBarProgressUpdater() {
        try {
            mSeekBar.setProgress((int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100)); // This math construction give a percentage of "was playing"/"song length"
            if (mMediaPlayer.isPlaying()) {
                Runnable notification = () -> primarySeekBarProgressUpdater();
                handler.postDelayed(notification, 1000);
            }
        } catch (Exception ignored) {
        }
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
                                Log.e(">> MSG: ", e.getMessage() + " ");
                            }
                        } else {
                            Log.e(getClass().getSimpleName() + ".MediaPlayer", "fileDescriptor is not valid");
                        }
                    }

                    @Override
                    public void onError() {
                        Log.e(getClass().getSimpleName() + ".MediaPlayer", "Can't play audio file");
                    }
                }).execute(CONSTANT.BASE_URL + "/files/"
                        + SINGER + "/"
                        + ALBUM + "/"
                        + ALBUM + ".mp3");
            }
            wasPlaying = false;
        } catch (Exception e) {
            Log.e("ERROR_ )))", " " + e.getMessage());
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
                    Log.e(">> TrackingTouch: ", String.valueOf(seekBar.getProgress()));
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

    private void initView() {
        fab = view.findViewById(R.id.fab);
        fabRepeat = view.findViewById(R.id.fab_repeat);
        mSeekBar = (SeekBar) view.findViewById(R.id.seek_play);
        mSeekBar.setMax(99); // It means 100% .0-99
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
                // setup song from http://www.hrupin.com/wp-content/uploads/mp3/testsong_20_sec.mp3
                // URL to mediaplayer data source
                mMediaPlayer.prepare();
                // you must call this method after setup the datasource
                // in setDataSource method. After calling prepare() the
                // instance of MediaPlayer starts load data from URL to internal buffer.
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaFileLengthInMilliseconds = mMediaPlayer.getDuration(); // gets the song length in milliseconds from URL

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
            //            if (wasPlaying) {
//                mMediaPlayer.pause();
//                mLrcView.pause();
//                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
//                        android.R.drawable.ic_media_play));
//                wasPlaying = false;
//            } else {
//                if (mMediaPlayer != null) {
//                    mMediaPlayer.start();
//                    mLrcView.resume();
//                }
//                fab.setImageDrawable(ContextCompat.getDrawable(mContext,
//                        android.R.drawable.ic_media_pause));
//                wasPlaying = true;
//            }
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
        mLrcView.setOnPlayIndicatorLineListener((time, content)
                -> mMediaPlayer.seekTo((int) time));
        //        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (fromUser) {
//                    mHandler.removeCallbacks(mRunnable);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                mHandler.post(mRunnable);
//                Log.e(">> TrackingTouch: ", String.valueOf(seekBar.getProgress()));
//                mMediaPlayer.seekTo(seekBar.getProgress());
//            }
//        });
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
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position*/
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
        /** MediaPlayer onCompletion event handler. Method which calls then song playing is complete*/
        // buttonPlayPause.setImageResource(R.drawable.button_play);
        fab.setImageDrawable(ContextCompat.getDrawable(mContext,
                android.R.drawable.ic_media_play));
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        /** Method which updates the SeekBar secondary progress by current song loading from URL position*/
        mSeekBar.setSecondaryProgress(percent);
    }
}
