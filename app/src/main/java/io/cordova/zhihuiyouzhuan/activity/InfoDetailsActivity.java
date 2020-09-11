package io.cordova.zhihuiyouzhuan.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.MimeTypeMap;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity;
import io.cordova.zhihuiyouzhuan.utils.CookieUtils;
import io.cordova.zhihuiyouzhuan.utils.DensityUtil;
import io.cordova.zhihuiyouzhuan.utils.PermissionsUtil;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.ScreenSizeUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.web.WebLayout;
import io.cordova.zhihuiyouzhuan.widget.MyDialog;
import okhttp3.Headers;

import static io.cordova.zhihuiyouzhuan.utils.MyApp.getInstance;


/**
 * Created by Administrator on 2019/4/16 0016.
 */

public class InfoDetailsActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;

//    @BindView(R.id.swipeLayout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
     @BindView(R.id.ll_web)
    LinearLayout linearLayout;
    protected AgentWeb mAgentWeb;

    PermissionsUtil permissionsUtil;
    @BindView(R.id.layout_back)
    RelativeLayout layout_back;
    String appUrl,title,tgc;
    String appUrl2;
    String time;
    String msgsender;

    @Override
    protected int getResourceId() {
        return R.layout.activity_info_details;
    }

    @Override
    protected void initView() {
        super.initView();

        title = getIntent().getStringExtra("title2");
        appUrl = getIntent().getStringExtra("appUrl");
        appUrl2 = getIntent().getStringExtra("appUrl2");
        msgsender = getIntent().getStringExtra("msgsender");
        time = getIntent().getStringExtra("time");

        tgc = (String) SPUtils.get(getApplicationContext(), "TGC", "");
        if (StringUtils.isEmpty(title)){
            tv_title.setText("系统消息");
        }else {
            tv_title.setText(title);
        }

        Log.e("InfoDetailsActivity",appUrl +"  换行    \n"+appUrl2);
        //appUrl2 = "【轻院通知】您的成绩查询通知已发放，请使用“轻院服务门户”的用户名和密码点击进入<a href=\"http://xytz.zzuli.edu.cn:8080/result/inquiry\">点击查看</a>并在通知文末填写反馈。同时，请点击<a href=\"http://info.zzuli.edu.cn/_t598/2019/0124/c13520a193789/page.htm\">  此处</a>及时完成学生基本信息补录。（信息中心）";
       if(appUrl!= null){
           mAgentWeb = AgentWeb.with(this)//
                   .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                   .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
////                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(getResources().getColor(R.color.title_bar_bg),3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    .setWebChromeClient(mWebChromeClient) //WebChromeClient
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                   .setWebLayout(new WebLayout(this))
                   .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                   .createAgentWeb()//创建AgentWeb。
                   .ready()//设置 WebSettings。
                   .go(appUrl);
           mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
       }else {


           String standard = "\n" +
                   "<html>\n" +
                   "\n" +
                   "<head>\n" +
                   "    <meta charset=\"utf-8\">\n" + "<title>"+title+"</title>\n" +
                   "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no\">\n" +
                   "    <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                   "    <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                   "    <style type=\"text/css\">\n" +
                   "        *{\n" +
                   "            margin: 0;\n" +
                   "            padding: 0;\n" +
                   "         }\n" +
                   "         html,body{\n" +
                   "            width: 100%;\n" +
                   "            height: 100%;\n" +
                   "            font-size: 12px;\n" +
                   "         }\n" +
                   "        h2{\n" +
                   "            text-align: center;\n" +
                   "            margin-bottom: 1rem;\n" +
                   "            font-size: 2rem;\n" +
                   "        }\n" +
                   "        h5{\n" +
                   "           text-align: center;\n" +
                   "            margin-bottom: 1rem; \n" +
                   "        }\n" +
                   "         h5 span{\n" +
                   "            display: inline-block;\n" +
                   "            color: #666;\n" +
                   "            font-weight: normal;\n" +
                   "            margin: 0 1rem;\n" +
                   "         }\n" +
                   "        .content{\n" +
                   "            padding: 1rem;\n" +
                   "        }\n" +
                   "        .message_detail_area{\n" +
                   "            border-top: 1px dotted #ccc;\n" +
                   "            font-size: 1.2rem;\n" +
                   "            line-height: 1.6rem;\n" +
                   "            padding: 1rem 0;\n" +
                   "        }\n" +
                   "    </style>\n" +
                   "<body class=\"combg\">\n" +
                   "<div class=\"content\">\n" +
                   "        <div class=\"mui-content-padded\">\n" +
                   "            <div class=\"message_detail_title\">\n" + "<h2>"+title+"</h2>\n" +
                   "                <h5><span>发布人："+msgsender+"</span><span>发布时间："+stampToDate(time)+"</span></h5>\n" +
                   "            </div>\n" +
                   "            <article class=\"message_detail_area\">"+appUrl2+"</article>\n" +
                   "        </div>\n" +
                   "</div>\n" +
                   "</body>\n" +
                   "</html>";

           /*String standard = "<html> \n" +
                   "<head> \n" +
                   "<title>"+title+"</title> \n"+
                   "<style type=\"text/css\"> \n" +
                   "body {font-size:13px;}\n" +
                   "</style> \n" +
                   "</head> \n" +
                   "<body>" +
                   "<script type='text/javascript'>" +
                   "w  indow.onload = function(){\n" +
                   "var $img = document.getElementsByTagName('img');\n" +
                   "for(var p in  $img){\n" +
                   " $img[p].style.width = '100%%';\n" +
                   "$img[p].style.height ='auto'\n" +
                   "}\n" +
                   "}" +
                   "</script>" +
                   appUrl2
                   + "</body>" +
                   "</html>";*/
           mAgentWeb = AgentWeb.with(this)//
                   .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                   .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
////                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(getResources().getColor(R.color.title_bar_bg),3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
//                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
//                .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
//                .setWebChromeClient(mWebChromeClient) //WebChromeClient
//                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                   .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                   .createAgentWeb()//创建AgentWeb。
                   .ready()//设置 WebSettings。
                   .go(standard);
           mAgentWeb.getUrlLoader().loadDataWithBaseURL(null,standard,"text/html","UTF-8",null);
           mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
       }

        /*if (!mAgentWeb.back()){
            finish();
        }*/
    }

    @Override
    protected void initData() {
        super.initData();
        layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mAgentWeb.back()){
                    InfoDetailsActivity.this.finish();
                }
            }
        });
    }


    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//            view.loadUrl("about:blank");// 避免出现默认的错误界面

            mAgentWeb = AgentWeb.with(getParent())//
                    .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
////                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(getResources().getColor(R.color.title_bar_bg),3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    .setWebChromeClient(mWebChromeClient) //WebChromeClient
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                    .setWebLayout(new WebLayout(getParent()))
                    .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                    .createAgentWeb()//创建AgentWeb。
                    .ready()//设置 WebSettings。
                    .go(appUrl);
            mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
//            view.loadUrl("about:blank");// 避免出现默认的错误界面

            mAgentWeb = AgentWeb.with(getParent())//
                    .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
////                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(getResources().getColor(R.color.title_bar_bg),3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    .setWebChromeClient(mWebChromeClient) //WebChromeClient
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                    .setWebLayout(new WebLayout(getParent()))
                    .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                    .createAgentWeb()//创建AgentWeb。
                    .ready()//设置 WebSettings。
                    .go(appUrl);
            mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//            view.loadUrl("about:blank");// 避免出现默认的错误界面
            mAgentWeb = AgentWeb.with(getParent())//
                    .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
                    .useDefaultIndicator(getResources().getColor(R.color.colorPrimaryDark), 1)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
////                .setAgentWebParent((LinearLayout) view, -1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))//传入AgentWeb的父控件。
//                .useDefaultIndicator(getResources().getColor(R.color.title_bar_bg),3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                    .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                    .setWebViewClient(mWebViewClient)//WebViewClient ， 与 WebView 使用一致 ，但是请勿获取WebView调用setWebViewClient(xx)方法了,会覆盖AgentWeb DefaultWebClient,同时相应的中间件也会失效。
                    .setWebChromeClient(mWebChromeClient) //WebChromeClient
                    .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.DISALLOW)//打开其他页面时，弹窗质询用户前往其他应用 AgentWeb 3.0.0 加入。
                    .setWebLayout(new WebLayout(getParent()))
                    .interceptUnkownUrl() //拦截找不到相关页面的Url AgentWeb 3.0.0 加入。
                    .createAgentWeb()//创建AgentWeb。
                    .ready()//设置 WebSettings。
                    .go(appUrl);
            mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);

            super.onReceivedError(view, request, error);
        }
        @Override
        public void onPageCommitVisible(WebView view, String url) {
            super.onPageCommitVisible(view, url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CookieManager cookieManager = CookieManager.getInstance();
            if(Build.VERSION.SDK_INT>=21){
                cookieManager.setAcceptThirdPartyCookies(view, true);
            }



        }
        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }
            Log.e("url",url);
            //String isapp = url.substring(url.indexOf("/") + 2, 11);

           /* if (url.contains("http://kys.zzuli.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }

             String intercept = url.substring(0,url.indexOf(":")+3);
            if (hasApplication(intercept)){
                try {
                    //直接根据Scheme打开软件  拼接参数
                    if (url.contains("{memberid}")){
                        String s1=  URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"personName",""), "UTF-8");
                        url =  url.replace("{memberid}", s1);
                    }
                    if (url.contains("{memberAesEncrypt}")){
                        String memberAesEncrypt = AesEncryptUtile.encrypt((String) SPUtils.get(MyApp.getInstance(),"personName",""), String.valueOf(appsBean.getAppSecret()));
                        String s2=  URLEncoder.encode(memberAesEncrypt, "UTF-8");
                        url =  url.replace("{memberAesEncrypt}", s2);
//                                                 appUrl.substring(0,appUrl.indexOf("\"{memberAesEncrypt}\""));
                    }
                    if (url.contains("{quicklyTicket}")){
                        String s3 =  URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"TGC",""), "UTF-8");
                        url = url.replace("{quicklyTicket}",s3);
                    }
                    Log.e("TAG", appUrl+"");
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(appUrl));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                startActivity(intent);
            }*/
           if(url.contains("chaoxingshareback")){
               Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
               startActivity(intent);
           }


            return super.shouldOverrideUrlLoading(view, request);
        }


        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }


            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            CookieUtils.syncCookie("http://mobile.havct.edu.cn","CASTGC="+tgc,getApplication());
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };
    private WebChromeClient mWebChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //   do you work
            Log.i("Info", "onProgress:" + newProgress);
        }

        /*Title*/
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            if (mTitleTextView != null) {
//                mTitleTextView.setText(title);
//            }
        }

    };

    DownloadListener downloadListener2 = new DownloadListener() {
        @Override
        public void onDownloadStart(final String url, String s1, String s2, String s3, long l) {
            Log.e("onDownloadStart",url);

            if(url.contains(".png")|| url.contains(".jpg") || url.contains(".jpeg")){

                OkGo.<String>get(url)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {


                                Headers headers = response.getRawResponse().headers();
                                String Disposition = headers.get("Content-Disposition");
                                if(Disposition != null){

                                    if(Disposition.contains("attachment")){
                                        logOut(url);
                                        return;
                                    }
                                }
                            }
                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);

                            }
                        });


            }else {
                logOut(url);
            }





        }


    };
    /**
     * @return IAgentWebSettings
     */
    public IAgentWebSettings  getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;


            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
                return super.setDownloader(webView, downloadListener2);
            }


        };
    }
    private static final int REQUEST_SHARE_FILE_CODE = 120;
    private DownloadingService mDownloadingService;
//    /**
//     * 更新于 AgentWeb  4.0.0
//     */
//    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {
//        /**
//         *
//         * @param url                下载链接
//         * @param userAgent          UserAgent
//         * @param contentDisposition ContentDisposition
//         * @param mimetype           资源的媒体类型
//         * @param contentLength      文件长度
//         * @param extra              下载配置 ， 用户可以通过 Extra 修改下载icon ， 关闭进度条 ， 是否强制下载。
//         * @return true 表示用户处理了该下载事件 ， false 交给 AgentWeb 下载
//         */
//        @Override
//        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
//            Log.i("下载链接", "onStart:" + url);
//            extra.setOpenBreakPointDownload(true) // 是否开启断点续传
//                    .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
//                    .setConnectTimeOut(6000) // 连接最大时长
//                    .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
//                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
//                    .setParallelDownload(false)  // 串行下载更节省资源哦
//                    .setEnableIndicator(true)  // false 关闭进度通知
//                    //.addHeader("Cookie", "xx") // 自定义请求头
//                    //  .setAutoOpen(false) // 下载完成自动打开
//                    .setForceDownload(true); // 强制下载，不管网络网络类型
//            return false;
//        }
//
//        /**
//         *
//         * 不需要暂停或者停止下载该方法可以不必实现
//         * @param url
//         * @param downloadingService  用户可以通过 DownloadingService#shutdownNow 终止下载
//         */
//        @Override
//        public void onBindService(String url, DownloadingService downloadingService) {
//            super.onBindService(url, downloadingService);
//            mDownloadingService = downloadingService;
//            Log.i("停止下载", "onBindService:" + url + "  DownloadingService:" + downloadingService);
//        }
//
//        /**
//         * 回调onUnbindService方法，让用户释放掉 DownloadingService。
//         * @param url
//         * @param downloadingService
//         */
//        @Override
//        public void onUnbindService(String url, DownloadingService downloadingService) {
//            super.onUnbindService(url, downloadingService);
//            mDownloadingService = null;
//            Log.i("回调onUnbindService方法", "onUnbindService:" + url);
//        }
//
//        /**
//         *
//         * @param url  下载链接
//         * @param loaded  已经下载的长度
//         * @param length    文件的总大小
//         * @param usedTime   耗时 ，单位ms
//         * 注意该方法回调在子线程 ，线程名 AsyncTask #XX 或者 AgentWeb # XX
//         */
//        @Override
//        public void onProgress(String url, long loaded, long length, long usedTime) {
//            int mProgress = (int) ((loaded) / Float.valueOf(length) * 100);
//            Log.i("下载进度", "onProgress:" + mProgress);
//            super.onProgress(url, loaded, length, usedTime);
//        }
//
//        /**
//         *
//         * @param path 文件的绝对路径
//         * @param url  下载地址
//         * @param throwable    如果异常，返回给用户异常
//         * @return true 表示用户处理了下载完成后续的事件 ，false 默认交给AgentWeb 处理
//         */
//        @Override
//        public boolean onResult(String path, String url, Throwable throwable) {
//            if (null == throwable) { //下载成功
//                //do you work
//                Log.e("下载失败",path);
//                Log.e("下载失败",url);
//
//                Uri shareFileUrl = FileUtil.getFileUri(InfoDetailsActivity.this,null,new File(path));
//                Log.e("path2", String.valueOf(shareFileUrl));
//                new Share2.Builder(InfoDetailsActivity.this)
//                        .setContentType(ShareContentType.FILE)
//                        .setShareFileUri(shareFileUrl)
//                        .setTitle("Share File")
//                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
//                        .build()
//                        .shareBySystem();
//
//            } else {//下载成功
//                Log.e("path",path);
//
//                Uri shareFileUrl = FileUtil.getFileUri(InfoDetailsActivity.this,null,new File(path));
//                Log.e("path2", String.valueOf(shareFileUrl));
//                new Share2.Builder(InfoDetailsActivity.this)
//                        .setContentType(ShareContentType.FILE)
//                        .setShareFileUri(shareFileUrl)
//                        .setTitle("Share File")
//                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
//                        .build()
//                        .shareBySystem();
//
//            }
//            return false; // true  不会发出下载完成的通知 , 或者打开文件
//        }
//    };

    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mAgentWeb.destroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(KeyEvent.KEYCODE_BACK == keyCode){
            if (!mAgentWeb.back()){
                InfoDetailsActivity.this.finish();
                return true;

            }


        }
        return super.onKeyDown(keyCode, event);
    }

    /*
 * 将时间戳转换为时间
 */
    public static String stampToDate(String s){
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lt = new Long(s);
        Date date = new Date(lt);
        res = simpleDateFormat.format(date);
        return res;
    }

    /**
     * 判断是否安装了应用
     * @return true 为已经安装
     */
    private boolean hasApplication(String scheme) {
        PackageManager manager = getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse(scheme));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }

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

            return false;
        }
    };

    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        @Override
        public void onProgress(String url, long downloaded, long length, long usedTime) {
            super.onProgress(url, downloaded, length, usedTime);
            Log.e("onProgress下载",url);
        }

        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            Log.e("onBindService下载",url);
            // downloadingService.shutdownNow();
        }

        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);

        }

        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Log.e("onStart下载",url);

            Log.e("当前类型",mimetype);
            Log.e("当前类型2",contentDisposition);
            Log.e("当前类型3", String.valueOf(contentLength));

            String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);
            String destPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getAbsolutePath() + File.separator + fileName;
            extra.setForceDownload(true);
            new DownloadTask().execute(url, destPath);




            return false;
        }

        //内部类
        class DownloadTask extends AsyncTask<String, Void, Void> {
            // 传递两个参数：URL 和 目标路径
            private String url;
            private String destPath;

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected Void doInBackground(String... params) {

                url = params[0];
                destPath = params[1];
                Log.e("文件的路径",destPath);
                OutputStream out = null;
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(params[0]);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setConnectTimeout(15000);
                    urlConnection.setReadTimeout(15000);
                    InputStream in = urlConnection.getInputStream();
                    out = new FileOutputStream(params[1]);
                    byte[] buffer = new byte[10 * 1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    in.close();
                } catch (IOException e) {

                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException e) {

                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

//                Intent handlerIntent = new Intent(Intent.ACTION_VIEW);
//                String mimeType = getMIMEType(url);
//                Uri uri = Uri.fromFile(new File(destPath));
////                getFileIntent(new File(destPath));
//                handlerIntent.setDataAndType(uri, mimeType);
//
//                startActivity(handlerIntent);
                installApk(InfoDetailsActivity.this,new File(destPath));
            }
        }

        private String getMIMEType(String url) {
            String type = null;
            String extension = MimeTypeMap.getFileExtensionFromUrl(url);

            if (extension != null) {
                type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
            }
            return type;
        }
        public  Intent getFileIntent(File file){

//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
            Uri uri = Uri.fromFile(file);
            String type = getMIMEType(file);
            Log.i("tag", "type="+type);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.addCategory("android.intent.category.DEFAULT");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, type);
            return intent;
        }
        public String getMIMEType(File f){
            String type="";
            String fName=f.getName();
      /* 取得扩展名 */
            String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
            if(end.equals("pdf")){
                type = "application/pdf";//
            }
            else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                    end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
                type = "audio/*";
            }
            else if(end.equals("3gp")||end.equals("mp4")){
                type = "video/*";
            }
            else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                    end.equals("jpeg")||end.equals("bmp")){
                type = "image/*";
            }
            else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
                type = "application/vnd.android.package-archive";
            }
            else if(end.equals("pptx")||end.equals("ppt")){
                type = "application/vnd.ms-powerpoint";
            }else if(end.equals("docx")||end.equals("doc")){
                type = "application/vnd.ms-word";
            }else if(end.equals("xlsx")||end.equals("xls")){
                type = "application/vnd.ms-excel";
            }
            else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
                type="*/*";
            }
            return type;
        }



    };

    public static  void installApk(Context context,File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            Uri uri = Uri.fromFile(file);
            Uri apkUri = FileProvider.getUriForFile(context, "io.cordova.zhihuitongji.provider", file);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri uri = Uri.fromFile(file);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        context.startActivity(intent);

    }
    public IAgentWebSettings getSettings2() {


        return new AbsAgentWebSettings() {


            private AgentWeb mAgentWeb;


            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {

                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }


        };
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

                tag = 1;


                mAgentWeb = AgentWeb.with(InfoDetailsActivity.this)
                        .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                        .setWebChromeClient(mWebChromeClient)
                        .setWebViewClient(mWebViewClient2)
                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                        //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .setWebLayout(new WebLayout(InfoDetailsActivity.this))
                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
//                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                        .setAgentWebWebSettings( getSettings2())//设置 IAgentWebSettings。
                        .createAgentWeb()
                        .ready()
                        .go(urldown);

                mAgentWeb = AgentWeb.with(InfoDetailsActivity.this)
                        .setAgentWebParent(linearLayout, new LinearLayout.LayoutParams(-1, -1))
                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                        .setWebChromeClient(mWebChromeClient)
                        .setWebViewClient(mWebViewClient2)
                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                        //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .setWebLayout(new WebLayout(InfoDetailsActivity.this))
                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
//                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                        .setAgentWebWebSettings( getSettings())//设置 IAgentWebSettings。
                        .createAgentWeb()
                        .ready()
                        .go(appUrl);



            }

            public  Intent getFileIntent(File file){

//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
                Uri uri = Uri.fromFile(file);
                String type = getMIMEType(file);
                Log.i("tag", "type="+type);
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setDataAndType(uri, type);
                return intent;
            }
            public String getMIMEType(File f){
                String type="";
                String fName=f.getName();
      /* 取得扩展名 */
                String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      /* 依扩展名的类型决定MimeType */
                if(end.equals("pdf")){
                    type = "application/pdf";//
                }
                else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                        end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
                    type = "audio/*";
                }
                else if(end.equals("3gp")||end.equals("mp4")){
                    type = "video/*";
                }
                else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                        end.equals("jpeg")||end.equals("bmp")){
                    type = "image/*";
                }
                else if(end.equals("apk")){
        /* android.permission.INSTALL_PACKAGES */
                    type = "application/vnd.android.package-archive";
                }
                else if(end.equals("pptx")||end.equals("ppt")){
                    type = "application/vnd.ms-powerpoint";
                }else if(end.equals("docx")||end.equals("doc")){
                    type = "application/vnd.ms-word";
                }else if(end.equals("xlsx")||end.equals("xls")){
                    type = "application/vnd.ms-excel";
                }
                else{
//        /*如果无法直接打开，就跳出软件列表给用户选择 */
                    type="*/*";
                }
                return type;
            }

        });
        Log.e("小伙子打死你",urldown);
//                mAgentWeb = AgentWeb.with(BaseWebCloseActivity.this)
//                        .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
//                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
//                        .setWebChromeClient(mWebChromeClient)
//                        .setWebViewClient(mWebViewClient)
//                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
//                        //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
//                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
//                        .setWebLayout(new WebLayout(BaseWebCloseActivity.this))
//                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
//                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
//                        .setAgentWebWebSettings( getSettings())//设置 IAgentWebSettings。
//                        .createAgentWeb()
//                        .ready()
//                        .go(appServiceUrl);


    }

    String urldown = "";
    @SuppressLint("WrongConstant")
    private WebViewClient mWebViewClient2 = new WebViewClient() {



        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();

            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();

            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            view.loadUrl("about:blank");// 避免出现默认的错误界面
            view.removeAllViews();

            super.onReceivedError(view, request, error);
        }


        @Override
        public void onPageCommitVisible(WebView view, String url) {

            super.onPageCommitVisible(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                handler.proceed(); // 接受所有网站的证书
            }


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            boolean b = mAgentWeb.getWebCreator().getWebView().canGoBack();


            WebSettings webSettings = view.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能


        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }


            if (url.contains(UrlRes.HOME2_URL + "/cas/login")) {
                if (StringUtils.isEmpty((String) SPUtils.get(getInstance(), "username", "")) || tgc.equals("")) {
                    Intent intent = new Intent(getApplicationContext(), LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }


            urldown = "";
            urldown = url;
            if(urldown.contains("download")){
                logOut(urldown);
            }

            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            CookieUtils.syncCookie(UrlRes.HOME2_URL, "CASTGC=" + tgc, getApplication());


        }


    };
}
