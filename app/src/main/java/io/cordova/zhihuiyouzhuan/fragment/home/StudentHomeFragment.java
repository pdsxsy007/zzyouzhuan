package io.cordova.zhihuiyouzhuan.fragment.home;


import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.activity.YsNewsActivity;
import io.cordova.zhihuiyouzhuan.adapter.YsAppAdapter;
import io.cordova.zhihuiyouzhuan.bean.CountBean;
import io.cordova.zhihuiyouzhuan.bean.NewsModulesBean;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.fragment.BlankFragment;
import io.cordova.zhihuiyouzhuan.utils.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.NoScrollViewPager;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Cuilei on 2020/9/22.
 */

public class StudentHomeFragment extends BaseFragment {

    @BindView(R.id.xingqi_tv)
    TextView xingqiTv;

    @BindView(R.id.date_tv)
    TextView dateTv;
    @BindView(R.id.message_num)
    TextView messageNum;
    CountBean countBean1;
    @BindView(R.id.vp)
    NoScrollViewPager mViewPager0;
    List<String> titles2;
    @BindView(R.id.service_rc)
    RecyclerView serviceRc;
    @BindView(R.id.tl)
    SlidingTabLayout mTabLayout_0;
    YsAppBean ysAppBean;
    YsNewsBean ysNewsBean;
    List<YsNewsBean.Obj> objlist = new ArrayList<>();
    String mtitle;
    YsNewsBean.Obj obj1= new YsNewsBean.Obj();
    List<YsNewsBean.Obj.PortalNewsList> datas = new ArrayList<>();
    @BindView(R.id.more_tv)
    TextView  moreTv;
    String title;
    int typeName;
    int pos1;
    List<Integer> types;
    @BindView(R.id.name_tv)
    TextView name;


    @BindView(R.id.today_class)
    TextView today;

    @BindView(R.id.tom_class)
    TextView tom;
    private ArrayList<Fragment> mFragments2 = new ArrayList<>();
    @Override
    public int getLayoutResID() {
        return R.layout.fragment_student_home;
    }
    YsAppAdapter ysAppAdapter;
    List<YsAppBean.Obj.Apps> objList1;
    NewsModulesBean newsModulesBean;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void initData() {
        super.initData();
        netWorkSystemMsg();
        getType();
        netWorkUserMsg();
        objList1 = new ArrayList<>();
        titles2 = new ArrayList<>();
        types = new ArrayList<>();
        serviceRc.setLayoutManager(new GridLayoutManager(getActivity(),4));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日");

        dateTv.setText(simpleDateFormat.format(new Date()));
        switch ( calendar.get(Calendar.DAY_OF_WEEK)-1) {
            case 0:
                xingqiTv.setText("SUN.");
                break;
            case 1:
                xingqiTv.setText("MON.");
                break;
            case 2:
                xingqiTv.setText("TUE.");
                break;
            case 3:
                xingqiTv.setText("WED.");
                break;
            case 4:
                xingqiTv.setText("THU.");
                break;
            case 5:
                xingqiTv.setText("FRI.");
                break;
            case 6:
                xingqiTv.setText("SAT.");
                break;

        }
        getAppList();


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

                            mtitle = ysNewsBean.getObj().get(0).getPortalNewsModuleName();
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

        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BaseWebActivity4.class);
                intent.putExtra("appUrl", "http://39.101.203.225:8084/microapplication/app/login/appLogin?indexUrl=/wyy/kcb_student.html");
                startActivity(intent);
            }
        });
        tom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), BaseWebActivity4.class);
                intent.putExtra("appUrl", "http://39.101.203.225:8084/microapplication/app/login/appLogin?indexUrl=/wyy/kcb_student.html");
                startActivity(intent);
            }
        });


}


    private void netWorkSystemMsg() {

        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_countUnreadMessagesForCurrentUser)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s",response.body());

                        countBean1 = JSON.parseObject(response.body(), CountBean.class);
                        messageNum.setText( "您有"+countBean1.getCount()+"条信息待查看，请及时查看");

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
    private void initTablayout2() {
        //Log.e("清理前",mFragments.size() +  "");
        mFragments2.clear();
        //Log.e("清理后",mFragments.size() +  "");


        for (int i = 0; i < titles2.size(); i++) {

            BlankFragment simpleCardFragment =  BlankFragment.newInstance(datas, titles2.get(i),getActivity());
            mFragments2.add(simpleCardFragment);

            //Log.e("s数据",s);
        }

      MyPagerAdapter2 adapter = new MyPagerAdapter2(getChildFragmentManager());
        mViewPager0.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        mViewPager0.setOffscreenPageLimit(0);
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
}
