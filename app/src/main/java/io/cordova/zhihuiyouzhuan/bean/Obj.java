package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/4/24.
 */

public class Obj {

        private List<BackInfor> backInfo;

        private String linkUrl;

        private String count;

    public void setBackInfo(List<BackInfor> backInfo){
        this.backInfo = backInfo;
    }
    public List<BackInfor> getBackInfo(){
        return this.backInfo;
    }
    public void setLinkUrl(String linkUrl){
        this.linkUrl = linkUrl;
    }
    public String getLinkUrl(){
        return this.linkUrl;
    }
    public void setCount(String count){
        this.count = count;
    }
    public String getCount(){
        return this.count;
    }
}
