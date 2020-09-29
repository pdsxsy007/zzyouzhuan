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
import io.cordova.zhihuiyouzhuan.bean.YsAppBean;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Cuilei on 2020/9/22.
 */

public class YsMoreAppAdapter extends CommonAdapter<YsAppBean.Obj.Apps> {
    Context mcontext;
    public YsMoreAppAdapter(Context context, int layoutId, List<YsAppBean.Obj.Apps> datas) {
        super(context, layoutId, datas);
        mcontext = context;
    }

    @Override
    protected void convert(ViewHolder holder, YsAppBean.Obj.Apps apps, int position) {

        holder.setText(R.id.app_tv,apps.getAppName());
        ImageView appImg = holder.getView(R.id.app_img);

            Glide.with(mcontext).load(UrlRes.HOME3_URL + apps.getAppImages()).into(appImg);




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent(mContext, BaseWebActivity4.class);
                    intent.putExtra("appUrl",apps.getAppUrl());
                    intent.putExtra("appName",apps.getAppName());
                    intent.putExtra("appId",apps.getAppId()+"");
                    mContext.startActivity(intent);


            }
        });
    }
}
