package io.cordova.zhihuiyouzhuan.fragment.home;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
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
import io.cordova.zhihuiyouzhuan.activity.YsNewsActivity;
import io.cordova.zhihuiyouzhuan.adapter.NewsAdapter3;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.adapter.YsNewsAdapter;
import io.cordova.zhihuiyouzhuan.bean.ItemNewsBean;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean2;
import io.cordova.zhihuiyouzhuan.bean.NewsModulesBean;
import io.cordova.zhihuiyouzhuan.bean.ServiceAppListBean;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.fragment.SimpleCardFragment;
import io.cordova.zhihuiyouzhuan.fragment.SimpleCardFragment2;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.NoScrollViewPager;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.netState;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Cuilei on 2020/9/10.
 */

public class TeacherHomeFragment extends BaseFragment {

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

    YsNewsBean ysNewsBean;
    int typeName;
    NewsModulesBean newsModulesBean;
    List<YsNewsBean.Obj.PortalNewsList> newsLists1;
    List<YsNewsBean.Obj.PortalNewsList> newsLists2;
    List<YsNewsBean.Obj.PortalNewsList> newsLists3;
    List<Integer> types;
    String title;

    YsAppAdapter ysAppAdapter;
    private int flag = 0;
    int pos = 0;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    boolean isLogin =false;

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
    @BindView(R.id.tl_1)
    SlidingTabLayout mTabLayout_1;
    @Override
    public int getLayoutResID() {
        return R.layout.fragment_teacher_home;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.0 全透明实现
            //getWindow.setStatusBarColor(Color.TRANSPARENT)
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //4.4 全透明状态栏
            getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//        newsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsLists1 = new ArrayList<>();
        newsLists2 = new ArrayList<>();
        newsLists3 = new ArrayList<>();
        objList1 = new ArrayList<>();
        types = new ArrayList<>();

//        getType();


        serviceRc.setLayoutManager(new GridLayoutManager(getActivity(),4));
        getAppList();
    }

    @Override
    public void initData() {
        super.initData();
        OkGo.<String>post(UrlRes.news_url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news11111111111",response.body());
                        ysNewsBean = JsonUtil.parseJson(response.body(), YsNewsBean.class);


                        title = ysNewsBean.getObj().get(0).getPortalNewsModuleName();




//                        newsList.setAdapter(new YsNewsAdapter(getActivity(),R.layout.item_ys_news,newsLists1,ysNewsBean.getObj().get(0).getPortalNewsModuleName()));
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

    }

    @Override
    public void initListener() {
        super.initListener();

//        title1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                title = ysNewsBean.getObj().get(0).getPortalNewsModuleName();
//                typeName = types.get(0);
//
//                line1.setBackgroundDrawable(getResources().getDrawable(R.drawable.biao_bg));
//                line2.setBackgroundDrawable(null);
//                line3.setBackgroundDrawable(null);
//                newsList.setAdapter(new YsNewsAdapter(getActivity(),R.layout.item_ys_news,newsLists1,ysNewsBean.getObj().get(0).getPortalNewsModuleName()));
//            }
//        });
//
//        title2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                typeName = types.get(1);
//                title = ysNewsBean.getObj().get(1).getPortalNewsModuleName();
//                line1.setBackgroundDrawable(null);
//                line2.setBackgroundDrawable(getResources().getDrawable(R.drawable.biao_bg));
//                line3.setBackgroundDrawable(null);
//                newsList.setAdapter(new YsNewsAdapter(getActivity(),R.layout.item_ys_news,newsLists2,ysNewsBean.getObj().get(1).getPortalNewsModuleName()));
//            }
//        });
//
//        title3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                typeName = types.get(2);
//                title = ysNewsBean.getObj().get(2).getPortalNewsModuleName();
//                line1.setBackgroundDrawable(null);
//                line2.setBackgroundDrawable(null);
//                line3.setBackgroundDrawable(getResources().getDrawable(R.drawable.biao_bg));
//
//                newsList.setAdapter(new YsNewsAdapter(getActivity(),R.layout.item_ys_news,newsLists3,ysNewsBean.getObj().get(2).getPortalNewsModuleName()));
//            }
//        });


    }


    private void getType(){
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getPortalNewsModules)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("types11111111111",response.body());

                        newsModulesBean = JsonUtil.parseJson(response.body(),NewsModulesBean.class);
                        for (int i = 0;i< newsModulesBean.getObj().size();i++){
                            types.add(newsModulesBean.getObj().get(i).getPortalNewsModuleId());
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

    private void getAppList(){
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
                           if(objList.get(i).getModulesCode().equals("index_tjfw")){
                               objList1.addAll(objList.get(i).getApps());
                           }
                       }
                       ysAppAdapter = new YsAppAdapter(getActivity(),R.layout.item_app,objList1);
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
