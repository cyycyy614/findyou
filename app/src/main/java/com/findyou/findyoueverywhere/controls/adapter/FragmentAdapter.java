package com.findyou.findyoueverywhere.controls.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Package_name:com.hero.zhaoq.navigationbgsliding.adapter
 * Author:zhaoQiang
 * Email:zhao_hero@163.com
 * Date:2016/9/8  12:40
 *
 *  注意继承自   FragmentPagerAdapter
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Fragment> list;

    public FragmentAdapter(android.support.v4.app.FragmentManager fm, Context context, List<Fragment> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        int temp = 0;
        if(list != null){
            temp = list.size();
        }
        return temp;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }
}
