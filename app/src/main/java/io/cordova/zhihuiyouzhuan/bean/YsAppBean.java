package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/9/22.
 */

public class YsAppBean {

    private boolean success;
    private String msg;
    private String attributes;

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

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<Obj> getObj() {
        return obj;
    }

    public void setObj(List<Obj> obj) {
        this.obj = obj;
    }

    private String count;

    private List<Obj> obj;
    public static class Obj{

        private int modulesId;
        private String modulesCode;
        private String modulesParentCode;
        private String modulesName;
        private int modulesSort;
        private String modulesCreateTime;
        private int modulesState;
        private String modulesImages;
        private List<String> childMenus;

        public int getModulesId() {
            return modulesId;
        }

        public void setModulesId(int modulesId) {
            this.modulesId = modulesId;
        }

        public String getModulesCode() {
            return modulesCode;
        }

        public void setModulesCode(String modulesCode) {
            this.modulesCode = modulesCode;
        }

        public String getModulesParentCode() {
            return modulesParentCode;
        }

        public void setModulesParentCode(String modulesParentCode) {
            this.modulesParentCode = modulesParentCode;
        }

        public String getModulesName() {
            return modulesName;
        }

        public void setModulesName(String modulesName) {
            this.modulesName = modulesName;
        }

        public int getModulesSort() {
            return modulesSort;
        }

        public void setModulesSort(int modulesSort) {
            this.modulesSort = modulesSort;
        }

        public String getModulesCreateTime() {
            return modulesCreateTime;
        }

        public void setModulesCreateTime(String modulesCreateTime) {
            this.modulesCreateTime = modulesCreateTime;
        }

        public int getModulesState() {
            return modulesState;
        }

        public void setModulesState(int modulesState) {
            this.modulesState = modulesState;
        }

        public String getModulesImages() {
            return modulesImages;
        }

        public void setModulesImages(String modulesImages) {
            this.modulesImages = modulesImages;
        }

        public List<String> getChildMenus() {
            return childMenus;
        }

        public void setChildMenus(List<String> childMenus) {
            this.childMenus = childMenus;
        }

        public List<Apps> getApps() {
            return apps;
        }

        public void setApps(List<Apps> apps) {
            this.apps = apps;
        }

        public String getModulesParentName() {
            return modulesParentName;
        }

        public void setModulesParentName(String modulesParentName) {
            this.modulesParentName = modulesParentName;
        }

        private List<Apps> apps;
        private String modulesParentName;

        public static class Apps{

            private int appId;
            private String appName;
            private String appBelongSystem;
            private String appImages;
            private String appIntroduction;
            private int appLinkedWay;
            private String appIosSchema;
            private String appIosDownloadLink;
            private String appAndroidSchema;
            private String appAndroidDownloadLink;
            private int appIntranet;
            private int appUpordown;
            private int appRecommend;
            private int appSort;

            public int getAppId() {
                return appId;
            }

            public void setAppId(int appId) {
                this.appId = appId;
            }

            public String getAppName() {
                return appName;
            }

            public void setAppName(String appName) {
                this.appName = appName;
            }

            public String getAppBelongSystem() {
                return appBelongSystem;
            }

            public void setAppBelongSystem(String appBelongSystem) {
                this.appBelongSystem = appBelongSystem;
            }

            public String getAppImages() {
                return appImages;
            }

            public void setAppImages(String appImages) {
                this.appImages = appImages;
            }

            public String getAppIntroduction() {
                return appIntroduction;
            }

            public void setAppIntroduction(String appIntroduction) {
                this.appIntroduction = appIntroduction;
            }

            public int getAppLinkedWay() {
                return appLinkedWay;
            }

            public void setAppLinkedWay(int appLinkedWay) {
                this.appLinkedWay = appLinkedWay;
            }

            public String getAppIosSchema() {
                return appIosSchema;
            }

            public void setAppIosSchema(String appIosSchema) {
                this.appIosSchema = appIosSchema;
            }

            public String getAppIosDownloadLink() {
                return appIosDownloadLink;
            }

            public void setAppIosDownloadLink(String appIosDownloadLink) {
                this.appIosDownloadLink = appIosDownloadLink;
            }

            public String getAppAndroidSchema() {
                return appAndroidSchema;
            }

            public void setAppAndroidSchema(String appAndroidSchema) {
                this.appAndroidSchema = appAndroidSchema;
            }

            public String getAppAndroidDownloadLink() {
                return appAndroidDownloadLink;
            }

            public void setAppAndroidDownloadLink(String appAndroidDownloadLink) {
                this.appAndroidDownloadLink = appAndroidDownloadLink;
            }

            public int getAppIntranet() {
                return appIntranet;
            }

            public void setAppIntranet(int appIntranet) {
                this.appIntranet = appIntranet;
            }

            public int getAppUpordown() {
                return appUpordown;
            }

            public void setAppUpordown(int appUpordown) {
                this.appUpordown = appUpordown;
            }

            public int getAppRecommend() {
                return appRecommend;
            }

            public void setAppRecommend(int appRecommend) {
                this.appRecommend = appRecommend;
            }

            public int getAppSort() {
                return appSort;
            }

            public void setAppSort(int appSort) {
                this.appSort = appSort;
            }

            public int getAppHeat() {
                return appHeat;
            }

            public void setAppHeat(int appHeat) {
                this.appHeat = appHeat;
            }

            public String getAppCreateTime() {
                return appCreateTime;
            }

            public void setAppCreateTime(String appCreateTime) {
                this.appCreateTime = appCreateTime;
            }

            public int getAppLoginFlag() {
                return appLoginFlag;
            }

            public void setAppLoginFlag(int appLoginFlag) {
                this.appLoginFlag = appLoginFlag;
            }

            public String getAppSecret() {
                return appSecret;
            }

            public void setAppSecret(String appSecret) {
                this.appSecret = appSecret;
            }

            public String getAppNewWindow() {
                return appNewWindow;
            }

            public void setAppNewWindow(String appNewWindow) {
                this.appNewWindow = appNewWindow;
            }

            public int getAppModulesId() {
                return appModulesId;
            }

            public void setAppModulesId(int appModulesId) {
                this.appModulesId = appModulesId;
            }

            public String getAppUrl() {
                return appUrl;
            }

            public void setAppUrl(String appUrl) {
                this.appUrl = appUrl;
            }

            public String getPortalAppIcon() {
                return portalAppIcon;
            }

            public void setPortalAppIcon(String portalAppIcon) {
                this.portalAppIcon = portalAppIcon;
            }

            public String getSystemName() {
                return systemName;
            }

            public void setSystemName(String systemName) {
                this.systemName = systemName;
            }

            public String getIconCount() {
                return iconCount;
            }

            public void setIconCount(String iconCount) {
                this.iconCount = iconCount;
            }

            public String getPortalAppAuthentication() {
                return portalAppAuthentication;
            }

            public void setPortalAppAuthentication(String portalAppAuthentication) {
                this.portalAppAuthentication = portalAppAuthentication;
            }

            public String getSystemIdList() {
                return systemIdList;
            }

            public void setSystemIdList(String systemIdList) {
                this.systemIdList = systemIdList;
            }

            public String getPortalRoleAppModulesList() {
                return portalRoleAppModulesList;
            }

            public void setPortalRoleAppModulesList(String portalRoleAppModulesList) {
                this.portalRoleAppModulesList = portalRoleAppModulesList;
            }

            private int appHeat;
            private String appCreateTime;
            private int appLoginFlag;
            private String appSecret;
            private String appNewWindow;
            private int appModulesId;
            private String appUrl;
            private String portalAppIcon;
            private String systemName;
            private String iconCount;
            private String portalAppAuthentication;
            private String systemIdList;
            private String portalRoleAppModulesList;
        }

    }

}
