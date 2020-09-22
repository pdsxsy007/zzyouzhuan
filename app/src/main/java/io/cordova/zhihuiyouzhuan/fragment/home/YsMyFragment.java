package io.cordova.zhihuiyouzhuan.fragment.home;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.utils.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.widget.XCRoundImageView;


/**
 * Created by Cuilei on 2020/9/21.
 */

public class YsMyFragment extends BaseFragment {


    @BindView(R.id.iv_user_head)
    XCRoundImageView userHead;
    @BindView(R.id.name_tv)
    TextView mameTv;
    @BindView(R.id.department)
    TextView departmentTv;

    YsAppBean ysAppBean;
    List<YsAppBean.Obj.Apps> objList1;
    List<YsAppBean.Obj.Apps> objList2;
    YsAppAdapter ysAppAdapter;
    YsAppAdapter ysAppAdapter2;
    @BindView(R.id.wd_rc)
    RecyclerView wdRc;
    @BindView(R.id.cg_rc)
    RecyclerView cgRc;

    @Override
    public int getLayoutResID() {
        return R.layout.fragment_ys_my;
    }

    @Override
    public void initData() {
        super.initData();
        netWorkUserMsg();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity());
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        wdRc.setLayoutManager(linearLayoutManager);
        cgRc.setLayoutManager(linearLayoutManager2);
        objList1 = new ArrayList<>();
        objList2 = new ArrayList<>();
        getAppList();
        getAppList2();
    }

    /**个人信息*/
    UserMsgBean userMsgBean;
    private void netWorkUserMsg() {
        try {
            OkGo.<String>post(UrlRes.HOME_URL + UrlRes.User_Msg)
                    .params("userId", (String) SPUtils.get(MyApp.getInstance(),"userId",""))
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("result1",response.body()+"   --防空");
                            userMsgBean = JSON.parseObject(response.body(), UserMsgBean.class);
                            if (userMsgBean.isSuccess()) {
                                if(null != userMsgBean.getObj()) {
                                    if(userMsgBean.getObj().getModules().getMemberOtherDepartment() != null){
                                        departmentTv.setText(userMsgBean.getObj().getModules().getMemberOtherDepartment());
                                    }

                                    mameTv.setText(userMsgBean.getObj().getModules().getMemberNickname());

                                    StringBuilder sb = new StringBuilder();
                                    if(userMsgBean.getObj().getModules().getRolecodes()!= null){

                                        if (userMsgBean.getObj().getModules().getRolecodes().size() > 0){
                                            for (int i = 0; i < userMsgBean.getObj().getModules().getRolecodes().size(); i++) {
                                                sb.append(userMsgBean.getObj().getModules().getRolecodes().get(i).getRoleCode()).append(",");
                                            }
                                            String ss = sb.substring(0, sb.lastIndexOf(","));
                                            Log.e("TAG",ss);
                                            SPUtils.put(MyApp.getInstance(),"rolecodes",ss);
                                        }

                                    }

                                     /*获取头像*/
                                    netGetUserHead();
//                                    netWorkMyData();//我的信息
                                }else {
//                                    llMyData.setVisibility(View.GONE);
                                }



                            }
                        }
                    });
        }catch (Exception e){

        }


    }

    private void netGetUserHead() {
//        ?memberId=admin&pwd=d632eeeb1548643667060e18656e0112
        try {
            String pwd = URLEncoder.encode(userMsgBean.getObj().getModules().getMemberPwd(),"UTF-8");
            String ingUrl =  UrlRes.HOME2_URL + "/authentication/public/getHeadImg?memberId="+userMsgBean.getObj().getModules().getMemberUsername()+"&pwd="+pwd;

            Glide.with(getActivity())
                    .load(ingUrl)
                    .asBitmap()
                    .placeholder(R.mipmap.tabbar_user_pre)
                    .signature(new StringSignature(UUID.randomUUID().toString()))
                    .into(userHead);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    private void getAppList(){
        OkGo.<String>get(UrlRes.HOME_URL +UrlRes.Service_APP_List)

                .params("Version","1.0")
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("rolecodes",(String) SPUtils.get(MyApp.getInstance(),"rolecodes",""))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ysAppBean =  JsonUtil.parseJson(response.body(),YsAppBean.class);

                        List<YsAppBean.Obj> objList = ysAppBean.getObj();
                        for(int i=0;i<objList.size();i++){
                            if(objList.get(i).getModulesCode().equals("wd_wdxx")){
                                objList1.addAll(objList.get(i).getApps());
                            }
                        }
                        ysAppAdapter = new YsAppAdapter(getActivity(),R.layout.item_app,objList1);
                        wdRc.setAdapter(ysAppAdapter);
                        ysAppAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
                    }
                });
    }

    private void getAppList2(){
        OkGo.<String>get(UrlRes.HOME_URL +UrlRes.Service_APP_List)

                .params("Version","1.0")
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("rolecodes",(String) SPUtils.get(MyApp.getInstance(),"rolecodes",""))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        ysAppBean =  JsonUtil.parseJson(response.body(),YsAppBean.class);

                        List<YsAppBean.Obj> objList = ysAppBean.getObj();
                        for(int i=0;i<objList.size();i++){
                            if(objList.get(i).getModulesCode().equals("wd_wdcg")){
                                objList2.addAll(objList.get(i).getApps());
                            }
                        }
                        ysAppAdapter2 = new YsAppAdapter(getActivity(),R.layout.item_app,objList2);
                        cgRc.setAdapter(ysAppAdapter2);
                        ysAppAdapter2.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
                    }
                });
    }
}
