package com.supcon.mes.module_sample.ui.input;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.PopupWindowEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.ui.view.AddFileListView;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.FileUtil;
import com.supcon.mes.module_lims.utils.FileUtils;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_sample.model.api.SingleSampleResultInputAPI;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.bean.SampleInspectItemEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.contract.SingleSampleResultInputContract;
import com.supcon.mes.module_sample.presenter.SingleSampleResultInputPresenter;
import com.supcon.mes.module_sample.ui.adapter.SingleSampleInpectAdapter;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */
@Presenter(value = {
        SingleSampleResultInputPresenter.class,
})
@Controller(value = {
        LimsFileUpLoadController.class
})
@Router(Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM)
public class SingleSampleResultInputItemActivity extends BaseRefreshRecyclerActivity<SampleInspectItemEntity> implements SingleSampleResultInputContract.View {


    SingleSampleInpectAdapter adapter;
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;
    SampleEntity sampleEntity;
    String filePath;
    SampleInspectItemEntity itemEntity;
    @Override
    protected int getLayoutID() {
        return R.layout.ac_single_sample_input_result_item;
    }

    @Override
    protected IListAdapter<SampleInspectItemEntity> createAdapter() {
        return adapter = new SingleSampleInpectAdapter(context);
    }

    @Override
    protected void onInit() {
        super.onInit();
        sampleEntity = (SampleEntity) getIntent().getSerializableExtra("sampleEntity");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(R.string.lims_single_sample_result_input);
        adapter.activity = this;
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(3, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(this));
        contentView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, "没有数据", false));
        contentView.setScrollBarSize(2);
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });
        refreshListController.setOnRefreshListener(() -> {
            presenterRouter.create(SingleSampleResultInputAPI.class).getSampleCom(sampleEntity.getId());
            presenterRouter.create(SingleSampleResultInputAPI.class).getSampleInspectItem(sampleEntity.getId());
        });
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                itemEntity=adapter.getItem(position);
                if (action == 1) {
//                    getController(LimsFileUpLoadController.class).
//                            showPopup(SingleSampleResultInputItemActivity.this)
//                            .setOnSuccessListener(new OnSuccessListener<FileDataEntity>() {
//                        @Override
//                        public void onSuccess(FileDataEntity fileDataEntity) {
//                            filePath=fileDataEntity.getLocalPath();
//                            itemEntity.setFileUploadMultiFileNames(fileDataEntity.getPath());
//                            itemEntity.setFileUploadMultiFileIcons(fileDataEntity.getFileIcon());
//                            itemEntity.setFilePath(filePath);
//                        }
//                    });
                }else if (action==2){
                    if (!TextUtils.isEmpty(itemEntity.getFilePath())){
                        File file=new File(itemEntity.getFilePath());
                        if (FileUtils.imageFile(file) || FileUtils.videoFile(file)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("file", file);
                            IntentRouter.go(context, Constant.Router.FILE_LOOK, bundle);
                        }else {
                            startActivity(Util.openFile(SingleSampleResultInputItemActivity.this,itemEntity.getFilePath()));
                        }
                    }else {
                        ToastUtils.show(context,"没有可查看的文件");
                    }
                }
            }
        });


    }

    @Override
    public void getSampleComSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getSampleComFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
        refreshListController.refreshComplete();
    }

    @Override
    public void getSampleInspectItemSuccess(SingleInspectionItemListEntity entity) {
        adapter.setConclusionEntities(entity.data);
    }

    @Override
    public void getSampleInspectItemFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

}
