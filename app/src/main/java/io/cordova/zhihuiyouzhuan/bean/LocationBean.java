package io.cordova.zhihuiyouzhuan.bean;

/**
 * Created by Administrator on 2019/8/15 0015.
 */

public class LocationBean {
    /*{
        "success": 1,
            "message": "成功",
            "latitude": "34.819860",
            "longitude": "113.557899"
    }*/

    private boolean success;

    private String message;

    private String latitude;

    private String longitude;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setLatitude(String latitude){
        this.latitude = latitude;
    }
    public String getLatitude(){
        return this.latitude;
    }
    public void setLongitude(String longitude){
        this.longitude = longitude;
    }
    public String getLongitude(){
        return this.longitude;
    }
}
