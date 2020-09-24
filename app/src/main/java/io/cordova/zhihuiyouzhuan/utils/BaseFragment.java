package io.cordova.zhihuiyouzhuan.utils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gyf.immersionbar.ImmersionBar;
import com.lzy.okgo.OkGo;

import butterknife.ButterKnife;
import io.cordova.zhihuiyouzhuan.R;

/**
 * Fragment的基类
 * 1.规范代码结构
 * 2.精简代码
 *
 * @author wangdh
 */
public abstract class BaseFragment extends Fragment {
    protected View statusBarView;
    protected Toolbar toolbar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), getLayoutResID(), null);
        ButterKnife.bind(this, view);

        initView(view);
        initData();
        initListener();
        statusBarView = view.findViewById(R.id.status_bar_view);
        toolbar = view.findViewById(R.id.toolbar);
        fitsLayoutOverlap();
        /*if (!netState.isConnect(getActivity()) ){
            ToastUtils.showToast(getActivity(),"网络异常!");
            netStateType = 1;
        }*/
        return view;
    }

    /**
     * 获取Activity显示的布局：
     *
     * @return：布局id
     */
    public abstract int getLayoutResID();

    /**
     * 初始化View
     */
    public void initView(View view){

    }

    /**
     * 初始化监听：点击监听、设置适配器、设置条目点击监听
     */
    public void initListener() {

    }

    /**
     * 初始化数据
     */
    public void initData() {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //根据 Tag 取消请求
        OkGo.getInstance().cancelTag(this);
        //LocationUtils.getInstance(MyApp.getInstance()).removeLocationUpdatesListener();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    private void fitsLayoutOverlap() {
        if (statusBarView != null) {
            ImmersionBar.setStatusBarView(this, statusBarView);
        } else {
            ImmersionBar.setTitleBar(this, toolbar);
        }
    }

}
