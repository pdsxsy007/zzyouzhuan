package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;

/**
 * Created by Cuilei on 2020/9/22.
 */

public class YsAppAdapter2 extends CommonAdapter<YsAppBean.Obj> {
    Context mcontext;
    List<YsAppBean.Obj .Apps> appdatas;
    List<YsAppBean.Obj > mdatas;
    public YsAppAdapter2(Context context, int layoutId, List<YsAppBean.Obj> datas) {
        super(context, layoutId, datas);
        mcontext = context;
        appdatas = new ArrayList<>();
        mdatas = datas;
    }

    @Override
    protected void convert(ViewHolder holder, YsAppBean.Obj apps, int position) {



        RecyclerView recyclerView = holder.getView(R.id.item_rc);
        recyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
            if(mdatas.get(position).getModulesCode().contains("yyzx_")){
                appdatas.clear();
                holder.setText(R.id.service_title,apps.getModulesName());

                appdatas.addAll(mdatas.get(position).getApps());
                recyclerView.setAdapter(new YsAppAdapter(mContext,R.layout.item_app,appdatas));
        }else {
                holder.setVisible(R.id.service_title,false);
            }



    }
}
