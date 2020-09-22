package io.cordova.zhihuiyouzhuan.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.adapter.YsMoreNewsAdapter;
import io.cordova.zhihuiyouzhuan.adapter.YsNewsAdapter;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsMoreBean;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;

/**
 * Created by Cuilei on 2020/9/16.
 */

public class YsNewsActivity extends BaseActivity {

    @BindView(R.id.news_name)
    TextView newName;
    @BindView(R.id.news_list)
    RecyclerView newsList;
    @BindView(R.id.swipeLayout)
    SmartRefreshLayout mSwipeRefreshLayout;
    List<YsNewsMoreBean.Obj> newsLists1;
    @BindView(R.id.header)
    ClassicsHeader header;
    YsNewsMoreBean ysNewsBean;
    YsMoreNewsAdapter newsAdapter;
    @BindView(R.id.back_img)
    ImageView back;
    int pageNum = 1;
    int pageSize = 10;
    @Override
    protected int getResourceId() {
        return R.layout.activity_ys_news;
    }

    @Override
    protected void initData() {
        super.initData();

        newName.setText(getIntent().getStringExtra("title"));

        getData(pageNum);
        mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNum++;
                getData(pageNum);
                refreshlayout.finishLoadmore();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                newsLists1.clear();
                getData(1);
                refreshlayout.finishRefresh();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }



    @Override
    protected void initView() {
        super.initView();
        header.setEnableLastTime(false);
        newsList.setLayoutManager(new LinearLayoutManager(this));

        newsLists1 = new ArrayList<>();


    }


    private void getData(int pageNum){

        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getNews)
                .params("portalNewsModuleId",getIntent().getIntExtra("typeid",0))
                .params("pageNum",pageNum)
                .params("pageSize",pageSize)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news11111111111",response.body());
                        ysNewsBean = JsonUtil.parseJson(response.body(), YsNewsMoreBean.class);
                        if(ysNewsBean.getObj().size() != 0){
                            newsLists1.addAll(ysNewsBean.getObj());


                            newsAdapter = new YsMoreNewsAdapter(YsNewsActivity.this,R.layout.item_ys_news,newsLists1,getIntent().getStringExtra("title"));
                            newsAdapter.notifyDataSetChanged();
                            newsList.setAdapter(newsAdapter);
                        }else{
                            Toast.makeText(getApplicationContext(),"没有更多新闻",Toast.LENGTH_SHORT).show();
                            return;
                        }



                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

}
