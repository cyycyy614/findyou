package com.findyou.findyoueverywhere.ui.user.edit;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.api.http.ApiUser;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.base.EventMessenger;
import com.findyou.findyoueverywhere.base.ViewType;
import com.findyou.findyoueverywhere.bean.common.UserInfoBean;
import com.findyou.findyoueverywhere.constant.EventConst;
import com.findyou.findyoueverywhere.constant.FragmentConst;
import com.findyou.findyoueverywhere.controls.album.AlbumFragment;
import com.findyou.findyoueverywhere.utils.ActivityManagerUtils;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.PicUtils;
import com.findyou.findyoueverywhere.utils.ToastUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.http.QueryString;
import com.findyou.findyoueverywhere.utils.json.JsonUtils;
import com.findyou.findyoueverywhere.utils.log.log;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpImageItem;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends BaseFragment {

    @BindView(R.id.iv_headimage)
    CircleImageView iv_headimage;

    @BindView(R.id.tv_nickname)
    EditText tv_nickname;

    @BindView(R.id.tv_phone)
    EditText tv_phone;

    @BindView(R.id.tv_email)
    EditText tv_email;

    @BindView(R.id.tv_sex)
    TextView tv_sex;

    @BindView(R.id.tv_birthday)
    TextView tv_birthday;
//
//    @BindView(R.id.iv_headimage)
//    CircleImageView iv_headimage;
//
//    @BindView(R.id.tv_location)
//    TextView tv_location;

    String headimage = "";

    public void initSaveBar(){
        TextView textView = (TextView)getRightView(ViewType.TextView);
        if(textView != null){
            textView.setText("保存");
            textView.setTextColor(Color.parseColor("#ffffff"));
            textView.setOnClickListener((View view)-> {
                log.write("headimage:" + headimage);
                if(TextUtils.isEmpty(headimage)){
                    ToastUtils.showToast(getContext(),"请上传头像!");
                    return;
                }
                if(TextUtils.isEmpty(tv_nickname.getText().toString())){
                    ToastUtils.showToast(getContext(),"请输入昵称!");
                    return;
                }
                if(!TextUtils.isEmpty(tv_phone.getText().toString())){
                    if(!ToolUtils.isPhoneNumber(tv_phone.getText().toString())){
                        ToastUtils.showToast(getContext(), "请输入正确的手机号!");
                        return;
                    }
                }
                QueryString q = new QueryString();
                q.add("uid", app.me.uid);
                q.add("nickname", tv_nickname.getText().toString());
                q.add("phone", tv_phone.getText().toString());
                q.add("email", tv_email.getText().toString());
                q.add("birthday", tv_birthday.getText().toString());
                q.add("sex", sex);
                q.add("headimage", headimage);
                ApiUser.updateUserInfo(q, (res)->{
                    UserInfoBean bean = JsonUtils.convert(res.data, UserInfoBean.class);
                    if(bean != null){
                        app.me = bean;
                    }
                    ToastUtils.showToast(getContext(), "更新用户信息成功!");
                });
            });
        }
    }

    public int getLayoutId(){
        initSaveBar();
        registerEvent();
        return R.layout.fragment_me_1;
    }

    public void initView(){
        setTitle("编辑资料");
        initUI();
    }

    public void initUI(){
        if(tv_nickname != null){
            tv_nickname.setText(app.me.nickname);
        }
        tv_birthday.setText(app.me.birthday);
        tv_email.setText(app.me.email);
        tv_phone.setText(app.me.phone);
        if(app.me.sex == 1){
            tv_sex.setText("男");
        }else{
            tv_sex.setText("女");
        }
        log.write("hello,world");
        sex = app.me.sex;
        headimage = app.me.headimage;
        PicUtils.loadImage(getContext(), app.me.headimage, iv_headimage);
    }

    public void onVisibleChanged(boolean isVisible){
        if(isVisible){

        }
    }

//    private void initUserInfo(){
//        if(app.me == null){
//            return;
//        }
//        if(iv_headimage != null){
//            if(!TextUtils.isEmpty(app.me.headimage)) {
//                PicUtils.loadImage(getContext(), app.me.headimage + "?" + new Date().getTime(), iv_headimage);
//            }
//        }
//        if(tv_nickname != null){
//            tv_nickname.setText(app.me.nickname);
//        }
//        if(tv_signature != null){
//            tv_signature.setText(app.me.sign);
//        }
//        if(tv_sex != null){
//            if(app.me.sex){
//                tv_sex.setText("男");
//            }else{
//                tv_sex.setText("女");
//            }
//        }
//        if(tv_birthday != null){
//            tv_birthday.setText(app.me.birthday);
//        }
//
//        //位置信息
//        if(tv_location != null){
//            tv_location.setText(app.me.location);
//        }
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
//        //initUserInfo();
//    }
//
    @OnClick(R.id.layout_headimage)
    public void layout_headimage_Clicked(View view){
        Bundle bundle = new Bundle();
        bundle.putInt("curr_count", 0);
        bundle.putInt("count", 1);
        ActivityManagerUtils.startActivity(getContext(), AlbumFragment.class, bundle);
    }
//
//    @OnClick(R.id.layout_nickname)
//    public void layout_nickname_Clicked(View view){
//        //ActivityManagerUtils.startActivity(this.getContext(), FragmentConst.EditNickname);
//    }
//
//    @OnClick(R.id.layout_signature)
//    public void layout_signature_Clicked(View view){
//        //ActivityManagerUtils.startActivity(this.getContext(), FragmentConst.EditSignature);
//    }
//
    @OnClick(R.id.layout_sex)
    public void layout_sex_Clicked(View view){
        SexPopWindow pop = new SexPopWindow(getActivity());
        pop.showFromBottom();
    }
//
//    @OnClick(R.id.layout_location)
//    public void layout_location_Clicked(View view){
//
//    }
//
    @OnClick(R.id.layout_birthday)
    public void layout_birthday_Clicked(View view){
        BirthdayPopWindow pop = new BirthdayPopWindow(getActivity(), app.me.birthday);
        pop.showFromBottom();
    }

    private int sex;
    @Subscribe
    public void onEventMessage(EventMessenger event) {
        switch (event.from) {
            case EventConst.API_EDIT_BIRTHDAY:
                if (tv_birthday != null) {
                    String birthday = event.obj.toString();
                    tv_birthday.setText(birthday);
                }
                break;
            case EventConst.API_EDIT_SEX:
                sex = (int)event.obj;
                if(tv_sex != null){
                    if(sex == 1){
                        tv_sex.setText("男");
                    }else{
                        tv_sex.setText("女");
                    }
                }
                break;
            case EventConst.API_UPLOAD_FILES:
                if(event.obj == null){
                    return;
                }
                List<PhotoUpImageItem> list = JsonUtils.convert_array(event.obj.toString(), PhotoUpImageItem.class);
                int index1 = images.size() - 1;
                if(index1 < 0){
                    index1 = 0;
                }
                images.clear();
                images.addAll(index1, list);
                //LoadingUtils.show("上传头像...");
                uploadfiles();
                break;
        }
    }
    int uploadNumber = 0;
    private List<PhotoUpImageItem> images = new ArrayList<>();
    public void uploadfiles(){
        uploadNumber = 0;
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
                            headimage = res.data;
                            log.write("update headimage ok:" + headimage);
                            uploadNumber++;
                        }
                        if(uploadNumber == images.size()){
                            ToastUtils.showToast(getContext(), "保存成功!");
                            LoadingUtils.close();
                            if(iv_headimage != null){
                                iv_headimage.post(()->{
                                    Picasso.with(getContext()).load(upfile).into(iv_headimage);
                                });
                            }
                        }
                    });
                });
            }catch (Exception e){
                e.printStackTrace();
                LoadingUtils.close();
            }
        }
    }
}

