package com.example.root.douclient.adapter;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.root.douclient.fragments.ForumFragment;
import com.example.root.douclient.fragments.NewsFragment;
import com.example.root.douclient.fragments.NewsFragmentTest;

/**
 * Created by root on 04.12.15.
 */
public class TabsPagerFragmentAdapter extends FragmentPagerAdapter {

    private String[] tabs;

    public TabsPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
        tabs = new String[] {
                "Лента",
                "Форум"
        };

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return NewsFragmentTest.getInstance("http://dou.ua/lenta/page/");
            case 1:
                return NewsFragmentTest.getInstance("http://dou.ua/forums/page/");
        }
        return null;
    }

    @Override
    public int getCount() {
        return tabs.length;
    }
}
