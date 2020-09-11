package io.cordova.zhihuiyouzhuan.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.OnClick;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.CountBean;
import io.cordova.zhihuiyouzhuan.bean.OAMessageBean;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity2;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import io.cordova.zhihuiyouzhuan.web.BaseWebActivity4;

/**
 * Created by Administrator on 2018/11/22 0022.
 */

public class MyToDoMsg2Activity extends BaseActivity2 {
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.tv_msg_num)
    TextView tv_msg_num;

    @BindView(R.id.db_msg_num)
    TextView db_msg_num;

    @BindView(R.id.dy_msg_num)
    TextView dy_msg_num;

    @BindView(R.id.dy_msg_present)
    TextView dy_msg_present;

    @BindView(R.id.yb_msg_num)
    TextView yb_msg_num;

    @BindView(R.id.yy_msg_num)
    TextView yy_msg_num;

    @BindView(R.id.db_msg_present)
    TextView db_msg_present;

    @BindView(R.id.yb_msg_present)
    TextView yb_msg_present;

    @BindView(R.id.yy_msg_present)
    TextView yy_msg_present;

    @BindView(R.id.my_msg_present)
    TextView my_msg_present;

    @BindView(R.id.system_msg_present)
    TextView system_msg_present;

    @BindView(R.id.oa_notice_present)
    TextView oa_notice_present;

    @BindView(R.id.oa_email_present)
    TextView oa_email_present;

    @BindView(R.id.oa_email_num)
    TextView oa_email_num;
    @BindView(R.id.ll_db_msg)
    LinearLayout dbMsg;
    @BindView(R.id.ll_oa_notice)
    LinearLayout oaNotice;
    @BindView(R.id.ll_oa_email)
    LinearLayout oaEmail;

    @BindView(R.id.oa_notice_num)
    TextView oaNoticeTv;
    @Override
    protected int getResourceId() {
        return R.layout.activity_my_to_do_msg;
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle.setText("消息中心");

//        netWorkYyMsg();
    }

     String dburl;
     String emailurl;
     String noticeurl;

    @OnClick({R.id.ll_system_msg, R.id.ll_db_msg, R.id.ll_dy_msg, R.id.ll_yb_msg, R.id.ll_yy_msg, R.id.ll_my_msg,R.id.ll_oa_notice,R.id.ll_oa_email})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_system_msg://系统消息
                intent = new Intent(MyApp.getInstance(), SystemMsgActivity.class);
                intent.putExtra("msgType","系统消息");
                startActivity(intent);
                break;
            case R.id.ll_db_msg://d待办消息
                intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",dburl);
                startActivity(intent);
                break;
            case R.id.ll_dy_msg://待阅消息
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","dy");
                intent.putExtra("msgType","待阅消息");
                startActivity(intent);
                break;
            case R.id.ll_yb_msg://已办消息
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","yb");
                intent.putExtra("msgType","已办消息");
                startActivity(intent);
                break;
            case R.id.ll_yy_msg://已阅消息
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","yy");
                intent.putExtra("msgType","已阅消息");
                startActivity(intent);
                break;
            case R.id.ll_my_msg://我的消息
                intent = new Intent(MyApp.getInstance(), MyShenqingActivity.class);
                intent.putExtra("type","sq");
                intent.putExtra("msgType","我的申请");
                startActivity(intent);
                break;
            case R.id.ll_oa_notice:
                intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",noticeurl);
                startActivity(intent);

                break;
            case R.id.ll_oa_email:
                intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",emailurl);
                startActivity(intent);
                break;
        }
    }

   /** 获取消息数量*/

    private void netWorkSystemMsg() {
        String userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_countUnreadMessagesForCurrentUser)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
                        Log.e("s系统消息数量",countBean.getObj());
                        //yy_msg_num.setText(countBean.getCount()+"");
                        if(countBean.getObj().equals("0")){
                            system_msg_present.setText("您还有未读的系统消息");
                            tv_msg_num.setText(countBean.getObj()+"");
                        }else {
                            tv_msg_num.setText(countBean.getObj()+"");
                            system_msg_present.setText("您还有"+countBean.getObj()+"条未读系统消息");
                        }

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("s",response.toString());
                    }
                });
    }


    private void netWorkDbMsg() {
        netWorkOAToDoMsg();
    }

    private void netWorkDyMsg() {
        netWorkOAToDoMsg1();
    }



    private void netWorkYbMsg() {
        netWorkOAToDoMsg2();
    }



    private void netWorkYyMsg() {
        netWorkOAToDoMsg3();
    }

    private void netWorkSqMsg() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "sq")
                .params("workType", "worksq")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s申请",response.toString());
                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
                        //yy_msg_num.setText(countBean.getCount()+"");
                        my_msg_present.setText("已申请"+countBean.getCount()+"条");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });

    }
    private void netWorkOAToDoMsg2() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "yb")
                .params("workType", "workdb")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s已办",response.toString());
                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
                        yb_msg_num.setText(countBean.getCount()+"");
                        yb_msg_present.setText("OA已办消息"+countBean.getCount()+"条");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                    }
                });
    }

    private void netWorkOAToDoMsg3() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "yy")
                .params("workType", "workdb")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s已阅",response.toString());
                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
                        yy_msg_num.setText(countBean.getCount()+"");
                        yy_msg_present.setText("OA已阅消息"+countBean.getCount()+"条");
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });



    }


    private void netWorkOAToDoMsg1() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "dy")
                .params("workType", "workdb")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待阅",response.toString());
                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
                        dy_msg_num.setText(countBean.getCount()+"");
                        if(countBean.getCount() == 0){
                            dy_msg_present.setText("您还没有OA待阅消息");
                        }else {
                            dy_msg_present.setText("您还有OA待阅消息"+countBean.getCount()+"条");
                        }

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });


    }
    private void netWorkOAToDoMsg() {
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
//                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
//                .params("type", "db")
//                .params("workType", "workdb")
//                .execute(new StringCallback() {
//                    @Override
//                    public void onSuccess(Response<String> response) {
//                        Log.e("s待办",response.toString());
//                        CountBean countBean = JSON.parseObject(response.body(), CountBean.class);
//                        db_msg_num.setText(countBean.getCount()+"");
//                        if(countBean.getCount() == 0){
//                            db_msg_present.setText("您还有未处理OA待办消息");
//                        }else {
//                            db_msg_present.setText("您还有OA待办消息"+countBean.getCount()+"条");
//                        }
//                    }
//
//                    @Override
//                    public void onError(Response<String> response) {
//                        super.onError(response);
//
//                    }
//                });

        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","1")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待办",response.body());
                        OAMessageBean oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
                        db_msg_num.setText(oaMessageBean.getObj().getCount());
                        if(oaMessageBean.getObj().getCount().equals("0")){
                            db_msg_present.setText("您还没有未处理OA待办消息");
                        }else {
                            db_msg_present.setText("您还有OA待办消息"+oaMessageBean.getObj().getCount()+"条");
                        }
                        dburl = oaMessageBean.getObj().getLinkUrl();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }


    private void netWorkOANotice() {


        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("OA公告",response.body());
                        OAMessageBean oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
                        if(oaMessageBean.getObj().getCount().equals("0")){
                            oa_notice_present.setText("您还没有未读OA公告");
                        }else {
                            oa_notice_present.setText("您还有OA公告"+oaMessageBean.getObj().getCount()+"条");

                        }
                        oaNoticeTv.setText(oaMessageBean.getObj().getCount() );
                        noticeurl = oaMessageBean.getObj().getLinkUrl();
                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }

    private void netWorkOAEmail() {


        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getOaworkflow)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type","3")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s待办",response.body());
                        OAMessageBean oaMessageBean = JSON.parseObject(response.body(),OAMessageBean.class);
                        oa_email_num.setText(oaMessageBean.getObj().getCount());
                        if(oaMessageBean.getObj().getCount().equals("0")){
                            oa_email_present.setText("您还没有未读OA邮件");
                        }else {
                            oa_email_present.setText("您还有OA未读邮件"+oaMessageBean.getObj().getCount()+"条");
                        }
                        emailurl  = oaMessageBean.getObj().getLinkUrl();

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
        netWorkSystemMsg();

//        netWorkDyMsg();
//        netWorkYbMsg();
//        netWorkYyMsg();
//        netWorkSqMsg();

        if(SPUtils.get(getApplicationContext(),"rolecodes","").equals("student")){
            dbMsg.setVisibility(View.GONE);
            oaEmail.setVisibility(View.GONE);
            oaNotice.setVisibility(View.GONE);
        }else{
            netWorkDbMsg();
            netWorkOANotice();
            netWorkOAEmail();
        }

    }
}
