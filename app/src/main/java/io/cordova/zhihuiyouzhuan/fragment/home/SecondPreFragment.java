package io.cordova.zhihuiyouzhuan.fragment.home;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.activity.LoginActivity2;
import io.cordova.zhihuiyouzhuan.activity.MyToDoMsg2Activity;
import io.cordova.zhihuiyouzhuan.adapter.NewsAdapter3;
import io.cordova.zhihuiyouzhuan.bean.CountBean;
import io.cordova.zhihuiyouzhuan.bean.ItemNewsBean;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean;
import io.cordova.zhihuiyouzhuan.bean.NewsBean2;
import io.cordova.zhihuiyouzhuan.bean.OAMessageBean;
import io.cordova.zhihuiyouzhuan.fragment.BaseFragment;
import io.cordova.zhihuiyouzhuan.utils.BadgeView;
import io.cordova.zhihuiyouzhuan.utils.LighterHelper;
import io.cordova.zhihuiyouzhuan.utils.MobileInfoUtils;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.StringUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import me.samlss.lighter.Lighter;
import me.samlss.lighter.interfaces.OnLighterListener;
import me.samlss.lighter.parameter.Direction;
import me.samlss.lighter.parameter.LighterParameter;
import me.samlss.lighter.parameter.MarginOffset;
import me.samlss.lighter.shape.CircleShape;

/**
 * Created by Administrator on 2019/8/21 0021.
 */

public class SecondPreFragment extends BaseFragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener{

    RelativeLayout rl_msg_app;

    private BadgeView badge1;
    boolean isLogin = false;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    NewsAdapter3 adapter;
    OAMessageBean oaMessageBean = new OAMessageBean();
    OAMessageBean oaEmail = new OAMessageBean();
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

    int num1,num2,num3,num4,num5;
    String count;
   int page = 1;
    List<NesItemBean.Obj> objList1 = new ArrayList<>();
    List<NesItemBean.Obj> objList2 = new ArrayList<>();
    List<NesItemBean.Obj> objList3 = new ArrayList<>();
    List<NesItemBean.Obj> objList4 = new ArrayList<>();
    List<NesItemBean.Obj> objList5 = new ArrayList<>();
    SmartRefreshLayout mSwipeRefreshLayout;
    int pos = 0;
    private int flag = 0;
    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_second_pre;
    }

    @Override
    protected void initView(final View view) {
        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));



        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL, false);

        recyclerView.setLayoutManager(mLinearLayoutManager);



        xyTz = view.findViewById(R.id.xy_tz);
        rmzt = view.findViewById(R.id.rm_zt);
        jwSta = view.findViewById(R.id.jw_sta);
        xsSta = view.findViewById(R.id.xs_sta);
        zsSta = view.findViewById(R.id.zs_sta);
        noticeNews = view.findViewById(R.id.notice_news);
        noticeNews.setOnClickListener(this);

        xyTz.setOnClickListener(this);
        rmzt.setOnClickListener(this);
        xyTz.setOnClickListener(this);
        zsSta.setOnClickListener(this);
        jwSta.setOnClickListener(this);
        xsSta.setOnClickListener(this);
        radioGroup = view.findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(this);
        rl_msg_app = view.findViewById(R.id.rl_msg_app1);
        getNews2("5");
        radioGroup.check(R.id.jw_sta);
        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"userId",""));
        rl_msg_app.setVisibility(View.VISIBLE);
        count = (String) SPUtils.get(getActivity(), "count", "");
        badge1 = new BadgeView(getActivity(), rl_msg_app);
        remind();
        if (!isLogin){
            badge1.hide();
        }else {
            if(count != null){
                if(count.equals("0")){

                    badge1.hide();
                }else {
                    badge1.show();
                }
            }else {
                badge1.hide();
            }



        }


        rl_msg_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
                if (isLogin){
                    Intent intent = new Intent(MyApp.getInstance(), MyToDoMsg2Activity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MyApp.getInstance(), LoginActivity2.class);
                    startActivity(intent);
                }
            }
        });

        String home02 = (String) SPUtils.get(MyApp.getInstance(), "home02", "");
        if(home02.equals("")){
            setGuideView();
        }


        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                getNews2("5");


               /* mTabLayout_1.setSelected(true);
                mTabLayout_1.setCurrentTab(pos);*/
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

    private void setGuideView() {


        CircleShape circleShape = new CircleShape(10);
        circleShape.setPaint(LighterHelper.getDashPaint()); //set custom paint
        View tipView1 = getLayoutInflater().inflate(R.layout.fragment_home_gl, null);
        // 使用图片
        Lighter.with(getActivity()
        )
                .setBackgroundColor(0xB9000000)
                .setOnLighterListener(new OnLighterListener() {
                    @Override
                    public void onShow(int index) {


                    }

                    @Override
                    public void onDismiss() {
                        SPUtils.put(MyApp.getInstance(),"home02","1");
                    }
                })
                .addHighlight(new LighterParameter.Builder()
                        //.setHighlightedViewId(R.id.rl_msg_app)
                        .setHighlightedView(rl_msg_app)
                        .setTipLayoutId(R.layout.fragment_home_gl3)
                        //.setLighterShape(new RectShape(80, 80, 50))
                        //.setLighterShape(circleShape)
                        .setTipViewRelativeDirection(Direction.BOTTOM)
                        .setTipViewRelativeOffset(new MarginOffset(150, 0, 30, 0))
                        .build()).show();
    }

    private void remind() { //BadgeView的具体使用



        String count = (String) SPUtils.get(getActivity(), "count", "");
        if(count.equals("")){
            count = "0";
        }

        //badge1.setText(count); // 需要显示的提醒类容
        if (Integer.parseInt(count) > 99) {

            badge1.setText("99+");
        }else{
            badge1.setText(count); // 需要显示的提醒类容

        }
        badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge1.setTextColor(Color.WHITE); // 文本颜色
        badge1.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        //badge1.setBackgroundResource(R.mipmap.icon_message_png); //设置背景图片
        badge1.setTextSize(10); // 文本大小
        badge1.setBadgeMargin(3, 3); // 水平和竖直方向的间距

        if(count.equals("0")){
            badge1.hide();
        }else {
            badge1.show();// 只有显示

        }

    }

    private void netWorkOAEmail() {


        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","3")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待办",response.body());
                        oaEmail = JSON.parseObject(response.body(),OAMessageBean.class);
                        count = Integer.parseInt(oaEmail.getObj().getCount()) + Integer.parseInt(countBean1.getObj()) + Integer.parseInt(oaMessageBean.getObj().getCount()) + Integer.parseInt(oaMessageBean2.getObj().getCount()) + "";
                        if(SPUtils.get(getActivity(),"rolecodes","").equals("student")){
                            count = Integer.parseInt(countBean1.getObj()) + "";
                        }
                        if(null == count){
                            count = "0";
                        }

                        SPUtils.put(MyApp.getInstance(),"count",count+"");
                        if(!count.equals("") && !"0".equals(count)){
                            remind();
                            SPUtils.get(getContext(),"count","");
                        }else {
                            badge1.hide();
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
    OAMessageBean oaMessageBean2;
    private void netWorkOANotice() {


        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待办",response.body());
                        oaMessageBean2 = JSON.parseObject(response.body(),OAMessageBean.class);
                        if(oaMessageBean2.getObj().getCount().equals("0")){

                        }else {


                        }
                        netWorkOAEmail();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
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
                        Log.e("s",response.toString());

                        countBean1 = JSON.parseObject(response.body(), CountBean.class);
                        //yy_msg_num.setText(countBean.getCount()+"");
                        netWorkOAToDoMsg();//OA待办

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    /**OA消息列表*/
    private void netWorkOAToDoMsg() {
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type", "db")
//                .params("workType", "workdb")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s",response.toString());
//
//                        countBean2 = JSON.parseObject(response.body(), CountBean.class);
//                        netWorkDyMsg();
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//                        Log.e("sssssss",response.toString());
//                    }
//                });
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("oa待办",response.body());
                        oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
                        netWorkOANotice();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();


        }else{  // 在最前端显示 相当于调用了onResume();
            isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
            netInsertPortal("2");
            String count = (String) SPUtils.get(getActivity(),"count","");
            Log.e("count-------",count);
            //badge1.setText(count);

            if (!isLogin){
                badge1.hide();
            }else {

                netWorkSystemMsg();

                //remind(rl_msg_app);
            }


        }
    }

    private void netInsertPortal(final String insertPortalAccessLog) {
        String imei = MobileInfoUtils.getIMEI(getActivity());
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Four_Modules)
                .params("portalAccessLogMemberId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("portalAccessLogEquipmentId",(String) SPUtils.get(MyApp.getInstance(),"imei",""))//设备ID
                .params("portalAccessLogTarget", insertPortalAccessLog)//访问目标
                .params("portalAccessLogVersionNumber", (String) SPUtils.get(getActivity(),"versionName", ""))//版本号
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











    List<String> newstitle;
    List<String> newstitleUrl;
    List<List<ItemNewsBean>> lists;
    List<String> mlists = new ArrayList<>();

    private void getNewsData(final View view) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("isReturnWithUrl","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        Gson gson = new Gson();
//                        NewsBean newsBean = JsonUtil.parseJson(response.body(),NewsBean.class);
//                        JsonObject jsonObject = new JsonObject();



                        NewsBean newsBean = gson.fromJson(response.body(), new TypeToken<NewsBean>(){}.getType());


                        Map<String, List<ItemNewsBean>> obj = (Map<String, List<ItemNewsBean>>) newsBean.getObj();

                        newstitle = new ArrayList<>();
                        newstitleUrl = new ArrayList<>();
                        int i = 0;
                        List<Map<String,List<ItemNewsBean>>> list = new ArrayList<>();

                        lists = new ArrayList<>();

                        mFragments.clear();
                        newstitle.clear();
                        mlists.clear();
                        lists.clear();
                        for (Map.Entry<String, List<ItemNewsBean>> entry : obj.entrySet()) {

                            String key = entry.getKey();
                            if(key.contains("[@gilight]")){//最版的
                                flag = 1;
                                String[] split = key.split("\\[@gilight\\]");
                                List<ItemNewsBean> value = entry.getValue();

                                String s = gson.toJson(value);
                                mlists.add(s);
                                newstitle.add(split[0]);
                                newstitleUrl.add(split[1]);
                                lists.add(value);
                            }else {//老版的
                                flag = 0;
                                List<ItemNewsBean> value = entry.getValue();

                                String s = gson.toJson(value);
                                mlists.add(s);
                                newstitle.add(key);
                                lists.add(value);
                            }

                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }


    @Override
    public void onClick(View view) {

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
                        for(int i=0; i<15; i++){
                            if(objList.size() == 14){
                                objList1.clear();
                                objList1.add(objList.get(0));
                                objList1.add(objList.get(1));
                                objList1.add(objList.get(2));
                                objList1.add(objList.get(3));
                                objList1.add(objList.get(4));
                                objList1.add(objList.get(5));
                                objList1.add(objList.get(6));
                                objList1.add(objList.get(7));
                                objList1.add(objList.get(8));
                                objList1.add(objList.get(9));
                                objList1.add(objList.get(10));
                                objList1.add(objList.get(11));
                                objList1.add(objList.get(12));
                                objList1.add(objList.get(13));
                            }else{

                                 objList1.add(objList.get(i));
                            }

                        }
                        if(objList.size() >15){
                            for (int i=15;i<25; i++){


                                    objList2.add(objList.get(i));


                            }

                        }

                        if(objList.size() >25){
                            for (int i=25;i<35; i++){

                                objList3.add(objList.get(i));
                            }
                        }

                        if(objList.size()>35) {
                            for (int i = 35; i < 45; i++) {
                               if(objList.size() == 40){
                                   objList4.clear();
                                   objList4.add(objList.get(35));
                                   objList4.add(objList.get(36));
                                   objList4.add(objList.get(37));
                                   objList4.add(objList.get(38));
                                   objList4.add(objList.get(39));
                               }else{

                                   objList4.add(objList.get(i));
                               }

                            }
                        }
                        if(objList.size() > 45){
                            for (int i=45;i<50; i++){
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
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

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
}
