package io.cordova.zhihuiyouzhuan.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
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


import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.bean.BannerBean;
import io.cordova.zhihuiyouzhuan.bean.ItemNewsBean;
import io.cordova.zhihuiyouzhuan.bean.MyCollectionBean;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean2;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.NoScrollViewPager;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.netState;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2019/8/21 0021.
 */

public class HomeFragment extends BaseFragment {

    Banner banner;



    private ArrayList<Fragment> mFragments = new ArrayList<>();


    NoScrollViewPager mViewPager;


    SlidingTabLayout mTabLayout_1;

    SmartRefreshLayout mSwipeRefreshLayout;
    int pos = 0;

    boolean isLogin =false;

    LinearLayout llMyCollection;
    RecyclerView myDataList;
    ImageView iv_qr;
    TextView tv_title;
    String userId;
    private int flag = 0;
   List< List<NesItemBean.Obj>> objLists  = new ArrayList<>();
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_first;
    }

    @Override
    protected void initView(final View view) {

        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
        banner = view.findViewById(R.id.banner);
        mViewPager = view.findViewById(R.id.vp_2);
        mTabLayout_1 = view.findViewById(R.id.tl_1);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        llMyCollection = view.findViewById(R.id.ll_my_collection);
        myDataList = view.findViewById(R.id.my_collection_list);
        iv_qr = view.findViewById(R.id.iv_qr);
        tv_title = view.findViewById(R.id.tv_title);
        getBannerData();

        getNews2(view);
        getNews3(view);
        getNews4(view);
        getNews5(view);
        getNews6(view);
        getNews7(view);
//        getNews8(view);
        initTablayout(view,flag);

        userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        if(userId.equals("")){
            getHotData();
        }else {
            getHotData2();
        }
        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraTask();
            }
        });
//        getNewsData(view);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getBannerData();
                userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
                if(userId.equals("")){
                    getHotData();
                }else {
                    getHotData2();
                }
//                getNewsData(view);

                //+mViewPager.setCurrentItem(0);
                refreshlayout.finishRefresh();
            }
        });


        registerBoradcastReceiver();

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


    List<String> newstitle = new ArrayList<>();
    List<String> newstitleUrl;
    List<List<ItemNewsBean>> lists;
    List<String> mlists = new ArrayList<>();

//    private void getNewsData(final View view) {
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
//                .params("isReturnWithUrl","1")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("news",response.body());
//                        Gson gson = new Gson();
//                        NewsBean newsBean = gson.fromJson(response.body(), new TypeToken<NewsBean>(){}.getType());
//
//
//                        Map<String, List<ItemNewsBean>> obj = (Map<String, List<ItemNewsBean>>) newsBean.getObj();
//                        newstitle = new ArrayList<>();
//                        newstitleUrl = new ArrayList<>();
//                        lists = new ArrayList<>();
//
//                        mFragments.clear();
//                        newstitle.clear();
//                        mlists.clear();
//                        lists.clear();
//
//                        for (Map.Entry<String, List<ItemNewsBean>> entry : obj.entrySet()) {
//
//                            String key = entry.getKey();
//                            if(key.contains("[@gilight]")){//最版的
//                                flag = 1;
//                                String[] split = key.split("\\[@gilight\\]");
//                                List<ItemNewsBean> value = entry.getValue();
//
//                                String s = gson.toJson(value);
//                                mlists.add(s);
//                                newstitle.add(split[0]);
//                                newstitleUrl.add(split[1]);
//
//                            }else {//老版的
//                                flag = 0;
//                                List<ItemNewsBean> value = entry.getValue();
//
//                                String s = gson.toJson(value);
//                                mlists.add(s);
//                                newstitle.add(key);
//
//                            }
//
//                        }
//
//                        initTablayout(view,flag);
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//
//                    }
//                });
//    }

    NewsBean2 newsBean2 ;
    NesItemBean nesItemBean;
    List<NesItemBean.Obj> objList ;
    private void getNews2(final View view){//学院通知
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","2")
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
                        objList = new ArrayList<>();
                        objList.addAll(nesItemBean.getData());

                        SimpleCardFragment3 simpleCardFragment = new SimpleCardFragment3(objList, 0, newstitleUrl.get(0), newstitle.get(0));
                        mFragments.add(simpleCardFragment);
//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }
    List<NesItemBean.Obj> objList2 ;
    private void getNews3(final View view){//公告
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","3")
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
                        objList2 = new ArrayList<>();
                        objList2.addAll(nesItemBean.getData());
                        SimpleCardFragment3 simpleCardFragment2 = new SimpleCardFragment3(objList2, 1, newstitleUrl.get(1), newstitle.get(1));
                        mFragments.add(simpleCardFragment2);

//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }

    List<NesItemBean.Obj> objList3 ;
    private void getNews4(final View view){//热门主题
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","4")
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
                        objList3 = new ArrayList<>();
                        objList3.addAll(nesItemBean.getData());
                        SimpleCardFragment3 simpleCardFragment3 = new SimpleCardFragment3(objList3, 2, newstitleUrl.get(2), newstitle.get(2));
                        mFragments.add(simpleCardFragment3);
//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }
    List<NesItemBean.Obj> objList4 ;
    private void getNews5(final View view){//教务处
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","5")
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
                        objList4 = new ArrayList<>();
                        objList4.addAll(nesItemBean.getData());
                        objLists.add(objList4);
//                        initTablayout(view,flag);
                        SimpleCardFragment3 simpleCardFragment4 = new SimpleCardFragment3(objList4, 3, newstitleUrl.get(3), newstitle.get(3));
                        mFragments.add(simpleCardFragment4);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }

    List<NesItemBean.Obj> objList5 ;
    private void getNews6(final View view){//学生处
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","6")
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
                        objList5 = new ArrayList<>();
                        objList5.addAll(nesItemBean.getData());
                        SimpleCardFragment3 simpleCardFragment5 = new SimpleCardFragment3(objList5, 4, newstitleUrl.get(4), newstitle.get(4));
                        mFragments.add(simpleCardFragment5);
//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }

    List<NesItemBean.Obj> objList6 ;
    private void getNews7(final View view){//招生处
        OkGo.<String>post(UrlRes.news_url)
                .params("flag","7")
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
                        objList6 = new ArrayList<>();
                        objList6.addAll(nesItemBean.getData());
                        objLists.add(objList6);

                        SimpleCardFragment3 simpleCardFragment6 = new SimpleCardFragment3(objList6, 5, newstitleUrl.get(5), newstitle.get(5));
                        mFragments.add(simpleCardFragment6);
//                        initTablayout(view,flag);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });

    }
//    List<NesItemBean.Obj> objList7 ;
//    private void getNews8(final View view){//新闻中心
//        OkGo.<String>post(UrlRes.news_url)
//                .params("flag","8")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("news11111111111",response.body());
//                        Gson gson = new Gson();
//                        newsBean2 = gson.fromJson(response.body(),NewsBean2.class);
//
//                        String obj = newsBean2.getObj();
//
//                        Map<String,Object> jsonToMap = JSONObject.parseObject(obj);
//                        System.out.println("jsonToMap："+jsonToMap);
//                        nesItemBean = JSONObject.parseObject(jsonToMap.get("str").toString(),NesItemBean.class);
//                        System.out.println("jsonToData："+ jsonToMap.get("str").toString());
//                        objList7 = new ArrayList<>();
//                        objList7.addAll(nesItemBean.getData());
//                        objLists.add(objList7);
////                        initTablayout(view,flag);
//                    }
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//
//                    }
//                });
//
//    }
    private void initTablayout(View view, int flag) {
        Log.e("清理前",mFragments.size() +  "");
        mFragments.clear();
        Log.e("清理后",mFragments.size() +  "");
        newstitle = new ArrayList<>();
        newstitle.add("学院通知" );
        newstitle.add("公告" );
        newstitle.add("热门主题" );
        newstitle.add("教务处" );
        newstitle.add("学生处" );
        newstitle.add("招生处" );
        newstitle.add("新闻中心" );

        newstitleUrl = new ArrayList<>();
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");
        newstitleUrl.add("http://www.baidu.com");

          flag = 1;
        if(flag == 0){
            for (int i = 0; i < newstitle.size(); i++) {

                mFragments.add(SimpleCardFragment2.getInstance(mlists.get(i),i,newstitle.get(i)));
            }

        }else {
//            for (int i = 0; i < newstitle.size(); i++) {
//                SimpleCardFragment simpleCardFragment = new SimpleCardFragment(mlists.get(i), i, newstitleUrl.get(i), newstitle.get(i));
//                mFragments.add(simpleCardFragment);
//                String s = mlists.get(i);
//                Log.e("s数据",s);
//            }







            MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
            adapter.notifyDataSetChanged();
            mViewPager.setAdapter(adapter);
            mViewPager.setOffscreenPageLimit(0);
            mTabLayout_1.setViewPager(mViewPager);

            mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
                @Override
                public void onTabSelect(int i) {
                    mViewPager.setCurrentItem(i);
                    pos = i;
                }

                @Override
                public void onTabReselect(int i) {

                }
            });
            mTabLayout_1.setCurrentTab(pos);



//                String s = mlists.get(i);
//                Log.e("s数据",s);


        }


    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return newstitle.get(position);
        }

        @Override
        public Fragment getItem(int position) {
         /*   Fragment fragment = mFragments.get(position);
            Bundle bundle=new Bundle();
            bundle.putString("jsonmy",mlists.get(position));
            fragment.setArguments(bundle);*/

            return mFragments.get(position);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }


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

    private void getBannerData() {
        String s = UrlRes.HOME_URL + UrlRes.findBroadcastListUrl;
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findBroadcastListUrl)
                .params("broadcastState", 0)
                .params("broadcastEquipment",0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("banner",response.body());
                        BannerBean bannerBean = JsonUtil.parseJson(response.body(),BannerBean.class);
                        boolean success = bannerBean.getSuccess();
                        if(success == true){
                            List<BannerBean.Banners> list = bannerBean.getObj().getList();
                            initBanner(list);
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }







    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private void initBanner(final List<BannerBean.Banners> list) {
        images.clear();
        titles.clear();
        for (int i = 0; i < list.size(); i++) {
            String broadcastUrl = list.get(i).getBroadcastImage();
            images.add(UrlRes.HOME3_URL+broadcastUrl);
            titles.add(list.get(i).getBroadcastTitle());
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

                String broadcastUrl = list.get(position).getBroadcastUrl();
                if(broadcastUrl.equals("")){

                    ToastUtils.showToast(getActivity(),"暂无详情地址");
                }else {
                    //跳转到轮播图详情页
                    Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    intent.putExtra("appUrl",list.get(position).getBroadcastUrl());
                    //intent.putExtra("appId","");
                    intent.putExtra("appName",list.get(position).getBroadcastTitle());
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
                            Log.e("url  ==",collectionBean.getObj().get(position).getAppUrl() + "");

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
                        Log.e("result1",response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
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
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }


    public void onScanQR() {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));
        Log.e("tag  = ","点击了");
        QRCodeManager.getInstance()
                .with(getActivity())
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        //controlLog.append("\n\n(结果)" + result);
                        Log.e("QRCodeManager = ",result);
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
    /**二维码返回结果接收*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
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
}
