package io.cordova.zhihuiyouzhuan.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.net.URLEncoder;
import java.util.Calendar;

import butterknife.BindView;
import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.VerCodeBean;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity2;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import io.cordova.zhihuiyouzhuan.bean.UserMsgBean;
import io.cordova.zhihuiyouzhuan.utils.JsonUtil;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;

/**
 * Created by Administrator on 2019/6/19 0019.
 * 人脸识别验证码校验页面
 *
 *
 *
 *
 */

public class CodeBind2Activity extends BaseActivity2 implements View.OnClickListener {


    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.layout_back)
    RelativeLayout layout_back;

    @BindView(R.id.tv_phone)
    TextView tv_phone;

    @BindView(R.id.btn_login)
    Button btn_login;

    @BindView(R.id.et_content)
    EditText et_content;

    @BindView(R.id.webView)
    WebView webView;

    String userId;
    String phone;
    String username;
    UserMsgBean userMsgBean;
    private int type = 8;
    @Override
    protected int getResourceId() {
        return R.layout.activity_codebind2;
    }

    @Override
    protected void initView() {
        super.initView();
        tvTitle.setText("安全验证");
        userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        phone = getIntent().getStringExtra("phone");
//        phone = (String) SPUtils.get(MyApp.getInstance(), "phone", "");
        username = (String) SPUtils.get(MyApp.getInstance(), "username", "");
        getCode(username,phone);
        String mobile = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        tv_phone.setText("设置主设备需要进行短信验证,已向("+mobile+")发送验证码");
        layout_back.setOnClickListener(this);
        btn_login.setOnClickListener(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_back:
                finish();
                break;
            case R.id.btn_login:
                String content = et_content.getText().toString().trim();
                if(content.equals("")){
                    ToastUtils.showToast(CodeBind2Activity.this,"请输入验证码!");
                    return;
                }

                checkCode(content);
                break;

        }
    }

    private void checkCode(String content) {
        try {
            //final String username = URLEncoder.encode(AesEncryptUtile.encrypt(content, key), "UTF-8");
            ViewUtils.createLoadingDialog(CodeBind2Activity.this);
            final String type0 = URLEncoder.encode(AesEncryptUtile.encrypt("8", AesEncryptUtile.key), "UTF-8");
            String contact = URLEncoder.encode(AesEncryptUtile.encrypt(phone, AesEncryptUtile.key), "UTF-8");
            final String vcode = URLEncoder.encode(AesEncryptUtile.encrypt(content, AesEncryptUtile.key), "UTF-8");
            String personName = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
            //String userName = AesEncryptUtile.decrypt(username,key) ;
            String userid = AesEncryptUtile.encrypt(personName + "_" + Calendar.getInstance().getTimeInMillis(), AesEncryptUtile.key);
            OkGo.<String>get(UrlRes.HOME2_URL + UrlRes.verificationUrl)
                    .params("openid","123456")
                    .params("memberId",userid)
                    .params("type",type0)
                    .params("verificationCode",vcode)
                    .execute(new StringCallback() {

                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("校验验证吗",response.body());
                            ViewUtils.cancelLoadingDialog();
                            VerCodeBean verCodeBean = JsonUtil.parseJson(response.body(),VerCodeBean.class);
                            boolean success = verCodeBean.getSuccess();
                            if(success){
                                Intent intent = getIntent();
                                setResult(1,intent);
                                finish();

                            }else {
                                ToastUtils.showToast(CodeBind2Activity.this,verCodeBean.getMsg());
                            }

                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            ViewUtils.cancelLoadingDialog();

                        }
                    });

        } catch (Exception e) {

        }
    }

    private void getCode(String username, String phone) {
       /* dlm 用户账号
        type 验证码类型(0:手机绑定,1:邮箱绑定,2:手机找回密码,3:邮箱找回密码,4:手机号登录,5:手机解绑,6:邮箱解绑)
        contact 联系方式(手机号或邮箱)
                openId*/
        try {
            //String username = URLEncoder.encode(AesEncryptUtile.encrypt(content, key), "UTF-8");
            String type0 = URLEncoder.encode(AesEncryptUtile.encrypt(type+"", AesEncryptUtile.key), "UTF-8");
            String contact = URLEncoder.encode(AesEncryptUtile.encrypt(phone, AesEncryptUtile.key), "UTF-8");
            String personName = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
            String username1 = AesEncryptUtile.encrypt(personName + "_" + Calendar.getInstance().getTimeInMillis(), AesEncryptUtile.key);
            OkGo.<String>get(UrlRes.HOME2_URL + UrlRes.sendVerificationUrl)
                    .params("openId","123456")
                    .params("dlm", username1)
                    .params("type",type0)
                    .params("contact",contact)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("sdsd",response.body());

                            VerCodeBean verCodeBean = JsonUtil.parseJson(response.body(),VerCodeBean.class);
                            boolean success = verCodeBean.getSuccess();
                            if(success){
                                int frequency = verCodeBean.getAttributes().getFrequency();
//                                CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(tv_phone, frequency*60*1000, 1000);
//                                mCountDownTimerUtils.start();
                            }else {
                                ToastUtils.showToast(CodeBind2Activity.this,verCodeBean.getMsg());
                            }

                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);

                        }
                    });

        } catch (Exception e) {

        }

    }
}
