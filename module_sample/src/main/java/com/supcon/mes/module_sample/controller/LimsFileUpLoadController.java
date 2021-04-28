package com.supcon.mes.module_sample.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.controller.BasePresenterController;
import com.supcon.common.view.base.fragment.BaseFragment;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.model.api.AttachmentAPI;
import com.supcon.mes.middleware.model.bean.AttachmentEntity;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.BapResultEntity;
import com.supcon.mes.middleware.model.contract.AttachmentContract;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.presenter.AttachmentPresenter;
import com.supcon.mes.middleware.ui.view.AddFileListView;
import com.supcon.mes.module_lims.model.api.FileUpAPI;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.model.contract.FileUpContract;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.presenter.FileUpLoadPresenter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by wanghaidong on 2020/8/25
 * Email:wanghaidong1@supcon.com
 */
@Presenter(value = {FileUpLoadPresenter.class, AttachmentPresenter.class})
public class LimsFileUpLoadController extends BasePresenterController implements FileUpContract.View, AttachmentContract.View {

    PopupWindow popupWindow;
    private View contentViewSign;
    private static int CAMERAPRESS = 3;//拍照权限
    private static int FILE_SELECT = 4;//文件选择
    private final static int CAMERA = 1;//拍照
    File imageFile;
    Uri imageUri; //图片路径
    OnSuccessListener<FileDataEntity> onSuccessListener;
    OnSuccessListener<List<AttachmentSampleInputEntity>> fileOnSuccessListener;
    private Activity context;
    private String filePath;
    private BaseFragment fragment;

    public void setContext(Activity context) {
        this.context = context;
    }

    public LimsFileUpLoadController showPopup(Activity context, BaseFragment fragment) {
        if (popupWindow != null && popupWindow.isShowing()) {
            return this;
        }
        this.context = context;
        this.fragment = fragment;
        contentViewSign = LayoutInflater.from(context).inflate(R.layout.pop_file_upload, null);
        popupWindow = new PopupWindow(contentViewSign);
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //在PopupWindow里面就加上下面代码，让键盘弹出时，不会挡住pop窗口。
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //点击空白处时，隐藏掉pop窗口
        popupWindow.setFocusable(false);
//        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        backgroundAlpha(0.5f);
        //添加pop窗口关闭事件
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1.0f);
            }
        });
        popupWindow.showAtLocation(context.getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        Button camera = contentViewSign.findViewById(R.id.camera);
        Button gallery = contentViewSign.findViewById(R.id.gallery);
        TextView cancel = contentViewSign.findViewById(R.id.cancel);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    //在此处添加你的按键处理 xxx
                    if (Build.VERSION.SDK_INT >= 23) {
                        //android 6.0权限问题
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            ToastUtils.show(context, context.getResources().getString(R.string.lims_open_camera));
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERAPRESS);

                        } else {
                            startCamera();
                        }
                    } else {
                        startCamera();
                    }
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    //打开选择,本次允许选择的数量
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                    fragment.startActivityForResult(intent, FILE_SELECT);
                }
                backgroundAlpha(1.0f);
                popupWindow.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    backgroundAlpha(1.0f);
                    popupWindow.dismiss();
                }
            }
        });

        return this;
    }

    private String cameraName;

    //拍照
    public void startCamera() {

        cameraName = DateUtil.dateFormat(System.currentTimeMillis());
        imageFile = new File(context.getExternalCacheDir(), cameraName + ".png");
        backgroundAlpha(1.0f);
        try {
            if (imageFile.exists()) {
                imageFile.delete();
            }
            imageFile.createNewFile();

            if (Build.VERSION.SDK_INT < 24) {
                imageUri = Uri.fromFile(imageFile);
            } else {
                //Android 7.0系统开始 使用本地真实的Uri路径不安全,使用FileProvider封装共享Uri
                //参数二:fileprovider绝对路径 com.dyb.testcamerademo：项目包名
                imageUri = AddFileListView.getUriForFile24(context, imageFile);
            }

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //照相
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); //指定图片输出地址
            fragment.startActivityForResult(intent, CAMERA); //启动照相
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        context.getWindow().setAttributes(lp); //添加pop窗口关闭事件
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 获取用户选择文件的URI
        if (requestCode == CAMERA && resultCode == Activity.RESULT_OK) {
            try {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).onLoading(context.getResources().getString(R.string.lims_upload_file));
                }

                if (context instanceof BaseFragmentActivity) {
                    ((BaseFragmentActivity) context).onLoading(context.getResources().getString(R.string.lims_upload_file));
                }

                filePath = AddFileListView.getPath(context, imageUri);
                File file = new File(filePath);
                presenterRouter.create(AttachmentAPI.class).bapUploadAttachment(file);
            } catch (Exception e) {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).closeLoader();
                }

                if (context instanceof BaseFragmentActivity) {
                    ((BaseFragmentActivity) context).closeLoader();
                }
                e.printStackTrace();
            }
        } else if (requestCode == FILE_SELECT && resultCode == Activity.RESULT_OK) {
            // 通过ContentProvider查询文件路径
            try {
                Uri uri = data.getData();
                filePath = AddFileListView.getPath(context, uri);
                File file = new File(filePath);
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).onLoading(context.getResources().getString(R.string.lims_upload_file));
                }

                if (context instanceof BaseFragmentActivity) {
                    ((BaseFragmentActivity) context).onLoading(context.getResources().getString(R.string.lims_upload_file));
                }
                presenterRouter.create(AttachmentAPI.class).bapUploadAttachment(file);
            } catch (Exception e) {
                if (context instanceof BaseActivity) {
                    ((BaseActivity) context).closeLoader();
                }

                if (context instanceof BaseFragmentActivity) {
                    ((BaseFragmentActivity) context).closeLoader();
                }
                e.printStackTrace();
            }
        }
    }

    public OnSuccessListener<List<AttachmentSampleInputEntity>> getFileOnSuccessListener() {
        return fileOnSuccessListener;
    }

    public void setFileOnSuccessListener(OnSuccessListener<List<AttachmentSampleInputEntity>> fileOnSuccessListener) {
        this.fileOnSuccessListener = fileOnSuccessListener;
    }

    public OnSuccessListener<FileDataEntity> getOnSuccessListener() {
        return onSuccessListener;
    }

    public void setOnSuccessListener(OnSuccessListener<FileDataEntity> onSuccessListener) {
        this.onSuccessListener = onSuccessListener;
    }


    public LimsFileUpLoadController loadFile(List<String> id, List<String> fileName) {
        presenterRouter.create(FileUpAPI.class).downloadFile(id, fileName);
        return this;
    }

    @Override
    public void downloadFileSuccess(List entity) {
        if (fileOnSuccessListener != null) {
            fileOnSuccessListener.onSuccess(entity);
        }
    }

    @Override
    public void downloadFileFailed(String errorMsg) {

    }

    @Override
    public void uploadAttachmentSuccess(String entity) {

    }

    @Override
    public void uploadAttachmentFailed(String errorMsg) {

    }

    @Override
    public void deleteAttachmentSuccess(BapResultEntity entity) {

    }

    @Override
    public void deleteAttachmentFailed(String errorMsg) {

    }

    @Override
    public void bapUploadAttachmentSuccess(BAP5CommonEntity entity) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).onLoadSuccessAndExit(context.getResources().getString(R.string.lims_upload_succeed), new OnLoaderFinishListener() {
                @Override
                public void onLoaderFinished() {
                    loaderFinishListener(entity);
                }
            });
        }

        if (context instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) context).onLoadSuccessAndExit(context.getResources().getString(R.string.lims_upload_succeed), new OnLoaderFinishListener() {
                @Override
                public void onLoaderFinished() {
                    loaderFinishListener(entity);
                }
            });
        }
    }


    @Override
    public void bapUploadAttachmentFailed(String errorMsg) {
        if (context instanceof BaseActivity) {
            ((BaseActivity) context).onLoadFailed(errorMsg);
        }

        if (context instanceof BaseFragmentActivity) {
            ((BaseFragmentActivity) context).onLoadFailed(errorMsg);
        }
    }

    private void loaderFinishListener(BAP5CommonEntity entity){
        if (onSuccessListener != null) {
            AttachmentEntity attachmentEntity = (AttachmentEntity) entity.data;
            if (attachmentEntity == null) {
                return;
            }
            FileDataEntity fileDataEntity = new FileDataEntity();
            fileDataEntity.setFileIcon(attachmentEntity.fileIcon);
            fileDataEntity.setPath(attachmentEntity.path);
            fileDataEntity.setLocalPath(filePath);
            onSuccessListener.onSuccess(fileDataEntity);
        }
    }
}
