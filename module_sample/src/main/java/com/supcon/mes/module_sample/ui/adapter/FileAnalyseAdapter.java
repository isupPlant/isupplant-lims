package com.supcon.mes.module_sample.ui.adapter;

import android.content.Context;
import android.widget.CheckBox;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.FileAnalyseEntity;
import com.supcon.mes.module_sample.model.bean.FileAnalyseListEntity;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/1/19
 * Email:wanghaidong1@supcon.com
 * desc:
 */
public class FileAnalyseAdapter extends BaseListDataRecyclerViewAdapter<FileAnalyseEntity> {
    public FileAnalyseAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder<FileAnalyseEntity> getViewHolder(int viewType) {
        return new FileAnalyseViewHolder(context);
    }
    class FileAnalyseViewHolder extends BaseRecyclerViewHolder<FileAnalyseEntity>{

        @BindByTag("itemFileName")
        CustomTextView itemFileName;
        @BindByTag("itemCollectionCode")
        CustomTextView itemCollectionCode;
        @BindByTag("itemMD5")
        CustomTextView itemMD5;
        @BindByTag("checkBox")
        CheckBox checkBox;
        public FileAnalyseViewHolder(Context context) {
            super(context);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_analyse_sample_file;
        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(itemView)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        FileAnalyseEntity data=getItem(getAdapterPosition());
                        data.check=!data.check;
                        for(FileAnalyseEntity entity:getList()){
                            if (data.id.longValue()==entity.id.longValue())
                                continue;
                            else
                                entity.check=false;
                        }
                        notifyDataSetChanged();
                    });
        }

        @Override
        protected void update(FileAnalyseEntity data) {
            itemFileName.setValue(data.name);
            itemCollectionCode.setValue(data.collectCode);
            itemMD5.setValue(data.md5);
            checkBox.setChecked(data.check);
        }
    }
}
