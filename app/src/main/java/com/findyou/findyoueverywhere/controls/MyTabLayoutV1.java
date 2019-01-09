package com.findyou.findyoueverywhere.controls;

import android.content.Context;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

public class MyTabLayoutV1 extends LinearLayout {

    private String[] titles;
    private List<View> list;
    private TabLayoutCallback callback;
    private int spaceWidth = 0; //间隔距离
    private int layoutid = 0;

    public MyTabLayoutV1(Context context) {
        super(context);
        init();
    }

    public MyTabLayoutV1(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyTabLayoutV1(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        list = new ArrayList<>();
    }

    public void setTabData(String[] titles){
        this.titles = titles;
        int i = 0;
        for(i=0; i<titles.length; i++){
            if(layoutid == 0){
                layoutid = R.layout.layout_tab_segment;
            }
            View spaceView = null;
            if(spaceWidth > 0) {
                spaceView = new View(getContext());
                LayoutParams spaceLayoutParams = new LayoutParams(spaceWidth, LayoutParams.MATCH_PARENT);
                spaceView.setLayoutParams(spaceLayoutParams);
            }
            View view = View.inflate(getContext(), layoutid, null);
            int width = UIUtils.getScreenWidth(getContext()) / titles.length;
            LayoutParams layoutParams = new LayoutParams(width, LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(layoutParams);

            final TextView tv_title = view.findViewById(R.id.tv_title);
            if(tv_title != null){
                tv_title.setText(titles[i]);
            }
            view.setTag(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (int)view.getTag();
                    setCurrentItem(index);
                    if(callback != null){
                        callback.onClickItem(v, index);
                    }
                }
            });
            list.add(view);
            if(spaceWidth > 0) {
                this.addView(spaceView);
            }
            this.addView(view);
        }
        setCurrentItem(0);
    }

    public void setCurrentItem(int itemIndex){
        TextView tv_title;
        for(int i=0; i<list.size(); i++){
            View view = list.get(i);
            //TextPaint tp = tv.getPaint();
            //tv.setSelected(false);
            //tp.setFakeBoldText(false);
            if(callback != null){
                callback.onNormalStyle(view);
            }
            if(i == itemIndex){
                list.get(i).setSelected(true);
                //tp.setFakeBoldText(true);
                if(callback != null){
                    callback.onHoverStyle(view);
                }
            }
        }
    }

    public void setCallback(TabLayoutCallback callback){
        this.callback = callback;
    }

    public void setLayoutId(int layoutId){
        this.layoutid = layoutId;
    }

    public void setSpaceWidth(int width){
        spaceWidth = width; //spaceWidth == -1 等分
    }
}