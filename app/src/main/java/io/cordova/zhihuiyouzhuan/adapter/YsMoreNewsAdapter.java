package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.YsNewsBean;
import io.cordova.zhihuiyouzhuan.bean.YsNewsMoreBean;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class YsMoreNewsAdapter extends CommonAdapter<YsNewsMoreBean.Obj> {
    Context mContext;
    String mtitle;
    public YsMoreNewsAdapter(Context context, int layoutId, List<YsNewsMoreBean.Obj> datas, String title) {
        super(context, layoutId, datas);
        mContext = context;
        mtitle = title;
    }

    @Override
    protected void convert(ViewHolder holder, final YsNewsMoreBean.Obj obj, int position) {

        holder.setText(R.id.news_type,mtitle);
        holder.setText(R.id.news_title,obj.getPortalNewsTitle());
        holder.setText(R.id.time_tv,obj.getPortalNewsApplyTime());

        ImageView imageView = holder.getView(R.id.news_img);
        Glide.with(mContext).load(UrlRes.HOME3_URL + obj.getPortalNewsTitleImg()).into(imageView);
    }

    }


