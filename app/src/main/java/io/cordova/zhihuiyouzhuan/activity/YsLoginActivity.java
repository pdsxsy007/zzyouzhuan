package io.cordova.zhihuiyouzhuan.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.URLEncoder;
import java.util.Calendar;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.Main2Activity;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.YsMainActivity;
import io.cordova.zhihuiyouzhuan.bean.Constants;
import io.cordova.zhihuiyouzhuan.bean.LoginBean;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.CookieUtils;
import io.cordova.zhihuiyouzhuan.utils.LoginBaseActivity;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtil;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.T;
import io.cordova.zhihuiyouzhuan.utils.fingerUtil.MD5Util;

import static io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile.key;

/**
 * Created by Cuilei on 2020/9/10.
 */

public class YsLoginActivity extends LoginBaseActivity {

    @BindView(R.id.user_et)
    EditText userEt;
    @BindView(R.id.psd_et)
    EditText psdEt;
    @BindView(R.id.login_btn)
    TextView loginBtn;
    private String s1;
    private String s2;
    @BindView(R.id.webView)
    WebView webView;
    String update;
    @Override
    protected int getResourceId() {

           return R.layout.activity_ys_login;
           }

    @Override
    protected void initData() {


        super.initData();
        ImmersionBar.with(YsLoginActivity.this).keyboardEnable(true).statusBarDarkFont(false).init();
        update = getIntent().getStringExtra("update");
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StringUtils.getEditTextData(userEt).isEmpty() && StringUtils.getEditTextData(psdEt).isEmpty()){
                    T.showShort(MyApp.getInstance(),"请输入用户名或密码");
                    return;
                }
                if(StringUtils.getEditTextData(userEt).isEmpty()){
                    T.showShort(MyApp.getInstance(),"请输入用户名");
                    return;
                }

                if(StringUtils.getEditTextData(psdEt).isEmpty()){
                    T.showShort(MyApp.getInstance(),"请输入密码");
                    return;
                }
                String uname = StringUtils.getEditTextData(userEt);
                String pwd = StringUtils.getEditTextData(psdEt);
                netWorkLogin(uname,pwd);

            }
        });


    }

    LoginBean loginBean;
    String tgt;
    private void netWorkLogin(String uname, String pwd) {
        try {
//            URLEncoder.encode( ,"UTF-8")
            s1 = URLEncoder.encode(AesEncryptUtile.encrypt(uname,key),"UTF-8");
            s2 =  URLEncoder.encode(AesEncryptUtile.encrypt(pwd,key),"UTF-8");
            SPUtils.put(MyApp.getInstance(),"phone",userEt.getText().toString().trim()+"");
            SPUtils.put(MyApp.getInstance(),"pwd",psdEt.getText().toString().trim()+"");
            Log.e("login","s1 = "+ s1 + "  ,s2  = " + s2);

        } catch (Exception e) {
            e.printStackTrace();
        }

        ;
        try {
            String imei =  AesEncryptUtile.encrypt((String) SPUtils.get(this, "imei", ""), key);
            OkGo.<String>get(UrlRes.HOME2_URL +"/cas/casApiLoginController")
                    .params("openid","123456")
//                    .params("openid","123456zxd")
                    .params("username",s1)
                    .params("password",s2)
                    .params("equipmentId",imei)
                    .params("type","1")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("result1",response.body());

                            loginBean = JSON.parseObject(response.body(),LoginBean.class);
                            if (loginBean.isSuccess() ) {

                                try {
                                    CookieManager cookieManager =  CookieManager.getInstance();
                                    cookieManager.removeAllCookie();
                                    tgt = AesEncryptUtile.decrypt(loginBean.getAttributes().getTgt(),key);

                                    String userName = AesEncryptUtile.decrypt(loginBean.getAttributes().getUsername(),key) ;

                                    webView.setWebViewClient(mWebViewClient);
                                    //webView.loadUrl("http://iapp.zzuli.edu.cn/portal/login/appLogin");
                                    webView.loadUrl(UrlRes.HOME_URL+"/portal/login/appLogin");
                                    String userId  = AesEncryptUtile.encrypt(userName+  "_"+ Calendar.getInstance().getTimeInMillis(),key);
                                    SPUtils.put(MyApp.getInstance(),"time",Calendar.getInstance().getTimeInMillis()+"");
                                    SPUtils.put(MyApp.getInstance(),"userId",userId);
                                    SPUtils.put(MyApp.getInstance(),"personName",userName);
                                    SPUtils.put(getApplicationContext(),"TGC",tgt);
                                    SPUtils.put(getApplicationContext(),"username",s1);
                                    SPUtils.put(getApplicationContext(),"password",s2);


                                    Log.e("login","tgt = "+ tgt + "  ,userName  = " + userName);
                                    netWorkUserMsg();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                T.showShort(MyApp.getInstance(),loginBean.getMsg());
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

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

                                            Intent intent = new Intent(YsLoginActivity.this,YsMainActivity.class);
                                            startActivity(intent);
                                            finish();

                                            Intent intent7 = new Intent();
                                            intent.putExtra("refreshService","dongtai");
                                            intent.setAction("refresh2");
                                            sendBroadcast(intent7);

                                            Intent intent2 = new Intent();
                                            intent2.setAction("refresh3");
                                            sendBroadcast(intent2);

                                            //本地存储账号用户指纹登录时显示账号信息
                                            StringBuffer stringBuffer = new StringBuffer();
                                            SPUtil.getInstance().putString(Constants.SP_ACCOUNT, userEt.getText().toString());
                                            stringBuffer.append( userEt.getText().toString());
                                            stringBuffer.append(psdEt.getText().toString());
                                            SPUtil.getInstance().putString(Constants.SP_A_P, MD5Util.md5Password(stringBuffer.toString()));
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
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }


           /* if (url.contains("http://kys.zzuli.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"userId",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }
*/
            return super.shouldOverrideUrlLoading(view, request);
        }

        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            /*if (url.contains("http://kys.zzuli.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"userId",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }*/

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            //CookieUtils.syncCookie("http://kys.zzuli.edu.cn","CASTGC="+tgt,getApplication());
            CookieUtils.syncCookie(UrlRes.HOME_URL,"CASTGC="+tgt,getApplication());

        }

    };
}
