package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/9/22.
 */

public class YsUseAppBean {

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

        public PortalAppAuthentication getPortalAppAuthentication() {
            return portalAppAuthentication;
        }

        public void setPortalAppAuthentication(PortalAppAuthentication portalAppAuthentication) {
            this.portalAppAuthentication = portalAppAuthentication;
        }

        public List<String> getPortalRoleAppModulesList() {
            return portalRoleAppModulesList;
        }

        private PortalAppAuthentication portalAppAuthentication;

        public String getSystemIdList() {
            return systemIdList;
        }

        public void setSystemIdList(String systemIdList) {
            this.systemIdList = systemIdList;
        }

        public void setPortalRoleAppModulesList(List<String> portalRoleAppModulesList) {
            this.portalRoleAppModulesList = portalRoleAppModulesList;
        }

        private String systemIdList;


        private List<String> portalRoleAppModulesList;
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







        public class PortalAppAuthentication{

            public int getAppAuthenticationAppId() {
                return appAuthenticationAppId;
            }

            public void setAppAuthenticationAppId(int appAuthenticationAppId) {
                this.appAuthenticationAppId = appAuthenticationAppId;
            }

            public String getAppAuthenticationFace() {
                return appAuthenticationFace;
            }

            public void setAppAuthenticationFace(String appAuthenticationFace) {
                this.appAuthenticationFace = appAuthenticationFace;
            }

            public String getAppAuthenticationFingerprint() {
                return appAuthenticationFingerprint;
            }

            public void setAppAuthenticationFingerprint(String appAuthenticationFingerprint) {
                this.appAuthenticationFingerprint = appAuthenticationFingerprint;
            }

            public String getAppAuthenticationGesture() {
                return appAuthenticationGesture;
            }

            public void setAppAuthenticationGesture(String appAuthenticationGesture) {
                this.appAuthenticationGesture = appAuthenticationGesture;
            }

            public String getAppAuthenticationPassword() {
                return appAuthenticationPassword;
            }

            public void setAppAuthenticationPassword(String appAuthenticationPassword) {
                this.appAuthenticationPassword = appAuthenticationPassword;
            }

            public String getCount() {
                return count;
            }

            public void setCount(String count) {
                this.count = count;
            }

            private int appAuthenticationAppId;
            private String appAuthenticationFace;
            private String appAuthenticationFingerprint;
            private String appAuthenticationGesture;
            private String appAuthenticationPassword;
            private String count;
        }



        }




}
