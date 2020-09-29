package io.cordova.zhihuiyouzhuan.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.activity.MoreAppActivity;
import io.cordova.zhihuiyouzhuan.bean.NaturePicBean;
import io.cordova.zhihuiyouzhuan.utils.BitmapUtils;
import io.cordova.zhihuiyouzhuan.utils.PermissionsUtil;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;

import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.bean.AppOrthBean;
import io.cordova.zhihuiyouzhuan.bean.BaseBean;
import io.cordova.zhihuiyouzhuan.bean.LocationBean;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.CookieUtils;
import io.cordova.zhihuiyouzhuan.utils.DensityUtil;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MobileInfoUtils;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.ScreenSizeUtils;
import io.cordova.zhihuiyouzhuan.utils.SoundPoolUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.T;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.widget.MyDialog;
import io.cordova.zhihuiyouzhuan.widget.TestWebView;
import me.samlss.lighter.Lighter;
import me.samlss.lighter.interfaces.OnLighterListener;
import me.samlss.lighter.parameter.Direction;
import me.samlss.lighter.parameter.LighterParameter;
import me.samlss.lighter.parameter.MarginOffset;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
//import sun.misc.BASE64Encoder;

import static com.zhihu.matisse.internal.utils.PathUtils.getPath;
import static io.cordova.zhihuiyouzhuan.UrlRes.HOME_URL;
import static io.cordova.zhihuiyouzhuan.UrlRes.addPortalReadingAccessUrl;
import static io.cordova.zhihuiyouzhuan.UrlRes.functionInvocationLogUrl;
import static io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile.key;
import static io.cordova.zhihuiyouzhuan.utils.MyApp.getInstance;


/**
 * Created by cenxiaozhong on 2017/5/26.
 * <p>
 * source code  https://github.com/Justson/AgentWeb
 */

@SuppressLint("Registered")
public class BaseWebActivity4 extends AppCompatActivity implements GestureDetector.OnGestureListener ,PermissionsUtil.IPermissionsCallback{
    protected AgentWeb mAgentWeb;

    @BindView(R.id.webView)
    TestWebView webView;

    @BindView(R.id.layout_close)
    RelativeLayout rvClose;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.rl_no)
    RelativeLayout rl_no;

    @BindView(R.id.rb_sc)
    ImageView rbSc;

    @BindView(R.id.ll_shoushi)
    RelativeLayout ll_shoushi;

    private LinearLayout mLinearLayout;
    String appServiceUrl, tgc,appId,search,oaMsg;
    private String time;
    boolean isFirst = true;
    String appName;
    String scan;
    private int flag = 0;

    /** Android 5.0以下版本的文件选择回调 */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /** Android 5.0及以上版本的文件选择回调 */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;

    protected static final int REQUEST_CODE_FILE_PICKER = 51426;


    protected String mUploadableFileTypes = "*/*";
    GestureDetector gestureDetector;
    protected static final float FLIP_DISTANCE = 400;
    private PermissionsUtil permissionsUtil;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        ImmersionBar.with(BaseWebActivity4.this).keyboardEnable(true).statusBarDarkFont(false).init();
        gestureDetector = new GestureDetector(this,this);
        mLinearLayout = (LinearLayout) this.findViewById(R.id.container);
        rvClose.setVisibility(View.GONE);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);
        tgc = (String) SPUtils.get(getApplicationContext(), "TGC", "");
        appServiceUrl = getIntent().getStringExtra("appUrl");


        appId = getIntent().getStringExtra("appId");
        search = getIntent().getStringExtra("search");//微应用地址'
        oaMsg = getIntent().getStringExtra("oaMsg");//oa 消息

        appName = getIntent().getStringExtra("appName");
        scan = getIntent().getStringExtra("scan");

        mTitleTextView.setText(appName);
        if (!StringUtils.isEmpty(oaMsg)){
            if (!appServiceUrl.contains("fromnewcas=Y")){
                appServiceUrl = appServiceUrl + "&fromnewcas=Y";
            }
        }

        if(null != scan){
            if(scan.equals("scan")){
                rbSc.setVisibility(View.GONE);
            }
        }
        if(null != appId){
            rbSc.setVisibility(View.VISIBLE);
        }else {
            rbSc.setVisibility(View.GONE);
        }
        String tgc = (String) SPUtils.get(BaseWebActivity4.this, "TGC", "");
        CookieUtils.syncCookie("http://mobile.havct.edu.cn","CASTGC="+tgc,getApplication());
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                //.setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(new OpenFileChromeClient())
                .setWebViewClient(mWebViewClient)
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebLayout(new WebLayout4(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .createAgentWeb()
                .ready()
                .go(appServiceUrl);

        Log.e("获取到的url",appServiceUrl);
        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface());


        netWorkIsCollection();
        initListener();

        String home05 = (String) SPUtils.get(MyApp.getInstance(), "home05", "");
        if(home05.equals("")){
            setGuideView();
        }

        sBegin = Calendar.getInstance().getTimeInMillis() + "";

        s1 = stringToDate(sBegin);
        //ceshiData();
        permissionsUtil = PermissionsUtil
                .with(this)
                .requestCode(0)
                .isDebug(true)
                .permissions(PermissionsUtil.Permission.Location.ACCESS_FINE_LOCATION)
                .request();
//        getMyLocation();
    }


    private void getMyLocation() {
        LocationClientOption option = new LocationClientOption();

        //设置坐标类型
        option.setCoorType("bd09ll");
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        //option.setOpenGps(true);
        //option.setWifiCacheTimeOut(20);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationDescribe(true);
        option.setNeedDeviceDirect(true);
        //设置扫描间隔为1秒
        option.setScanSpan(300000);
        //option.setScanSpan(1000);

        //传入设置好的信息
        mLocationClient.setLocOption(option);

        mLocationClient.start();


    }

    public LocationClient mLocationClient;
    private MyLocationListener myListener = new MyLocationListener();

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {
        Log.e("权限获取回调222","onPermissionsGranted");
        mLocationClient.stop();

        if(requestCode == 0){
            getMyLocation();
        }else if(requestCode == 1){
//            onScanQR();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {
        Log.e("权限被拒绝回调","onPermissionsDenied");
        mLocationClient.stop();
        if(requestCode == 0){
            getMyLocation();
        }
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            boolean b = location.hasAltitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float direction = location.getDirection();
            int locationWhere = location.getLocationWhere();

            Log.e("errorCode",errorCode+"");
            Log.e("latitude",latitude+"");
            Log.e("longitude",longitude+"");
            Log.e("altitude——base",altitude+"");
            Log.e("speed",speed+"");
            Log.e("direction",direction+"");
            Log.e("locationWhere",locationWhere+"");

            com.baidu.location.Address address = location.getAddress();
            String addressLine = address.address;
            String country = address.country;
            String countryCode = address.countryCode;
            String province = address.province;
            String city = address.city;
            String cityCode = address.cityCode;
            String district = address.district;
            String town = address.town;
            String adcode = address.adcode;
            String street = address.street;
            String streetNumber = address.streetNumber;
            if(addressLine != null){
                SPUtils.put(BaseWebActivity4.this,"addressLine",addressLine);
            }
            if(country != null){
                SPUtils.put(BaseWebActivity4.this,"country",country);
            }

            if(countryCode != null){
                SPUtils.put(BaseWebActivity4.this,"countryCode",countryCode);
            }

            if(province != null){
                SPUtils.put(BaseWebActivity4.this,"province",province);
            }

            if(city != null){
                SPUtils.put(BaseWebActivity4.this,"city",city);
            }

            if(cityCode != null){
                SPUtils.put(BaseWebActivity4.this,"cityCode",cityCode);
            }

            if(district != null){
                SPUtils.put(BaseWebActivity4.this,"district",district);
            }

            if(adcode != null){
                SPUtils.put(BaseWebActivity4.this,"adcode",adcode);
            }

            if(street != null){
                SPUtils.put(BaseWebActivity4.this,"street",street);
            }

            if(streetNumber != null){
                SPUtils.put(BaseWebActivity4.this,"streetNumber",streetNumber);
            }
            if(town != null){
                SPUtils.put(BaseWebActivity4.this,"town",town);
            }

            SPUtils.put(BaseWebActivity4.this,"latitude",latitude+"");
            SPUtils.put(BaseWebActivity4.this,"longitude",longitude+"");
            SPUtils.put(BaseWebActivity4.this,"altitude",altitude+"");
            SPUtils.put(BaseWebActivity4.this,"speed",speed+"");
            SPUtils.put(BaseWebActivity4.this,"direction",direction+"");
            SPUtils.put(BaseWebActivity4.this,"locationWhere",locationWhere+"");
        }
    }


    /**
     * 获取我的经纬度
     */
    private void getMyLocation2() {


        //1.获取位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        locationProvider = LocationManager.GPS_PROVIDER;

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
            Location location = locationManager.getLastKnownLocation(locationProvider);
            if (location!=null){
                //showLocation(location);
                double latitude = location.getLatitude();//纬度
                double longitude = location.getLongitude();//经度
                Log.e("获取到纬度",latitude+ "");
                Log.e("获取到经度",longitude+ "");
                SPUtils.put(BaseWebActivity4.this,"latitude",latitude+"");
                SPUtils.put(BaseWebActivity4.this,"longitude",longitude+"");
                Geocoder gc = new Geocoder(this, Locale.getDefault());
                List<Address> locationList = null;
                try {
                    locationList = gc.getFromLocation(latitude, longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(locationList != null && locationList.size() > 0){
                    Address address = locationList.get(0);//得到Address实例
                    String countryName = address.getCountryName();//得到国家名称，比方：中国
                    String locality = address.getLocality();//得到城市名称，比方：北京市
                    for (int i = 0; address.getAddressLine(i) != null; i++) {
                        String addressLine = address.getAddressLine(i);//得到周边信息。包含街道等。i=0，得到街道名称
                        SPUtils.put(BaseWebActivity4.this,"addressLine",addressLine);
                    }
                }else {
                    SPUtils.put(BaseWebActivity4.this,"latitude","");
                    SPUtils.put(BaseWebActivity4.this,"longitude","");
                    SPUtils.put(BaseWebActivity4.this,"addressLine","");
                }

            }else{
                SPUtils.put(BaseWebActivity4.this,"latitude","");
                SPUtils.put(BaseWebActivity4.this,"longitude","");
                SPUtils.put(BaseWebActivity4.this,"addressLine","");
                //locationManager.requestLocationUpdates(locationProvider, 300000, 0,mListener);
            }
            //locationManager.requestLocationUpdates(locationProvider, 300000, 0,mListener);

        }else {
            SPUtils.put(BaseWebActivity4.this,"latitude","");
            SPUtils.put(BaseWebActivity4.this,"longitude","");
            SPUtils.put(BaseWebActivity4.this,"addressLine","");
        }

    }


    private void setGuideView() {

        // 使用图片
        Lighter.with(this)
                .setBackgroundColor(0xB9000000)
                .setOnLighterListener(new OnLighterListener() {
                    @Override
                    public void onShow(int index) {


                    }

                    @Override
                    public void onDismiss() {
                        SPUtils.put(MyApp.getInstance(),"home05","1");
                    }
                })
                .addHighlight(new LighterParameter.Builder()
                        .setHighlightedViewId(R.id.ll_shoushi)
                        .setTipLayoutId(R.layout.fragment_home_gl2)
                        //.setLighterShape(new RectShape(80, 80, 50))
                        .setTipViewRelativeDirection(Direction.BOTTOM)
                        .setTipViewRelativeOffset(new MarginOffset(0, 10, 0, 20))
                        .build()).show();
    }

    BaseBean appTime;
    private void networkAppStatTime() {
        OkGo.<String>post(HOME_URL+ UrlRes.APP_Time)
                .params( "responseTime",time )
                .params( "responseAppId",appId)
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        //handleResponse(response);
                        Log.e("Tag",response.body());
                        appTime = JSON.parseObject(response.body(),BaseBean.class);
                        if (appTime.isSuccess()) {
                            isFirst = false;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        isFirst = true;
                    }
                });


    }

    BaseBean baseBean;
    /**
     * 请求检测是否收藏页面
     * */
    private void netWorkIsCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Query_IsCollection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        //handleResponse(response);
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);

                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });

    }

    private void initListener() {
        rbSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0){
                    //请求收藏
                    networkCollection();
                }else {
                    //取消收藏
                    cancelCollection();
                }
            }
        });

    }

    /**请求收藏*/
    private void networkCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Add_Collection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);
                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                            Intent intent = new Intent();
                            intent.putExtra("refreshService","dongtai");
                            intent.setAction("refresh2");
                            sendBroadcast(intent);
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        rbSc.setBackgroundResource(R.mipmap.sc_icon);
                    }
                });
    }

    /**取消收藏*/
    private void cancelCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Cancel_Collection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);
                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                            Intent intent = new Intent();
                            intent.putExtra("refreshService","dongtai");
                            intent.setAction("refresh2");
                            sendBroadcast(intent);
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                    }
                });

    }
    /**
     * IEventHandler 处理WebView相关返回事件
     */
    @OnClick({R.id.iv_back, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (!mAgentWeb.back()){
                    BaseWebActivity4.this.finish();
                }
                break;
            case R.id.iv_close:
                BaseWebActivity4.this.finish();
                break;
        }
    }
    long start;
    long end;
    String urldown = "";
    String sBegin;//开始时间戳
    String s1;//开始时间
    @SuppressLint("WrongConstant")
    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageCommitVisible(WebView view, String url) {

            super.onPageCommitVisible(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            // super.onReceivedSslError(view, handler, error);
            /**
             *  Webview在安卓5.0之前默认允许其加载混合网络协议内容
             *  在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
             */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                handler.proceed(); // 接受所有网站的证书
            }


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            boolean b = mAgentWeb.getWebCreator().getWebView().canGoBack();
            if(b){
                rvClose.setVisibility(View.VISIBLE);

            }else {
                rvClose.setVisibility(View.GONE);
            }

            WebSettings webSettings = view.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);//允许js 弹窗
            // 启用或禁用WebView访问文件数据。
            webSettings.setAllowFileAccess(true);
            // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
            webSettings.setAllowFileAccessFromFileURLs(true);
            // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
            webSettings.setAllowUniversalAccessFromFileURLs(false);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能

            end =   Calendar.getInstance().getTimeInMillis();
            time =(end - start) +"";
            if (!StringUtils.isEmpty(time) && !StringUtils.isEmpty(appId) && isFirst){
                networkAppStatTime();
            }

            sBegin = Calendar.getInstance().getTimeInMillis() + "";

            s1 = stringToDate(sBegin);

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }



            if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username","")) || tgc.equals("")){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }


            urldown = "";
            urldown =url;



            return super.shouldOverrideUrlLoading(view, request);
        }

        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username","")) || tgc.equals("")){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }

            urldown = "";
            urldown =url;
            Log.e("urldown",urldown);
            webView.loadUrl(urldown);
            webView.setWebViewClient(new WebViewClient(){
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    Log.e("myurl",url);
                    return true;
                }

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    Log.e("myurl",request.getUrl().toString());
                    return super.shouldOverrideUrlLoading(view, request);

                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    Log.e("myurl","结束了");
                    Log.e("myurl",url);
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.e("myurl","开始了");
                    Log.e("myurl",url);
                }
            });

//            webView.setLoadFinishListener(new CLWebView.LoadFinishListener() {
//                @Override
//                public void onLoadFinish(WebView webView) {
//                    Log.e("urldown",urldown);
//
//
//                    upLoadWebInfo();
//                    String url1 = webView.getUrl();
//                    urldown = url1;
//                    sBegin = Calendar.getInstance().getTimeInMillis() + "";
//
//                    s1 = stringToDate(sBegin);
//                    Log.e("urldown",urldown);
//                    Log.e("myurl","加载结束了");
//                    Log.e("myurl",url1);
//                    String downLoadType = (String) SPUtils.get(BaseWebActivity4.this, "downLoadType", "");
//                    DownLoadBean downLoadBean = JsonUtil.parseJson(downLoadType,DownLoadBean.class);
//                    List<String> downLoadTypeList = downLoadBean.getString();
//                    for (int i = 0; i < downLoadTypeList.size(); i++) {
//                        if(urldown.contains(downLoadTypeList.get(i))){
//                            Log.e("tag",downLoadTypeList.get(i));
//                            logOut(urldown);
//                            break;
//                        }else {
//
//                        }
//                    }
//                }
//            });

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            CookieUtils.syncCookie(UrlRes.HOME2_URL,"CASTGC="+tgc,getApplication());
            if (!StringUtils.isEmpty(appId)){
                start =  Calendar.getInstance().getTimeInMillis() ;
                Log.i("Info", "start:  " + start );
            }

        }




    };

    /**
     * @Description: String类型毫秒数转换成日期
     * [@param](http://my.oschina.net/param) lo 毫秒数
     * @return String yyyy-MM-dd HH:mm:ss
     */
    public static String stringToDate(String lo){
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * 结束时间
     */
    private void upLoadWebInfo() {
        String s = Calendar.getInstance().getTimeInMillis() + "";


        long sCha = Long.parseLong(s) - Long.parseLong(sBegin);
        String userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        try {
            String decrypt = AesEncryptUtile.decrypt(userId, key);
            Map<String,String> map=new HashMap<String,String>();
            map.put("portalReadingAccessEquipmentId",(String) SPUtils.get(MyApp.getInstance(),"imei",""));
            map.put("portalReadingAccessMemberId",decrypt);
            map.put("portalReadingAccessLongitude",(String) SPUtils.get(MyApp.getInstance(),"longitude",""));
            map.put("portalReadingAccessLatitude",(String) SPUtils.get(MyApp.getInstance(),"latitude",""));
            map.put("portalReadingAccessAddress",(String) SPUtils.get(MyApp.getInstance(),"addressLine",""));
            map.put("portalReadingAccessTime",s1);
            map.put("portalReadingAccessUrl",urldown);
            map.put("portalReadingAccessReadTime",sCha/1000+"");
            JSONObject json =new JSONObject(map);
            String s3 = json.toString();
            Log.e("content",json.toString());
            String content = AesEncryptUtile.encrypt(s3, key);

            OkGo.<String>post(HOME_URL+addPortalReadingAccessUrl)
                    .params("json", content)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("s",response.toString());

                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public String getFileExtension(File file) {
        String extension = "";
        try {
            if (file != null && file.exists()) {
                String name = file.getName();
                extension = name.substring(name.lastIndexOf("."));
                extension = extension.replace(".","");
            }
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }
    private MyDialog m_Dialog;
    private int tag = 0;
    private void logOut(final String urldown) {
        m_Dialog = new MyDialog(this, R.style.dialogdialog);
        Window window = m_Dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_down, null);
        RelativeLayout rl_sure = view.findViewById(R.id.rl_sure);
        RelativeLayout rl_sure1 = view.findViewById(R.id.rl_sure1);

        int width = ScreenSizeUtils.getWidth(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - DensityUtil.dip2px(this,24),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        m_Dialog.setContentView(view, layoutParams);
        m_Dialog.show();
        m_Dialog.setCanceledOnTouchOutside(true);
        rl_sure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();

            }
        });
        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();
                //setDownloader(webView, defaultDownload);
                tag = 1;
                mAgentWeb = AgentWeb.with(BaseWebActivity4.this)
                        .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                        .setWebChromeClient(mWebChromeClient)
                        .setWebViewClient(mWebViewClient)
                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                        .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .setWebLayout(new WebLayout(BaseWebActivity4.this))
                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                        .setAgentWebWebSettings( getSettings())//设置 IAgentWebSettings。
                        .createAgentWeb()
                        .ready()
                        .go(urldown);

            }
        });
    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //   do you work

            if (newProgress == 100 && start > 0){
                end =   Calendar.getInstance().getTimeInMillis();
                time =(end - start) +"";
            }

            if (!StringUtils.isEmpty(time) && !StringUtils.isEmpty(appId) && isFirst){
                networkAppStatTime();
            }
            Log.i("Info", "onProgress:" + newProgress);
            Log.i("Info", "onProgress:" +  time);

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
            if(appName != null){
                mTitleTextView.setText(appName);
            }
        }


    };
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {
            Log.i("请求权限进行拦截 ", "mUrl:" + url + "  permission:" + mGson.toJson(permissions) + " action:" + action);
            return false;
        }
    };

    public String getUrl() {
        return "http://iapp.zzuli.edu.cn/portal/portal-app/app-5/index.html?isAPP=true";
    }

    private Gson mGson = new Gson();

    /**
     * 更新于 AgentWeb  4.0.0
     */
    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        /**
         *
         * @param url                下载链接
         * @param userAgent          UserAgent
         * @param contentDisposition ContentDisposition
         * @param mimetype           资源的媒体类型
         * @param contentLength      文件长度
         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
         */
        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Log.i("下载链接", "onStart:" + url);
            ViewUtils.createLoadingDialog(BaseWebActivity4.this);
            extra.setOpenBreakPointDownload(true) // 是否开启断点续传
                    .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
                    .setConnectTimeOut(6000) // 连接最大时长
                    .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
                    .setParallelDownload(false)  // 串行下载更节省资源哦
                    .setEnableIndicator(false)  // false 关闭进度通知
                    //.addHeader("Cookie", "xx") // 自定义请求头
                    .setAutoOpen(true) // 下载完成自动打开
                    .setForceDownload(true); // 强制下载，不管网络网络类型

            return false;
        }




        @Override
        public boolean onResult(String path, String url, Throwable throwable) {
            ViewUtils.cancelLoadingDialog();
            if (null == throwable) { //下载成功
                //do you work
                Log.e("下载失败", path);
                Log.e("下载失败", url);

                Uri shareFileUrl = FileUtil.getFileUri(getApplicationContext(), null, new File(path));
                Log.e("path2", String.valueOf(shareFileUrl));
                new Share2.Builder(BaseWebActivity4.this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(shareFileUrl)
                        .setTitle("Share File")
                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                        .build()
                        .shareBySystem();

            } else {//下载成功
                Log.e("path", path);

                Uri shareFileUrl = FileUtil.getFileUri(getApplicationContext(), null, new File(path));
                Log.e("path2", String.valueOf(shareFileUrl));
                new Share2.Builder(BaseWebActivity4.this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(shareFileUrl)
                        .setTitle("Share File")
                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                        .build()
                        .shareBySystem();

            }
            return false; // true  不会发出下载完成的通知 , 或者打开文件
        }
    };

    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;


            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            /**
             * AgentWeb 4.0.0 内部删除了 DownloadListener 监听 ，以及相关API ，将 Download 部分完全抽离出来独立一个库，
             * 如果你需要使用 AgentWeb Download 部分 ， 请依赖上 compile 'com.just.agentweb:download:4.0.0 ，
             * 如果你需要监听下载结果，请自定义 AgentWebSetting ， New 出 DefaultDownloadImpl，传入DownloadListenerAdapter
             * 实现进度或者结果监听，例如下面这个例子，如果你不需要监听进度，或者下载结果，下面 setDownloader 的例子可以忽略。
             * @param webView
             * @param downloadListener
             * @return WebListenerManager
             */
            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {

              /*  return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));*/


                if(tag == 0){
                    Log.e("tag---------","111111");
                    return super.setDownloader(webView,downloadListener);

                }else {
                    tag = 0;
                    Log.e("tag---------","222222");
                    return super.setDownloader(webView,
                            DefaultDownloadImpl
                                    .create((Activity) webView.getContext(),
                                            webView,
                                            mDownloadListenerAdapter,
                                            null,
                                            this.mAgentWeb.getPermissionInterceptor()));
                }

                //return super.setDownloader(webView,downloadListener);
            }


        };
    }

    private static final int REQUEST_SHARE_FILE_CODE = 120;



    /**
     * 清除 WebView 缓存
     */
    @SuppressLint("WrongConstant")
    private void toCleanWebCache() {

        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(getApplicationContext(), "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
//            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }

    /**震动响铃*/
    private Handler handler = new MyHandler(this);




    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            //关闭铃声
            SoundPoolUtils.stopRing();
            //关闭震动
            SoundPoolUtils.virateCancle(BaseWebActivity4.this);

        }
    }

    /**扫码*/
    private static final int QR_CODE = 55846;
    private Handler deliver = new Handler(Looper.getMainLooper());
    /**Js调用内部类*/
    public class AndroidInterface {


        /**震动响铃*/
        @JavascriptInterface
        public void playSoundAndVibration() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    setAlarmParams();
                    handler.sendEmptyMessageDelayed(0, 1100);
                }
            });

        }

        /**清理缓存*/
        @JavascriptInterface
        public void cleanUpAppCache() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    toCleanWebCache();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }
        /**返回上一页*/
        @JavascriptInterface
        public void backToLastUrl() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    if (!mAgentWeb.back()){
                        finish();
                    }else {
                        mAgentWeb.back();
                    }

                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }


        /**进入扫描二维码页面*/
        @JavascriptInterface
        public void nativeScanQRCode(final String invocationLogAppId,final String invocationLogFunction) {
            ceshiData(invocationLogAppId,invocationLogFunction,"nativeScanQRCode");

        }
        /**手机定位坐标*/
        @JavascriptInterface
        public void nativeGetLocation(final String invocationLogAppId,final String invocationLogFunction) {
            if (EasyPermissions.hasPermissions(BaseWebActivity4.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ceshiData(invocationLogAppId,invocationLogFunction, "nativeGetLocation");
            } else {
                permissionsUtil = PermissionsUtil
                        .with(BaseWebActivity4.this)
                        .requestCode(0)
                        .isDebug(true)
                        .permissions(PermissionsUtil.Permission.Location.ACCESS_FINE_LOCATION)
                        .request();
            }
        }
        /**关闭当前页面*/
        @JavascriptInterface
        public void nativeCloseCurrentPage(final String invocationLogAppId,final String invocationLogFunction) {
            ceshiData(invocationLogAppId,invocationLogFunction, "nativeCloseCurrentPage");

            Log.i("Info", "Thread:" + Thread.currentThread());
        }

        /**手机调用拍照*/
        @JavascriptInterface
        public void nativeGetPicture(final String invocationLogAppId,final String invocationLogFunction,float ratio) {
            ceshiData2(invocationLogAppId,invocationLogFunction, "nativeGetPicture",ratio);

            Log.i("Info", "Thread:" + Thread.currentThread());
        }

        /**关闭当前页面*/
        @JavascriptInterface
        public void closeCurrentPage() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }

        @JavascriptInterface
        public void nativeCASign(final String invocationLogAppId,final String invocationLogFunction,final String signDataId,final String sendTime,final boolean closeFlag) {
            Log.e("nativeCASign","nativeCASign执行了");


        }
        /**手机定位坐标*/
        @JavascriptInterface
        public void getLocation() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    onLoctionCoordinate();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }



        /**进入扫描二维码页面*/
        @JavascriptInterface
        public void ScanQRCode() {


            deliver.post(new Runnable() {
                @Override
                public void run() {
                    qrPermission();
                    if (allowedScan){
                        onScanQR();
                    }else {
                        Toast.makeText(getApplicationContext(),"请允许权限后尝试",Toast.LENGTH_SHORT).show();
                        qrPermission();
                    }


                }
            });

        }

    }


    /**
     * 二维码扫描权限申请
     * 应用内获取通知消息*/
    boolean allowedScan = false;
    private void qrPermission() {
        //同时请求多个权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(new io.reactivex.functions.Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e("用户已经同意该权限", permission.name + " is granted.");
                            //   Log.d(TAG, permission.name + " is granted.");
                            allowedScan =true;

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            allowedScan =false;
                            Log.e("用户拒绝了该权限", permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //   Log.d(TAG, permission.name + " is denied. More info should be provided.");

                        } else {
                            allowedScan =false;
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //   Log.d(TAG, permission.name + " is denied.");
                            Log.e("用户拒绝了该权限", permission.name + permission.name + " is denied.");
//                            BaseWebActivity.this.finish();
                        }
                    }
                });

    }


    /**
     * 进入扫描二维码页面
     * @param
     */
    String loginQrUrl;
    public void onScanQR() {
//        QRCodeManager.getInstance().with(getActivity()).scanningQRCode(1);
        Log.e("tag  = ","点击了");
        QRCodeManager.getInstance()
                .with(this)
                .setReqeustType(0)
                .setRequestCode(QR_CODE)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        //controlLog.append("\n\n(结果)" + result);
                        Log.e("QRCodeManager = ",result);
                        loginQrUrl = result;
                        mAgentWeb.getWebCreator().getWebView().loadUrl(result);

                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        //   controlLog.append("\n\n(错误)" + errorMsg.toString());
                        Log.e("QRCodeManager = = ",errorMsg.toString());
                    }

                    @Override
                    public void onCancel() {
                        //controlLog.append("\n\n(取消)扫描任务取消了");
                        Log.e("QRCodeManager = = = ","扫描任务取消了");
                    }

                    /**
                     * 当点击手动添加时回调
                     *
                     * @param requestCode
                     * @param resultCode
                     * @param data
                     */
                    @Override
                    public void onManual(int requestCode, int resultCode, Intent data) {
                        Log.e("QRCodeManager","点击了手动添加了");
                    }
                });

    }
    private Location location;
    private LocationManager locationManager;
    private String locationProvider;       //位置提供器

    /**手机定位坐标*/
    @SuppressLint("WrongConstant")
    public void onLoctionCoordinate(){
        String latitude =(String) SPUtils.get(MyApp.getInstance(), "latitude", "");
        String longitude =(String) SPUtils.get(MyApp.getInstance(), "longitude", "");
        LocationBean locationBean = new LocationBean();
        locationBean.setSuccess(true);
        locationBean.setMessage("成功");
        //locationBean.setLatitude(location.getLatitude()+"");
        locationBean.setLatitude(latitude+"");
        //locationBean.setLongitude(location.getLongitude()+"");
        locationBean.setLongitude(longitude+"");
        Gson gson = new Gson();
        String s = gson.toJson(locationBean);
        String jsonParams = s;
        String url2 = "javascript:getAndroidParams('"+jsonParams+"')";
        Log.e("url",url2);
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("value",value);
            }
        });


    }




    /** 响铃震动提示*/
    @SuppressLint("WrongConstant")
    private void setAlarmParams() {
        //AudioManager provides access to volume and ringer mode control.
        AudioManager volMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (volMgr.getRingerMode()) {//获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_SILENT://静音模式，值为0，这时候不震动，不响铃
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式，值为1，这时候震动，不响铃
                SoundPoolUtils.vibrate(this, new long[]{1000, 500, 1000, 500}, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL://常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
                SoundPoolUtils.playRing(this);
                SoundPoolUtils.vibrate(this, new long[]{500, 1000, 500, 1000}, 0);
                break;
            default:
                break;
        }
    }


    private void ceshiData(String appid, String function, final String typeName) {
        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",appid)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",function)
                    .params("domainName",urldown)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                String invocationLogFunction = appOrthBean.getObj().getInvocationLogFunction();
                                if(typeName.equals(invocationLogFunction)){
                                    if(invocationLogFunction.equals("nativeScanQRCode")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                cameraTask();



                                            }
                                        });
                                    }  else if(invocationLogFunction.equals("nativeGetLocation")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onLoctionCoordinate();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeCloseCurrentPage")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetPicture")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialogss();
                                            }
                                        });
                                    }

                                }
                            }else {
                                ToastUtils.showToast(BaseWebActivity4.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ceshiData2(String appid, String function, final String typeName,float ratio) {
        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",appid)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",function)
                    .params("domainName",urldown)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                String invocationLogFunction = appOrthBean.getObj().getInvocationLogFunction();
                                if(typeName.equals(invocationLogFunction)){
                                    if(invocationLogFunction.equals("nativeScanQRCode")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                cameraTask();



                                            }
                                        });
                                    }  else if(invocationLogFunction.equals("nativeGetLocation")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onLoctionCoordinate();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeCloseCurrentPage")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetPicture")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                dialogss();
                                            }
                                        });
                                    }

                                }
                            }else {
                                ToastUtils.showToast(BaseWebActivity4.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(BaseWebActivity4.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            onScanQR();

        } else {//没有相应权限，获取相机权限
         /*
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
            permissionsUtil=  PermissionsUtil
                    .with(this)
                    .requestCode(1)
                    .isDebug(true)//开启log
                    .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                    .request();
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void photoTask() {
        if (EasyPermissions.hasPermissions(BaseWebActivity4.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing

            testTakePhoto();
            ;//调用相机照相
        } else {//没有相应权限，获取相机权限
            // Ask for one permission
            permissionsUtil=  PermissionsUtil
                    .with(this)
                    .requestCode(1)
                    .isDebug(true)//开启log
                    .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                    .request();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //需要调用onRequestPermissionsResult
        permissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private AlertDialog.Builder builder;
    private void dialogss() {



        final String items[] = {"打开相机", "打开相册"};
        builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                //Toast.makeText(MActivity.this, items[which], Toast.LENGTH_SHORT).show();
                if (items[which].equals("打开相机")) {

                   /* if(Build.VERSION.SDK_INT >= 23){
                        boolean hasPermissions = checkPermissions(MyDataActivity.this
                                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , REQUEST_CODE_PERMISSION_CAMERA, com.laojiang.imagepickers.R.string.dialog_imagepicker_permission_camera_message);
                        //有权限就直接拍照
                        if (hasPermissions){
                            testTakePhoto();
                        }
                    }else {

                    }*/
                    photoTask();

                } else if (items[which].equals("打开相册")) {
                    getAlbum();
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private Uri fileUri1;
    File mPhotoFile;
    //打开系统相机
    private void testTakePhoto() {





        //指定相机拍照的存储路径
        mPhotoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            //判断文件是否存在，存在删除，不存在创建
            if (mPhotoFile.exists()){
                mPhotoFile.delete();
            }
            mPhotoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //指定intent跳转到系统相机
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=24){
            fileUri1= FileProvider.getUriForFile(BaseWebActivity4.this,"io.cordova.zhihuitongji.provider",mPhotoFile);
            Log.e("获取到的url为",fileUri1.toString());
        }else {
            fileUri1 = Uri.fromFile(mPhotoFile);
        }


        //设置存储路径
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        try {
            //开启相机
            startActivityForResult(captureIntent, 5);
        } catch (Exception e) {
            Log.e("相机异常",e.getMessage());
            Toast.makeText(this, "跳转系统相机异常", Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * 读取图片的旋转的角度
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片，使图片保持正确的方向。
     *
     * @param bitmap  原始图片
     * @param degrees 原始图片的角度
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        if (degrees == 0 || null == bitmap) {
            return bitmap;
        }
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap bmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return bmp;
    }
    int REQUEST_CODE_CHOOSE = 2;
    //打开相册
    private void getAlbum() {



        Matisse
                .from(this)

                //选择图片
                .choose(MimeType.ofImage())
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为9
                .maxSelectable(9)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(0.8f)
                //蓝色主题
                .theme(R.style.Matisse_Zhihu)
                //黑色主题
                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new GlideEngine())

                //请求码
                .forResult(REQUEST_CODE_CHOOSE);
    }


    public String getPath2(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }



    private Uri uritempFile;
    //剪切图片
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        //getRealFilePath(this,tempUri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    }



    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
        upLoadWebInfo();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
        netInsertPortal("5");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();

        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    private class OpenFileChromeClient extends WebChromeClient {

        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选
                openFileInput(null, filePathCallback, allowMultiple);
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
            if(appName != null){
                mTitleTextView.setText(appName);
            }
        }


    }


    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        //Android 5.0以下版本
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst;

        //Android 5.0及以上版本
        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }

        i.setType(mUploadableFileTypes);

        startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);

    }

    public  File compressImage2(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 20) {  //循环判断如果压缩后图片是否大于20kb,大于继续压缩 友盟缩略图要求不大于18kb
            baos.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            long length = baos.toByteArray().length;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date(System.currentTimeMillis());
        //图片名
        String filename = format.format(date);

        File file = new File(Environment.getExternalStorageDirectory(), filename + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            try {
                fos.write(baos.toByteArray());
                fos.flush();
                fos.close();
            } catch (IOException e) {

                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        Log.d("=-=-=-=-=-", "compressImage: " + file);
        // recycleBitmap(bitmap);
        return file;
    }
    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, intent);
        permissionsUtil.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK) {//打开相册返回
                //图片路径 同样视频地址也是这个 根据requestCode
                List<String> pathList = Matisse.obtainPathResult(intent);

                for (int i = 0; i < pathList.size(); i++) {
                    String extension = pathList.get(i);

                    extension = extension.substring(extension.lastIndexOf("."));
                    extension = extension.replace(".","");
                    Bitmap bitmap = BitmapFactory.decodeFile(pathList.get(i));
                    Bitmap scaledBitmap = BitmapUtils.compressByWidth(bitmap, 300);
                    String image = BitmapUtils.bitmapToBase64(scaledBitmap);
                    //String image = getFileBase64(file);
                    NaturePicBean naturePicBean = new NaturePicBean();
                    naturePicBean.setSuccess(true);
                    naturePicBean.setMessage("成功");

                    naturePicBean.setImage(URLEncoder.encode( "data:image/" + extension + ";base64," +image));


                    Gson gson = new Gson();
                    String s = gson.toJson(naturePicBean);
                    String jsonParams = s;

                    String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
                    Log.e("url",url2);
                    mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.e("value",value);
                        }
                    });
                }
            }
        }


//        if (requestCode == 2) {
//            if (resultCode == RESULT_OK) {//打开相册返回
//                Uri albumUri = geturi(intent);
//
//                File file = new File(String.valueOf(albumUri));
////                    File file = saveBitmapFile2(bitmap);
//                String image = getFileBase64(file);
//                NaturePicBean naturePicBean = new NaturePicBean();
//                naturePicBean.setSuccess(1);
//                naturePicBean.setMessage("成功");
//                naturePicBean.setImage(image);
//                Gson gson = new Gson();
//                String s = gson.toJson(naturePicBean);
//                String jsonParams = s;
//
//                String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
//                Log.e("url",url2);
//                mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String value) {
//                        //此处为 js 返回的结果
//                        Log.e("value",value);
//                    }
//                });
////                cutImage(albumUri);
//
//            }
//        }


        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {//拍照返回

//                Bitmap bitmap=BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
////                    Bitmap bitmap =   BitmapFactory.decodeStream(this.getContentResolver().openInputStream(fileUri1));
//                File file = saveBitmapFile2(bitmap);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String extension = mPhotoFile.getAbsolutePath();
                        extension = extension.substring(extension.lastIndexOf("."));
                        extension = extension.replace(".","");


                        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                        Bitmap bitmap2 = rotateBitmap(bitmap,getBitmapDegree(mPhotoFile.getAbsolutePath()));

                        Bitmap scaledBitmap = BitmapUtils.compressByWidth(bitmap2, 300);
                        String image = BitmapUtils.bitmapToBase64(scaledBitmap);
                        NaturePicBean naturePicBean = new NaturePicBean();
                        naturePicBean.setSuccess(true);
                        naturePicBean.setMessage("成功");
                        naturePicBean.setImage( URLEncoder.encode( "data:image/" + extension + ";base64," +image));
                        Gson gson = new Gson();
                        String s = gson.toJson(naturePicBean);
                        String jsonParams = s;

                        String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
                        Log.e("url",url2);
                        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                                Log.e("value",value);
                            }
                        });
                    }
                }).run();

                //                cutImage(fileUri1);
            }
        }
/*        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {//拍照返回

//                Bitmap bitmap=BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
////                    Bitmap bitmap =   BitmapFactory.decodeStream(this.getContentResolver().openInputStream(fileUri1));
//                File file = saveBitmapFile2(bitmap);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        Bitmap bitmap =  getimage(mPhotoFile.getAbsolutePath());
                        File file = compressImage2(bitmap);
                        String image = getFileBase64(file);
                        NaturePicBean naturePicBean = new NaturePicBean();
                        naturePicBean.setSuccess(true);
                        naturePicBean.setMessage("成功");
                        naturePicBean.setImage( URLEncoder.encode( "data:image/" + getFileExtension(file) + ";base64," +image));
                        Gson gson = new Gson();
                        String s = gson.toJson(naturePicBean);
                        String jsonParams = s;

                        String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
                        Log.e("url",url2);
                        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                                Log.e("value",value);
                            }
                        });
                    }
                }).run();

                //                cutImage(fileUri1);
            }
        }*/
        if (requestCode == 3) {
//            if (resultCode == RESULT_OK) {
//                //将Uri图片转换为Bitmap
//                try {
//                    Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uritempFile));
//                    File file = new File(String.valueOf(uritempFile));
////                    File file = saveBitmapFile2(bitmap);
//                    String image = getFileBase64(file);
//                    NaturePicBean naturePicBean = new NaturePicBean();
//                    naturePicBean.setSuccess(true);
//                    naturePicBean.setMessage("成功");
//                    naturePicBean.setImage(image);
//                    Gson gson = new Gson();
//                    String s = gson.toJson(naturePicBean);
//                    String jsonParams = s;
//
//                    String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
//                    Log.e("url",url2);
//                    mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
//                        @Override
//                        public void onReceiveValue(String value) {
//                            //此处为 js 返回的结果
//                            Log.e("value",value);
//                        }
//                    });
////                        upImg(file);
//                }catch (Exception e){
//                       e.printStackTrace();
//
//                       Log.e("出错了", e.getMessage());
//                }
//
//
//            }
        }



//        if (requestCode == REQUEST_CODE_FILE_PICKER) {
//            if (resultCode == Activity.RESULT_OK) {
//                if (intent != null) {
//                    //Android 5.0以下版本
//                    if (mFileUploadCallbackFirst != null) {
//                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
//                        mFileUploadCallbackFirst = null;
//                    }
//                    else if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
//                        Uri[] dataUris = null;
//
//                        try {
//                            if (intent.getDataString() != null) {
//                                dataUris = new Uri[] { Uri.parse(intent.getDataString()) };
//                            }
//                            else {
//                                if (Build.VERSION.SDK_INT >= 16) {
//                                    if (intent.getClipData() != null) {
//                                        final int numSelectedFiles = intent.getClipData().getItemCount();
//
//                                        dataUris = new Uri[numSelectedFiles];
//
//                                        for (int i = 0; i < numSelectedFiles; i++) {
//                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                        catch (Exception ignored) { }
//                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
//                        mFileUploadCallbackSecond = null;
//                    }
//                }
//            }
//
//
//            else {
//                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
//                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
//                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
//                if (mFileUploadCallbackFirst != null) {
//                    mFileUploadCallbackFirst.onReceiveValue(null);
//                    mFileUploadCallbackFirst = null;
//                }
//                else if (mFileUploadCallbackSecond != null) {
//                    mFileUploadCallbackSecond.onReceiveValue(null);
//                    mFileUploadCallbackSecond = null;
//                }
//            }
//        }
    }


    private Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( baos.toByteArray().length / 1024>100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }
    private Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath,newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是1280*720分辨率，所以高和宽我们设置为
        float hh = 1280f;//这里设置高度为1280f
        float ww = 720f;//这里设置宽度为720f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }
    //    public static String getFileBase64(File file){
//                InputStream in = null;
//                byte[] data = null;
//                try{
//                        in = new FileInputStream(file);
//                       data = new byte[in.available()];
//                      in.read(data);
//                    }catch (IOException e){
//                         e.printStackTrace();
//                  }finally {
//                        if (in != null) {
//                                try {    in.close();    } catch (IOException e) {    e.printStackTrace();    }
//                            }
//                    }
//              //Base64编码
//               BASE64Encoder encoder = new BASE64Encoder();
//               return encoder.encode(data);
//          }
    private void netInsertPortal(final String insertPortalAccessLog) {
        String imei = MobileInfoUtils.getIMEI(this);
        OkGo.<String>post(HOME_URL + UrlRes.Four_Modules)
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


    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
       /* if(motionEvent.getX() - motionEvent1.getX() > FLIP_DISTANCE)
        {
            Toast.makeText(this, "左滑", Toast.LENGTH_SHORT).show();
            return true;
        }*/
        float x = motionEvent1.getX();
        float x1 = motionEvent.getX();
        Log.e("x",x+"");
        Log.e("x1",x1+"");
        if(motionEvent1.getX() - motionEvent.getX() > FLIP_DISTANCE)
        {
            /*Toast.makeText(this, "右滑", Toast.LENGTH_SHORT).show();
            onBackPressed();*/
            boolean b = mAgentWeb.getWebCreator().getWebView().canGoBack();
            if(b){
                mAgentWeb.back();

                Log.e("ACTION_MOVE","ACTION_MOVE");

            }else {
                finish();
                Log.e("ACTION_MOVE","ACTION_MOVE---finish");
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //TouchEvent dispatcher.
        if (gestureDetector != null) {
            if (gestureDetector.onTouchEvent(ev))
                //If the gestureDetector handles the event, a swipe has been executed and no more needs to be done.
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }
}
