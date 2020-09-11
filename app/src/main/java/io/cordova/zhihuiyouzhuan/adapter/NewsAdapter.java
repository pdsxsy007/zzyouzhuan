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

public class NewsAdapter extends CommonAdapter<ItemNewsBean2> {
    Context mContext;
    public NewsAdapter(Context context, int layoutId, List<ItemNewsBean2> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final ItemNewsBean2 s, int position) {

        holder.setText(R.id.tv_left,s.getNewsTitle());
        holder.setText(R.id.tv_right,s.getNewsDate());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",s.getNewsHref());
                //intent.putExtra("appId","");
                intent.putExtra("appName",s.getNewsTitle());
                mContext.startActivity(intent);
            }
        });




    }

}
