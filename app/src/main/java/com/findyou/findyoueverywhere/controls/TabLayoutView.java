package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutView extends LinearLayout {
    String[] titles;
    List<View> list;
    TabLayoutCallback callback;
    private int imgres[][];

    public TabLayoutView(Context context) {
        super(context);
        init();
    }

    public TabLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TabLayoutView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        list = new ArrayList<>();
    }

    public void setTabData(int[][] imgres, String[] titles, int currentIndex){
        this.titles = titles;
        this.imgres = imgres;
        int i = 0;
        if(imgres != null){
            for(i=0; i<imgres.length; i++){
                int item_imgres[] = imgres[i];
                View view = View.inflate(getContext(), R.layout.main_bottom_layout_tab_item, null);
                TextView tv_name = view.findViewById(R.id.tv_name);
                ImageView iv_image = view.findViewById(R.id.iv_image);
                if(iv_image != null){
                    if(currentIndex == i){
                        iv_image.setImageResource(item_imgres[0]);
                    }else{
                        iv_image.setImageResource(item_imgres[1]);
                    }
                    //iv_image.setBackgroundResource(imgres[i]);
                }
                if(tv_name != null && titles != null){
                    tv_name.setVisibility(VISIBLE);
                    tv_name.setText(titles[i]);
                }else {
                    tv_name.setVisibility(GONE);
                }

                view.setTag(i);
                view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1.0f));
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int index = (int)v.getTag();
                        setCurrentItem(index);
                        if(callback != null){
                            callback.onClickItem(v, index);
                        }
                    }
                });

                list.add(view);
                this.addView(view);
            }
        }
        setCurrentItem(0);
    }

    public void setCurrentItem(int itemIndex){
        TextView tv_title;
        for(int i=0; i<list.size(); i++){
            int item_imgres[] = imgres[i];
            View view = list.get(i);
            TextView tv_name = view.findViewById(R.id.tv_name);
            ImageView iv_image = view.findViewById(R.id.iv_image);
            if(tv_name != null){
                tv_name.setSelected(false);
            }
            if(iv_image != null){
                iv_image.setSelected(false);
            }

            if(i == itemIndex){
                if(tv_name != null){
                    tv_name.setSelected(true);
                }
                if(iv_image != null){
                    iv_image.setSelected(true);
                }
            }

            if(itemIndex == i){
                iv_image.setImageResource(item_imgres[0]);

            }else{
                iv_image.setImageResource(item_imgres[1]);
            }
        }
    }

    public void setCallback(TabLayoutCallback callback){
        this.callback = callback;
    }
}
