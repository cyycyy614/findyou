package com.findyou.findyoueverywhere.ui.main.find.category;

import android.app.Activity;
import android.app.Dialog;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.CommonCallback2;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.bean.StoryInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.MyGridView;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.MyRecyclerView;
import com.findyou.findyoueverywhere.ui.main.find.FindStoryFragment;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.InputViewDialog;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.OptPopWindow;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.PhotoPreviewPop;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.SharePopWindow;
import com.findyou.findyoueverywhere.utils.AndroidBug5497Workaround;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JObject;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.findyou.findyoueverywhere.utils.share.ShareChannel;
import com.findyou.findyoueverywhere.utils.share.ShareUtils;
import com.findyou.findyoueverywhere.utils.span.SpanTextUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.otto.Subscribe;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.finalteam.loadingviewfinal.RecyclerViewFinal;

public class Category1Fragment extends BaseFragment {
    @BindView(R.id.listView)
    MyListView listView;

    public class ItemInfoBean{
        public int id;
        public String title;
        public String time;
    }
    public class ImageInfoBean{
        public String url;
    }
    public class CommentInfoBean{
        public int id;
        public int storyid;
        public int uid;
        public String name;
        public int type;
        public int touid; //回复的人
        public String toname; //回复的人的名字
        public String content;
        public String create_time;
    }

    private CommonAdapter adapter;
    private List<StoryInfoBean> data;
    private int pageIndex = 0;
    private int type;
    private Dialog dialog;
    private View currentView;
    int like_width;
    int like_img_space;
    private Handler handler;

    public int getLayoutId() {
        registerEvent();
        return R.layout.find_category1_ragment;
    }

    public void setType(int type){
        this.type = type;
    }

    public void initView(){
        like_width = UIUtils.dp2px(getContext(), 18);
        like_img_space = UIUtils.dp2px(getContext(), 3);
        data = new ArrayList<>();
        handler = new Handler();
        AndroidBug5497Workaround.assistActivity(getActivity(), new AndroidBug5497Workaround.OnKeyboardToggleListener() {
            @Override
            public void onKeyboardShow(int keyboardSize) {
                if(currentView != null){
                    ImageView iv_opt = currentView.findViewById(R.id.iv_opt);
                    if(iv_opt != null) {
                        handler.postDelayed(()->{
                            showListView(currentView);
                        },20);
                    }
                }
            }
            @Override
            public void onKeyboardHide(int keyboardSize) {
                log.write("onKeyboardHide.");
                if(dialog != null){
                    dialog.dismiss();
                }
            }
        });

//        RecyclerViewFinal recyclerViewFinal = recyclerView.getRecyclerViewFinal();
//        recyclerViewFinal.setHasFixedSize(true);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
//        recyclerViewFinal.setLayoutManager(layoutManager);

        adapter = new CommonAdapter<StoryInfoBean>(getContext(), R.layout.fragment_find_story_item, data) {
            @Override
            protected void convert(ViewHolder holder, StoryInfoBean item, int position) {
                TextView tv_nickname = holder.getView(R.id.tv_nickname);
                tv_nickname.setText(item.nickname);
                TextView tv_time = holder.getView(R.id.tv_time);
                tv_nickname.setText(item.create_time);
                ImageView iv_headimage = holder.getView(R.id.iv_headimage);
                PicUtils.loadImage(getContext(), item.headimage, iv_headimage);

                TextView tv_content = holder.getView(R.id.tv_content);
                tv_content.setText(item.content);

                LinearLayout lin_likes = holder.getView(R.id.lin_likes);
                lin_likes.setVisibility(View.GONE);
                TextView tv_likes = holder.getView(R.id.tv_likes);
                tv_likes.setText("");
                tv_likes.setVisibility(View.GONE);

                LinearLayout lin_comment_list = holder.getView(R.id.lin_comment_list);
                lin_comment_list.removeAllViews();
//                TextView tv_comment_list = holder.getView(R.id.tv_comment_list);
//                tv_comment_list.setText("");
//                tv_comment_list.setVisibility(View.GONE);

                ImageView iv_opt = holder.getView(R.id.iv_opt);
                iv_opt.setOnClickListener((v)->{
                    item.position = position;
                    item.type = type;
                    currentView = holder.getConvertView();
                    OptPopWindow popWindow = new OptPopWindow(getActivity(), item);
                    popWindow.showPopWindow(iv_opt);
                });

                //图片显示
                MyGridView gridView = holder.getView(R.id.gridView);
                String[] img = item.images.split(";");
                List<ImageInfoBean> images = new ArrayList<>();
                List<String> images1 = new ArrayList<>();
                for(int i=0; i<img.length; i++){
                    ImageInfoBean bean = new ImageInfoBean();
                    bean.url = img[i];
                    if(TextUtils.isEmpty(bean.url)){
                        continue;
                    }
                    images.add(bean);
                    images1.add(bean.url);
                }
                com.zhy.adapter.abslistview.CommonAdapter adapter = new com.zhy.adapter.abslistview.CommonAdapter<ImageInfoBean>(getContext(), R.layout.fragment_find_story_item_image, images) {
                    @Override
                    protected void convert(com.zhy.adapter.abslistview.ViewHolder holder, ImageInfoBean item, int position) {
                        ImageView iv_image = holder.getView(R.id.iv_image);
                        int px = UIUtils.dp2px(getContext(), 80);
                        iv_image.getLayoutParams().height = (UIUtils.getScreenWidth(getContext()) - px) / 3;
                        PicUtils.loadImage(getContext(), item.url, iv_image);
                        //点击事件
                        View itemView = holder.getConvertView();
                        itemView.setOnClickListener((View view)->{
                            PhotoPreviewPop pop = new PhotoPreviewPop(getActivity(), images1, position);
                            pop.showFromCenter();
                        });
                    }
                };
                gridView.setAdapter(adapter);

                //赞
                item.position = position;
                displayLikes(holder.getConvertView(), item);
                displayCommentItems(holder.getConvertView(), item);
//                //评论
//                if(item.comment == null){
//                    item.comment = new ArrayList<>();
//                }
//                List<CommentInfoBean> comments = item.comment;
//                for(int i=0; i<3; i++){
////                    CommentInfoBean bean = new CommentInfoBean();
////                    bean.name = "小白";
////                    bean.toname = "大黄";
////                    bean.msg = "你好啊你好啊你好啊";
////                    comments.add(bean);
//                }
//                //评论
//                LinearLayout lin_comment_list = holder.getView(R.id.lin_comment_list);
//                if(item.comment.size() == 0){
//                    lin_comment_list.setVisibility(View.GONE);
//                }else {
//                    lin_comment_list.setVisibility(View.VISIBLE);
//                }
//                MyGridView listView = holder.getView(R.id.listView);
//
//                com.zhy.adapter.abslistview.CommonAdapter commentAdapter = new com.zhy.adapter.abslistview.CommonAdapter<CommentInfoBean>(getContext(), R.layout.fragment_find_story_item_comment_item, item.comment) {
//                    @Override
//                    protected void convert(com.zhy.adapter.abslistview.ViewHolder holder1, CommentInfoBean item1, int position1) {
//                        TextView iv_comment = holder1.getView(R.id.tv_comment);
//                        iv_comment.setText("");
//                        SpanTextUtils.insertText(iv_comment, item1.name, "#0000ff");
//                        if(item1.type == 1) {
//                            SpanTextUtils.insertText(iv_comment, "回复", "#666666");
//                            SpanTextUtils.insertText(iv_comment, item1.toname, "#0000ff");
//                            //SpanTextUtils.insertClickText(iv_comment, );
//                        }
//                        SpanTextUtils.insertText(iv_comment, ": " + item1.content, "#666666");
//                        SpanTextUtils.insertText(iv_comment, " ", "#666666");
//                        if(item1.type == 0){
//                            iv_comment.setOnClickListener((v)->{
//                                dialog = showInputComment(getActivity(), position, this, iv_comment, "回复" + item1.name, 1, item1.uid);
//                            });
//                        }
//                    }
//                };
//                listView.setAdapter(commentAdapter);
//                LinearLayout lin_comment = holder.getView(R.id.lin_comment);
//                lin_comment.setOnClickListener((v)->{
//                    dialog = showInputComment(getActivity(), position, commentAdapter, lin_comment, "请输入评论", 0, 0);
//                });

//                ApiCommon.getStoryCommentItems(item.id, (res)->{
//                    List<CommentInfoBean> list = JsonUtils.convert_array(res.data, CommentInfoBean.class);
//                    if(list == null){
//                        return;
//                    }
//                    if(item.comment == null){
//                        item.comment = new ArrayList<>();
//                    }
//                    item.comment.clear();
//                    item.comment.addAll(list);
//                    commentAdapter.notifyDataSetChanged();
//                });
            }
        };
        listView.setAdapter(adapter);
        listView.setCallback(new MyListView.ListViewCallback() {
            @Override
            public void onRefresh() {
                pageIndex = 0;
                loadData();
            }

            @Override
            public void onLoadMore() {
                pageIndex++;
                loadData();
            }

            @Override
            public void onClickItem(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        loadData();
    }

    public void loadData(){
        ApiCommon.getStoryItems(type, pageIndex, (res)->{
            List<StoryInfoBean> list = JsonUtils.convert_array_nesting(res.data, new TypeToken<List<StoryInfoBean>>(){}.getType());
            if(list == null){
                return;
            }
            if(pageIndex == 0){
                data.clear();
            }
            data.addAll(list);
            if(data.size() == 0){
                listView.setEmptyView(true);
            }else {
                listView.setEmptyView(false);
            }
            adapter.notifyDataSetChanged();
        });
    }

    /**
     * 弹出评论对话框
     */
    private Dialog showInputComment(Activity activity, int position, View view, CharSequence hint, int type, int touid) {
        StoryInfoBean item = data.get(position);
        final InputViewDialog dialog = new InputViewDialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.setContentView(R.layout.view_input_comment);
        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        final int[] coord = new int[2];
        if (view != null) {
            view.getLocationOnScreen(coord);
        }
        final EditText et_content = (EditText) dialog.findViewById(R.id.input_comment);
        et_content.setHint(hint);
        et_content.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                return true;
            }
            return false;
        });

        final TextView tv_submit = (TextView) dialog.findViewById(R.id.btn_publish_comment);
        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = et_content.getText().toString();
                if(TextUtils.isEmpty(content)){
                    ToastUtils.showToast(getContext(),"评论内容不能为空!");
                    return;
                }
                if(item.comment == null){
                    item.comment = new ArrayList<>();
                }
                CommentInfoBean bean = new CommentInfoBean();
                bean.storyid = item.id;
                bean.uid = app.me.uid;
                bean.name = app.me.nickname;
                bean.touid = item.uid;
                bean.toname = item.nickname;
                bean.content = content;
                bean.type = type;
                List<CommentInfoBean> c = new ArrayList<>();
                if(item.comment != null) {
                    c.addAll(item.comment);
                }
                c.add(bean);
                String comment = JsonUtils.toString(c);
                ApiCommon.commentStoryItems(item.id, comment,(res)->{
                    if(item.comment == null){
                        item.comment = new ArrayList<>();
                    }
                    item.comment.add(bean);
                    displayCommentItems(view, item);
                    ToastUtils.showToast(getContext(), "评论成功!");
                    dialog.dismiss();
                });
            }
        });
        dialog.setCancelable(true);
        dialog.show();

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //if (listener != null) {
//                int[] inputViewCoordinatesInScreen = new int[2];
//                dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(inputViewCoordinatesInScreen);
//                //dialog.findViewById(R.id.input_comment_container).getLocationInWindow(inputViewCoordinatesInScreen);
//                // 传入 输入框距离屏幕顶部（不包括状态栏）的位置
//                //listener.onShow(coord);
//                //showListView(view, coord, inputViewCoordinatesInScreen);
//                //}
//            }
//        }, 500);
        return dialog;
    }

    public void showListView(View view,int[] coord, int[] inputViewCoordinatesInScreen){
        if (listView != null) {
            // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
            //int span = view.getId() == R.id.lin_comment ? 0 : view.getHeight();
            int span = view.getHeight() + UIUtils.dp2px(getContext(), 12);
            int distance = coord[1] + span - inputViewCoordinatesInScreen[1];
            log.write("distance:" + distance);
            listView.smoothScrollBy(coord[1] + span - inputViewCoordinatesInScreen[1], 1000);
        }
    }


    public void showListView(View view){
        if(dialog == null){
            return;
        }
        if(view == null){
            return;
        }
        ImageView iv_opt = view.findViewById(R.id.iv_opt);
        final int[] coord = new int[2];
        if (iv_opt != null) {
            iv_opt.getLocationOnScreen(coord);
        }

        final int[] inputViewCoordinatesInScreen = new int[2];
        dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(inputViewCoordinatesInScreen);

        try {
            if (listView != null) {
                // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
                //int span = view.getId() == R.id.lin_comment ? 0 : view.getHeight();
                //view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                LinearLayout lin_block = view.findViewById(R.id.lin_block);
                int span = 0;
                if(lin_block != null) {
                    lin_block.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    span = lin_block.getMeasuredHeight();
                }
                span += UIUtils.dp2px(getContext(), 25);
                int distance = coord[1] + span - inputViewCoordinatesInScreen[1];
                log.write("distance:" + distance);
                listView.smoothScrollBy(distance, 1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_STORY_LIKE_SUCCESSFUL:
                int position = Integer.parseInt(event.obj.toString());
                if (position > data.size() - 1) {
                    return;
                }
                StoryInfoBean item = data.get(position);
                if (item.type != type) {
                    return;
                }

//                if(item.likes == null){
//                    data.get(position).likes = new ArrayList<>();
//                }
//                StoryInfoBean.LikeInfoBean likeInfoBean = item.getLikeBean();
//                app.me.nickname = "hello";
//                likeInfoBean.uid = app.me.uid;
//                likeInfoBean.name = app.me.nickname;
//                boolean isfind = false;
//                for(int i=0; i<item.likes.size(); i++){
//                    StoryInfoBean.LikeInfoBean bean = item.likes.get(i);
//                    if(app.me.uid == bean.uid){
//                        //isfind = true;
//                        //item.likes.remove(i);
//                        break;
//                    }
//                }
//                if(!isfind){
//                    item.likes.add(likeInfoBean);
//                    ToastUtils.showToast(getContext(), "点赞成功!");
//                }else{
//                    ToastUtils.showToast(getContext(), "取消点赞!");
//                }
//                if(currentView != null){
//                    TextView tv_likes = currentView.findViewById(R.id.tv_likes);
//                    View view_line = currentView.findViewById(R.id.view_line);
//                    if(item.likes.size() == 0){
//                        tv_likes.setVisibility(View.GONE);
//                        view_line.setVisibility(View.GONE);
//                    }else {
//                        tv_likes.setVisibility(View.VISIBLE);
//                        view_line.setVisibility(View.VISIBLE);
//                    }
//                    tv_likes.setText("");
//                    if(tv_likes != null){
//                        if(item.likes.size() != 0) {
//                            SpanTextUtils.insertImage(tv_likes, R.drawable.ic_ok, like_width, like_width, 0, 0);
//                            SpanTextUtils.insertText(tv_likes, " ", "#000000");
//                        }
//                        for(int i=0; i<item.likes.size(); i++){
//                            SpanTextUtils.insertText(tv_likes, item.likes.get(i).name + " ", "#666666");
//                        }
//                    }
//                }
                displayLikes(currentView, item);
                break;
            case EventConst.API_STORY_COMMENT:
                int position1 = Integer.parseInt(event.obj.toString());
                if (position1 > data.size() - 1) {
                    return;
                }
                StoryInfoBean item1 = data.get(position1);
                if (item1.type != type) {
                    return;
                }
                displayComment(currentView, item1);
                break;
            case EventConst.API_STORY_PUBLISH_SUCCESSFUL:
                //故事发布成功
                loadData();
                break;
            case EventConst.API_STORY_ITEM_SHARE:
                JObject jo = new JObject(event.obj.toString());
                int channel = jo.getInt("channel");
                int type = jo.getInt("type");
                if(type != this.type){
                    return;
                }
                //String str2 = event.obj.toString();
                if(ToolUtils.isNumeric(channel + "")){
                    //int i = Integer.parseInt(str2);
                    switch (channel){
                        case 0: //微信好友
                            shareWeixinWechat();
                            break;
                        case 1: //朋友圈
                            shareWeixinMoment();
                            break;
                        case 2: //qq
                            break;
                        case 3: //weibo
                            break;
                    }
                }
                break;
        }
    }

    public void shareWeixinWechat(){
        JObject json = new JObject();
        json.put("type", 1); //网址
        json.put("title", "标题");
        json.put("content", "内容");
        json.put("url", "http://www.baidu.com");
        json.put("image", "https://ps.ssl.qhimg.com/dmfd/417_417_/t01cdf17f58c08fc41c.jpg");
        ShareUtils.share(getActivity(), ShareChannel.SHARE_CHANNEL_WECHAT_WECHAT, json, new CommonCallback2() {
            @Override
            public void successful(Object obj) {
                log.write("share successful.");
                ToastUtils.showToast(getContext(), "分享成功!");
            }
            @Override
            public void error(Object obj) {
                log.write("share error.");
                ToastUtils.showToast(getContext(), "分享失败!");
            }
            @Override
            public void cancel(Object obj) {
                log.write("share cancel.");
                ToastUtils.showToast(getContext(), "取消成功!");
            }
        });
    }

    public void shareWeixinMoment(){
        JObject json = new JObject();
        json.put("type", 1); //网址
        json.put("title", "标题");
        json.put("content", "内容");
        json.put("url", "http://www.baidu.com");
        json.put("image", "https://ps.ssl.qhimg.com/dmfd/417_417_/t01cdf17f58c08fc41c.jpg");
        ShareUtils.share(getActivity(), ShareChannel.SHARE_CHANNEL_WECHAT_MOMENT, json, new CommonCallback2() {
            @Override
            public void successful(Object obj) {
                log.write("share successful.");
                ToastUtils.showToast(getContext(), "分享成功!");
            }
            @Override
            public void error(Object obj) {
                log.write("share error.");
                ToastUtils.showToast(getContext(), "分享失败!");
            }
            @Override
            public void cancel(Object obj) {
                log.write("share cancel.");
                ToastUtils.showToast(getContext(), "取消成功!");
            }
        });
    }

    public void displayComment(View view, StoryInfoBean item){
//        if(item.comment == null){
//            return;
//        }
        if(view != null){
//            LinearLayout lin_comment_list = view.findViewById(R.id.lin_comment_list);
//            ImageView iv_opt = view.findViewById(R.id.iv_opt);
            if(item.type == 0){
                dialog = showInputComment(getActivity(), item.position, view, "请输入评论", 0, 0);
            }
        }
    }

    public void displayLikes(View view, StoryInfoBean item){
        if(view == null){
            return;
        }
        LinearLayout lin_likes = view.findViewById(R.id.lin_likes);
        TextView tv_likes = view.findViewById(R.id.tv_likes);
        if(item.likes == null){
            tv_likes.setVisibility(View.GONE);
            lin_likes.setVisibility(View.GONE);
            return;
        }
        if(view != null){
            if(item.likes.size() == 0){
                tv_likes.setVisibility(View.GONE);
                lin_likes.setVisibility(View.GONE);
            }else {
                lin_likes.setVisibility(View.VISIBLE);
                tv_likes.setVisibility(View.VISIBLE);
                log.write("item.position:" + item.position);
            }
            tv_likes.setText("");
            if(tv_likes != null){
                if(item.likes.size() != 0) {
                    SpanTextUtils.insertImage(tv_likes, R.drawable.ic_ok, like_width, like_width, 0, 0);
                    SpanTextUtils.insertText(tv_likes, " ", "#000000");
                }
                for(int i=0; i<item.likes.size(); i++){
                    SpanTextUtils.insertText(tv_likes, item.likes.get(i).name + " ", "#666666");
                }
            }
        }
    }

    public void displayCommentItems(View view, StoryInfoBean item){
        if(view == null){
            return;
        }
        LinearLayout lin_comment_list = view.findViewById(R.id.lin_comment_list);
        //TextView tv_comment_list = view.findViewById(R.id.tv_comment_list);
        View view_line = view.findViewById(R.id.view_line);
        if(item.comment == null){
            lin_comment_list.setVisibility(View.GONE);
            //tv_comment_list.setVisibility(View.GONE);
            view_line.setVisibility(View.GONE);
            return;
        }
        if(view != null){
            if(item.comment.size() == 0){
                lin_comment_list.setVisibility(View.GONE);
                //tv_comment_list.setVisibility(View.GONE);
                view_line.setVisibility(View.GONE);
            }else {
                lin_comment_list.setVisibility(View.VISIBLE);
                view_line.setVisibility(View.GONE);
                if(item.likes == null){
                    view_line.setVisibility(View.GONE);
                }else {
                    if(item.likes.size() > 0) {
                        view_line.setVisibility(View.VISIBLE);
                    }
                }
                log.write("item.position:" + item.position);
            }
            //tv_comment_list.setText("");
            lin_comment_list.removeAllViews();
            if(lin_comment_list != null){
                for(int i=0; i<item.comment.size(); i++) {
                    CommentInfoBean bean = item.comment.get(i);
                    //TextView iv_comment = view.getView(R.id.tv_comment);
                    //iv_comment.setText("");
                    TextView tv_comment = new TextView(getContext());
                    SpanTextUtils.insertText(tv_comment, bean.name, "#0000ff");
                    if (bean.type == 1) {
                        SpanTextUtils.insertText(tv_comment, "回复", "#666666");
                        SpanTextUtils.insertText(tv_comment, bean.toname, "#0000ff");
                        //SpanTextUtils.insertClickText(iv_comment, );
                    }
                    SpanTextUtils.insertText(tv_comment, ": " + bean.content, "#666666");
                    SpanTextUtils.insertText(tv_comment, " ", "#666666");
                    if (bean.type == 0) {
                        tv_comment.setOnClickListener((v) -> {
                            currentView = view;
                            dialog = showInputComment(getActivity(), item.position, view, "回复" + bean.name, 1, bean.uid);
                        });
                    }
                    lin_comment_list.addView(tv_comment);
                }
            }
        }
    }


}
