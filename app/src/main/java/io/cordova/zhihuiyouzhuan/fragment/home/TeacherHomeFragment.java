package io.cordova.zhihuiyouzhuan.fragment.home;


import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.YsMainActivity;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.activity.SystemMsgActivity;
import io.cordova.zhihuiyouzhuan.activity.YsNewsActivity;
import io.cordova.zhihuiyouzhuan.adapter.NewsAdapter3;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.adapter.YsNewsAdapter;
import io.cordova.zhihuiyouzhuan.bean.BannerBean;
import io.cordova.zhihuiyouzhuan.bean.CountBean;
import io.cordova.zhihuiyouzhuan.bean.ItemNewsBean;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean2;
import io.cordova.zhihuiyouzhuan.bean.NewsModulesBean;
import io.cordova.zhihuiyouzhuan.bean.OAMessageBean;
import io.cordova.zhihuiyouzhuan.bean.ServiceAppListBean;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.fragment.BlankFragment;
import io.cordova.zhihuiyouzhuan.fragment.SimpleCardFragment;
import io.cordova.zhihuiyouzhuan.fragment.SimpleCardFragment2;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.NoScrollViewPager;
import io.cordova.zhihuiyouzhuan.utils.PermissionsUtil;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import io.cordova.zhihuiyouzhuan.utils.netState;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;
import pub.devrel.easypermissions.AfterPermissionGranted;

/**
 * Created by Cuilei on 2020/9/10.
 */

public class TeacherHomeFragment extends BaseFragment implements PermissionsUtil.IPermissionsCallback {

//    @BindView(R.id.news_title_1)
//    TextView title1;
//    @BindView(R.id.news_title_2)
//    TextView title2;
//    @BindView(R.id.news_title_3)
//    TextView title3;

//    @BindView(R.id.line1)
//    TextView line1;
//    @BindView(R.id.line2)
//    TextView line2;
//    @BindView(R.id.line3)
//    TextView line3;

//    @BindView(R.id.news_list)
//    RecyclerView newsList;

    @BindView(R.id.more_tv)
    TextView  moreTv;
    @BindView(R.id.more_tv_2)
    TextView  moreTv2;
    @BindView(R.id.banner)
    Banner banner;
    YsNewsBean ysNewsBean;
    int typeName;
    NewsModulesBean newsModulesBean;

    List<Integer> types;
    String title;

    YsAppAdapter ysAppAdapter;
    private int flag = 0;
    int pos = 0;
    int pos1 = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    boolean isLogin =false;
    List<String> images = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    List<String> titles2;
    @BindView(R.id.vp)
    NoScrollViewPager mViewPager0;

    @BindView(R.id.service_rc)
    RecyclerView serviceRc;
    @BindView(R.id.tl)
    SlidingTabLayout mTabLayout_0;
    @BindView(R.id.vp_2)
    NoScrollViewPager mViewPager;

    YsAppBean ysAppBean;
    List<YsAppBean.Obj.Apps> objList1;
    List<YsAppBean.Obj.Apps> objList2;
    @BindView(R.id.tl_1)
    SlidingTabLayout mTabLayout_1;
    @BindView(R.id.message_num)
    TextView messageNum;
    @BindView(R.id.tv_title)
    TextView tv_title;
    String newsUrl;
    String mtiele;
    List<YsNewsBean.Obj.PortalNewsList> datas;
    List<YsNewsBean.Obj> objlist = new ArrayList<>();
    YsNewsBean.Obj obj1= new YsNewsBean.Obj();

    @BindView(R.id.name_tv)
    TextView name;
    @BindView(R.id.db_num)
    TextView dbNum;
    @BindView(R.id.oa_dy)
    TextView dyNum;
    @BindView(R.id.sys_msg)
    LinearLayout sysMsg;
    @BindView(R.id.scan_img)
    ImageView iv_qr;
    PermissionsUtil permissionsUtil;
    @Override
    public int getLayoutResID() {
        return R.layout.fragment_teacher_home;
    }

    @Override
    public void initView(View view) {
        super.initView(view);


//        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));

        objList1 = new ArrayList<>();
        objList2 = new ArrayList<>();
        types = new ArrayList<>();
        datas = new ArrayList<>();
        netWorkSystemMsg();
        netWorkUserMsg();
        getType();

        getOAdb();
        netWorkOAEmail();
        serviceRc.setLayoutManager(new GridLayoutManager(getActivity(),4));
        getAppList();
        getBannerData();
    }

    @Override
    public void initData() {
        super.initData();
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.news_url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news11111111111",response.body());


                        ysNewsBean = JsonUtil.parseJson(response.body(), YsNewsBean.class);

                        if(ysNewsBean.isSuccess()){

                            for(int i=0;i<ysNewsBean.getObj().size();i++){

                                objlist.addAll(ysNewsBean.getObj());
                            }

                            mtiele = ysNewsBean.getObj().get(0).getPortalNewsModuleName();
                            obj1 = ysNewsBean.getObj().get(0);
                            datas.addAll(ysNewsBean.getObj().get(0).getPortalNewsList());

                            initTablayout2();
                        }





                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
        moreTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), YsNewsActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("typeid",typeName);
                getActivity().startActivity(intent);
            }
        });


        getNewsData();


        moreTv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),BaseWebActivity4.class);
                intent.putExtra("appUrl",newsUrl);
                getActivity().startActivity(intent);
            }
        });
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
                                            name.setText(userMsgBean.getObj().getModules().getMemberNickname() + "   "+ userMsgBean.getObj().getModules().getMemberAcademicNumber());
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
    public void initListener() {
        super.initListener();


        sysMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SystemMsgActivity.class));
            }
        });

        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//提交
            cameraTask();
            }
        });

    }


    private void getType(){
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getPortalNewsModules)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("types11111111111",response.body());
                        titles2 = new ArrayList<>();

                        newsModulesBean = JsonUtil.parseJson(response.body(),NewsModulesBean.class);
                        for (int i = 0;i< newsModulesBean.getObj().size();i++){

                            types.add(newsModulesBean.getObj().get(i).getPortalNewsModuleId());
                            titles2.add(newsModulesBean.getObj().get(i).getPortalNewsModuleName());
                        }

                        typeName = types.get(0);
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    List<String> newstitle;
    List<String> newstitleUrl;
    List<List<ItemNewsBean>> lists;
    List<String> mlists = new ArrayList<>();

    private void getNewsData() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNews)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Log.e("news",response.body());
                        Gson gson = new Gson();
                        NewsBean newsBean = gson.fromJson(response.body(), new TypeToken<NewsBean>() {
                        }.getType());


                        Map<String, List<ItemNewsBean>> obj = (Map<String, List<ItemNewsBean>>) newsBean.getObj();
                        newstitle = new ArrayList<>();
                        newstitleUrl = new ArrayList<>();
                        lists = new ArrayList<>();

                        mFragments.clear();
                        newstitle.clear();
                        mlists.clear();
                        lists.clear();

                        for (Map.Entry<String, List<ItemNewsBean>> entry : obj.entrySet()) {

                            String key = entry.getKey();
                            if (key.contains("[@gilight]")) {//最版的
                                flag = 1;
                                String[] split = key.split("\\[@gilight\\]");
                                List<ItemNewsBean> value = entry.getValue();

                                String s = gson.toJson(value);
                                mlists.add(s);
                                newstitle.add(split[0]);
                                newstitleUrl.add(split[1]);

                            } else {//老版的
                                flag = 0;
                                List<ItemNewsBean> value = entry.getValue();

                                String s = gson.toJson(value);
                                mlists.add(s);
                                newstitle.add(key);
                                newsUrl = "http://www.zyedu.org/index/xyzt.htm";
                                ItemNewsBean[] array = new Gson().fromJson(s,ItemNewsBean[].class);
                                List<ItemNewsBean> list = Arrays.asList(array);
                                for(int i=0; i< list.size();i++){
                                    newstitleUrl.add(list.get(i).getNewsHref());
                                }

                            }

                        }

                        initTablayout(1);
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void initTablayout2() {
        //Log.e("清理前",mFragments.size() +  "");
        mFragments2.clear();
        //Log.e("清理后",mFragments.size() +  "");


        for (int i = 0; i < titles2.size(); i++) {

            BlankFragment simpleCardFragment =  BlankFragment.newInstance(objlist.get(i).getPortalNewsList(), titles2.get(i),getActivity());
            mFragments2.add(simpleCardFragment);

            //Log.e("s数据",s);
        }

        MyPagerAdapter2 adapter = new MyPagerAdapter2(getChildFragmentManager());
        mViewPager0.setAdapter(adapter);
        mTabLayout_0.setViewPager(mViewPager0);
        mTabLayout_0.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager0.setCurrentItem(position);
                pos1 = position;
                typeName = types.get(position);
                title = titles2.get(position);
                obj1 = objlist.get(position);
                datas.clear();
                datas.addAll(ysNewsBean.getObj().get(position).getPortalNewsList());

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        mTabLayout_0.setCurrentTab(pos1);
    }

    private void initTablayout( int flag) {
        //Log.e("清理前",mFragments.size() +  "");
        mFragments.clear();
        //Log.e("清理后",mFragments.size() +  "");

        if(flag == 0){
            for (int i = 0; i < newstitle.size(); i++) {

                mFragments.add(SimpleCardFragment2.getInstance(mlists.get(i),i,newstitle.get(i)));
            }

        }else {
            for (int i = 0; i < newstitle.size(); i++) {
                SimpleCardFragment simpleCardFragment = new SimpleCardFragment(mlists.get(i), i, newstitleUrl.get(i), newstitle.get(i));
                mFragments.add(simpleCardFragment);
                String s = mlists.get(i);
                //Log.e("s数据",s);
            }
//            for (int i = 0; i < newstitle.size(); i++) {
//
//                mFragments.add(SimpleCardFragment2.getInstance(mlists.get(i),i,newstitle.get(i)));
//            }

        }

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mViewPager.setOffscreenPageLimit(0);
        mTabLayout_1.setViewPager(mViewPager);

        mTabLayout_1.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int i) {
                mViewPager.setCurrentItem(i);
                pos = i;
                switch (i){
                    case 0:
                        newsUrl = "http://www.zyedu.org/index/xyzt.htm";
                        break;
                    case 1:
                        newsUrl = "http://www.zyedu.org/index/xwgk.htm";
                        break;
                    case 2:
                        newsUrl = "http://www.zyedu.org/index/tzgg.htm";
                        break;
                    case 3:
                        newsUrl = "http://www.zyedu.org/index/xyxw.htm";
                        break;
                }
            }

            @Override
            public void onTabReselect(int i) {

            }
        });
        mTabLayout_1.setCurrentTab(pos1);
    }

    OAMessageBean oaMessageBean;
    private void getOAdb(){
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("oa待办",response.body());
                        oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
                        if(oaMessageBean.getCount() != null){
                            dbNum.setText(oaMessageBean.getCount()+"/条");
                        }else {
                            dbNum.setText("0/条");
                        }

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        ViewUtils.cancelLoadingDialog();
                    }
                });

    }



    OAMessageBean oaEmail = new OAMessageBean();

    private void netWorkOAEmail() {


        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","3")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待办",response.body());
                        oaEmail = JSON.parseObject(response.body(),OAMessageBean.class);
                        //dyNum.setText(oaEmail.getCount() + "/条");
                        if(oaEmail.getCount() != null){
                            dbNum.setText(oaEmail.getCount()+"/条");
                        }else {
                            dbNum.setText("0/条");
                        }
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }

    private static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {

        permissionsUtil=  PermissionsUtil
                .with(this)
                .requestCode(1)
                .isDebug(true)//开启log
                .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                .request();
    }

    public void onScanQR() {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));

        QRCodeManager.getInstance()
                .with(getActivity())
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
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
    public void onPermissionsGranted(int requestCode, String... permission) {
        onScanQR();
    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {

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

    private class MyPagerAdapter2 extends FragmentStatePagerAdapter {
        public MyPagerAdapter2(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments2.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles2.get(position);
        }

        @Override
        public Fragment getItem(int position) {
         /*   Fragment fragment = mFragments.get(position);
            Bundle bundle=new Bundle();
            bundle.putString("jsonmy",mlists.get(position));
            fragment.setArguments(bundle);*/
            return mFragments2.get(position);
        }


        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }


    }



    /**
     * 判断是否安装了应用
     * @return true 为已经安装
     */
    private boolean hasApplication(String scheme) {
        PackageManager manager = getActivity().getPackageManager();
        Intent action = new Intent(Intent.ACTION_VIEW);
        action.setData(Uri.parse(scheme));
        List list = manager.queryIntentActivities(action, PackageManager.GET_RESOLVED_FILTER);
        return list != null && list.size() > 0;
    }




    //轮播图
    private void getBannerData() {
        String s = UrlRes.HOME_URL + UrlRes.findBroadcastListUrl;
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findBroadcastListUrl)
                .tag(this)
                .params("broadcastState", 0)
                .params("broadcastEquipment", 0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        //Log.e("banner",response.body());
                        BannerBean bannerBean = JsonUtil.parseJson(response.body(), BannerBean.class);
                        boolean success = bannerBean.getSuccess();
                        if (success == true) {
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

    /* 记录该微应用的的访问量
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

    CountBean countBean1;
    private void netWorkSystemMsg() {

        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_countUnreadMessagesForCurrentUser)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s",response.body());

                        countBean1 = JSON.parseObject(response.body(), CountBean.class);
                        messageNum.setText( "您有"+countBean1.getObj()+"条信息待查看，请及时查看");

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }
    private void getAppList(){
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
                        if(objList1.size()> 7){
                            for(int i=0; i<7; i++){
                                objList2.add(objList1.get(i));
                            }
                            YsAppBean.Obj.Apps app = new YsAppBean.Obj.Apps();
                            app.setAppImages(getResourcesUri(R.drawable.more_app));
                            app.setAppName("更多应用");
                            objList2.add(app);
                        }else{
                            objList2.addAll(objList1);
                        }

                        ysAppAdapter = new YsAppAdapter(getActivity(),R.layout.item_app,objList2);
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
    private String getResourcesUri(@DrawableRes int id) {
        Resources resources = getResources();
        String uriPath = ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(id) + "/" +
                resources.getResourceTypeName(id) + "/" +
                resources.getResourceEntryName(id);
        return uriPath;
    }

}
