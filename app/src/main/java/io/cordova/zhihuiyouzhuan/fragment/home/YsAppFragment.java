package io.cordova.zhihuiyouzhuan.fragment.home;


import android.graphics.Color;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter2;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.utils.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StatusBarUtil;

/**
 * Created by Cuilei on 2020/9/10.
 */

public class YsAppFragment extends BaseFragment {

    @BindView(R.id.service_rc)
    RecyclerView serviceRc;


    YsAppBean ysAppBean;
    List<YsAppBean.Obj.Apps> objList1;
    YsAppAdapter2 ysAppAdapter;
    @Override
    public int getLayoutResID() {
        return R.layout.fragment_ys_app;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        objList1 = new ArrayList<>();

        serviceRc.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void initData() {
        super.initData();

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
                            if(objList.get(i).getModulesCode().contains("yyzx_")){
                                objList1.addAll(objList.get(i).getApps());
                            }
                        }
                        ysAppAdapter = new YsAppAdapter2(getActivity(),R.layout.item_service_bnshi,objList);
                        serviceRc.setAdapter(ysAppAdapter);
                        ysAppAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
                    }
                });
    }
}