package com.bintutu.shop.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bintutu.shop.R;
import com.bintutu.shop.bean.BaseResponse;
import com.bintutu.shop.bean.DetailBean;
import com.bintutu.shop.bean.LeftBean;
import com.bintutu.shop.bean.LoginBean;
import com.bintutu.shop.bean.RightBean;
import com.bintutu.shop.bean.TAGBean;
import com.bintutu.shop.bean.UploadBean;
import com.bintutu.shop.okgo.DialogCallback;
import com.bintutu.shop.okgo.JsonCallback;
import com.bintutu.shop.okgo.LzyResponse;
import com.bintutu.shop.okgo.ServerModel;
import com.bintutu.shop.ui.BaseActivity;
import com.bintutu.shop.ui.adapter.DetailAdapter;
import com.bintutu.shop.ui.view.ImageDailog;
import com.bintutu.shop.ui.view.ImageTwoDailog;
import com.bintutu.shop.ui.view.LoginDailog;
import com.bintutu.shop.utils.AppConstant;
import com.bintutu.shop.utils.ConfigManager;
import com.bintutu.shop.utils.Constant;
import com.bintutu.shop.utils.DebugLog;
import com.bintutu.shop.utils.GlideUtil;
import com.bintutu.shop.utils.Utils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import okhttp3.MediaType;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class DetailActivity extends BaseActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.detai_scroll)
    HorizontalScrollView detaiScroll;
    @BindView(R.id.detail_lin_image)
    LinearLayout mLinImage;
    @BindView(R.id.detail_image_left)
    ImageView detailImageLeft;
    @BindView(R.id.detail_image_center)
    ImageView detailImageCenter;
    @BindView(R.id.detail_image_right)
    ImageView detailImageRight;
    @BindView(R.id.detail_image_left_two)
    ImageView detailImageLeftTwo;
    @BindView(R.id.detail_image_center_two)
    ImageView detailImageCenterTwo;
    @BindView(R.id.detail_image_right_two)
    ImageView detailImageRightTwo;
    @BindView(R.id.detail_but_left)
    Button detailButLeft;
    @BindView(R.id.detail_but_delete)
    Button detailButDelete;
    @BindView(R.id.detail_but_right)
    Button detailButRight;
    @BindView(R.id.ready_but_return)
    Button detailButReturn;
    @BindView(R.id.ready_but_home)
    Button detailButHome;
    @BindView(R.id.detail_but_upload)
    Button detailButUpload;
    @BindView(R.id.detail_image_footleft)
    ImageView detailImageFootleft;
    @BindView(R.id.detail_image_footright)
    ImageView detailImageFootright;
    @BindView(R.id.detail_image_plantarleft)
    ImageView detailImagePlantarleft;
    @BindView(R.id.detail_image_plantarright)
    ImageView detailImagePlantarright;
    @BindView(R.id.data_lin_addtag)
    LinearLayout dataLinAddtag;
    @BindView(R.id.detail_edit_remark)
    EditText dataEditRemark;
    private List<DetailBean> DetailList = new ArrayList<>();
    private List<TAGBean> TaglList = new ArrayList<>();
    private LoginDailog loginDailog;

    private int[] ImageRes = {
            R.mipmap.leftfoot_internal,
            R.mipmap.leftfoot_surface,
            R.mipmap.leftfoot_outside,
            R.mipmap.rightfoot_internal,
            R.mipmap.rightfoot_surface,
            R.mipmap.rightfoot_outside};
    private Gson gson;
    private LeftBean leftBean;
    private RightBean rightBean;
    private String number = "0";
    private String loginnumber;
    private String loginphone;
    private String logincustomer_id;
    private String uploadid;
    private HashMap<View, Bitmap> map;
    private String imagefile;
    private List<TAGBean> tag;



    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
    }


    @Override
    protected void init() {
        gson = new Gson();
        //设置Tag
        ConfigManager.Device.setTag(1);

        Intent intent = getIntent();
        number = intent.getStringExtra(Constant.ItentKey1);
        //
        getLeft();
        //
        loginDailog = new LoginDailog(this);
        //初始化Recyclerview
        showRecyclerview();
        //加载四张图片
        LoadingImage();
        try {
            imagefile = Environment.getExternalStorageDirectory().getCanonicalPath() + "/Bintutu";
        } catch (IOException e) {
            e.printStackTrace();
        }



        SetImage();

        detailButLeft.setEnabled(false);
        detailButRight.setEnabled(true);
        detaiScroll.scrollTo(200, 0);

    }




    @Override
    protected void setListener() {
        detailButDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                map.clear();
                SetImage();
                TaglList.clear();
            }
        });

        detailButReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.Foot.setCustomer_id("");
                ConfigManager.Foot.setCustomer_phone("");
                ConfigManager.Foot.setIdid("");
                ConfigManager.Foot.setchoosed_color_id("");
                ConfigManager.Foot.setchoosed_exclusive_id("");
                ConfigManager.Foot.setchoosed_sole_accessory_id("");
                ConfigManager.Foot.setchoosed_sole_material_id("");
                finish();
            }
        });
        detailButHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConfigManager.Foot.setCustomer_id("");
                ConfigManager.Foot.setCustomer_phone("");
                ConfigManager.Foot.setIdid("");
                ConfigManager.Foot.setchoosed_color_id("");
                ConfigManager.Foot.setchoosed_exclusive_id("");
                ConfigManager.Foot.setchoosed_sole_accessory_id("");
                ConfigManager.Foot.setchoosed_sole_material_id("");
                startActivity(new Intent(DetailActivity.this, MainActivity.class));
                finish();
            }
        });

        detailButLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailButLeft.setEnabled(false);
                detailButRight.setEnabled(true);
                detailImageLeft.setVisibility(View.VISIBLE);
                detailImageCenter.setVisibility(View.VISIBLE);
                detailImageRight.setVisibility(View.VISIBLE);
                detailImageLeftTwo.setVisibility(View.GONE);
                detailImageCenterTwo.setVisibility(View.GONE);
                detailImageRightTwo.setVisibility(View.GONE);
            }
        });
        detailButRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detailButLeft.setEnabled(true);
                detailButRight.setEnabled(false);
                detailImageLeft.setVisibility(View.GONE);
                detailImageCenter.setVisibility(View.GONE);
                detailImageRight.setVisibility(View.GONE);
                detailImageLeftTwo.setVisibility(View.VISIBLE);
                detailImageCenterTwo.setVisibility(View.VISIBLE);
                detailImageRightTwo.setVisibility(View.VISIBLE);

            }
        });

        detailImageLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("左脚内侧",detailImageLeft, map.get(detailImageLeft));
            }
        });
        detailImageCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("左脚脚面", detailImageCenter, map.get(detailImageCenter));
            }
        });
        detailImageRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("左脚外侧", detailImageRight, map.get(detailImageRight));
            }
        });
        detailImageLeftTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("右脚内侧", detailImageLeftTwo, map.get(detailImageLeftTwo));
            }
        });
        detailImageCenterTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("右脚脚面", detailImageCenterTwo, map.get(detailImageCenterTwo));
            }
        });
        detailImageRightTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSpaceImage("右脚外侧", detailImageRightTwo, map.get(detailImageRightTwo));
            }
        });


        detailButUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ConfigManager.Foot.getCustomer_id()!=null&&!ConfigManager.Foot.getCustomer_id().equals("")){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date date = new Date(System.currentTimeMillis());
                    loginnumber =simpleDateFormat.format(date);
                    loginphone = ConfigManager.Foot.getCustomer_phone();
                    logincustomer_id = ConfigManager.Foot.getCustomer_id();
                    upload(loginnumber,loginphone,logincustomer_id);
                }else {
                    loginDailog.show();
                }

            }
        });

        loginDailog.seLogintListener(new LoginDailog.OnLoginClickListener() {
            @Override
            public void Data(String number, String phone, String customer_id) {
                loginnumber = number;
                loginphone = phone;
                ConfigManager.Foot.setCustomer_id(customer_id);
                ConfigManager.Foot.setCustomer_phone(phone);
                logincustomer_id = customer_id;
                upload(loginnumber,loginphone,logincustomer_id);
            }
        });

    }

    private void showSpaceImage(String name, ImageView view, Bitmap imageRe) {

        ImageTwoDailog imageTwoDailog = new ImageTwoDailog(this);
        imageTwoDailog.show();
        imageTwoDailog.setImage(name,view, imageRe);
      /*  ImageDailog imageDailog = new ImageDailog(this);
        imageDailog.show();
        imageDailog.setImage(name,view, imageRe);
        imageDailog.setImageClickListener(new ImageDailog.OnImageClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onSetData(ImageView view, Bitmap viewBitmap, TAGBean tagBean) {
                map.put(view, viewBitmap);
                view.setBackground(new BitmapDrawable(getResources(), viewBitmap));

                if(!TaglList.contains(tagBean)) {
                    TaglList.add(tagBean);
                }
                addTag();
            }
        });*/

    }

    private void showRecyclerview() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerview.setLayoutManager(linearLayoutManager);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.setItemAnimator(new DefaultItemAnimator());
    }


    public void getLeft() {
        OkGo.<BaseResponse<String>>get(AppConstant.LEFT_JSON(number))
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        DebugLog.e("......" + response.body());
                        getRight();
                        String leftJson = String.valueOf(response.body());
                        leftBean = gson.fromJson(leftJson, LeftBean.class);

                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                    }
                });
    }

    private void getRight() {
        OkGo.<BaseResponse<String>>get(AppConstant.RIGHT_JSON(number))
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        DebugLog.e("......" + response.body());
                        String rightJson = String.valueOf(response.body());
                        rightBean = gson.fromJson(rightJson, RightBean.class);
                        getData();
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                    }
                });
    }

    public void getData() {

        DetailList.add(new DetailBean(getResources().getString(R.string.FootLen), 1, leftBean.get_1_FootLen(), rightBean.get_1_FootLen()));
        DetailList.add(new DetailBean(getResources().getString(R.string.ZhiWei), 2, leftBean.get_2_ZhiWei(), rightBean.get_2_ZhiWei()));
        DetailList.add(new DetailBean(getResources().getString(R.string.FuWei), 3, leftBean.get_3_FuWei(), rightBean.get_3_FuWei()));
        DetailList.add(new DetailBean(getResources().getString(R.string.DouWei), 4, leftBean.get_4_DouWei(), rightBean.get_4_DouWei()));
        DetailList.add(new DetailBean(getResources().getString(R.string.JiaoWanWei), 5, leftBean.get_5_JiaoWanWei(), rightBean.get_5_JiaoWanWei()));
        DetailList.add(new DetailBean(getResources().getString(R.string.JiaoZhiZhou), 6, leftBean.get_6_JiaoZhiZhou(), rightBean.get_6_JiaoZhiZhou()));
        DetailList.add(new DetailBean(getResources().getString(R.string.WaiHuaiXiaGao), 7, leftBean.get_7_WaiHuaiXiaGao(), rightBean.get_7_WaiHuaiXiaGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.HouGenTuGao), 8, leftBean.get_8_HouGenTuGao(), rightBean.get_8_HouGenTuGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.ZhouShangGao), 9, leftBean.get_9_ZhouShangGao(), rightBean.get_9_ZhouShangGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.FuWeiGao), 10, leftBean.get_10_FuWeiGao(), rightBean.get_10_FuWeiGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.ZhiZhiGao), 11, leftBean.get_11_1ZhiZhiGao(), rightBean.get_11_1ZhiZhiGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.DaMoZhiGao), 12, leftBean.get_12_DaMoZhiGao(), rightBean.get_12_DaMoZhiGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.JiaoWanGao), 13, leftBean.get_13_JiaoWanGao(), rightBean.get_13_JiaoWanGao()));
        DetailList.add(new DetailBean(getResources().getString(R.string.JiaoZhiKuan), 14, leftBean.get_14_JiaoZhiKuan(), rightBean.get_14_JiaoZhiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.ZhiWeiKuan), 15, leftBean.get_15_ZhiWeiKuan(), rightBean.get_15_ZhiWeiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.DiBanKuan), 16, leftBean.get_16_DiBanKuan(), rightBean.get_16_DiBanKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.MuZhiLiKuan), 17, leftBean.get_17_MuZhiLiKuan(), rightBean.get_17_MuZhiLiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.XiaoZhiWaiKuan), 18, leftBean.get_18_XiaoZhiWaiKuan(), rightBean.get_18_XiaoZhiWaiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.lZhizhiLiKuan), 19, leftBean.get_19_1ZhizhiLiKuan(), rightBean.get_19_1ZhizhiLiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.SZhizhiLiKuan), 20, leftBean.get_20_5ZhizhiLiKuan(), rightBean.get_20_5ZhizhiLiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.YaoWoWaiKuan), 21, leftBean.get_21_YaoWoWaiKuan(), rightBean.get_21_YaoWoWaiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.ZhongXinDiKuan), 22, leftBean.get_22_ZhongXinDiKuan(), rightBean.get_22_ZhongXinDiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.JiaoHuaiNeiKuan), 23, leftBean.get_23_JiaoHuaiNeiKuan(), rightBean.get_23_JiaoHuaiNeiKuan()));
        DetailList.add(new DetailBean(getResources().getString(R.string.MuZhiWaiTuChang), 24, leftBean.get_24_MuZhiWaiTuChang(), rightBean.get_24_MuZhiWaiTuChang()));
        DetailList.add(new DetailBean(getResources().getString(R.string.XiaoZhiDuanChang), 25, leftBean.get_25_XiaoZhiDuanChang(), rightBean.get_25_XiaoZhiDuanChang()));
        DetailList.add(new DetailBean(getResources().getString(R.string.XiaoZhiWaiTuChang), 26, leftBean.get_26_XiaoZhiWaiTuChang(), rightBean.get_26_XiaoZhiWaiTuChang()));
        DetailList.add(new DetailBean(getResources().getString(R.string.lZhiZhiChang), 27, leftBean.get_27_1ZhiZhiChang(), rightBean.get_27_1ZhiZhiChang()));
        DetailList.add(new DetailBean(getResources().getString(R.string.SZhiZhiChang), 28, leftBean.get_28_5ZhiZhiChang(), rightBean.get_28_5ZhiZhiChang()));
        DetailList.add(new DetailBean(getResources().getString(R.string.FuGuChang), 29, leftBean.get_29_FuGuChang(), rightBean.get_29_FuGuChang()));

        DetailAdapter detailAdapter = new DetailAdapter(DetailList);
        mRecyclerview.setAdapter(detailAdapter);
    }


    private void upload(String number, String phone, String customer_id) {

        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("left", leftBean);
        map.put("right", rightBean);
        String detailData = gson.toJson(map);

        sortListTag();

        String foot_remark = gson.toJson(tag);



        final String remark = dataEditRemark.getText().toString().trim();
        //上传数据
        OkGo.<BaseResponse<String>>post(AppConstant.NEW_DATA)
                .params("name", number)//自定的一个名字
                .params("customer_id", customer_id)//手机号验证码请求成功返回的id
                .params("customer_phone", phone)//手机号
                .params("shop_id", ConfigManager.Device.getShopID())//商铺号
                .params("device_id", ConfigManager.Device.getEquipmentID())//判断设备在不在线的返回的数据
                .params("foot_remark", foot_remark)//数据界面上面需要标记的图片json
                .params("detail_data", detailData)//left.json+right.json
                .params("remark", remark+"")//备注
                .execute(new JsonCallback<BaseResponse<String>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<String>> response) {
                        String data = String.valueOf(response.body());
                        UploadBean uploadBean = gson.fromJson(data, UploadBean.class);
                        if (uploadBean != null & uploadBean.getCode() == 0) {
                            uploadid = uploadBean.getResult().getId();
                            DebugLog.e(uploadid+".................customerid");
                            downloadZip();
                        }
                    }

                    @Override
                    public void onError(Response<BaseResponse<String>> response) {
                    }
                });



    }




    private void downloadZip() {
        OkGo.<File>get(AppConstant.DATA_ZIP(number))
                .tag(this)
                .execute(new FileCallback(imagefile,"data.tgz") {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = (File) response.body();
                        Log.d("checkUpdateReceiver", file+"文件下载完成");
                        Log.d("checkUpdateReceiver", "文件下载完成");
                        UploadZip(file);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                //上传zip
                                //上传图片
                                GetImage();

                            }
                        }).start();
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        Log.d("checkUpdateReceiver" , "文件下载中");
                    }

                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        Log.d("checkUpdateReceiver" , "开始下载");
                    }

                    @Override
                    public void onError(Response<File> response) {
                        super.onError(response);
                    }
                });
    }

    private void UploadZip(File file) {
        File files = new File(imagefile+"/data.tgz");
        //上传图片
        OkGo.<LzyResponse<ServerModel>>post(AppConstant.UPLOAD_ZIP)

               .params("id",uploadid)
                .params("file",files,"data.tgz", MediaType.parse("application/x-tar"))
                .execute(new DialogCallback<LzyResponse<ServerModel>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<ServerModel>> response) {
                        Intent intent = new Intent(DetailActivity.this, UploadSucessActivity.class);
                        intent.putExtra(Constant.ItentKey1, uploadid);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onError(Response<LzyResponse<ServerModel>> response) {

                    }
                });
    }

    MediaType Image = MediaType.parse("image/png; charset=utf-8");

    private void GetImage() {


        List<String> imageliat = Utils.getAllFiles(imagefile,"jpg");

        UploadImage(imageliat);


    }

    private void UploadImage(List<String> imageliat) {
        for (String file :imageliat){

            //上传图片
            OkGo.<LzyResponse<ServerModel>>post(AppConstant.UPLOAD_IMAGE)

                    .params("id",uploadid)
                    .params("file",new File(file))
                    .execute(new JsonCallback<LzyResponse<ServerModel>>() {
                        @Override
                        public void onSuccess(Response<LzyResponse<ServerModel>> response) {

                        }

                        @Override
                        public void onError(Response<LzyResponse<ServerModel>> response) {

                        }
                    });

        }
    }


    public void addTag() {
        dataLinAddtag.setVisibility(View.VISIBLE);
        dataLinAddtag.removeAllViews();
        if (TaglList!=null&&TaglList.size()>0) {
            for (int i = 0; i < TaglList.size(); i++) {
                TAGBean tagBean = TaglList.get(i);
                for (int a = 0; a < tagBean.getDetailList().size(); a++) {
                    TAGBean.DetailListBean detailListBean = TaglList.get(i).getDetailList().get(a);
                    View view = LayoutInflater.from(DetailActivity.this).inflate(R.layout.detail_tag_item, null);
                    TextView number = view.findViewById(R.id.tag_text_number);
                    number.setText(detailListBean.getIndex() + "");
                    TextView title = view.findViewById(R.id.tag_text_title);
                    title.setText("[" + tagBean.getName() + "] : ");
                    TextView content = view.findViewById(R.id.tag_text_content);
                    content.setText(detailListBean.getContent());
                    dataLinAddtag.addView(view);
                }
            }
        }else {
            dataLinAddtag.setVisibility(View.GONE);
        }
    }


    private void SetImage() {
        Bitmap leftfoot_internal = BitmapFactory.decodeResource(getResources(), R.mipmap.leftfoot_internal);
        Bitmap leftfoot_surface = BitmapFactory.decodeResource(getResources(), R.mipmap.leftfoot_surface);
        Bitmap leftfoot_outside = BitmapFactory.decodeResource(getResources(), R.mipmap.leftfoot_outside);
        Bitmap rightfoot_internal = BitmapFactory.decodeResource(getResources(), R.mipmap.rightfoot_internal);
        Bitmap rightfoot_surface = BitmapFactory.decodeResource(getResources(), R.mipmap.rightfoot_surface);
        Bitmap rightfoot_outside = BitmapFactory.decodeResource(getResources(), R.mipmap.rightfoot_outside);

        map = new HashMap<>();
        map.put(detailImageLeft, leftfoot_internal);
        map.put(detailImageCenter, leftfoot_surface);
        map.put(detailImageRight, leftfoot_outside);
        map.put(detailImageLeftTwo, rightfoot_internal);
        map.put(detailImageCenterTwo, rightfoot_surface);
        map.put(detailImageRightTwo, rightfoot_outside);

        detailImageLeft.setBackground(new BitmapDrawable(getResources(), map.get(detailImageLeft)));
        detailImageCenter.setBackground(new BitmapDrawable(getResources(), map.get(detailImageCenter)));
        detailImageRight.setBackground(new BitmapDrawable(getResources(), map.get(detailImageRight)));
        detailImageLeftTwo.setBackground(new BitmapDrawable(getResources(), map.get(detailImageLeftTwo)));
        detailImageCenterTwo.setBackground(new BitmapDrawable(getResources(), map.get(detailImageCenterTwo)));
        detailImageRightTwo.setBackground(new BitmapDrawable(getResources(), map.get(detailImageRightTwo)));

    }

    private void LoadingImage() {

        GlideUtil.load(DetailActivity.this, AppConstant.IMAGE_ONE(number), detailImageFootleft);
        GlideUtil.load(DetailActivity.this, AppConstant.IAMGE_TWO(number), detailImageFootright);
        GlideUtil.load(DetailActivity.this, AppConstant.IMAGE_TREE(number), detailImagePlantarleft);
        GlideUtil.load(DetailActivity.this, AppConstant.IAMGE_FOUR(number), detailImagePlantarright);


        GlideUtil.load(DetailActivity.this, AppConstant.IMAGE_ONE(number), detailImageFootleft,"1-show.jpg");
        GlideUtil.load(DetailActivity.this, AppConstant.IAMGE_TWO(number), detailImageFootright,"0-show.jpg");
        GlideUtil.load(DetailActivity.this, AppConstant.IMAGE_TREE(number), detailImagePlantarleft,"5_l-show.jpg");
        GlideUtil.load(DetailActivity.this, AppConstant.IAMGE_FOUR(number), detailImagePlantarright,"5_r-show.jpg");

    }


    private void sortListTag() {
        tag = new ArrayList<>();
        TAGBean medial_left = null;
        TAGBean face_left= null;
        TAGBean lateral_left= null;
        TAGBean medial_right= null;
        TAGBean face_right= null;
        TAGBean lateral_right= null;

        List<TAGBean.DetailListBean> detailmedial_left =new ArrayList<>();
        List<TAGBean.DetailListBean> detailface_left =new ArrayList<>();
        List<TAGBean.DetailListBean> detaillateral_left =new ArrayList<>();
        List<TAGBean.DetailListBean> detailmedial_right =new ArrayList<>();
        List<TAGBean.DetailListBean> detailface_right =new ArrayList<>();
        List<TAGBean.DetailListBean> detaillateral_right =new ArrayList<>();

        for (int i=0;i<TaglList.size();i++){
            if (TaglList.get(i).getName().equals("左脚内侧")){
                medial_left = TaglList.get(i);
                detailmedial_left.addAll(TaglList.get(i).getDetailList()) ;
            }
            if (TaglList.get(i).getName().equals("左脚脚面")){
                face_left = TaglList.get(i);
                detailface_left.addAll(TaglList.get(i).getDetailList()) ;
            }
            if (TaglList.get(i).getName().equals("左脚外侧")){
                lateral_left = TaglList.get(i);
                detaillateral_left.addAll(TaglList.get(i).getDetailList()) ;
            }
            if (TaglList.get(i).getName().equals("右脚内侧")){
                medial_right = TaglList.get(i);
                detailmedial_right.addAll(TaglList.get(i).getDetailList()) ;
            }
            if (TaglList.get(i).getName().equals("右脚脚面")){
                face_right = TaglList.get(i);
                detailface_right.addAll(TaglList.get(i).getDetailList()) ;
            }
            if (TaglList.get(i).getName().equals("右脚外侧")){
                lateral_right = TaglList.get(i);
                detaillateral_right.addAll(TaglList.get(i).getDetailList()) ;

            }
        }

        if (medial_left!=null){
            medial_left.setDetailList(detailmedial_left);
            tag.add(medial_left);
        }
        if (face_left!=null){
            face_left.setDetailList(detailface_left);
            tag.add(face_left);
        }
        if (lateral_left!=null){
            lateral_left.setDetailList(detaillateral_left);
            tag.add(lateral_left);
        }
        if (medial_right!=null){
            medial_right.setDetailList(detailmedial_right);
            tag.add(medial_right);
        }
        if (face_right!=null){
            face_right.setDetailList(detailface_right);
            tag.add(face_right);
        }
        if (lateral_right!=null){
            lateral_right.setDetailList(detaillateral_right);
            tag.add(lateral_right);
        }

    }
}
