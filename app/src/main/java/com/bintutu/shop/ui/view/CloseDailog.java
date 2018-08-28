package com.bintutu.shop.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bintutu.shop.R;

public class CloseDailog extends Dialog {

    private Context context;
    private ImageView img;
    private TextView txt;

    public CloseDailog(Context context) {
        super(context, R.style.close_dialog);
        this.context=context;
        //加载布局文件
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.close_dialog, null);
       /* img=(ImageView) view.findViewById(R.id.iphone_progress_dialog_img);
        txt=(TextView) view.findViewById(R.id.iphone_progress_dialog_txt);
        //给图片添加动态效果
        Animation anim= AnimationUtils.loadAnimation(context, R.anim.progressbar);
        img.setAnimation(anim);
        txt.setText(R.string.iphone_progressbar_dialog_txt);*/
        //dialog添加视图
        setContentView(view);

    }

    public void setMsg(String msg){
        txt.setText(msg);
    }
    public void setMsg(int msgId){
        txt.setText(msgId);
    }

}