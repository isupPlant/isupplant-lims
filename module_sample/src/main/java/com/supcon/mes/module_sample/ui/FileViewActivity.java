package com.supcon.mes.module_sample.ui;

import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.util.GlideUtil;
import com.supcon.mes.module_lims.utils.FileUtils;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/26
 * Email:wanghaidong1@supcon.com
 */
@Router(value = Constant.Router.FILE_LOOK)
public class FileViewActivity extends BaseActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    File file;
    @BindByTag("titleText")
    TextView titleText;

    ImageView imageFileView;
    @BindByTag("video")
    VideoView video;
    @Override
    protected int getLayoutID() {
        return R.layout.ac_file_view;
    }

    @Override
    protected void onInit() {
        super.onInit();
        file = (File) getIntent().getSerializableExtra("file");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(file.getName());
        imageFileView = findViewById(R.id.imageFileView);
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });

        if (file!=null) {
            if (FileUtils.imageFile(file)) {
                RequestOptions requestOptions = new RequestOptions()
                        .placeholder(R.drawable.ic_pic_default)
                        .override(DisplayUtil.dip2px(300, context), DisplayUtil.dip2px(300, context))
                        .centerInside();
                imageFileView.setVisibility(View.VISIBLE);
                Glide.with(context).load(file).into(imageFileView);
            } else if (FileUtils.videoFile(file)) {
                MediaController localMediaController = new MediaController(this);
                localMediaController.setVisibility(View.VISIBLE);
                video.setMediaController(localMediaController);
                String authority = getPackageName() + ".fileprovider";
                Uri fileUri = FileProvider.getUriForFile(context, authority, file);
                Uri uri = FileUtils.uriString(file.getPath(), fileUri);
                video.setVideoURI(uri);
                video.requestFocus();
                video.start();
            }
        }
    }
}

