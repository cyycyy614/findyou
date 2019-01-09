package com.findyou.findyoueverywhere.utils.span;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Administrator on 2018/2/5 0005.
 */

public class ClickSpan extends ClickableSpan {

    ClickListener listener;
    String color;

    public interface ClickListener{
        public void OnClick(View view);
    }

    public ClickSpan(String color, ClickListener listener){
        this.listener = listener;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(false);
        if(this.color == null){
            this.color = "#000000";
        }
        ds.setColor(Color.parseColor(this.color));
    }

    @Override
    public void onClick(View widget) {
        if(this.listener != null){
            this.listener.OnClick(widget);
        }
    }
}
