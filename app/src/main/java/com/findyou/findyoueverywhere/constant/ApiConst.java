package com.findyou.findyoueverywhere.constant;

/**
 * Created by Administrator on 2018/1/19 0019.
 */

public class ApiConst {
//    public final static String BASE_ROOT_URL = "http://www.joysw.com:8090/";
    public final static String BASE_ROOT_URL = "http://192.168.60.188:8090/";
//    public final static String BASE_ROOT_URL = "http://sw.joyvc.com:8090/";
    public final static String BASE_URL_DATA = BASE_ROOT_URL + "api/"; //数据接口
    public final static String BASE_URL_MODULE = "http://www.joysw.com:8090/api/"; //功能模块
    public final static String BASE_URL_IMAGES = "http://www.joysw.com:8880/images/"; //图片存储
    public final static int PAGE_SIZE = 30; //分页大小

    public static String getUrlNew(String moduleUrl){
        String url = BASE_URL_DATA + moduleUrl;
        return url;
    }

    public static String getModuleUrl(String moduleUrl){
        String url = BASE_URL_DATA + moduleUrl;
        return url;
    }

    public static String getImageUrl(String imageUrl){
        String url = BASE_URL_IMAGES + imageUrl;
        return url;
    }

    public final static String API_app_getAppConfig = "app.asmx/getAppConfig";

    //Module==================================================================================================
    public final static String API_MODULE_UP_IMAGES = "file/upload"; //文件接口 ok

    //Common==================================================================================================
    //common
    public final static String API_COMMON_HELP = "common/help";
    public final static String API_COMMON_HELP_ITEM = "common/help/getItem";
    public final static String API_COMMON_HELP_SEARCH = "common/help/search";
    public final static String API_COMMON_APP_INFO = "common/getAppInfo";
    public final static String API_COMMON_GET_MSG = "common/msg";
    public final static String API_COMMON_GET_BANNERS = "common/banner";
    public final static String API_COMMON_NEWS_LIST = "common/news";
    public final static String API_COMMON_NEWS_ITEM = "common/news/getItem";
    public final static String API_COMMON_ADD_STORY_ITEM = "common/story/add";
    public final static String API_COMMON_GET_STORY_ITEMS = "common/story";
    public final static String API_COMMON_LIKE_STORY_ITEM = "common/story/like";
    public final static String API_COMMON_COMMENT_STORY_ITEM = "common/story/comment";
    //public final static String API_COMMON_COMMENT_STORY_ITEMS = "common/story/comment";

    //auth
    public final static String API_AUTH_TOKEN = "token"; //App的版本信息 ok
    //app
    public final static String API_APP_VERSION_INFO = "app/getAppInfo"; //App的版本信息 ok
    public final static String API_APP_GET_BANNERS = "app/getBanners"; //广告接口 ok
    //public final static String API_APP_GET_CHANNEL = "app/getAppUpdateInfoFromChannel";
    public final static String API_APP_CREATE_USER_CHANNEL_INFO = "app/registerChannelInfo";

    //user
    public final static String API_LOGIN = "user/login"; //登录
    public final static String API_LOGOUT = "user/logout"; //退出
    public final static String API_REGISTER = "user/register"; //注册
    public final static String API_THIRD_LOGIN = "user/thirdLogin"; //第三方登录
    public final static String API_RESET_PASSWORD = "user/resetPassword"; //找回密码
    public final static String API_SEND_CODE = "user/sendAuthCode"; //发送验证码
    public final static String API_CHANGE_PASSWORD = "user/changePassword"; //修改密码

    //guardian
    //被监护人
    public final static String API_GUARDIAN_child_ADD = "guardian/child/add"; //添加
    public final static String API_GUARDIAN_child_REMOVE = "guardian/child/remove"; //删除
    public final static String API_GUARDIAN_child_LIST = "guardian/child/list"; //列表
    public final static String API_GUARDIAN_child_GET_ITEM = "guardian/child/getItem"; //单个
    public final static String API_GUARDIAN_child_UPDATE_ITEM = "guardian/child/update"; //修改
    public final static String API_GUARDIAN_child_UPDATE_IMEI = "guardian/child/changeImei"; //更换终端
    public final static String API_GUARDIAN_CHILD_IS_EXIST_BIND_DEVICE = "guardian/child/isExistBindDevice";
    public final static String API_GUARDIAN_CHILD_IS_EXIST_CONST = "guardian/child/constant";

    //监护人
    public final static String API_GUARDIAN_ADD = "guardian/add"; //添加
    public final static String API_GUARDIAN_LIST = "guardian/list"; //列表
    public final static String API_GUARDIAN_UPDATE = "guardian/update"; //更新
    public final static String API_GUARDIAN_SET_MAIN = "guardian/setMain";
    public final static String API_GUARDIAN_REMOVE = "guardian/remove"; //删除
    public final static String API_GUARDIAN_UNBIND = "guardian/unbind";

    //报警设置
    public final static String API_GUARDIAN_WARN_SET = "guardian/warn/setting";
    public final static String API_GUARDIAN_WARN_GET = "guardian/warn/getItem";
    public final static String API_GUARDIAN_WARN_HISTORY = "guardian/warn/history";

    //电子围栏
    public final static String API_GUARDIAN_EFENCE_ADD_ITEM = "guardian/efence/add";
    public final static String API_GUARDIAN_EFENCE_DEL_ITEM = "guardian/efence/remove";
    public final static String API_GUARDIAN_EFENCE_GET_ITEM = "guardian/efence/getItem";
    public final static String API_GUARDIAN_EFENCE_UPDATE_ITEM = "guardian/efence/update";
    public final static String API_GUARDIAN_EFENCE_LIST = "guardian/efence/list";

    //终端设置
    public final static String API_GUARDIAN_DEVICE_SET_ITEM = "guardian/device/setting";
    public final static String API_GUARDIAN_DEVICE_GET_ITEM = "guardian/device/getItem";

    //follow
    public final static String API_MAIN_GET_FOLLOWERS = "live/main/getFollowers"; //我关注的人 ok
    public final static String API_MAIN_GET_FANS = "live/main/getFans";

    //user info
    public final static String API_USER_UPDATE_NICKNAME = "user/updateNickname"; //更新昵称 ok
    public final static String API_USER_UPDATE_HEADIMAGE = "user/updateHeadImage"; //头像 ok
    public final static String API_USER_UPDATE_SIGN = "user/updateSign"; //签名 ok
    public final static String API_USER_UPDATE_SEX = "user/updateSex"; //性别 ok
    public final static String API_USER_UPDATE_BIRTHDAY = "user/updateBirthday"; //生日 ok
    public final static String API_USER_UPDATE_USER_INFO = "user/updateUserInfo"; //生日 ok

    public final static String API_USER_GET_USER_INFO = "user/getUserInfo"; //用户信息 ok
    public final static String API_USER_BIND_PHONE_NUMBER = "user/bindPhoneNumber"; //绑定手机 ok
    public final static String API_USER_FEEDBACK = "user/feedback"; //反馈 ok
    public final static String API_USER_REAL_NAME_VERIFY = "user/realnameVerify"; //反馈 ok
    public final static String API_USER_UPDATE_LOCATION = "user/updateLocation"; //更新位置信息

    //pay items
    public final static String API_PAY_GET_ITEMS = "pay/getPayItems"; //到得充值的项 ok
    public final static String API_PAY_PAY = "pay/pay"; //去充值 ok
    public final static String API_PAY_List = "pay/getPayList"; //充值记录
    public final static String API_PAY_Cash_List = "pay/getCashList"; //提现记录
    //主界面
    public final static String API_MAIN_FOLLOW = "live/main/follow"; //ok
    public final static String API_MAIN_IS_FOLLOW = "live/main/isFollow"; //ok
    public final static String API_GET_USER_INFO_POP_WINDOW = "live/main/getUserInfoPopWindow"; //小主页 ok
    public final static String API_MAIN_SEARCH_BY_USERID = "live/main/searchByUserId"; //ok
    public final static String API_MAIN_SEARCH_BY_NICKNAME = "live/main/searchByNickname"; //ok

    //chat
    //public final static String API_CHAT_GET_RECONTACT = "live/chat/getRecentContacts";
    //public final static String API_CHAT_ADD_RECONTACT = "live/chat/addRecentContact";

    //Dynamic
    public final static String API_PUBLISH_DYNAMIC = "dynamic/publish"; //发布动态
    public final static String API_GET_DYNAMICS = "dynamic/getDynamics";
    public final static String API_GET_DYNAMIC_COMMENTS = "dynamic/getDynamicComments";
    public final static String API_GET_DYNAMIC_LIKES = "dynamic/getDynamicLikes";
    public final static String API_GET_DYNAMIC_ItemInfo = "dynamic/getDynamicItemInfo";
    public final static String API_GET_DYNAMIC_COMMENT = "dynamic/comment";
    public final static String API_GET_DYNAMIC_LIKE = "dynamic/like";

    //Live==================================================================================================
    //manager operate
    public final static String API_LIVE_GET_ROOM_MANAGERS = "live/manager/getManagers"; //得到管理员列表 ok
    public final static String API_LIVE_ADD_ROOM_MANAGER = "live/manager/add"; //添加管理员 ok
    public final static String API_LIVE_DEL_ROOM_MANAGER = "live/manager/remove"; //删除管理员 ok
    public final static String API_LIVE_MANAGER_TIPOFF = "live/manager/tipOff"; //举报 ok
    public final static String API_LIVE_ROOM_GAG = "live/manager/gag"; //禁言 ok
    public final static String API_LIVE_GET_GAG_USERS = "live/manager/getGagUsers"; //禁言列表 ok

    //get gifts list.
    public final static String API_LIVE_GET_GIFTS = "live/room/getGifts"; //礼物列表 ok
    public final static String API_GET_ANCHOR_INFO = "live/room/getAnchorInfo"; //主播信息 ok
    public final static String API_SEND_GIFT = "live/room/sendGift"; //ok
    public final static String API_LIVE_ROOM_INFO = "live/room/getRoomInfo"; //ok
    public final static String API_LIVE_TICK_LIVE = "live/room/tickLive"; //直播心跳

    //主播
    public final static String API_LIVE_APPLY_LIVE = "live/anchor/applyLive"; //申请直播
    public final static String API_LIVE_UPDATE_IMAGE = "live/anchor/updateLiveImage"; //直播封面
    public final static String API_LIVE_UPDATE_GUILDID = "live/anchor/updateGuildId"; //公会

    //观众
    public final static String API_LIVE_IN_ROOM = "live/room/inRoom"; //进入房间
    public final static String API_LIVE_OUT_ROOM = "live/room/outRoom"; //出房间
    public final static String API_LIVE_AUDIENCE = "live/room/getAudience"; //观众列表

}
