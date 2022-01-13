package com.ryca.lyric.acitivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.ryca.lyric.BuildConfig;
import com.ryca.lyric.R;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitiviy_splash);
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(findViewById(R.id.icon));
        YoYo.with(Techniques.Shake)
                .duration(1000)
                .playOn(findViewById(R.id.icon));
        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(findViewById(R.id.tv_com));

        TextView tv = findViewById(R.id.version);
        tv.setText(getString(R.string.version) + "  " + BuildConfig.VERSION_NAME);

        YoYo.with(Techniques.FadeIn)
                .duration(1000)
                .playOn(findViewById(R.id.version));
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void init() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
            overridePendingTransition(R.anim.fade_out, R.anim.fade_in);
        }, 3000);
    }
}
