package io.cordova.zhihuiyouzhuan.utils;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.cxz.swipelibrary.SwipeBackActivity;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.ButterKnife;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;

import static io.cordova.zhihuiyouzhuan.utils.MyApp.getInstance;


/**
 * Activity的基类
 *
 */

public abstract class BaseActivity extends SwipeBackActivity {
    private ProgressDialog dialog;

    public int netStateType = 0;
    protected Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(  getResourceId());
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        String imei = MobileInfoUtils.getIMEI(this);
        SPUtils.put(this,"imei",imei);
        initView();
        fitsLayoutOverlap();
        initHandler();
        initData();
        initListener();

        ActivityUtils.getActivityManager().addActivity(this);
        if (!netState.isConnect(BaseActivity.this) ){
            ToastUtils.showToast(BaseActivity.this,"网络连接异常!");
            //netStateType = 1;
        }

    }

    /**
     * 初始化通知栏
     */
    protected void initSystemBar() {
    }

    /**
     * 初始化监听
     */
    protected void initListener() {};

    /**
     * 设置布局文件
     *
     * @return
     */
    protected abstract int getResourceId();

    /**
     * 初始化控件
     */
    protected void initView() {}

    /**
     * 初始化数据
     */
    protected void initData() {}

    /**
     * 初始化handler
     */
    protected void initHandler() {}
    /**
     * 显示dialog
     */
    protected void showDialog(String s) {
        dialog = new ProgressDialog(this);
        dialog.setMessage(s);
        dialog.setCanceledOnTouchOutside(false);

        dialog.show();
    }



    /**
     * dialog停止
     */
    protected void stopDialog() {
        if (null != dialog ) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
       stopDialog();
        ActivityUtils.getActivityManager().finishActivity(this);
        OkGo.getInstance().cancelTag(this);

    }

    public void onBack(View view){
        finish();
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(requestedOrientation);
        return ;
    }


    @Override
    protected void onResume() {
        super.onResume();
        netInsertPortal("5");
    }

    private void netInsertPortal(final String insertPortalAccessLog) {
        String imei = MobileInfoUtils.getIMEI(this);
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Four_Modules)
                .params("portalAccessLogMemberId",(String) SPUtils.get(getInstance(),"userId",""))
                .params("portalAccessLogEquipmentId",(String) SPUtils.get(getInstance(),"imei",""))//设备ID
                .params("portalAccessLogTarget", insertPortalAccessLog)//访问目标
                .params("portalAccessLogVersionNumber", (String) SPUtils.get(this,"versionName", ""))//版本号
                .params("portalAccessLogOperatingSystem", "ANDROID")//版本号
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("sdsaas",response.body());

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    private void fitsLayoutOverlap() {

            ImmersionBar.setTitleBar(this, toolbar);

    }
}
