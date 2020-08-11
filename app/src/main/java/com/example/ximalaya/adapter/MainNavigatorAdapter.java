package com.example.ximalaya.adapter;

import android.content.Context;
import android.graphics.Color;

import com.example.ximalaya.R;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

public class MainNavigatorAdapter extends CommonNavigatorAdapter {

    private String[] mTitles;

    public MainNavigatorAdapter(Context context){

        mTitles = context.getResources().getStringArray(R.array.indicator_title);
    }


    @Override
    public int getCount() {

        return mTitles.length;
    }

    @Override
    public IPagerTitleView getTitleView(Context context, int index) {

        ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
        clipPagerTitleView.setText(mTitles[index]);
        clipPagerTitleView.setTextColor(Color.BLACK);
        clipPagerTitleView.setClipColor(Color.WHITE);
        return clipPagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        return null;
    }
}
