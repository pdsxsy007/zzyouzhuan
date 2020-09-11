package io.cordova.zhihuiyouzhuan.fragment.home;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.adapter.NewsAdapter3;
import io.cordova.zhihuiyouzhuan.bean.BannerBean2;
import io.cordova.zhihuiyouzhuan.bean.MyCollectionBean;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean2;
import io.cordova.zhihuiyouzhuan.fragment.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.PermissionsUtil;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.netState;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.umeng.socialize.utils.ContextUtil.getPackageName;

/**
 * Created by Administrator on 2019/8/21 0021.
 */

public class Home1PreFragment extends BaseFragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener, PermissionsUtil.IPermissionsCallback{

    Banner banner;


    int position = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();







    SmartRefreshLayout mSwipeRefreshLayout;
    int pos = 0;

    boolean isLogin =false;
    int page = 1;
    int count = 10;
    LinearLayout llMyCollection;
    RecyclerView myDataList;
    ImageView iv_qr;
    TextView tv_title;
//    List<String> newstitle = new ArrayList<>();
    String userId;
    private int flag =1;
    String contentid ;
    private boolean hasrefuse;
    @BindView(R.id.xy_tz)
    RadioButton xyTz;

    @BindView(R.id.notice_news)
    RadioButton noticeNews;

    @BindView(R.id.rm_zt)
    RadioButton rmzt;

    @BindView(R.id.jw_sta)
    RadioButton jwSta;

    @BindView(R.id.xs_sta)
    RadioButton xsSta;

    @BindView(R.id.zs_sta)
    RadioButton zsSta;


    RadioGroup radioGroup;
    RecyclerView recyclerView;

//    XyNewsFragment xyNewsFragment1;
//    NoticeNewsFragment xyNewsFragment2;
//    XueshuFragment xyNewsFragment3;
//    MediaNewsFragment xyNewsFragment4;


    List<NesItemBean.Obj> objList1 = new ArrayList<>();
    List<NesItemBean.Obj> objList2 = new ArrayList<>();
    List<NesItemBean.Obj> objList3 = new ArrayList<>();
    List<NesItemBean.Obj> objList4 = new ArrayList<>();
    List<NesItemBean.Obj> objList5 = new ArrayList<>();
    private PermissionsUtil permissionsUtil;
    NewsAdapter3 adapter;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_first_pre;
    }

    @Override
    protected void initView(final View view) {



        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
        banner = view.findViewById(R.id.banner);
        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);

        recyclerView.setLayoutManager(mLinearLayoutManager);

        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        llMyCollection = view.findViewById(R.id.ll_my_collection);
        myDataList = view.findViewById(R.id.my_collection_list);
        iv_qr = view.findViewById(R.id.iv_qr);
        tv_title = view.findViewById(R.id.tv_title);
        xyTz = view.findViewById(R.id.xy_tz);
        rmzt = view.findViewById(R.id.rm_zt);
        jwSta = view.findViewById(R.id.jw_sta);
        xsSta = view.findViewById(R.id.xs_sta);
        zsSta = view.findViewById(R.id.zs_sta);
        getBannerData();

        userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
//        if(userId.equals("")){
//            getHotData();
//        }else {
//            getHotData2();
//        }
        radioGroup = view.findViewById(R.id.radio_group);


        noticeNews = view.findViewById(R.id.notice_news);

        noticeNews.setOnClickListener(this);
        xyTz.setOnClickListener(this);
        rmzt.setOnClickListener(this);
        xyTz.setOnClickListener(this);
        zsSta.setOnClickListener(this);
        jwSta.setOnClickListener(this);
        xsSta.setOnClickListener(this);

        radioGroup.setOnCheckedChangeListener(this);
        initShowPage();
//        setTabSelection(0);

//        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(RefreshLayout refreshlayout) {
//                getBannerData();
//                userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
//                if(userId.equals("")){
////                    getHotData();
//                }else {
////                    getHotData2();
//                }
//
//                getNewsData1("160820","新医要闻");
//                getNewsData1("160821","公告通知");
//                getNewsData1("160805","学术动态");
//                getNewsData1("160821","媒体新医");
//                //+mViewPager.setCurrentItem(0);
//                refreshlayout.finishRefresh();
//
//
//            }
//        });





        getNews2("4");
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBannerData();
                getNews2("4");
                refreshlayout.finishRefresh();
            }
        });

        mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                getNewsDataMore();
                refreshlayout.finishLoadmore();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);



        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                permissionsUtil=  PermissionsUtil
                        .with(getActivity())
                        .requestCode(1)
                        .isDebug(true)//开启log
                        .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                        .request();
            }
        });
        registerBoradcastReceiver();

    }
    private void getNewsDataMore() {


        page = page + 1;

        objList.clear();
       switch (page){
//           case 1:
//               objList.addAll(objList1);
//               adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList);
//               recyclerView.setAdapter(adapter);
//               break;
           case 2:
               objList.addAll(objList1);
               objList.addAll(objList2);
               adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList);
               recyclerView.setAdapter(adapter);
               break;
           case 3:
               objList.addAll(objList1);
               objList.addAll(objList2);
               objList.addAll(objList3);

               adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList);
               recyclerView.setAdapter(adapter);
               break;
           case 4:
               objList.addAll(objList1);
               objList.addAll(objList2);
               objList.addAll(objList3);
               objList.addAll(objList4);
               adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList);
               recyclerView.setAdapter(adapter);
               break;
           case 5:
               objList.addAll(objList1);
               objList.addAll(objList2);
               objList.addAll(objList3);
               objList.addAll(objList4);
               objList.addAll(objList5);
               adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList);
               recyclerView.setAdapter(adapter);
               break;

       }

    }


    NewsBean2 newsBean2 ;
    NesItemBean nesItemBean;
    List<NesItemBean.Obj> objList = new ArrayList<>();
    private void getNews2(String flag){//学院通知
        OkGo.<String>post(UrlRes.news_url)
                .params("flag",flag)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news11111111111",response.body());
                        Gson gson = new Gson();
                        newsBean2 = gson.fromJson(response.body(),NewsBean2.class);

                        String obj = newsBean2.getObj();

                        Map<String,Object> jsonToMap = JSONObject.parseObject(obj);
                        System.out.println("jsonToMap："+jsonToMap);
                        nesItemBean = JSONObject.parseObject(jsonToMap.get("str").toString(),NesItemBean.class);
                        System.out.println("jsonToData："+ jsonToMap.get("str").toString());
                         objList.clear();
                        objList2.clear();
                        objList3.clear();
                        objList4.clear();
                        objList5.clear();

                        objList1.clear();

                        objList.addAll(nesItemBean.getData());
                        for(int i=0; i<10; i++){

                            objList1.add(objList.get(i));
                        }
                        if(objList.size() >10){
                            for (int i=10;i<20; i++){
                                 if(objList.size() == 14){
                                     objList2.clear();
                                     objList2.add(objList.get(10));
                                     objList2.add(objList.get(11));
                                     objList2.add(objList.get(12));
                                     objList2.add(objList.get(13));
                                 }else{
                                     
                                     objList2.add(objList.get(i));
                                 }

                            }

                        }

                        if(objList.size() >20){
                            for (int i=20;i<30; i++){

                                objList3.add(objList.get(i));
                            }
                        }

                        if(objList.size()>30) {
                            for (int i = 30; i < 40; i++) {

                                objList4.add(objList.get(i));
                            }
                        }
                        if(objList.size() > 40){
                            for (int i=40;i<50; i++){
                                objList5.add(objList.get(i));
                            }
                        }

                        adapter = new NewsAdapter3(getActivity(),R.layout.list_item_news,objList1);
                        recyclerView.setAdapter(adapter);
//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //需要调用onRequestPermissionsResult
        permissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
//    private void getNewsData1(String contentid, final String title) {
//
//        datalist1.clear();
//        count = 10;
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.xynews)
//                .params("contentid",contentid)
//                .params("start",page)
//                .params("count",count)
//
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        //Log.e("news",response.body());
//                        Gson gson = new Gson();
//
//                        XyNewsBean newsBean = gson.fromJson(response.body(), XyNewsBean.class);
//
//                        if(newsBean.getSuccess()){
//                            if(newsBean.getObj().size() != 0){
//                                datalist1.addAll(newsBean.getObj());
//                                adapter = new NewsAdapter(getActivity(),R.layout.list_item_new_news,datalist1,title);
//
//                                recyclerView.setAdapter(adapter);
//                                adapter.notifyDataSetChanged();
//                                adapter.notifyItemInserted(0);
//                            }
//
//
//                        }
//
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//
//                    }
//                });
//
//
//    }


    private void getHotData2() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findHeatAppListUrl)
                .params("userId",userId)
                //.params("collectionSize",12)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("hot有人登录",response.body());

                        collectionBean = JSON.parseObject(response.body(), MyCollectionBean.class);
                        if (collectionBean.isSuccess()) {
                            if(collectionBean.getObj() != null){
                                if (collectionBean.getObj().size() > 0) {
                                    llMyCollection.setVisibility(View.VISIBLE);
                                    myDataList.setVisibility(View.VISIBLE);
                                    setCollectionList();

                                } else {
                                    llMyCollection.setVisibility(View.GONE);

                                }
                            }
                        }else {
                            llMyCollection.setVisibility(View.GONE);
                            myDataList.setVisibility(View.GONE);
                        }

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }


















    MyCollectionBean collectionBean;


    private void getHotData() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findHeatAppListUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("hot",response.body());

                        collectionBean = JSON.parseObject(response.body(), MyCollectionBean.class);
                        if (collectionBean.isSuccess()) {
                            if(collectionBean.getObj() != null){
                                if (collectionBean.getObj().size() > 0) {
                                    llMyCollection.setVisibility(View.VISIBLE);

                                    setCollectionList();

                                } else {
                                    llMyCollection.setVisibility(View.GONE);

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



     List<BannerBean2> bannerlist = new ArrayList<>();
    private void getBannerData() {
//        String s = UrlRes.HOME_URL + UrlRes.findBroadcastListUrl;
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findBroadcastListUrl)
//                .params("broadcastState", 0)
//                .params("broadcastEquipment",0)
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        //Log.e("banner",response.body());
//                        BannerBean bannerBean = JsonUtil.parseJson(response.body(),BannerBean.class);
//                        boolean success = bannerBean.getSuccess();
//                        if(success == true){
//                            List<BannerBean.Banners> list = bannerBean.getObj().getList();
//                            initBanner(list);
//                        }
//
//
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//
//                    }
//                });

        OkGo.<String>post(UrlRes.news_url)
                .params("flag","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news11111111111",response.body());
                        Gson gson = new Gson();
                        newsBean2 = gson.fromJson(response.body(),NewsBean2.class);

                        String obj = newsBean2.getObj();

                        Map<String,Object> jsonToMap = JSONObject.parseObject(obj);
                        System.out.println("jsonToMap："+jsonToMap);
                        bannerlist= JSONObject.parseArray(jsonToMap.get("str").toString(),BannerBean2.class);
                        initBanner(bannerlist);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }







    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private void initBanner(final List<BannerBean2> list) {
        images.clear();
        titles.clear();
        for (int i = 0; i < list.size(); i++) {
            String broadcastUrl = list.get(i).getThumb();
            images.add(broadcastUrl);
            titles.add(list.get(i).getTitle());

        }
        tv_title.setText(titles.get(0));
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                String broadcastUrl = list.get(position).getUrl();
                if(broadcastUrl.equals("")){

                    ToastUtils.showToast(getActivity(),"暂无详情地址");
                }else {
                    //跳转到轮播图详情页
                    Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    intent.putExtra("appUrl",list.get(position).getUrl());
                    //intent.putExtra("appId","");
                    intent.putExtra("appName",list.get(position).getTitle());
                    startActivity(intent);
                }
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(titles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {


        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {//xyTz = view.findViewById(R.id.xy_tz);
        //rmzt = view.findViewById(R.id.rm_zt);
        //jwSta = view.findViewById(R.id.jw_sta);
        //xsSta = view.findViewById(R.id.xs_sta);
        //zsSta = view.findViewById(R.id.zs_sta);
        page = 1;
        switch (i){
            case R.id.xy_tz:


                getNews2("2");




                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("2");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();

                        refreshlayout.finishLoadmore();
                    }
                });

//                setTabSelection(0);
                break;

            case R.id.notice_news:

                getNews2("3");

//                setTabSelection(1);
                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("3");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();
                        refreshlayout.finishLoadmore();
                    }
                });

                break;

            case R.id.rm_zt:

                getNews2("4");


                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("4");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();
                        refreshlayout.finishLoadmore();
                    }
                });

                break;

            case R.id.jw_sta:


                getNews2("5");

                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("5");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();
                        refreshlayout.finishLoadmore();
                    }
                });

                break;

            case R.id.xs_sta:


                getNews2("6");


                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("6");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();
                        refreshlayout.finishLoadmore();
                    }
                });

                break;

            case R.id.zs_sta:


                getNews2("7");
                mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
                    @Override
                    public void onRefresh(RefreshLayout refreshlayout) {
                        getNews2("7");
                        refreshlayout.finishRefresh();
                    }
                });

                mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {

                    @Override
                    public void onLoadmore(RefreshLayout refreshlayout) {

                        getNewsDataMore();
                        refreshlayout.finishLoadmore();
                    }
                });

                break;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {
        Log.e("权限同意","权限同意");
        onScanQR();
    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {

    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */


            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);


        }

    }

    private void checkXj() {

        //判断用户是否给这些权限授权
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            //判断是否拒绝过
            hasrefuse = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA);
            hasrefuse = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA);
            if (hasrefuse) {
                //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                new AlertDialog.Builder(getActivity())
                        .setMessage("需要开启相机权限才能使用此功能")
                        .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //引导用户到设置中去进行设置
                                Intent intent = new Intent();
                                intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                intent.setData(Uri.fromParts("package", getPackageName(), null));
                                startActivity(intent);


                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create()
                        .show();
            } else {
                //如果没有拒绝过,进入回调
                ActivityCompat.requestPermissions(getActivity(), new String[]{ Manifest.permission.CALL_PHONE}, 1);
            }
        }
    }
    CommonAdapter<MyCollectionBean.ObjBean> collectionAdapter;
    private void setCollectionList() {
        myDataList.setLayoutManager(new GridLayoutManager(getActivity(), 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        List<MyCollectionBean.ObjBean> obj = collectionBean.getObj();
        List<MyCollectionBean.ObjBean> obj2 = new ArrayList<>();
        obj2.clear();
        if(obj.size() > 8){
            for (int i = 0; i <8 ; i++) {
                obj2.add(obj.get(i));
            }
        }else {
            for (int i = 0; i < obj.size(); i++) {
                obj2.add(obj.get(i));
            }
        }

        collectionAdapter = new CommonAdapter<MyCollectionBean.ObjBean>(getActivity(), R.layout.item_service_app, obj2) {
            @Override
            protected void convert(ViewHolder holder, MyCollectionBean.ObjBean objBean, int position) {


                holder.setText(R.id.tv_app_name,objBean.getAppName());

                   /*appIntranet  1 需要内网*/
                if (objBean.getAppIntranet()==1){
                    holder.setVisible(R.id.iv_del,true);
                    Glide.with(getActivity())
                            .load(R.mipmap.nei_icon)
                            .error(R.color.white)
                            .placeholder(R.mipmap.zwt)
                            .into((ImageView) holder.getView(R.id.iv_del));
                }else {
                    holder.setVisible(R.id.iv_del,false);
                }

                if (!isLogin) {
                    if (objBean.getAppLoginFlag()==0){
                        holder.setVisible(R.id.iv_lock_close,true);
                        Glide.with(getActivity())
                                .load(R.mipmap.lock_icon)
                                .error(R.mipmap.lock_icon)
                                .placeholder(R.mipmap.zwt)
                                .into((ImageView) holder.getView(R.id.iv_lock_close));
                    }else {
                        holder.setVisible(R.id.iv_lock_close,false);
                    }
                }

                if (null != objBean.getPortalAppIcon() && null != objBean.getPortalAppIcon().getTempletAppImage()){

                    Glide.with(getActivity())
                            .load(UrlRes.HOME3_URL + objBean.getPortalAppIcon().getTempletAppImage())
                            .error(R.mipmap.zwt)
                            .placeholder(R.mipmap.zwt)
                            .into((ImageView) holder.getView(R.id.iv_app_icon));
                }else {
                    Glide.with(getActivity())
                            .load(UrlRes.HOME3_URL + objBean.getAppImages())
                            .error(R.mipmap.zwt)
                            .placeholder(R.mipmap.zwt)
                            .into((ImageView) holder.getView(R.id.iv_app_icon));
                }
            }
        };
        myDataList.setAdapter(collectionAdapter);
        collectionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                collectionBean.getObj().get(position)
                LinearLayout ll_click = holder.itemView.findViewById(R.id.ll_click);
                //ll_click.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (netState.isConnect(getActivity())){
                    netWorkAppClick(collectionBean.getObj().get(position).getAppId());
                }
                if (null != collectionBean.getObj().get(position).getAppAndroidSchema()){
                    if (!isLogin){
                        Intent intent = new Intent(getActivity(),LoginActivity2.class);
                        startActivity(intent);
                    }else {
                        if(collectionBean.getObj().get(position).getAppUrl().equals(UrlRes.HOME_URL+"/portal/app/mailbox/qqEmailLogin")){
                                           /* webView.setWebViewClient(mWebViewClient);
                                            webView.loadUrl("http://iapp.zzuli.edu.cn/portal/login/appLogin");*/
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                            intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                            intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            if (collectionBean.getObj().get(position).getAppUrl().contains("{memberid}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                String s1= null;
                                try {
                                    s1 = URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"personName",""), "UTF-8");
                                    appUrl =  appUrl.replace("{memberid}", s1);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }else if(collectionBean.getObj().get(position).getAppUrl().contains("{memberAesEncrypt}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                try {
                                    String memberAesEncrypt = AesEncryptUtile.encrypt((String) SPUtils.get(MyApp.getInstance(),"personName",""), String.valueOf(collectionBean.getObj().get(position).getAppSecret()));
                                    String s2=  URLEncoder.encode(memberAesEncrypt, "UTF-8");
                                    appUrl =  appUrl.replace("{memberAesEncrypt}", s2);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if(collectionBean.getObj().get(position).getAppUrl().contains("{quicklyTicket}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                try {
                                    String s3 =  URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"TGC",""), "UTF-8");
                                    appUrl = appUrl.replace("{quicklyTicket}",s3);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                            }

                            intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                            intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                            startActivity(intent);
                            ll_click.setBackgroundColor(getResources().getColor(R.color.white));
                        }
                    }
                }else if (!collectionBean.getObj().get(position).getAppUrl().isEmpty()){
                    if (!isLogin){
                        //if(appsBean.getAppIntranet()==1){
                        if(collectionBean.getObj().get(position).getAppLoginFlag()==0){
                            Intent intent = new Intent(getActivity(),LoginActivity2.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            if (netState.isConnect(getActivity())) {
                                netWorkAppClick(collectionBean.getObj().get(position).getAppId());
                            }
                            intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                            intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                            intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                            startActivity(intent);
                        }

                    }else {
                        if (netState.isConnect(getActivity())) {
                            netWorkAppClick(collectionBean.getObj().get(position).getAppId());
                        }
                        if(collectionBean.getObj().get(position).getAppUrl().equals(UrlRes.HOME_URL+"/portal/app/mailbox/qqEmailLogin")){
                                           /* webView.setWebViewClient(mWebViewClient);
                                            webView.loadUrl("http://iapp.zzuli.edu.cn/portal/login/appLogin");*/
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                            intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                            intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            if (collectionBean.getObj().get(position).getAppUrl().contains("{memberid}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                String s1= null;
                                try {
                                    s1 = URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"personName",""), "UTF-8");
                                    appUrl =  appUrl.replace("{memberid}", s1);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }

                            }else if(collectionBean.getObj().get(position).getAppUrl().contains("{memberAesEncrypt}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                try {
                                    String memberAesEncrypt = AesEncryptUtile.encrypt((String) SPUtils.get(MyApp.getInstance(),"personName",""), String.valueOf(collectionBean.getObj().get(position).getAppSecret()));
                                    String s2=  URLEncoder.encode(memberAesEncrypt, "UTF-8");
                                    appUrl =  appUrl.replace("{memberAesEncrypt}", s2);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else if(collectionBean.getObj().get(position).getAppUrl().contains("{quicklyTicket}")){
                                String appUrl = collectionBean.getObj().get(position).getAppUrl();
                                try {
                                    String s3 =  URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"TGC",""), "UTF-8");
                                    appUrl = appUrl.replace("{quicklyTicket}",s3);
                                    intent.putExtra("appUrl",appUrl);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            else{
                                intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                            }
                            //Log.e("url  ==",collectionBean.getObj().get(position).getAppUrl() + "");

                            //intent.putExtra("appUrl",appsBean.getAppUrl());
                            intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                            intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                            startActivity(intent);

                        }

                    }


                }



            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        collectionAdapter.notifyDataSetChanged();
    }





    /**
     * 记录该微应用的的访问量
     * @param appId
     *
     * */
    private void netWorkAppClick(int appId) {
        OkGo.<String>get(UrlRes.HOME_URL +UrlRes.APP_Click_Number)

                .params("appId",appId)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Log.e("result1",response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        //Log.e("错误",response.body());
                    }
                });
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("refresh2");
        //注册广播
        getActivity().registerReceiver(broadcastReceiver, myIntentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("refresh2")){
                isLogin = true;
                initShowPage();
            }
        }
    };

    private void initShowPage() {

        userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        if(userId.equals("")){
            getHotData();
        }else {
            getHotData2();
        }
    }

    private static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {

        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!

            onScanQR();
            ;//调用相机照相
        } else {//没有相应权限，获取相机权限
            // Ask for one permission
//            EasyPermissions.requestPermissions(this, "获取照相机权限",
//                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            checkXj();
        }
    }


    public void onScanQR() {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));
        //Log.e("tag  = ","点击了");
        QRCodeManager.getInstance()
                .with(getActivity())
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        //controlLog.append("\n\n(结果)" + result);
                        //Log.e("QRCodeManager = ",result);
                        if(!isLogin){
                            Intent intent = new Intent(MyApp.getInstance(), LoginActivity2.class);
                            startActivity(intent);
                        }else {

                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            intent.putExtra("appUrl",result);
                            intent.putExtra("scan","scan");
                            startActivity(intent);
                        }

//                    intent.putExtra("appId",listBean.getAppId()+"");

                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        //   controlLog.append("\n\n(错误)" + errorMsg.toString());
                        //Log.e("QRCodeManager = = ",errorMsg.toString());
                    }

                    @Override
                    public void onCancel() {
                        //controlLog.append("\n\n(取消)扫描任务取消了");
                        //Log.e("QRCodeManager = = = ","扫描任务取消了");
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
                        //Log.e("QRCodeManager","点击了手动添加了");
                    }
                });

    }
    /**二维码返回结果接收*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //监听跳转到权限设置界面后再回到应用
        permissionsUtil.onActivityResult(requestCode, resultCode, intent);

        //注册onActivityResult
        if (requestCode == 55846){
            QRCodeManager.getInstance().with(getActivity()).onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden){
            userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
            if(userId.equals("")){
                getHotData();
            }else {
                getHotData2();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
//        if(userId.equals("")){
//            getHotData();
//        }else {
//            getHotData2();
//        }
    }

}
