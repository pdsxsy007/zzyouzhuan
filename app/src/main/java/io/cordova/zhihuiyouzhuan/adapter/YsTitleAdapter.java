package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.NewsModulesBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class YsTitleAdapter extends CommonAdapter<YsNewsBean.Obj> {
    Context mContext;
    List<YsNewsBean.Obj.PortalNewsList> datas;
    public YsTitleAdapter(Context context, int layoutId, List<YsNewsBean.Obj> datas) {
        super(context, layoutId, datas);
        mContext = context;

    }

    @Override
    protected void convert(ViewHolder holder, final YsNewsBean.Obj obj, int position) {

        datas = new ArrayList<>();
        holder.setText(R.id.news_title,obj.getPortalNewsModuleName());
        datas.addAll(obj.getPortalNewsList());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData();
                holder.setVisible(R.id.line,true);
                if(  holder.itemView.isClickable() == false){
                    holder.setVisible(R.id.line,false);
                }
            }


        });

    }

    public List<YsNewsBean.Obj.PortalNewsList> setData(){

        return  datas;
    }

    }


