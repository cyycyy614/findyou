package com.findyou.findyoueverywhere.utils.photo;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 异步线程类实现该功能
 */
public class PhotoUpAlbumHelper extends AsyncTask<Object, Object, Object> {

    public class PhotoUpImageBucket{
        public List<PhotoUpImageItem> imageList;
        public String bucketName;
        public int count;
    }

    final String TAG = getClass().getSimpleName();
    Context context;
    ContentResolver cr;
    // 缩略图列表
    HashMap<String, String> thumbnailList = new HashMap<String, String>();
    // 专辑列表
    List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    HashMap<String, PhotoUpImageBucket> bucketList = new HashMap<String, PhotoUpImageBucket>();
    private GetAlbumList getAlbumList;
    //获取实例
    public static PhotoUpAlbumHelper getHelper() {
        PhotoUpAlbumHelper instance = new PhotoUpAlbumHelper();
        return instance;
    }

    /**
     * 初始化
     * @param context
     */
    public void init(Context context) {
        if (this.context == null) {
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    /**
     * 得到缩略图，这里主要得到的是图片的ID值
     */
    private void getThumbnail() {
        String[] projection = { MediaStore.Images.Thumbnails._ID, MediaStore.Images.Thumbnails.IMAGE_ID,
                MediaStore.Images.Thumbnails.DATA};
        Cursor cursor1 = MediaStore.Images.Thumbnails.queryMiniThumbnails(cr, MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                MediaStore.Images.Thumbnails.MINI_KIND, projection);
        getThumbnailColumnData(cursor1);
        cursor1.close();
    }

    /**
     * 从数据库中得到缩略图
     * @param cur
     */
    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(MediaStore.Images.Thumbnails.DATA);
            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put("" + image_id, image_path);
            } while (cur.moveToNext());
        }
    }
    /**
     * 是否创建了图片集
     */
    boolean hasBuildImagesBucketList = false;
    /**
     * 得到图片集
     */
    private void buildImagesBucketList() {
        // 构造缩略图索引
        getThumbnail();
        // 构造相册索引
        String columns[] = new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.PICASA_ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.SIZE, MediaStore.Images.Media.BUCKET_DISPLAY_NAME };
        // 得到一个游标

        Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null,
                MediaStore.Images.Media.DATE_MODIFIED+" desc");
        if (cur.moveToFirst()) {
            // 获取指定列的索引
            int photoIDIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
            int photoPathIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            int bucketDisplayNameIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            int bucketIdIndex = cur.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID);
            /**
             * Description:这里增加了一个判断：判断照片的名
             * 字是否合法，例如.jpg .png等没有名字的格式
             * 如果图片名字是不合法的，直接过滤掉
             */
            do {
                if (cur.getString(photoPathIndex).substring(
                        cur.getString(photoPathIndex).lastIndexOf("/")+1,
                        cur.getString(photoPathIndex).lastIndexOf("."))
                        .replaceAll(" ", "").length()<=0)
                {
                    //Log.d(TAG, "出现了异常图片的地址：cur.getString(photoPathIndex)="+cur.getString(photoPathIndex));
                }else {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    String bucketName = cur.getString(bucketDisplayNameIndex);
                    String bucketId = cur.getString(bucketIdIndex);
                    PhotoUpImageBucket bucket = bucketList.get(bucketId);
                    //这里完成图片归并到响应的相册里去
                    if (bucket == null) {
                        bucket = new PhotoUpImageBucket();
                        bucketList.put(bucketId, bucket);
                        bucket.imageList = new ArrayList<PhotoUpImageItem>();
                        bucket.bucketName = bucketName;
                    }
                    bucket.count++;
                    PhotoUpImageItem imageItem = new PhotoUpImageItem();
                    imageItem.imageId = _id;
                    imageItem.imagePath = path;
                    bucket.imageList.add(imageItem);
                }
            } while (cur.moveToNext());
        }
        cur.close();
        hasBuildImagesBucketList = true;
    }

    /**
     * 得到图片集
     * @param refresh
     * @return
     */
    private List<PhotoUpImageBucket> getImagesBucketList(boolean refresh) {
        if (refresh || (!refresh && !hasBuildImagesBucketList)) {
            buildImagesBucketList();
        }
        List<PhotoUpImageBucket> tmpList = new ArrayList<PhotoUpImageBucket>();
        Iterator<Map.Entry<String, PhotoUpImageBucket>> itr = bucketList.entrySet().iterator();
        //将Hash转化为List
        while (itr.hasNext()) {
            Map.Entry<String, PhotoUpImageBucket> entry = (Map.Entry<String, PhotoUpImageBucket>) itr
                    .next();
            tmpList.add(entry.getValue());
        }
        return tmpList;
    }

    /**
     * 得到原始图像路径
     * @param image_id
     * @return
     */
    private String getOriginalImagePath(String image_id) {
        String path = null;
        String[] projection = { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA };
        Cursor cursor = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                MediaStore.Images.Media._ID + "=" + image_id, null, MediaStore.Images.Media.DATE_MODIFIED+" desc");
        if (cursor != null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        }
        return path;
    }

    public void setGetAlbumList(GetAlbumList getAlbumList) {
        this.getAlbumList = getAlbumList;
    }
    //回调接口，当完成相册和图片的获取之后，调用该接口的方法传递数据，这种方法很常用，大家务必掌握
    public interface GetAlbumList{
        public void getAlbumList(List<PhotoUpImageBucket> list);
    }

    @Override
    protected Object doInBackground(Object... params) {
        return getImagesBucketList((Boolean)(params[0]));
    }
    @SuppressWarnings("unchecked")
    @Override
    protected void onPostExecute(Object result) {
        super.onPostExecute(result);
        getAlbumList.getAlbumList((List<PhotoUpImageBucket>)result);
    }
}