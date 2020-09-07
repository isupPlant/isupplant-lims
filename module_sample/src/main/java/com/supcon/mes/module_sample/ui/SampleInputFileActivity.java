package com.supcon.mes.module_sample.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseActivity;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.view.CustomSwipeLayout;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.ui.adapter.SampleInputFileAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/9/3
 * Email:wanghaidong1@supcon.com
 */
@Router(Constant.Router.FILE_LIST_VIEW)
public class SampleInputFileActivity extends BaseActivity {
    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;
    List<AttachmentSampleInputEntity> attachmentSampleInputEntities;
    SampleInputFileAdapter adapter;
    List<AttachmentSampleInputEntity> deleteIds=new ArrayList<>();
    @Override
    protected int getLayoutID() {
        return R.layout.ac_sample_input_file;
    }

    @Override
    protected void onInit() {
        super.onInit();
        attachmentSampleInputEntities= (List<AttachmentSampleInputEntity>) getIntent().getSerializableExtra("attachments");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.lims_enclosure_list));
        adapter=new SampleInputFileAdapter(context);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(1, context)));
        contentView.addOnItemTouchListener(new CustomSwipeLayout.OnSwipeItemTouchListener(context));
        adapter.setList(attachmentSampleInputEntities);
        contentView.setAdapter(adapter);
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action==1){
                    AttachmentSampleInputEntity entity=adapter.getItem(position);
                    deleteIds.add(entity);
                    if (entity.getFile()!=null && entity.getFile().exists())
                        entity.getFile().delete();
                    adapter.remove(position);
                    attachmentSampleInputEntities.remove(entity);
                    adapter.notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public void back() {
        super.back();
        if (!deleteIds.isEmpty())
            EventBus.getDefault().post(new SelectDataEvent(deleteIds,"deleteIds"));
    }
}
