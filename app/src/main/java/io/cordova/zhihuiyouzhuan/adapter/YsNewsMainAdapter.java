package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class YsNewsMainAdapter extends CommonAdapter<YsNewsBean.Obj.PortalNewsList> {
    Context mContext;
    String mtitle;
    public YsNewsMainAdapter(Context context, int layoutId, List<YsNewsBean.Obj.PortalNewsList> datas, String title) {
        super(context, layoutId, datas);
        mContext = context;
        mtitle = title;
    }

    @Override
    protected void convert(ViewHolder holder, final YsNewsBean.Obj.PortalNewsList obj, int position) {

        holder.setText(R.id.news_type,mtitle);
        holder.setText(R.id.news_title,obj.getPortalNewsTitle());
        holder.setText(R.id.time_tv,obj.getPortalNewsApplyTime());

        ImageView imageView = holder.getView(R.id.news_img);
        Glide.with(mContext).load(UrlRes.HOME3_URL + obj.getPortalNewsTitleImg()).into(imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext,BaseWebActivity4.class);
                intent.putExtra("appUrl","http://39.101.203.225:8081/portal/dist/native/newsDetail.html?id=" + obj.getPortalNewsId());
                intent.putExtra("appName",obj.getPortalNewsTitle());
                mContext.startActivity(intent);
            }
        });
    }

    }


