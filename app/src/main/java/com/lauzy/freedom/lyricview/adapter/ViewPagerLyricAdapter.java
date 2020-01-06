package com.lauzy.freedom.lyricview.adapter;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lauzy.freedom.lyricview.fragment.AlbumsFragment;
import com.lauzy.freedom.lyricview.fragment.CommentsFragment;
import com.lauzy.freedom.lyricview.fragment.LyricViewFragment;
import com.lauzy.freedom.lyricview.fragment.SingerFragment;

public class ViewPagerLyricAdapter extends FragmentPagerAdapter {
private Context mContext;
    public ViewPagerLyricAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LyricViewFragment(mContext);
            case 1:
                return new CommentsFragment(mContext);
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = getItem(position).getClass().getName();
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());
    }
}
