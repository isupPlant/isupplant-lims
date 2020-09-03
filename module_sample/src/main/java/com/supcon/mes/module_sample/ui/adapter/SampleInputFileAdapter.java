package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;

import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.utils.FileUtils;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;


import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/9/3
 * Email:wanghaidong1@supcon.com
 */
public class SampleInputFileAdapter extends BaseListDataRecyclerViewAdapter<AttachmentSampleInputEntity> {
    public SampleInputFileAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<AttachmentSampleInputEntity> getViewHolder(int viewType) {
        return new SampleInputFileViewHolder(context);
    }
    class SampleInputFileViewHolder extends BaseRecyclerViewHolder<AttachmentSampleInputEntity>{

        @BindByTag("itemFileTv")
        TextView itemFileTv;
        @BindByTag("itemViewDelBtn")
        TextView itemViewDelBtn;
        @BindByTag("rl_item")
        RelativeLayout rl_item;
        public SampleInputFileViewHolder(Context context) {
            super(context);
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(rl_item)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        AttachmentSampleInputEntity entity=getItem(getAdapterPosition());
                        File file = entity.getFile();
                        if (FileUtils.imageFile(file) || FileUtils.videoFile(file)) {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("file", file);
                            IntentRouter.go(context, Constant.Router.FILE_LOOK, bundle);
                        } else {
                            Util.openFile(context, file.getPath());
                        }
                    });
            RxView.clicks(itemViewDelBtn)
                    .throttleFirst(2000,TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(itemViewDelBtn,1);
                    });
        }

        @Override
        protected int layoutId() {
            return R.layout.item_sample_input_file;
        }

        @Override
        protected void update(AttachmentSampleInputEntity data) {
            itemFileTv.setText(data.getName());
        }
    }
}
