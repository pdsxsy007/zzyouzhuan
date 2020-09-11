package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/3/24.
 */

public class NesItemBean {

    private String caturl;

    public String getCaturl() {
        return caturl;
    }

    public void setCaturl(String caturl) {
        this.caturl = caturl;
    }

    public List<Obj> getData() {
        return data;
    }

    public void setData(List<Obj> data) {
        this.data = data;
    }

    private List<Obj> data;

    public class Obj{

        private String inputtime;

        public String getInputtime() {
            return inputtime;
        }

        public void setInputtime(String inputtime) {
            this.inputtime = inputtime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        private String title;
        private String url;
    }

}
