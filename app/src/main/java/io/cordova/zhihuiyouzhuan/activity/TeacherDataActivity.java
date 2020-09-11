package io.cordova.zhihuiyouzhuan.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;

import io.cordova.zhihuiyouzhuan.R;
import io.cordova.zhihuiyouzhuan.UrlRes;
import io.cordova.zhihuiyouzhuan.bean.CurrencyBean;
import io.cordova.zhihuiyouzhuan.bean.GetServiceImgBean;
import io.cordova.zhihuiyouzhuan.bean.TeacherBean2;
import io.cordova.zhihuiyouzhuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyouzhuan.utils.BaseActivity2;
import io.cordova.zhihuiyouzhuan.utils.MyApp;
import io.cordova.zhihuiyouzhuan.utils.SPUtils;
import io.cordova.zhihuiyouzhuan.utils.T;
import io.cordova.zhihuiyouzhuan.utils.ToastUtils;
import io.cordova.zhihuiyouzhuan.utils.ViewUtils;
import io.cordova.zhihuiyouzhuan.widget.XCRoundImageView;
import io.reactivex.functions.Consumer;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.zhihu.matisse.internal.utils.PathUtils.getPath;


/**
 * Created by Administrator on 2018/11/21 0021.
 */

public class TeacherDataActivity extends BaseActivity2 {

    @BindView(R.id.iv_user_head)
    XCRoundImageView ivUserHead;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    //    @BindView(R.id.tv_company)
//    TextView tvCompany;
    @BindView(R.id.tv_student_number)
    TextView tvStudentNumber;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_grade)
    TextView tvGender;
    @BindView(R.id.tv_nation)
    TextView tvNation;
    @BindView(R.id.tv_department)
    TextView tvDepartment;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_native_place)
    TextView tvNativePlace;
    @BindView(R.id.tv_mobile)
    TextView tvMobile;
    @BindView(R.id.zzmm_tv)
    TextView zzmmTv;//政治面貌
    @BindView(R.id.idcard_tv)
    TextView idcardTv;//身份证号
    @BindView(R.id.job_status)
    TextView jobStatus;//岗位状态
    @BindView(R.id.jszc_tv)
    TextView jszcTv;//技术职称
    @BindView(R.id.work_time)
    TextView workTime;//工作年份
    @BindView(R.id.zgxw_tv)
    TextView zgxwTv;//最高学位
    @BindView(R.id.zgxl_tv)
    TextView zgxlTv;//最高学历

//    @BindView(R.id.rv_user_data)
//    RecyclerView rvUserData;

    @BindView(R.id.tv_type)
    TextView tv_type;


    @BindView(R.id.tv_app_setting)
            ImageView tv_app_setting;

    String mMobile;
    boolean allowedScan = false;
    @Override
    protected int getResourceId() {
        return R.layout.activity_teacher_data;
    }

    @OnClick({R.id.iv_user_head,R.id.ll_my_mobile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_user_head:
                selectImg();
                //打开相册选择图片
                //setPermission();
               /* if (allowedScan){
                  selectImg();
                }else {
                    Toast.makeText(this,"请允许权限后尝试",Toast.LENGTH_SHORT).show();
                    setPermission();
                }*/
                break;
            case R.id.ll_my_mobile:
                //跳转至修改手机页面
//                Intent intent = new Intent(MyApp.getInstance(),MyDataChangesActivity.class);
//                if (!StringUtils.isEmpty(mMobile)){
//                    intent.putExtra("mMobile",mMobile);
//                }
//                startActivity(intent);
                break;
        }
    }


    @Override
    protected void initView() {
        super.initView();
        tv_app_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        netWorkUserMsg();
        netGetUserHead();
//        netWorkUserMsg2();
    }


    private void netGetUserHead() {
//        ?memberId=admin&pwd=d632eeeb1548643667060e18656e0112
//                    String pwd = URLEncoder.encode(userMsgBean.getObj().getModules().getMemberPwd(),"UTF-8");
//        String ingUrl =  UrlRes.HOME2_URL+"/authentication/public/getHeadImg?memberId=" +userMsgBean.getObj().getModules().getMemberUsername()+"&pwd="+pwd;//+userMsgBean.getObj().getModules().getMemberUsername()+"&pwd="+pwd;
        String ingUrl = (String) SPUtils.get(TeacherDataActivity.this,"handphoto","");
        Glide.with(this)
                .load(ingUrl)
                .asBitmap()
                //.transform(new CircleCrop(getApplicationContext()))
                .error(R.mipmap.tabbar_user_pre)
                .signature(new StringSignature(UUID.randomUUID().toString()))
                .into(ivUserHead);
    }


    TeacherBean2 userMsgBean;
    private void netWorkUserMsg() {
        ViewUtils.createLoadingDialog(this);
        String name = (String) SPUtils.get(MyApp.getInstance(),"personName","");
        //http://192.168.30.5:8081/microapplication/swagger/index.html

        try {
            String userId = AesEncryptUtile.encrypt(name,"gilight@#1234567");
            OkGo.<String>post("http://mh.hntyxxh.com:18084/microapplication/api/v1/index/getStaffByStaffNumber")
//        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.User_Msg)
//                .params("userId", (String) SPUtils.get(MyApp.getInstance(),"userId",""))
                    .params("staffNumber", userId)

                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("当前数据",response.body());
                            userMsgBean = JSON.parseObject(response.body(), TeacherBean2.class);
                            ViewUtils.cancelLoadingDialog();
                            if (userMsgBean.isSuccess()) {
                                if(userMsgBean.getObj() != null){
                                    tvUserName.setText(userMsgBean.getObj().getStaffName());
                                    tvStudentNumber.setText(userMsgBean.getObj().getStaffNumber());
                                    tvName.setText(userMsgBean.getObj().getStaffName());
                                    tvDepartment.setText(userMsgBean.getObj().getStaffBelongUnit());
                                    if (userMsgBean.getObj().getStaffSex().equals("1")) {
                                        tvSex.setText("男");
                                    } else if(userMsgBean.getObj().getStaffSex().equals("2")){
                                        tvSex.setText("女");
                                    }else {
                                        tvSex.setText("");
                                    }



                                        tvNation.setText(userMsgBean.getObj().getStaffNational());

                                        tvMajor.setText(userMsgBean.getObj().getStaffCredentialsType());
                                        tvGender.setText(userMsgBean.getObj().getStaffLevel());
                                        tvClass.setText(userMsgBean.getObj().getStaffCurrentState());
                                        tvBirthday.setText(userMsgBean.getObj().getStaffBirthday());
                                        tvNativePlace.setText(userMsgBean.getObj().getStaffBirthSpace());
                                        tvMobile.setText(userMsgBean.getObj().getStaffCellphoneNumber());
                                        zzmmTv.setText(userMsgBean.getObj().getStaffPoliticalStatus());
                                        idcardTv.setText(userMsgBean.getObj().getStaffCredentialsId());
                                        jobStatus.setText(userMsgBean.getObj().getStaffPostState());
                                        workTime.setText(userMsgBean.getObj().getStaffWorkingYears());
                                        zgxlTv.setText(userMsgBean.getObj().getStaffAcademicQualifications());
                                        zgxwTv.setText(userMsgBean.getObj().getStaffAcademicDegree());


                                    mMobile = userMsgBean.getObj().getStaffCellphoneNumber();
                                    netGetUserHead();
                                }else {
                                    ToastUtils.showToast(TeacherDataActivity.this,"获取个人信息失败!");
                                    ViewUtils.cancelLoadingDialog();
                                }

                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            ViewUtils.cancelLoadingDialog();
                            T.showShort(MyApp.getInstance(), "没有数据哦，请稍后再试");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    private String img64Head;
    private String headPath;
    private static final int UP_Header = 55;//定义请求码常量
    private List<Uri> result;
    private List<String> path;
    private void selectImg() {
        dialogss();
    }

    private AlertDialog.Builder builder;
    private void dialogss() {



        final String items[] = {"打开相机", "打开相册"};
        builder = new AlertDialog.Builder(this);  //先得到构造器
        //builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //dialog.dismiss();
                //Toast.makeText(MActivity.this, items[which], Toast.LENGTH_SHORT).show();
                if (items[which].equals("打开相机")) {
                    cameraTask();
                   /* if(Build.VERSION.SDK_INT >= 23){
                        boolean hasPermissions = checkPermissions(MyDataActivity.this
                                , new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}
                                , REQUEST_CODE_PERMISSION_CAMERA, com.laojiang.imagepickers.R.string.dialog_imagepicker_permission_camera_message);
                        //有权限就直接拍照
                        if (hasPermissions){
                            testTakePhoto();
                        }
                    }else {
                        testTakePhoto();
                    }*/

                } else if (items[which].equals("打开相册")) {
                    getAlbum();
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
    private static final int RC_CAMERA_PERM = 123;
    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!

            testTakePhoto();

        } else {//没有相应权限，获取相机权限
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private Uri fileUri1;
    //打开系统相机
    private void testTakePhoto() {

        //指定相机拍照的存储路径
        File mPhotoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        //指定intent跳转到系统相机
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri1 = Uri.fromFile(mPhotoFile);
        //设置存储路径
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        try {
            //开启相机
            startActivityForResult(captureIntent, 5);
        } catch (Exception e) {
            Log.e("相机异常",e.getMessage());
            Toast.makeText(this, "跳转系统相机异常", Toast.LENGTH_SHORT).show();
        }
    }

    //打开相册
    private void getAlbum() {
        Intent getAlbumIntent = new Intent(Intent.ACTION_PICK);
        getAlbumIntent.setType("image/*");
        startActivityForResult(getAlbumIntent, 2);
    }


    public Uri geturi(Intent intent) {
        Uri uri = intent.getData();
        String type = intent.getType();
        if (uri.getScheme().equals("file") && (type.contains("image/"))) {
            String path = uri.getEncodedPath();
            if (path != null) {
                path = Uri.decode(path);
                ContentResolver cr =getContentResolver();
                StringBuffer buff = new StringBuffer();
                buff.append("(").append(MediaStore.Images.ImageColumns.DATA).append("=")
                        .append("'" + path + "'").append(")");
                Cursor cur = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        new String[]{MediaStore.Images.ImageColumns._ID},
                        buff.toString(), null, null);
                int index = 0;
                for (cur.moveToFirst(); !cur.isAfterLast(); cur.moveToNext()) {
                    index = cur.getColumnIndex(MediaStore.Images.ImageColumns._ID);
                    // set _id value
                    index = cur.getInt(index);
                }
                if (index == 0) {
                    // do nothing
                } else {
                    Uri uri_temp = Uri
                            .parse("content://media/external/images/media/"
                                    + index);
                    if (uri_temp != null) {
                        uri = uri_temp;
                    }
                }
            }
        }
        return uri;
    }
    private Uri uritempFile;
    //剪切图片
    protected void cutImage(Uri uri) {
        if (uri == null) {
            Log.i("alanjet", "The uri is not exist.");
        }
        //getRealFilePath(this,tempUri);
        Intent intent = new Intent("com.android.camera.action.CROP");
        //com.android.camera.action.CROP这个action是用来裁剪图片用的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            String url = getPath(this, uri);
            intent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
        }
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故只保存图片Uri，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //裁剪后的图片Uri路径，uritempFile为Uri类变量
        uritempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getPath() + "/" + "small.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(intent, 3);
    }

    public File saveBitmapFile2(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory() + ".jpg");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {//打开相册返回
                Uri albumUri = geturi(data);
                cutImage(albumUri);
            }
        }
        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {//拍照返回
                cutImage(fileUri1);
            }
        }
        if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                //将Uri图片转换为Bitmap
                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uritempFile));

                    File file = saveBitmapFile2(bitmap);

                    upImg(file);
                }catch (Exception e){

                }


            }
        }
    }


    GetServiceImgBean getServiceImgBean;
    //上传图片的到服务器
    private void upImg(File file) {
        File file1 = null;
        try {
            file1 = new File(new URI(uritempFile.toString()));
            OkGo.<String>post(UrlRes.HOME2_URL+ UrlRes.Upload_Img)
                    .tag(this)
                    .isMultipart(true)
                    .params( "file",file1)
                    .execute(new StringCallback(){
                        @Override
                        public void onSuccess(Response<String> response) {
                            //handleResponse(response);
                            Log.e("tag",response.body());
                            getServiceImgBean = JSON.parseObject(response.body(),GetServiceImgBean.class);
                            if (getServiceImgBean.isSuccess()){
                                gettImgUrl(getServiceImgBean.getObj());
                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.body());
                            T.showShort(getApplicationContext(),"找不到服务器了，请稍后再试");
                        }
                    });
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }


    }


    CurrencyBean currencyBean;//通用Bean
    private void gettImgUrl(final String obj) {
        String userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        String s = UrlRes.HOME2_URL + UrlRes.Get_Img_uri;

        OkGo.<String>post(UrlRes.HOME2_URL+ UrlRes.Get_Img_uri)
                .tag(this)
                //.params("imgageUrl",obj)
                //.params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("memberAesEncrypt",userId)
                .params("openIdScred","123456")
                .params("memberImage",obj)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("gettImgUrl",response.body());
                        currencyBean = JSON.parseObject(response.body(),CurrencyBean.class);
                        if (!obj.isEmpty()){

                            netGetUserHead();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("gettImgUrl",response.body());
                        T.showShort(getApplicationContext(),"找不到服务器了，请稍后再试");
                    }
                });
    }

    //获取服务器返回的url
    private void setPermission() {
        //同时请求多个权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e("用户已经同意该权限", permission.name + " is granted.");
                            //   Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            Log.e("用户拒绝了该权限", permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //   Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //   Log.d(TAG, permission.name + " is denied.");
                            Log.e("用户拒绝了该权限", permission.name + permission.name + " is denied.");
                        }
                    }
                });
    }

}
