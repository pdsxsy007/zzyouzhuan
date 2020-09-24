package io.cordova.zhihuiyouzhuan;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import io.cordova.zhihuiyouzhuan.activity.FaceNewActivity;
import io.cordova.zhihuiyouzhuan.activity.InfoDetailsActivity;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.activity.SplashActivity;
import io.cordova.zhihuiyouzhuan.activity.YsLoginActivity;
import io.cordova.zhihuiyouzhuan.bean.AddFaceBean;
import io.cordova.zhihuiyouzhuan.bean.AddTrustBean;
import io.cordova.zhihuiyouzhuan.bean.BaseBean;
import io.cordova.zhihuiyouzhuan.bean.CountBean;
import io.cordova.zhihuiyouzhuan.bean.CurrencyBean;
import io.cordova.zhihuiyouzhuan.bean.LoginBean;
import io.cordova.zhihuiyouzhuan.bean.NewStudentBean;
import io.cordova.zhihuiyouzhuan.bean.OAMessageBean;
import io.cordova.zhihuiyouzhuan.bean.UpdateBean;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.fragment.home.Home1PreFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.MyPre2Fragment;
import io.cordova.zhihuiyouzhuan.fragment.home.SecondPreFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.ServicePreFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.StudentHomeFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.TeacherHomeFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.YsAppFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.YsBanshiFragment;
import io.cordova.zhihuiyouzhuan.fragment.home.YsMyFragment;
import io.cordova.zhihuiyouzhuan.jpushutil.NotificationsUtils;
import io.cordova.zhihuiyouzhuan.utils.ActivityUtils;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.BadgeView;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity3;
import io.cordova.zhihuiyouzhuan.utils.CookieUtils;
import io.cordova.zhihuiyouzhuan.utils.DensityUtil;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MobileInfoUtils;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.PermissionsUtil;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.ScreenSizeUtils;
import io.cordova.zhihuiyouzhuan.utils.StatusBarUtil;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.SystemInfoUtils;
import io.cordova.zhihuiyouzhuan.utils.T;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import io.cordova.zhihuiyouzhuan.utils.netState;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;
import io.cordova.zhihuiyouzhuan.web.ChangeUpdatePwdWebActivity;
import io.cordova.zhihuiyouzhuan.widget.MyDialog;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AfterPermissionGranted;

import static io.cordova.zhihuiyouzhuan.utils.StatusBarUtil.TYPE_M;

@TargetApi(Build.VERSION_CODES.KITKAT)
public class YsMainActivity extends BaseActivity3 implements PermissionsUtil.IPermissionsCallback{

    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;
    @BindView(R.id.rb_my)
    RadioButton rbMy;

    @BindView(R.id.main_radioGroup)
    RadioGroup mainRadioGroup;




    boolean isFirst = true,isHome = true,isFind = true,isService = true, isMy = true,isFive = true,isLogin = false;
    String insertPortalAccessLog ;
    TeacherHomeFragment homePreFragment;

    StudentHomeFragment studentHomeFragment;
    YsBanshiFragment findPreFragment;
    YsAppFragment servicePreFragment;


    YsMyFragment myPre2Fragment;
    private String registrationId;
    CurrencyBean currencyBean;
    private static final String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    private Button button4;
    private BadgeView badge1;
    int perTag;
    private int flag = 0;
    public static boolean isForeground = false;
    String time;
    @BindView(R.id.webView)
    WebView webView;
    OAMessageBean oaMessageBean = new OAMessageBean();
    OAMessageBean oaEmail = new OAMessageBean();
    String count;
    String userId;
    private String FRAGMENT_STATE="fragment_state";
    private PermissionsUtil permissionsUtil;
    String CURRENT_INDEX = "CURRENT_INDEX";
    int mIndex = 0;
    @Override
    protected int getResourceId() {
        return R.layout.activity_main_ys;
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initView() {
        super.initView();
        ImmersionBar.with(YsMainActivity.this).keyboardEnable(false).statusBarDarkFont(true).init();
        MyApp.isrRunIng = "1";
//        showState();

        button4 = (Button) findViewById(R.id.btn_my);
//        提示消息
        badge1 = new BadgeView(this, button4);
        badge1.hide();
        Log.d("TAG", " registrationId : " + MyApp.registrationId);

        mainRadioGroup.check(R.id.rb_home_page);


        registerBoradcastReceiver();
//        getDownLoadType();
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



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
        //监听跳转到权限设置界面后再回到应用
        permissionsUtil.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }




    private static final int RC_CAMERA_PERM = 123;
    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        QRCodeManager.getInstance()
                .with(this)
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                        intent.putExtra("appUrl",result);
                        intent.putExtra("scan","scan");
                        startActivity(intent);

                    }

                    @Override
                    public void onError(Throwable errorMsg) {

                    }

                    @Override
                    public void onCancel() {

                    }


                    @Override
                    public void onManual(int requestCode, int resultCode, Intent data) {

                    }
                });
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);

    }


    private void getDownLoadType() {
        OkGo.<String>get(UrlRes.HOME_URL + UrlRes.getDownLoadTypeUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("下载类型",response.body());
                        SPUtils.put(YsMainActivity.this,"downLoadType",response.body());
                       /* DownLoadBean downLoadBean = JsonUtil.parseJson(response.body(),DownLoadBean.class);
                        List<String> string = downLoadBean.getString();*/


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

//    private void addMobieInfo() {
//
//        //获取运行内存的信息
//        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
//        manager.getMemoryInfo(info);
//        long totalMem = info.totalMem;
//
//        final StatFs statFs = new StatFs(Environment.getDataDirectory().getPath());
//        long totalCounts = statFs.getBlockCountLong();//总共的block数
//        long size = statFs.getBlockSizeLong(); //每格所占的大小，一般是4KB==
//        long totalROMSize = totalCounts *size; //内部存储总大小
//
//
//        String imei = MobileInfoUtils.getIMEI(this);
//        String deviceModel = SystemInfoUtils.getDeviceModel();
//        String displayVersion = SystemInfoUtils.getDISPLAYVersion();
//        String deviceAndroidVersion = SystemInfoUtils.getDeviceAndroidVersion();
//        String deviceCpu = SystemInfoUtils.getDeviceCpu();
//        String s = totalMem / 1024/1024/1024 + "GB";
//        String s3 = totalROMSize / 1024/1024/1024 + "GB";
//        String s4 = SystemInfoUtils.getWindowWidth(this) + "X" + SystemInfoUtils.getWindowHeigh(this);
//
//        Log.e("s",s);
//        Log.e("s3",s3);
//        Log.e("s4",s4);
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.addMobileInfoUrl)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("mobilePhoneModel", SystemInfoUtils.getDeviceModel())
//                .params("mobileVersion", SystemInfoUtils.getDISPLAYVersion())
//                .params("mobileSystemVersion", SystemInfoUtils.getDeviceAndroidVersion())
//                .params("mobileCpu", SystemInfoUtils.getDeviceCpu())
//                .params("mobileMemory", totalMem/1024+"GB")
//                .params("mobileStorageSpace", totalROMSize/1024+"GB")
//                .params("mobileScreen", SystemInfoUtils.getWindowWidth(this)+"X"+SystemInfoUtils.getWindowHeigh(this))
//                .params("mobileEquipmentId", imei)
//                .params("mobileSystemCategory", 1)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("SysMsg",response.body());
//
//
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//                    }
//                });
//    }

//    private void showState() {
//        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));
//        if (isLogin){
//
//            if (StringUtils.isEmpty(MyApp.registrationId)){
//                MyApp.registrationId =  JPushInterface.getRegistrationID(this);
//            }
//
//            SPUtils.put(this,"registrationId", MyApp.registrationId);
//            bindJpush();
//            /*悬浮窗权限检测*/
//           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
//                    if (!Settings.canDrawOverlays(this)) {
//                        //若未授权则请求权限
//
//                        if(perTag == 0){
//                            showDialog();
//                        }
//
//                    }else if (!NotificationsUtils.isNotificationEnabled(getApplicationContext())){
//                        perTag = 1;
//                        showDialog();
//                    }
//
//
//                }
//            }*/
//            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
//            boolean isOpened = manager.areNotificationsEnabled();
//            if(!isOpened){
//                showDialogs();
//            }
//        }
//    }







    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("refresh3");
        //注册广播
        registerReceiver(broadcastReceiver, myIntentFilter);
    }

    private int imageid = 0;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("refresh3")){
                String faceNewActivity = intent.getStringExtra("FaceNewActivity");
                Log.e("imageid",imageid+"");
                if(imageid == 0){
                    if(faceNewActivity != null){
                        imageid = 1;
                        Log.e("收到图片通知",faceNewActivity);
                        String bitmap  = (String) SPUtils.get(YsMainActivity.this, "bitmapnewsd", "");;
//                        upToServer(bitmap);
                    }else {
                        imageid = 0;
                        ViewUtils.cancelLoadingDialog();

//                        getUpdateInfo();
                    }
                }


            }
        }
    };




    @Override
    protected void initListener() {
        super.initListener();


        mainRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch (checkedId) {

                    case R.id.rb_home_page:
//                        StatusBarUtil.setStatusBarFontIconDark(YsMainActivity.this,TYPE_M);
                        ImmersionBar.with(YsMainActivity.this).keyboardEnable(false).statusBarDarkFont(true).init();
                        flag = 0;
                        showFragment(0);
                        mIndex = 0;
                        insertPortalAccessLog = "5";
                        if (isFive){
                            netInsertPortal(insertPortalAccessLog);
                        }
                        break;
                    case R.id.rb_recommend:
                        ImmersionBar.with(YsMainActivity.this).keyboardEnable(false).statusBarDarkFont(false).init();

                        flag = 1;
                        mIndex = 1;
                        showFragment(1);
                        insertPortalAccessLog = "2";
                        if (isFind){
                            netInsertPortal(insertPortalAccessLog);
                        }
                        break;
                    case R.id.rb_shopping:
                        ImmersionBar.with(YsMainActivity.this).keyboardEnable(false).statusBarDarkFont(false).init();
//                        StatusBarUtil.setStatusBarColor(YsMainActivity.this,getResources().getColor(R.color.app_theme_color));
                        flag = 2;
                        mIndex = 2;
                        showFragment(2);
                        insertPortalAccessLog = "3";
                        if (isService){
                            netInsertPortal(insertPortalAccessLog);
                        }
                        break;
                    case R.id.rb_my:
                        ImmersionBar.with(YsMainActivity.this).keyboardEnable(false).statusBarDarkFont(true, 0.2f).navigationBarColor(R.color.colorPrimary).init();
//                        StatusBarUtil.setStatusBarColor(YsMainActivity.this,getResources().getColor(R.color.app_theme_color));
                        //if (isLogin && netState.isConnect(getApplicationContext())){
                        if (isLogin ){
                            mIndex = 3;
                            showFragment(3);
                            flag = 3;
                            insertPortalAccessLog = "4";
                            if (isMy){
                                netInsertPortal(insertPortalAccessLog);
                            }
                        }else{
                            //mainRadioGroup.check(R.id.rb_home_page);
                            mIndex = flag;
                            switch (flag){
                                case 0:
                                    mainRadioGroup.check(R.id.rb_home_page);
                                    break;
                                case 1:
                                    mainRadioGroup.check(R.id.rb_recommend);
                                    break;
                                case 2:
                                    mainRadioGroup.check(R.id.rb_shopping);
                                    break;
                                case 3:
                                    mainRadioGroup.check(R.id.rb_my);
                                    break;
                            }
                            showFragment(flag);
                            Intent intent = new Intent(getApplicationContext(),YsLoginActivity.class);
                            startActivity(intent);
                        }
                        break;
                }
            }
        });
    }

    String  role = (String) SPUtils.get(MyApp.getInstance(),"rolecodes","");

    public void showFragment(int i) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        hideFragments(ft);
        Bundle mBundle = null;
        switch (i) {


            case 0:

                if(role.contains("student")){
                    if(studentHomeFragment != null){
                        ft.show(studentHomeFragment);
                    }else{
                        studentHomeFragment = new StudentHomeFragment();
                        ft.add(R.id.frameLayout, studentHomeFragment);
                    }
                }else{
                    if (homePreFragment != null)
                        ft.show(homePreFragment);
                    else {
                        homePreFragment = new TeacherHomeFragment();
                        ft.add(R.id.frameLayout, homePreFragment);
                    }
                }

                break;
            case 1:
                // 如果fragment1已经存在则将其显示出来
                if (findPreFragment != null)
                    ft.show(findPreFragment);
                else {
                    findPreFragment = new YsBanshiFragment();
                    ft.add(R.id.frameLayout, findPreFragment);
                }
                //ft.replace(R.id.frameLayout, findPreFragment)

              /*  if (null!= countBean2)
                    if (countBean2.getCount() + Integer.parseInt(countBean1.getObj()) + countBean3.getCount() >= 0) {
                        findPreFragment.setText1(countBean2.getCount() + Integer.parseInt(countBean1.getObj()) + countBean3.getCount() + "");
                    }*/

                break;
            case 2:
                // 如果fragment1已经存在则将其显示出来
                if (servicePreFragment != null)
                    ft.show(servicePreFragment);
                    // 否则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
                else {
                    servicePreFragment = new YsAppFragment();
                    ft.add(R.id.frameLayout, servicePreFragment);
                }

                break;
            case 3:

              /*if ( myPre2Fragment!= null)
                    ft.show(myPre2Fragment);

            else {

            }*/
                if (!netState.isConnect(YsMainActivity.this) ){
                    if(myPre2Fragment != null){
                        ft.show(myPre2Fragment);
                    }else {
                        myPre2Fragment = new YsMyFragment();
                        ft.add(R.id.frameLayout, myPre2Fragment);
                    }
                    ToastUtils.showToast(YsMainActivity.this,"网络连接异常!");
                }else {
                    if(myPre2Fragment != null){
                        ft.show(myPre2Fragment);
                    }else {
                        myPre2Fragment = new YsMyFragment();
                        ft.add(R.id.frameLayout, myPre2Fragment);
                    }
                   /* myPre2Fragment = new MyPre2Fragment();
                    ft.add(R.id.frameLayout, myPre2Fragment);*/

                }

                break;
        }
        ft.commit();
    }

    private void hideFragments(FragmentTransaction ft2) {
        if (homePreFragment != null)
            ft2.hide(homePreFragment);
        if(studentHomeFragment!= null){
            ft2.hide(studentHomeFragment);
        }
        if (servicePreFragment != null)
            ft2.hide(servicePreFragment);
        if (findPreFragment != null)
            ft2.hide(findPreFragment);
        if (myPre2Fragment != null)
            ft2.hide(myPre2Fragment);
    }

    /**
     * 第一次进入统计四大模块是否点击
     *
     * @param insertPortalAccessLog*/
    BaseBean baseBean ;
    private void netInsertPortal(final String insertPortalAccessLog) {
        String imei = MobileInfoUtils.getIMEI(this);
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Four_Modules)
                .params("portalAccessLogMemberId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("portalAccessLogEquipmentId",imei)//设备ID
                .params("portalAccessLogTarget", insertPortalAccessLog)//访问目标
                .params("portalAccessLogVersionNumber", (String) SPUtils.get(getApplicationContext(),"versionName", ""))//版本号
                .params("portalAccessLogOperatingSystem", "ANDROID")//版本号
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("sdsaas",response.body());
                        if (null != response.body()){
                            baseBean = JSON.parseObject(response.body(),BaseBean.class);
                            if (baseBean.isSuccess()){
                                if (insertPortalAccessLog.equals("1")){
                                    isHome = false;
                                }else  if (insertPortalAccessLog.equals("2")){
                                    isFind = false;
                                }else  if (insertPortalAccessLog.equals("3")){
                                    isService = false;
                                }else  if (insertPortalAccessLog.equals("4")){
                                    isMy = false;
                                }else  if (insertPortalAccessLog.equals("5")){
                                    isFive = false;
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }





    /**
     * 根据UserId 绑定Jpush*/
    private void bindJpush() {
        OkGo.<String>get(UrlRes.HOME4_URL+UrlRes.Registration_Id)
                .tag("Jpush")
                .params("equipType","android")
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("portalEquipmentMemberEquipmentId",(String)SPUtils.get(YsMainActivity.this,"registrationId",""))
                .params("portalEquipmentMemberGroup", "")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("JPush",response.body());
                        currencyBean = JSON.parseObject(response.body(),CurrencyBean.class);
                        if (currencyBean.isSuccess()){
                            //绑定成功
                            isFirst = false;
                            Log.e("JPush","绑定成功");
                        }else {
                            //绑定失败
                            isFirst = true;
                            Log.e("JPush","绑定失败");
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //绑定失败
                        isFirst = true;
                        Log.e("JPush","绑定失败");
                    }
                });
    }


    /** 开启通知权限 */
    private AlertDialog mAlertDialog;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void setPer() {
        if ( !NotificationsUtils.isNotificationEnabled(getApplicationContext())){
            showDialogs();
        }
    }
    /**推送设置弹窗*/
    private void showDialogs() {
        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您需要去开启推送权限么?\n")
                    .setNegativeButton("不需要", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })//
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            //NotificationsUtils.gotoSet(getApplicationContext());
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getApplicationContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }).create();
        }
        mAlertDialog.show();

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

    }

    /**
     * 点击2次退出
     */
    long waitTime = 2000;
    long touchTime = 0;
    public boolean onKeyDown(int keyCode, KeyEvent event) {



        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode) {

            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime) {
                T.showShort(YsMainActivity.this, "再按一次退出");
                touchTime = currentTime;
            } else {
                ActivityUtils.getActivityManager().finishAllActivity();
            }

            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    private void getUpdateInfo() {

        OkGo.<String>get(UrlRes.HOME_URL+UrlRes.getNewVersionInfo)
                .params("system","android")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("更新",response.body());

                        UpdateBean updateBean = JSON.parseObject(response.body(),UpdateBean.class);
                        String portalVersionNumber = updateBean.getObj().getPortalVersionNumber();
                        int portalVersionUpdate = updateBean.getObj().getPortalVersionUpdate();
                        String portalVersionDownloadAdress = updateBean.getObj().getPortalVersionDownloadAdress();
                        logShow(portalVersionUpdate,portalVersionDownloadAdress,portalVersionNumber);

                    }
                });

    }
    String localVersionName;
    private MyDialog m_Dialog;
    private int isUpdate = 0;
    private void logShow(int portalVersionUpdate, final String portalVersionDownloadAdress, final String portalVersionNumber) {
        localVersionName = SplashActivity.getLocalVersionName(this);
        m_Dialog = new MyDialog(this, R.style.dialogdialog);
        Window window = m_Dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_update, null);
        RelativeLayout rl_down = view.findViewById(R.id.rl_down);
        RelativeLayout rl_down2 = view.findViewById(R.id.rl_down2);
        TextView tv_version = view.findViewById(R.id.tv_version);
        RelativeLayout rl_cancel = view.findViewById(R.id.rl_cancel);

        int width = ScreenSizeUtils.getWidth(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - DensityUtil.dip2px(this,24),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        m_Dialog.setContentView(view, layoutParams);
        tv_version.setText(portalVersionNumber);
        rl_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(MyApp.getInstance(),"update",portalVersionNumber);
                m_Dialog.dismiss();
                //showNewSuDialog();
            }
        });
        String update = (String) SPUtils.get(MyApp.getInstance(), "update", "");
        Log.e("update",update);
        String a = portalVersionNumber.replace(".", "");
        String b = localVersionName.replace(".", "");
        int result = a.compareTo(b);
        System.out.println(result);
        if(result < 0){//a<b

        }else if(result == 0){ //a==b
            //showNewSuDialog();
        }else { //a>b
            if(update.equals(portalVersionNumber)){

                //showNewSuDialog();
            }else {
                m_Dialog.show();
                if (portalVersionUpdate == 1) {//1代表强制更新

                    isUpdate = 1;
                    m_Dialog.setCanceledOnTouchOutside(false);
                    m_Dialog.setCancelable(false);
                    rl_cancel.setVisibility(View.GONE);
                    rl_down2.setVisibility(View.GONE);
                }else {//普通更新
                    m_Dialog.setCanceledOnTouchOutside(false);
                    rl_cancel.setClickable(false);
                    isUpdate = 0;

                }
            }
        }

        rl_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();
                Intent intent = new Intent(YsMainActivity.this,InfoDetailsActivity.class);
                intent.putExtra("appUrl","http://yd.hntyxxh.com:18083/portal/portal-hnxxtjzyxy/portal-app/download/index.html");
                intent.putExtra("title2","下载地址");
                startActivity(intent);
                if(isUpdate == 1){
                    finish();
                }

            }
        });
        rl_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();

                //showNewSuDialog();
            }
        });


    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();

    }


    @Override
    protected void onResume() {
        super.onResume();

        Log.e("onResume","执行了");
        String isLoading3 = (String) SPUtils.get(YsMainActivity.this, "isloading3", "");
        if(!isLoading3 .equals("")){
            SPUtils.put(getApplicationContext(),"isloading3","");
            ViewUtils.createLoadingDialog2(YsMainActivity.this,true,"人脸上传中");

        }
        permissionsUtil=  PermissionsUtil
                .with(this)
                .requestCode(0)
                .isDebug(true)//开启log
                .permissions(PermissionsUtil.Permission.Phone.READ_PHONE_STATE)
                .request();

        String count = (String) SPUtils.get(this, "count", "");

        isForeground = true;
        showFragment(flag);
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));


        if (isLogin){

            time = (String) SPUtils.get(MyApp.getInstance(), "time", "");
            if(time.equals("")){
                time = Calendar.getInstance().getTimeInMillis()+"";
            }
            long nowTime = Calendar.getInstance().getTimeInMillis();
            long l = nowTime - Long.parseLong(time);
            long l1 = l / 1000 / 60 / 60;
            Log.e("l1的值",l1+"");
            if(l1>=3){
                netWorkLogin();

                netWorkUserMsg();
            }else {
                webView.setWebViewClient(mWebViewClient);
                webView.loadUrl(UrlRes.HOME2_URL+"/portal/login/appLogin");

//                if(count.equals("")){
//                    netWorkSystemMsg();
//                }else {
//
//                    netWorkSystemMsg();
//                }




            }


        }else {
            badge1.hide();
           /* if (Build.MANUFACTURER.equalsIgnoreCase("huaWei")) {
                addHuaWeiCut("0");
            }else if(Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
                xiaoMiShortCut("0");
            }else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")) {

                vivoShortCut("0");
            }*/

        }


    }



    private String s1;
    private String s2;
    LoginBean loginBean;
    String tgt;
    private void netWorkLogin() {
        try {
            String phone = (String) SPUtils.get(MyApp.getInstance(), "phone", "");
            String pwd = (String) SPUtils.get(MyApp.getInstance(), "pwd", "");

            s1 = URLEncoder.encode(AesEncryptUtile.encrypt(phone, AesEncryptUtile.key),"UTF-8");
            s2 =  URLEncoder.encode(AesEncryptUtile.encrypt(pwd, AesEncryptUtile.key),"UTF-8");


        } catch (Exception e) {
            e.printStackTrace();
        }


        OkGo.<String>get(UrlRes.HOME2_URL +"/cas/casApiLoginController")
//                .params("openid","123456zxd")
                .params("openid","123456")
                .params("username",s1)
                .params("password",s2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("result1",response.body());

                        loginBean = JSON.parseObject(response.body(),LoginBean.class);
                        if (loginBean.isSuccess() ) {

                            try {
                                CookieManager cookieManager =  CookieManager.getInstance();
                                cookieManager.removeAllCookie();

                                tgt = AesEncryptUtile.decrypt(loginBean.getAttributes().getTgt(), AesEncryptUtile.key);
                                String userName = AesEncryptUtile.decrypt(loginBean.getAttributes().getUsername(), AesEncryptUtile.key) ;


                                String userId  = AesEncryptUtile.encrypt(userName+ "_"+ Calendar.getInstance().getTimeInMillis(), AesEncryptUtile.key);
                                SPUtils.put(MyApp.getInstance(),"time",Calendar.getInstance().getTimeInMillis()+"");
                                SPUtils.put(MyApp.getInstance(),"userId",userId);
                                SPUtils.put(MyApp.getInstance(),"personName",userName);
                                SPUtils.put(getApplicationContext(),"TGC",tgt);
                                SPUtils.put(getApplicationContext(),"username",s1);
                                SPUtils.put(getApplicationContext(),"password",s2);
                                webView.setWebViewClient(mWebViewClient);
                                webView.loadUrl( UrlRes.HOME2_URL + "/portal/login/appLogin");
                                Intent intent = new Intent();
                                intent.putExtra("refreshService","dongtai");
                                intent.setAction("refresh2");
                                sendBroadcast(intent);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
//                            SPUtils.put(getApplicationContext(),"username","");
//                            Intent intent = new Intent(MyApp.getInstance(),YsMainActivity.class);
//                            startActivity(intent);
//                            finish();
                        }
                    }
                });
    }

//    CountBean countBean1;
//    /** 获取消息数量*/
//
//    private void netWorkSystemMsg() {
//
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_countUnreadMessagesForCurrentUser)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s",response.toString());
//
//                        countBean1 = JSON.parseObject(response.body(), CountBean.class);
//                        //yy_msg_num.setText(countBean.getCount()+"");
//                        netWorkOAToDoMsg();//OA待办
//
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//                    }
//                });
//    }

//    private void netWorkOAEmail() {
//        netWorkUserMsg();
//
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type","3")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s待办",response.body());
//                        oaEmail = JSON.parseObject(response.body(),OAMessageBean.class);
//                        count = Integer.parseInt(oaEmail.getObj().getCount()) + Integer.parseInt(countBean1.getObj()) + Integer.parseInt(oaMessageBean.getObj().getCount()) + Integer.parseInt(oaMessageBean2.getObj().getCount())+ "";
//                        if(SPUtils.get(getApplicationContext(),"rolecodes","").equals("student")){
//                            count = Integer.parseInt(countBean1.getObj()) + "";
//                        }
//                        if(null == count){
//                            count = "0";
//                        }
//
//                        SPUtils.put(MyApp.getInstance(),"count",count+"");
//                        if(!count.equals("") && !"0".equals(count)){
//                            remind();
//                            SPUtils.get(YsMainActivity.this,"count","");
//                        }else {
//                            badge1.hide();
//                        }
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        Log.e("newsid",response.body());
//                        ViewUtils.cancelLoadingDialog();
//                    }
//                });
//    }
//    OAMessageBean oaMessageBean2;
//    private void netWorkOANotice() {
//
//
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type","2")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s待办",response.body());
//                        oaMessageBean2 = JSON.parseObject(response.body(),OAMessageBean.class);
//                        if(oaMessageBean2.getObj().getCount().equals("0")){
//
//                        }else {
//
//
//                        }
//                        netWorkOAEmail();
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        Log.e("newsid",response.body());
//                        ViewUtils.cancelLoadingDialog();
//                    }
//                });
//    }
//    CountBean countBean2;
//    /**OA消息列表*/
//    private void netWorkOAToDoMsg() {
////        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
////                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
////                .params("type", "db")
////                .params("workType", "workdb")
////                .execute(new StringCallback() {
////                    @Override
////                    public void onSuccess(Response<String> response) {
////                        Log.e("s",response.toString());
////
////                        countBean2 = JSON.parseObject(response.body(), CountBean.class);
////                        netWorkDyMsg();
////                    }
////
////                    @Override
////                    public void onError(Response<String> response) {
////                        super.onError(response);
////                        Log.e("sssssss",response.toString());
////                    }
////                });
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type","1")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("oa待办",response.body());
//                        oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
//                        netWorkOANotice();
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        Log.e("newsid",response.body());
//                        ViewUtils.cancelLoadingDialog();
//                    }
//                });
//
//    }
//
//    CountBean countBean3;
//    private void netWorkDyMsg() {
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type", "dy")
//                .params("workType", "workdb")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s",response.toString());
//
//                        countBean3 = JSON.parseObject(response.body(), CountBean.class);
//
//                        //tvMyToDoMsgNum.setText(countBean2.getCount()+Integer.parseInt(countBean1.getObj())+countBean3.getCount()+"");
//                        String s = countBean2.getCount() + Integer.parseInt(countBean1.getObj()) + countBean3.getCount() + "";
//
//                        if(null == s){
//                            s = "0";
//                        }
//                        SPUtils.put(MyApp.getInstance(),"count",s+"");
//                        String count = (String) SPUtils.get(MyApp.getInstance(), "count", "");
//                       /* if (Build.MANUFACTURER.equalsIgnoreCase("huaWei")) {
//
//                            addHuaWeiCut(count);
//
//                        }else if(Build.MANUFACTURER.equalsIgnoreCase("Xiaomi")){
//                            xiaoMiShortCut(count);
//                        }else if (Build.MANUFACTURER.equalsIgnoreCase("vivo")) {
//                            vivoShortCut(count);
//                        }*/
//
//                        remind();
//                        if(s.equals("0")){
//
//                            badge1.hide();
//                        }else {
//                            badge1.show();
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//                    }
//                });
//    }









    //请求悬浮窗权限
    @TargetApi(Build.VERSION_CODES.O)
    private void getOverlayPermission() {
        perTag = 1;
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 0);
    }
    /**推送设置弹窗*/
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showDialog() {

        if (mAlertDialog == null) {
            mAlertDialog = new AlertDialog.Builder(this)
                    .setMessage("您需要去开启推送权限么?\n")
                    .setNegativeButton("不需要", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                    })//
                    .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                            if (perTag == 0){
                                getOverlayPermission();
                            }else if (perTag == 1){
                                perTag =0;
                                NotificationsUtils.requestNotify(getApplicationContext());
                            }
                        }
                    }).create();
        }

        mAlertDialog.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApp.isrRunIng = "0";


        unregisterReceiver(broadcastReceiver);
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("userAgent4",  view.getSettings().getUserAgentString());


        }

        //        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }


            if (url.contains(UrlRes.HOME2_URL + "/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),YsLoginActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains(UrlRes.HOME2_URL + "/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),YsLoginActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


            String tgc = (String) SPUtils.get(YsMainActivity.this, "TGC", "");
            CookieUtils.syncCookie(UrlRes.HOME2_URL,"CASTGC="+tgc,getApplication());


        }

    };


    private MyDialog m_Dialog2;
    boolean allowedScan = false;
    private void logOut() {
        m_Dialog2 = new MyDialog(this, R.style.dialogdialog);
        Window window = m_Dialog2.getWindow();
        window.setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_new_student, null);
        RelativeLayout rl_sure = view.findViewById(R.id.rl_sure);

        //int width = ScreenSizeUtils.getWidth(getActivity());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(-1,-1);
        m_Dialog2.setContentView(view, layoutParams);
        m_Dialog2.show();
        m_Dialog2.setCanceledOnTouchOutside(false);

        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(YsMainActivity.this, FaceNewActivity.class);
                startActivity(intent);
               /* if (allowedScan){

                }else {
                    setPermission2();
                }
*/

            }
        });

        m_Dialog2.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                if (keyCode==KeyEvent.KEYCODE_BACK && keyEvent.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        });
    }


    /**请求权限*/
    private void setPermission2() {
        //同时请求多个权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e("用户已经同意该权限", permission.name + " is granted.");
//                            Intent intent = new Intent(MyApp.getInstance(), QRScanActivity.class);
//                            startActivity(intent);
                            allowedScan = true;
                            //   Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            Log.e("用户拒绝了该权限", permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //   Log.d(TAG, permission.name + " is denied. More info should be provided.");
                            allowedScan = false;
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //   Log.d(TAG, permission.name + " is denied.");
                            Log.e("用户拒绝了该权限", permission.name + permission.name + " is denied.");
                            allowedScan = true;
                        }
                    }
                });
    }


    List<String> permission = new ArrayList<>();
    protected void setListener() {
         /*Manifest.permission.READ_PHONE_STATE,
                //Manifest.permission.SYSTEM_ALERT_WINDOW,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission. RECEIVE_WAP_PUSH,
                Manifest.permission. MANAGE_DOCUMENTS,
                Manifest.permission. MEDIA_CONTENT_CONTROL*/
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(  Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add( Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add( Manifest.permission.READ_EXTERNAL_STORAGE);
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(  Manifest.permission.RECORD_AUDIO);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            permission.add(  Manifest.permission.CAMERA);
        }
        if (!permission.isEmpty())
        {
            String[] permissions = permission.toArray(new String[permission.size()]);//将集合转化成数组
            //@onRequestPermissionsResult会接受次函数传的数据
            ActivityCompat.requestPermissions(this, permissions, 1);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0)
                {
                    for (int result : grantResults)
                    {
                        if (result != PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(this, "" +
                                    "必须统一授权才能使用本程序", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                } else
                {
//                    Toast.makeText(this, "" +
//                            "发生未知错误", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {
        if(requestCode == 0){

//            addMobieInfo();
        }else if(requestCode == 1){
//            onScanQR();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {

    }

}
