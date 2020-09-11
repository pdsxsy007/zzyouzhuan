package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.bean.NesItemBean;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class NewsAdapter3 extends CommonAdapter<NesItemBean.Obj> {
    Context mContext;
    public NewsAdapter3(Context context, int layoutId, List<NesItemBean.Obj> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final NesItemBean.Obj obj, int position) {
        holder.setText(R.id.tv_left,obj.getTitle());
        holder.setText(R.id.tv_right,obj.getInputtime());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",obj.getUrl());
                //intent.putExtra("appId","");
                intent.putExtra("appName",obj.getTitle());
                mContext.startActivity(intent);
            }
        });
    }








    }


