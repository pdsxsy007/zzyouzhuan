package io.cordova.zhihuiyouzhuan.activity;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.adapter.YsMoreAppAdapter;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;

/**
 * Created by Cuilei on 2020/9/24.
 */

public class MoreAppActivity extends BaseActivity {

    @BindView(R.id.app_list)
    RecyclerView appList;

    YsAppBean ysAppBean;
    List<YsAppBean.Obj.Apps> objList1;
    YsMoreAppAdapter ysAppAdapter;
    @BindView(R.id.back_img)
    ImageView rvClose;
    @Override
    protected int getResourceId() {
        return R.layout.activity_more_app;

    }

    @Override
    protected void initView() {
        super.initView();

        objList1 = new ArrayList<>();
        appList.setLayoutManager(new GridLayoutManager(MoreAppActivity.this,4));
    }

    @Override
    protected void initData() {
        super.initData();
        ImmersionBar.with(MoreAppActivity.this).keyboardEnable(true).statusBarDarkFont(false).init();
        OkGo.<String>get(UrlRes.HOME_URL +UrlRes.Service_APP_List)

                .params("Version","1.0")
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("rolecodes",(String) SPUtils.get(MyApp.getInstance(),"rolecodes",""))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("获取应用",response.body());
                        ysAppBean =  JsonUtil.parseJson(response.body(),YsAppBean.class);

                        List<YsAppBean.Obj> objList = ysAppBean.getObj();
                        for(int i=0;i<objList.size();i++){
                            if(objList.get(i).getModulesCode().equals("index_tjfw")){
                                objList1.addAll(objList.get(i).getApps());
                            }
                        }


                        ysAppAdapter = new YsMoreAppAdapter(MoreAppActivity.this,R.layout.item_app,objList1);
                        appList.setAdapter(ysAppAdapter);

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
                    }
                });
        rvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
