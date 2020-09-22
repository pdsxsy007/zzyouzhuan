package io.cordova.zhihuiyouzhuan.bean;

import java.util.List;

/**
 * Created by Cuilei on 2020/9/15.
 */

public class YsNewsBean {

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

    private String attributes;
    private String count;
    private List<Obj> obj;
    public class Obj{
        private int portalNewsModuleId;
        private String portalNewsModuleName;
        private int portalNewsModuleOrder;
        private String portalNewsModuleCreatetime;
        private String portalNewsModuleState;
        private String portalNewsModuleDelete;


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



        public List<PortalNewsList> getPortalNewsList() {
            return portalNewsList;
        }

        public void setPortalNewsList(List<PortalNewsList> portalNewsList) {
            this.portalNewsList = portalNewsList;
        }

        private List<PortalNewsList> portalNewsList;
        public class PortalNewsList{

            private int portalNewsId;
            private String portalNewsTitle;
            private String portalNewsTitleImg;
            private String portalNewsContent;
            private String portalNewsPublisher;
            private String portalNewsApplyTime;
            private String portalNewsState;
            private String portalNewsNewsModuleId;
            private String portalNewsClicks;
            private String portalNewsDelete;
            private String portalNewsTopping;
            private String portalNewsPublishTime;
            private String portalNewsDepartment;
            private String portalNewsMemberId;
            private String nickName;

            private PortalNewsRecord portalNewsRecord;

            public int getPortalNewsId() {
                return portalNewsId;
            }

            public void setPortalNewsId(int portalNewsId) {
                this.portalNewsId = portalNewsId;
            }

            public String getPortalNewsTitle() {
                return portalNewsTitle;
            }

            public void setPortalNewsTitle(String portalNewsTitle) {
                this.portalNewsTitle = portalNewsTitle;
            }

            public String getPortalNewsTitleImg() {
                return portalNewsTitleImg;
            }

            public void setPortalNewsTitleImg(String portalNewsTitleImg) {
                this.portalNewsTitleImg = portalNewsTitleImg;
            }

            public String getPortalNewsContent() {
                return portalNewsContent;
            }

            public void setPortalNewsContent(String portalNewsContent) {
                this.portalNewsContent = portalNewsContent;
            }

            public String getPortalNewsPublisher() {
                return portalNewsPublisher;
            }

            public void setPortalNewsPublisher(String portalNewsPublisher) {
                this.portalNewsPublisher = portalNewsPublisher;
            }

            public String getPortalNewsApplyTime() {
                return portalNewsApplyTime;
            }

            public void setPortalNewsApplyTime(String portalNewsApplyTime) {
                this.portalNewsApplyTime = portalNewsApplyTime;
            }

            public String getPortalNewsState() {
                return portalNewsState;
            }

            public void setPortalNewsState(String portalNewsState) {
                this.portalNewsState = portalNewsState;
            }

            public String getPortalNewsNewsModuleId() {
                return portalNewsNewsModuleId;
            }

            public void setPortalNewsNewsModuleId(String portalNewsNewsModuleId) {
                this.portalNewsNewsModuleId = portalNewsNewsModuleId;
            }

            public String getPortalNewsClicks() {
                return portalNewsClicks;
            }

            public void setPortalNewsClicks(String portalNewsClicks) {
                this.portalNewsClicks = portalNewsClicks;
            }

            public String getPortalNewsDelete() {
                return portalNewsDelete;
            }

            public void setPortalNewsDelete(String portalNewsDelete) {
                this.portalNewsDelete = portalNewsDelete;
            }

            public String getPortalNewsTopping() {
                return portalNewsTopping;
            }

            public void setPortalNewsTopping(String portalNewsTopping) {
                this.portalNewsTopping = portalNewsTopping;
            }

            public String getPortalNewsPublishTime() {
                return portalNewsPublishTime;
            }

            public void setPortalNewsPublishTime(String portalNewsPublishTime) {
                this.portalNewsPublishTime = portalNewsPublishTime;
            }

            public String getPortalNewsDepartment() {
                return portalNewsDepartment;
            }

            public void setPortalNewsDepartment(String portalNewsDepartment) {
                this.portalNewsDepartment = portalNewsDepartment;
            }

            public String getPortalNewsMemberId() {
                return portalNewsMemberId;
            }

            public void setPortalNewsMemberId(String portalNewsMemberId) {
                this.portalNewsMemberId = portalNewsMemberId;
            }

            public String getNickName() {
                return nickName;
            }

            public void setNickName(String nickName) {
                this.nickName = nickName;
            }

            public PortalNewsRecord getPortalNewsRecord() {
                return portalNewsRecord;
            }

            public void setPortalNewsRecord(PortalNewsRecord portalNewsRecord) {
                this.portalNewsRecord = portalNewsRecord;
            }

            public class PortalNewsRecord{

                   private int portalNewsRecordId;
                   private int portalNewsRecordNewsIdl;
                   private String portalNewsRecordMemberId;

                  public int getPortalNewsRecordId() {
                      return portalNewsRecordId;
                  }

                  public void setPortalNewsRecordId(int portalNewsRecordId) {
                      this.portalNewsRecordId = portalNewsRecordId;
                  }

                  public int getPortalNewsRecordNewsIdl() {
                      return portalNewsRecordNewsIdl;
                  }

                  public void setPortalNewsRecordNewsIdl(int portalNewsRecordNewsIdl) {
                      this.portalNewsRecordNewsIdl = portalNewsRecordNewsIdl;
                  }

                  public String getPortalNewsRecordMemberId() {
                      return portalNewsRecordMemberId;
                  }

                  public void setPortalNewsRecordMemberId(String portalNewsRecordMemberId) {
                      this.portalNewsRecordMemberId = portalNewsRecordMemberId;
                  }

                  public String getPortalNewsRecordOpinion() {
                      return portalNewsRecordOpinion;
                  }

                  public void setPortalNewsRecordOpinion(String portalNewsRecordOpinion) {
                      this.portalNewsRecordOpinion = portalNewsRecordOpinion;
                  }

                  public String getPortalNewsRecordState() {
                      return portalNewsRecordState;
                  }

                  public void setPortalNewsRecordState(String portalNewsRecordState) {
                      this.portalNewsRecordState = portalNewsRecordState;
                  }

                  public String getPortalNewsRecordCreatetime() {
                      return portalNewsRecordCreatetime;
                  }

                  public void setPortalNewsRecordCreatetime(String portalNewsRecordCreatetime) {
                      this.portalNewsRecordCreatetime = portalNewsRecordCreatetime;
                  }

                  private String portalNewsRecordOpinion;
                   private String portalNewsRecordState;
                   private String portalNewsRecordCreatetime;

              }

        }

    }


}
