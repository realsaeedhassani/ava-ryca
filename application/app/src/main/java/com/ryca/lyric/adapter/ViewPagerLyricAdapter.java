package com.ryca.lyric.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ryca.lyric.fragment.CommentsFragment;
import com.ryca.lyric.fragment.LyricViewFragment;

public class ViewPagerLyricAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private String mName;
    private int mAid, mSid;

    public ViewPagerLyricAdapter(String name,
                                 int aid, int sid, FragmentManager fm, Context context) {
        super(fm);
        mName = name;
        mContext = context;
        mAid = aid;
        mSid = sid;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LyricViewFragment(mContext, mName, mAid, mSid);
            case 1:
                return new CommentsFragment(mContext,mAid);
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
