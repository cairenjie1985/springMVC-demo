package com.caixin.transfer.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 类SystemProperties.java的实现描述：系统properties文件值
 * 
 */
public class SystemProperties {

    /*
     * mdp项目访问路径
     */
    public final static String  MDP_URL                     = "mdpUrl";

    
    /**
     * 图片上传路径
     */
    public final static String UPLOAD_ROOT_PATH = "uploadRootPath";
    
    /**
     * 图片存盘路径
     */
    public final static String UPLOAD_DRIVER = "uploadDriver";
    
    /**
     * 云端图片地址前缀
     */
    public final static String SKY_IMG_PATH = "skyImgPath";
    
    /**
     * 店铺logo存放地址
     */
    public final static String SHOP_LOGO_PATH = "shopLogoPath";
    
    /**
     * 品牌logo存放地址
     */
    public final static String SHOP_BRAND_LOGO_PATH  = "shopBrandLogoPath";
    
    /**
     * 商品图片存放地址
     */
    public final static String PRODUCT_IMG_PATH = "productImgPath";
    
    /**
     * 用户信息图片存放地址
     */
    public final static String USERINFO_IMG_PATH = "userInfoImgPath";
    
    /**
     * 店铺主页图片存放地址
     */
    public final static String SHOP_BANNER_PATH = "shopBannerPath";
    
    /**
     * 登录校验码模板
     */
    public final static String SMS_DLJYM = "dljym";
    
    /**
     * 注册校验码
     */
    public final static String HY_REGISTER = "hyregister";
    
    public final static String SOLR_URL = "solrUrl";
    
    /**
     * 商户付款，通知供应商发货
     * 
     * 亲爱的忆乡商家：您好，您有一个订单买家于{0}已经付款，请及时查看并发货，如缺货请及时与买家联系取消交易。订单号：{1}
     * 
     */
    public final static String C_PAYMENT_NOTIFY_B = "c_payment_notify_b";
    
    /**
     * 供应商发货提醒买家
     * 
     * 客官，您在忆乡购买的粮草已于今日搬上我们指定的马车，马车编号为{0}，近日将抵达您指定的地址，还请注意查收，如有延误，请及时与我们联系.
     * 
     */
    public final static String B_SEND_GOODS_NOTIFY_C = "b_send_goods_notify_c";
    
    /**
     * 找回密码
     */
    public final static String FIND_PASSCODE = "findpasscode";
    
    /**
     * session认证码超时时间
     */
    public final static String TIMESTAMP_TIMEOUT = "timestampTimeout";
    /**
     * 物流信息链接
     */
    public final static String LOGISTICS_URL = "logisticsUrl";
    
    public final static String SHOP_DEFAULT_LOGO_URL = "shopDefaultLogoUrl";

    /*
     * 用户激活邮件模板名称
     */
    public final static String  MAIL_ACTIVATE_TEMPLATE_NAME = "mailActivateTemplate";
    
    /*
     * 存放配置进来的key —— value
     */
    private Map<String, String> propertiesMap               = new HashMap<String, String>();

    public String getValue(String key) {
        String value = null;
        if (null != propertiesMap) {
            value = propertiesMap.get(key);
        }
        return value;
    }

    public void setPropertiesMap(Map<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

}
