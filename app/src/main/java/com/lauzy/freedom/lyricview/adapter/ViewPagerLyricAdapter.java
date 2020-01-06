package com.lauzy.freedom.lyricview.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.lauzy.freedom.lyricview.fragment.CommentsFragment;
import com.lauzy.freedom.lyricview.fragment.LyricViewFragment;

public class ViewPagerLyricAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String mName;

    public ViewPagerLyricAdapter(String name, FragmentManager fm, Context context) {
        super(fm);
        mName = name;
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LyricViewFragment(mContext, mName);
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
