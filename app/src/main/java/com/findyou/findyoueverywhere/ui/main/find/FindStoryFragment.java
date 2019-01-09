package com.findyou.findyoueverywhere.ui.main.find;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
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
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.StoryInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.MyGridView;
import com.findyou.findyoueverywhere.controls.MyListView;
import com.findyou.findyoueverywhere.controls.MyTabLayoutV1;
import com.findyou.findyoueverywhere.controls.NestingListView;
import com.findyou.findyoueverywhere.controls.TabLayoutCallback;
import com.findyou.findyoueverywhere.controls.adapter.FragmentAdapter;
import com.findyou.findyoueverywhere.ui.main.find.category.Category1Fragment;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.InputViewDialog;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.AndroidBug5497Workaround;
import com.findyou.findyoueverywhere.utils.KeyboardUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.findyou.findyoueverywhere.utils.span.SpanTextUtils;
import com.squareup.otto.Subscribe;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

public class FindStoryFragment extends BaseFragment {
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
//
//    @BindView(R.id.system_status_bar)
//    LinearLayout system_status_bar;

    @BindView(R.id.listView)
    MyListView listView;

    @BindView(R.id.tabLayout)
    MyTabLayoutV1 tabLayout;

//    @BindView(R.id.view_root)
//    LinearLayout view_root;

    @BindView(R.id.view_input)
    LinearLayout view_input;

    @BindView(R.id.et_comment)
    EditText et_comment;

    @BindView(R.id.vp_viewpager)
    ViewPager vp_viewpager;

    private List<StoryInfoBean> data;
    private int pageIndex = 0;
    private CommonAdapter adapter;
    private String[] titles = {"老人","小孩","宠物","其它"};
    private int type;
    private Dialog dialog;

    public int getLayoutId() {
        //showStatusBar(false);
        //showTitleBar(false);
        setTitle("找到你的故事圈");
        initSaveBar();
        registerEvent();
        return R.layout.fragment_find_story;
    }

    public void initSaveBar(){
        ImageView image = (ImageView)getRightView(ViewType.ImageView);
        if(image != null){
            image.setBackgroundResource(R.drawable.ic_camera);
            image.getLayoutParams().height = UIUtils.dp2px(getContext(), 18);
            image.getLayoutParams().width = UIUtils.dp2px(getContext(), 22);
            image.setOnClickListener((View view)-> {
                Bundle bundle = new Bundle();
                bundle.putInt("type", type);
                ActivityManagerUtils.startActivity(getActivity(), PublishDynamicFragment.class, bundle);
            });
        }
    }

    private List<Fragment> fragments;
    public void initView(){
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //getActivity().s

        fragments = new ArrayList<>();
        Category1Fragment c1 = new Category1Fragment();
        c1.setType(0);
        Category1Fragment c2 = new Category1Fragment();
        c2.setType(1);
        Category1Fragment c3 = new Category1Fragment();
        c3.setType(2);
        Category1Fragment c4 = new Category1Fragment();
        c4.setType(3);
        fragments.add(c1);
        fragments.add(c2);
        fragments.add(c3);
        fragments.add(c4);
        FragmentPagerAdapter adapter = new FragmentAdapter(getChildFragmentManager(), this.getContext(), fragments);
        vp_viewpager.setAdapter(adapter);
        vp_viewpager.setOffscreenPageLimit(3);
        //vp_viewpager.setNoScroll(false);

        tabLayout.setLayoutId(R.layout.fragment_message_tab_item);
        tabLayout.setTabData(titles);
        tabLayout.setCallback(new TabLayoutCallback() {
            @Override
            public void onClickItem(View view, int position) {
//                type = position;
//                pageIndex = 0;
//                loadData();
                vp_viewpager.setCurrentItem(position, true);
            }
            @Override
            public void onNormalStyle(View view){
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setTextColor(app.getContext().getColor(R.color.colorGray9));
                View line = view.findViewById(R.id.view_line);
                line.setVisibility(View.GONE);
            }
            @Override
            public void onHoverStyle(View view){
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setTextColor(app.getContext().getColor(R.color.defaultColor));
                View line = view.findViewById(R.id.view_line);
                line.setVisibility(View.VISIBLE);
            }
        });
        tabLayout.setCurrentItem(0);
        vp_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentItem(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        //data = new ArrayList<>();
//        for(int i=0; i<10; i++){
//            ItemInfoBean bean = new ItemInfoBean();
//            bean.title = "维修和洗护品类报价单上传规则的说明";
//            bean.time = "2017.08.21";
//            data.add(bean);
//        }
        //listView.
//        adapter = new CommonAdapter<StoryInfoBean>(getContext(), R.layout.fragment_find_story_item, data) {
//            @Override
//            protected void convert(ViewHolder holder, StoryInfoBean item, int position) {
//                TextView tv_nickname = holder.getView(R.id.tv_nickname);
//                tv_nickname.setText(item.nickname);
//                TextView tv_time = holder.getView(R.id.tv_time);
//                tv_nickname.setText(item.create_time);
//                ImageView iv_headimage = holder.getView(R.id.iv_headimage);
//                PicUtils.loadImage(getContext(), item.headimage, iv_headimage);
//
//                LinearLayout lin_like = holder.getView(R.id.lin_like);
//                lin_like.setOnClickListener((v)->{
//                    ApiCommon.likeStoryItems(item.id, (res)->{
//                        ToastUtils.showToast(getContext(), "点赞成功!");
//                    });
//                });
//                //图片显示
//                MyGridView gridView = holder.getView(R.id.gridView);
//                String[] img = item.images.split(";");
//                List<ImageInfoBean> images = new ArrayList<>();
//                for(int i=0; i<img.length; i++){
//                    ImageInfoBean bean = new ImageInfoBean();
//                    bean.url = img[i];
//                    if(TextUtils.isEmpty(bean.url)){
//                        continue;
//                    }
//                    images.add(bean);
//                }
//                CommonAdapter adapter = new CommonAdapter<ImageInfoBean>(getContext(), R.layout.fragment_find_story_item_image, images) {
//                    @Override
//                    protected void convert(ViewHolder holder, ImageInfoBean item, int position) {
//                        ImageView iv_image = holder.getView(R.id.iv_image);
//                        int px = UIUtils.dp2px(getContext(), 80);
//                        iv_image.getLayoutParams().height = (UIUtils.getScreenWidth(getContext()) - px) / 3;
//                        PicUtils.loadImage(getContext(), item.url, iv_image);
//                    }
//                };
//                gridView.setAdapter(adapter);
//
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
//                CommonAdapter commentAdapter = new CommonAdapter<CommentInfoBean>(getContext(), R.layout.fragment_find_story_item_comment_item, item.comment) {
//                    @Override
//                    protected void convert(ViewHolder holder1, CommentInfoBean item1, int position1) {
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
//
////                ApiCommon.getStoryCommentItems(item.id, (res)->{
////                    List<CommentInfoBean> list = JsonUtils.convert_array(res.data, CommentInfoBean.class);
////                    if(list == null){
////                        return;
////                    }
////                    if(item.comment == null){
////                        item.comment = new ArrayList<>();
////                    }
////                    item.comment.clear();
////                    item.comment.addAll(list);
////                    commentAdapter.notifyDataSetChanged();
////                });
//            }
//        };
//        listView.setAdapter(adapter);
//        listView.setCallback(new MyListView.ListViewCallback() {
//            @Override
//            public void onRefresh() {
//                pageIndex = 0;
//                //data.clear();
//                loadData();
//            }
//            @Override
//            public void onLoadMore() {
//                pageIndex++;
//                loadData();
//            }
//            @Override
//            public void onClickItem(AdapterView<?> parent, View view, int position, long id) {
//                StoryInfoBean bean = data.get(position);
//                Bundle bundle = new Bundle();
//                bundle.putInt("id", bean.id);
//                //ActivityManagerUtils.startActivity(getContext(), ArticleDetailFragment.class, bundle);
//            }
//        });
        //data.clear();
        //loadData();
    }
//    @OnClick(R.id.iv_publish)
//    public void iv_publish_Click(View view){
//        ActivityManagerUtils.startActivity(getContext(), PublishDynamicFragment.class, null);
//    }

//    @Subscribe
//    public void onEventMessage(EventMessenger event) {
//        switch (event.from) {
//            case EventConst.API_STORY_PUBLISH_SUCCESSFUL:
//                //发布成功更新列表
//                break;
//        }
//    }

//    /**
//     * 弹出评论对话框
//     */
//    private Dialog showInputComment(Activity activity, int position, CommonAdapter commentAdapter, View view, CharSequence hint, int type, int touid) {
//        StoryInfoBean item = data.get(position);
//        final InputViewDialog dialog = new InputViewDialog(activity, android.R.style.Theme_Translucent_NoTitleBar);
//        dialog.setContentView(R.layout.view_input_comment);
//        dialog.findViewById(R.id.input_comment_dialog_container).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                //if (listener != null) {
//                    //listener.onDismiss();
//                //}
//            }
//        });
//        final int[] coord = new int[2];
//        if (view != null) {
//            view.getLocationOnScreen(coord);
//        }
//        final EditText input = (EditText) dialog.findViewById(R.id.input_comment);
//        input.setHint(hint);
//        input.setOnEditorActionListener((v, actionId, event) -> {
//            if (actionId == EditorInfo.IME_ACTION_SEND) {
//                return true;
//            }
//            return false;
//        });
//
//        final TextView btn = (TextView) dialog.findViewById(R.id.btn_publish_comment);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String content = input.getText().toString();
//                if(TextUtils.isEmpty(content)){
//                    ToastUtils.showToast(getContext(),"评论内容不能为空!");
//                    return;
//                }
//                ApiCommon.commentStoryItems(item.id, type, touid, input.getText().toString(),(res)->{
//                    CommentInfoBean bean = new CommentInfoBean();
//                    bean.storyid = item.id;
//                    bean.uid = app.me.uid;
//                    bean.name = app.me.nickname;
//                    bean.touid = item.uid;
//                    bean.toname = item.nickname;
//                    if(item.comment == null){
//                        item.comment = new ArrayList<>();
//                    }
//                    item.comment.add(bean);
//                    adapter.notifyDataSetChanged();
//                    ToastUtils.showToast(getContext(), "评论成功!");
//                    dialog.dismiss();
//                });
//            }
//        });
//        dialog.setCancelable(true);
//        dialog.show();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //if (listener != null) {
//                    int[] inputViewCoordinatesInScreen = new int[2];
//                    dialog.findViewById(R.id.input_comment_container).getLocationOnScreen(inputViewCoordinatesInScreen);
//                    // 传入 输入框距离屏幕顶部（不包括状态栏）的位置
//                    //listener.onShow(coord);
//                showListView(view, coord, inputViewCoordinatesInScreen);
//                //}
//            }
//        }, 300);
//        return dialog;
//    }
//
//    public void showListView(View view,int[] coord, int[] inputViewCoordinatesInScreen){
//        if (listView != null) {
//            // 点击某条评论则这条评论刚好在输入框上面，点击评论按钮则输入框刚好挡住按钮
//            //int span = view.getId() == R.id.lin_comment ? 0 : view.getHeight();
//            int span = view.getHeight() + UIUtils.dp2px(getContext(), 12);
//            listView.smoothScrollBy(coord[1] + span - inputViewCoordinatesInScreen[1], 1000);
//        }
//    }
}
