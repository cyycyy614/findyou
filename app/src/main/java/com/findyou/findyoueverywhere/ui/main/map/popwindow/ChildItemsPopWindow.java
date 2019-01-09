package com.findyou.findyoueverywhere.ui.main.map.popwindow;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.BasePopWindow;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.ChildInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.ui.main.map.ChildMapFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ChildItemsPopWindow extends BasePopWindow {

    @BindView(R.id.listView)
    ListView listView;

    List<ChildInfoBean> data;
    private CommonAdapter adapter;

    public int getLayoutId() {
        mIsOutsideClose = false;
        isDropDown = true;
        return R.layout.child_items_pop_window;
    }

    public ChildItemsPopWindow(Activity activity, List<ChildInfoBean> items){
        super(activity);
        this.data = items;
        //this.data.clear();
//        for(int i=0; i<5; i++){
//            ChildInfoBean item = new ChildInfoBean();
//            item.name = "hello,world";
//            this.data.add(item);
//        }
    }

    public void initView(){
        adapter = new CommonAdapter<ChildInfoBean>(getContext(), R.layout.child_items_pop_window_item, data) {
            @Override
            protected void convert(ViewHolder holder, ChildInfoBean item, int position) {
                TextView tv_name = holder.getView(R.id.tv_name);
                tv_name.setText(item.name);
                if(position == (data.size() - 1)){
                    View view_line = holder.getView(R.id.view_line);
                    view_line.setVisibility(View.GONE);
                }
                View view = holder.getConvertView();
                view.setOnClickListener((res)->{
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("childs", JsonUtils.toString(data));
                    bundle1.putInt("index", position);
                    //ActivityManagerUtils.startActivity(getContext(), ChildMapFragment.class, bundle1);
                    postEvent(new EventMessenger(EventConst.API_MAP_SELECT_CHILD, position));
                    close();
                    //getContext().finish();
                });
            }
        };
        listView.setAdapter(adapter);
    }

    public void showPopWindow(View view){
        mView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        int width = mView.getMeasuredWidth();
        int space = UIUtils.dp2px(getContext(), 10);
        int xoff = 0 - width - space;
        int yspace = UIUtils.dp2px(getContext(), 1);
        int yoff = space;
        this.showPopWindow(view, xoff, 0, Gravity.NO_GRAVITY);
    }
}
