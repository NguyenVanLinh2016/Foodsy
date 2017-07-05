package com.linhnv.foodsy.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by linhnv on 21/05/2017.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mListFragments = new ArrayList<>();
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mListFragments.get(position);
    }

    @Override
    public int getCount() {
        return mListFragments.size();
    }
    public void addFrag(Fragment fragment){
        mListFragments.add(fragment);
    }

}
