package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.bean.ItemNewsBean2;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class YsNewsAdapter3 extends CommonAdapter<ItemNewsBean2> {
    Context mContext;
    String mtitle;
    public YsNewsAdapter3(Context context, int layoutId, List<ItemNewsBean2> datas, String title) {
        super(context, layoutId, datas);
        mContext = context;
        mtitle = title;
    }

    @Override
    protected void convert(ViewHolder holder, final ItemNewsBean2 obj, int position) {

        holder.setText(R.id.news_type,mtitle);
        holder.setText(R.id.news_title,obj.getNewsTitle());
        holder.setText(R.id.time_tv,obj.getNewsDate());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);

                //intent.putExtra("appId","");
                String url = obj.getNewsHref();
                if(url.contains("index/../")){
                    url = url.replace("index/..","");
                }else{
                    url = obj.getNewsHref();
                }
                intent.putExtra("appUrl", url);
                intent.putExtra("appName",obj.getNewsTitle());
                mContext.startActivity(intent);


    }
        });
    }

    }


