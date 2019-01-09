package com.findyou.findyoueverywhere.bean;

import com.findyou.findyoueverywhere.ui.main.find.FindStoryFragment;
import com.findyou.findyoueverywhere.ui.main.find.category.Category1Fragment;

import java.util.List;

public class StoryInfoBean {
    public class LikeInfoBean{
        public int uid;
        public String name;
    }
    public LikeInfoBean getLikeBean(){
        return new LikeInfoBean();
    }
    public int position; //位置
    public int type; //类
    public int id;
    public int uid;
    public String nickname;
    public String headimage;
    public String content;
    public String images;
    public int like;
    public int share;
    public List<Category1Fragment.CommentInfoBean> comment;
    public String create_time;
    public List<LikeInfoBean> likes;
}
