package com.findyou.findyoueverywhere.ui.user.edit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import com.findyou.findyoueverywhere.R;
import com.findyou.findyoueverywhere.app.app;
import com.findyou.findyoueverywhere.base.BaseFragment;
import com.findyou.findyoueverywhere.controls.album.AlbumFragment;
import com.findyou.findyoueverywhere.utils.BitmapUtils;
import com.findyou.findyoueverywhere.utils.LoadingUtils;
import com.findyou.findyoueverywhere.utils.ToolUtils;
import com.findyou.findyoueverywhere.utils.http.HttpClient;
import com.findyou.findyoueverywhere.utils.photo.PhotoUpImageItem;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/1/22 0022.
 */

public class EditHeadImageFragment extends BaseFragment {
    @BindView(R.id.iv_headimage)
    ImageView iv_headimage;

    private List<PhotoUpImageItem> images = new ArrayList<>();;

    public int getLayoutId(){
        setTitle("头像");
        return R.layout.fragment_me_headimage;
    }

    public void initView(){
        images.clear();
        if(AlbumFragment.selectedImages != null) {
            images.addAll(AlbumFragment.selectedImages);
        }
        if(images.size() > 0){
            String image = images.get(0).imagePath;
            Bitmap bitmap = BitmapFactory.decodeFile(image, null);
            iv_headimage.setImageBitmap(bitmap);
        }
    }

    @OnClick(R.id.tv_submit)
    public void tv_submit_Clicked(View view) {
        if (app.me == null) {
            return;
        }
        int userid = app.me.uid;
        //LoadingUtils.show("正在上传...");
        //上传图片
        for (int i = 0; i < images.size(); i++) {
            String image = images.get(i).imagePath;
            if (image == "") {
                continue;
            }
            try {
                String imgName = "head_" + userid;
                String imageName = ToolUtils.MD5(imgName).toLowerCase();
                //开始处理图片
                String fileName = BitmapUtils.scale(image, 240, 240);
                BitmapUtils.compress(getContext(), fileName, 32, (obj) -> {
                    File upfile = (File) obj;
                    List<File> files = new ArrayList<>();
                    files.add(upfile);
                    Map<String, String> map = new HashMap<>();
                    map.put("uid", String.valueOf(app.me.uid));
                    //map.put("imageType", UploadImageType.HeadImage.ordinal() + "");
                    map.put("imageName", imageName);
                    HttpClient.uploadImages(files, map, (res) -> {
                        if (res.data == null) {
                            return;
                        }
//                        ApiUser.doUpdateHeadImage(res.data, (res1) -> {
//                            LoadingUtils.close();
//                            AlbumFragment.selectedImages.clear();
//                            images.clear();
//                            app.Me.headimage = res.data;
//                            ToastUtils.showToast(getContext(), "设置头像成功!");
//                            getActivity().finish();
                        //});
                    });
                });
            } catch (Exception e) {
                LoadingUtils.close();
                e.printStackTrace();
            }
        }
    }
}
