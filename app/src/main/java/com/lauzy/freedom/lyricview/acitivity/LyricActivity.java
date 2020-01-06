package com.lauzy.freedom.lyricview.acitivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.lauzy.freedom.lyricview.R;
import com.lauzy.freedom.lyricview.adapter.ViewPagerLyricAdapter;
import com.lauzy.freedom.lyricview.api.ServiceGenerator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LyricActivity extends AppCompatActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivy_lyric);

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerLyricAdapter(getSupportFragmentManager(), getBaseContext()));

        ImageView dot1 = findViewById(R.id.dot1);
        ImageView dot2 = findViewById(R.id.dot2);
        ImageView comment = findViewById(R.id.comment);
        ImageView account = findViewById(R.id.account);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogComment();
            }
        });
        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRegister();
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        dot1.setImageResource(R.drawable.ic_dot1);
                        dot2.setImageResource(R.drawable.ic_dot2);
                        break;

                    case 1:
                        dot1.setImageResource(R.drawable.ic_dot2);
                        dot2.setImageResource(R.drawable.ic_dot1);
                        break;
                }


            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });


    }

    private void showDialogComment() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_send_comment);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }

    private void showDialogRegister() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_name_email);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.show();
    }
}