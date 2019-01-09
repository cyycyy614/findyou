package com.findyou.findyoueverywhere.ui.main.find;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiCommon;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.controls.album.AlbumFragment;
import com.findyou.findyoueverywhere.controls.dialog.LoadingDialog;
import com.findyou.findyoueverywhere.ui.main.find.popwindow.SelectCategoryPopWindow;
import com.findyou.findyoueverywhere.ui.main.map.popwindow.SelectTimePopWindow;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.UIUtils;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpImageItem;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class PublishDynamicFragment extends BaseFragment {
    public class ImageInfoBean{
        public String url;
    }

    @BindView(R.id.gridView)
    GridView gridView;

    @BindView(R.id.et_edit)
    EditText et_edit;

    @BindView(R.id.tv_category)
    TextView tv_category;

    private CommonAdapter adapter;
    private String[] category = {"老人","小孩","宠物","其它"};
    private int categoryIndex = 0;

    public int getLayoutId() {
        setTitle("发布消息");
        registerEvent();
        return R.layout.fragment_publish_dynamic;
    }

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("发布");
            textView.setOnClickListener((View view)-> {
                onSubmit();
            });
        }
    }

    public void initView(){
        Bundle bundle = getArguments();
        categoryIndex = bundle.getInt("type");
        tv_category.setText(category[categoryIndex]);

        initSaveBar();
        //GridView gridView = holder.getView(R.id.gridView);
        //List<ImageInfoBean> images = new ArrayList<>();
        images.clear();
        PhotoUpImageItem bean = new PhotoUpImageItem();
        images.add(bean);

//        for(int i=0; i<3; i++){
//            ImageInfoBean bean = new ImageInfoBean();
//            bean.url = "http://img1.imgtn.bdimg.com/it/u=2299010289,2795125034&fm=26&gp=0.jpg";
//            images.add(bean);
//        }
        adapter = new CommonAdapter<PhotoUpImageItem>(getContext(), R.layout.fragment_find_story_item_image, images) {
            @Override
            protected void convert(ViewHolder holder, PhotoUpImageItem item, int position) {
                ImageView iv_image = holder.getView(R.id.iv_image);
                int px = UIUtils.dp2px(getContext(), 40);
                iv_image.getLayoutParams().height = (UIUtils.getScreenWidth(getContext()) - px) / 3;
                if(TextUtils.isEmpty(item.imagePath)){
                    iv_image.setImageResource(R.drawable.ic_empty_phone);
                }else {
                    File file = new File(item.imagePath);
                    Picasso.with(getContext()).load(file).placeholder(R.drawable.default_128).into(iv_image);
                }
                View view = holder.getConvertView();
                view.setOnClickListener((v)->{
                    if(TextUtils.isEmpty(item.imagePath)){
                        Bundle bundle = new Bundle();
                        bundle.putInt("curr_count", images.size()-1);
                        bundle.putInt("count", maxCount);
                        ActivityManagerUtils.startActivity(getContext(), AlbumFragment.class, bundle);
                    }
                });
            }
        };
        gridView.setAdapter(adapter);
    }

    public void onSubmit(){
        String msg = et_edit.getText().toString();
        if(TextUtils.isEmpty(et_edit.getText().toString())){
            ToastUtils.showToast(getContext(), "请输入你的故事!");
            return;
        }
        if(images.size() > 1) {
            uploadfiles();
        }else {
            ApiCommon.addStoryItem(categoryIndex, et_edit.getText().toString(), "", (res1)->{
                ToastUtils.showToast(getContext(), "发布成功!");
                et_edit.setText("");
                images.clear();
                images.add(new PhotoUpImageItem());
                adapter.notifyDataSetChanged();
                postEvent(new EventMessenger(EventConst.API_STORY_PUBLISH_SUCCESSFUL));
                if(getActivity() != null) {
                    getActivity().finish();
                }
            });
        }
    }

    @OnClick(R.id.lin_category)
    public void lin_category_Click(View view){
        SelectCategoryPopWindow popWindow = new SelectCategoryPopWindow(getActivity(), category, categoryIndex);
        popWindow.showFromBottom();
    }

    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_UPLOAD_FILES:
                if(event.obj == null){
                    return;
                }
                List<PhotoUpImageItem> list = JsonUtils.convert_array(event.obj.toString(), PhotoUpImageItem.class);
                int index1 = images.size() - 1;
                if(index1 < 0){
                    index1 = 0;
                }
                images.addAll(index1, list);
                if(images.size() > maxCount){
                    images.remove(images.size()-1);
                }
                adapter.notifyDataSetChanged();
                break;
            case EventConst.API_STORY_SELECT_CATEGORY:
                categoryIndex = Integer.parseInt(event.obj.toString());
                String c = category[categoryIndex];
                if(tv_category != null){
                    tv_category.setText(c);
                }
                break;
        }
    }

    int uploadNumber = 1;
    private List<PhotoUpImageItem> images = new ArrayList<>();
    private int maxCount = 9;
    private List<String> imagefiles = new ArrayList<>();
    public void uploadfiles(){
        uploadNumber = 1;
        imagefiles.clear();
        LoadingDialog popWindow = new LoadingDialog(getActivity());
        popWindow.setText("正在发布...");
        popWindow.showFromCenter();
        //上传图片
        for(int i=0; i<images.size(); i++){
            String image = images.get(i).imagePath;
            if(image == ""){
                continue;
            }
            try {
                String fileName = BitmapUtils.scale(image, 540, 540);
                BitmapUtils.compress(getContext(), fileName, 32, (obj) -> {
                    File upfile = (File) obj;
                    List<File> files = new ArrayList<>();
                    files.add(upfile);
                    Map<String,String> map = new HashMap<>();
                    map.put("uid", String.valueOf(app.me.uid));
                    map.put("fileName", ToolUtils.getGuid());
                    HttpClient.uploadImages(files, map, (res) -> {
                        if(res.data != null) {
                            imagefiles.add(res.data);
                            log.write("update headimage ok:" + imagefiles.size());
                            uploadNumber++;
                        }
                        log.write("upload number:" + uploadNumber);
                        if(uploadNumber == images.size()){
                            log.write("ok");
                            String images1 = "";
                            for(int j=0; j<imagefiles.size(); j++){
                                images1 += imagefiles.get(j);
                                if(j != imagefiles.size() - 1){
                                    images1 += ";";
                                }
                            }
                            ApiCommon.addStoryItem(categoryIndex, et_edit.getText().toString(), images1, (res1)->{
                                ToastUtils.showToast(getContext(), "发布成功!");
                                popWindow.close();
                                et_edit.setText("");
                                images.clear();
                                images.add(new PhotoUpImageItem());
                                adapter.notifyDataSetChanged();
                            });
                        }
                    });
                });
            }catch (Exception e){
                e.printStackTrace();
                popWindow.close();
            }
        }
    }
}
