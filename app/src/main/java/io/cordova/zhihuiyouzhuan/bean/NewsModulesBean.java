package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/9/16.
 */

public class NewsModulesBean {

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public List<Obj> getObj() {
        return obj;
    }

    public void setObj(List<Obj> obj) {
        this.obj = obj;
    }

    private String count;
    private String attributes;

    private List<Obj> obj;
    public class Obj {
        private int portalNewsModuleId;
        private String portalNewsModuleName;
        private int portalNewsModuleOrder;

        public int getPortalNewsModuleId() {
            return portalNewsModuleId;
        }

        public void setPortalNewsModuleId(int portalNewsModuleId) {
            this.portalNewsModuleId = portalNewsModuleId;
        }

        public String getPortalNewsModuleName() {
            return portalNewsModuleName;
        }

        public void setPortalNewsModuleName(String portalNewsModuleName) {
            this.portalNewsModuleName = portalNewsModuleName;
        }

        public int getPortalNewsModuleOrder() {
            return portalNewsModuleOrder;
        }

        public void setPortalNewsModuleOrder(int portalNewsModuleOrder) {
            this.portalNewsModuleOrder = portalNewsModuleOrder;
        }

        public String getPortalNewsModuleCreatetime() {
            return portalNewsModuleCreatetime;
        }

        public void setPortalNewsModuleCreatetime(String portalNewsModuleCreatetime) {
            this.portalNewsModuleCreatetime = portalNewsModuleCreatetime;
        }

        public String getPortalNewsModuleState() {
            return portalNewsModuleState;
        }

        public void setPortalNewsModuleState(String portalNewsModuleState) {
            this.portalNewsModuleState = portalNewsModuleState;
        }

        public String getPortalNewsModuleDelete() {
            return portalNewsModuleDelete;
        }

        public void setPortalNewsModuleDelete(String portalNewsModuleDelete) {
            this.portalNewsModuleDelete = portalNewsModuleDelete;
        }

        private String portalNewsModuleCreatetime;
        private String portalNewsModuleState;
        private String portalNewsModuleDelete;

    }

}
