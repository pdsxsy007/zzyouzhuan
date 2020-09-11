package io.cordova.zhihuiyouzhuan.bean;

/**
 * Created by Cuilei on 2020/4/24.
 */

public class Root
{
    private boolean success;

    private String msg;

    private Obj obj;

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
    public void setObj(Obj obj){
        this.obj = obj;
    }
    public Obj getObj(){
        return this.obj;
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
