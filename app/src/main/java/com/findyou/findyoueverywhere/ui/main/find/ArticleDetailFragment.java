package com.findyou.findyoueverywhere.ui.main.find;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.BusManager;
import com.findyou.findyoueverywhere.bean.NewsInfoBean;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;

import butterknife.BindView;

public class ArticleDetailFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_content)
    TextView tv_content;

    public int getLayoutId() {
        setTitle("文章内容");
        return R.layout.fragment_article_detail;
    }

    public void initView(){
        Bundle bundle = getArguments();
        int id = bundle.getInt("id");
        ApiCommon.getNewsItem(id, (res)->{
            NewsInfoBean bean = JsonUtils.convert(res.data, NewsInfoBean.class);
            if(bean == null){
                return;
            }
            if(tv_title != null){
                tv_title.setText(bean.title);
            }
            if(tv_time != null){
                tv_time.setText(bean.create_time);
            }
            if(tv_content != null){
                tv_content.setText(Html.fromHtml(bean.content));
            }
        });
    }
}
