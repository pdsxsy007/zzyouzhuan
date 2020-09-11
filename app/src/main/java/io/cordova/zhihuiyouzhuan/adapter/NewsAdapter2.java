package io.cordova.zhihuiyouzhuan.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.bean.HomeNewsBean;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class NewsAdapter2 extends CommonAdapter<HomeNewsBean.Obj> {
    Context mContext;
    public NewsAdapter2(Context context, int layoutId, List<HomeNewsBean.Obj> datas) {
        super(context, layoutId, datas);
        mContext = context;
    }

    @Override
    protected void convert(ViewHolder holder, final HomeNewsBean.Obj s, int position) {

        holder.setText(R.id.tv_left,s.getNewsTitle());
        holder.setText(R.id.tv_right,s.getNewsDate());

       /* holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",s.getNewsHref());
                //intent.putExtra("appId","");
                intent.putExtra("appName",s.getNewsTitle());
                mContext.startActivity(intent);
            }
        });*/




    }

}
