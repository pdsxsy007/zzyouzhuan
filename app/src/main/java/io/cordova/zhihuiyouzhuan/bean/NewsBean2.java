package io.cordova.zhihuiyouzhuan.bean;

/**
 * Created by Cuilei on 2020/3/24.
 */

public class NewsBean2 {
    private boolean success;

    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    private String obj ;

    private String count;

    private String attributes;

    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }

    public void setCount(String count){
        this.count = count;
    }
    public String getCount(){
        return this.count;
    }
    public void setAttributes(String attributes){
        this.attributes = attributes;
    }
    public String getAttributes(){
        return this.attributes;
    }


}
