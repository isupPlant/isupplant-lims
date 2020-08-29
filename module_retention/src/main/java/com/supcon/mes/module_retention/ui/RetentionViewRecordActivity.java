package com.supcon.mes.module_retention.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_retention.R;
import com.supcon.mes.module_retention.model.api.RetentionRecordAPI;
import com.supcon.mes.module_retention.model.bean.RecordViewEntity;
import com.supcon.mes.module_retention.model.bean.RetentionEntity;
import com.supcon.mes.module_retention.model.contract.RetentionRecordContract;
import com.supcon.mes.module_retention.presenter.RetentionRecordPresenter;
import com.supcon.mes.module_retention.ui.adapter.RetentionViewRecordAdapter;

import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2020/8/28
 * Email:wanghaidong1@supcon.com
 */
@Presenter(value = RetentionRecordPresenter.class)
@Router(value = Constant.Router.RETENTION_VIEW_RECORD)
public class RetentionViewRecordActivity extends BaseRefreshRecyclerActivity<RecordViewEntity> implements RetentionRecordContract.View {

    @BindByTag("leftBtn")
    ImageButton leftBtn;
    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;
    RetentionViewRecordAdapter adapter;
    Long id;

    @Override
    protected IListAdapter<RecordViewEntity> createAdapter() {
        return adapter=new RetentionViewRecordAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_retention_view_record;
    }

    @Override
    protected void onInit() {
        super.onInit();
        Intent intent=getIntent();
        id=intent.getLongExtra("id",0);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(R.string.lims_retention_view_record);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childLayoutPosition = parent.getChildAdapterPosition(view);
                if (childLayoutPosition == 0) {
                    outRect.set(DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                } else {
                    outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                }
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    back();
                });

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(RetentionRecordAPI.class).getRetentionRecode(id);
            }
        });

    }

    @Override
    public void getRetentionRecodeSuccess(CommonBAP5ListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);

    }

    @Override
    public void getRetentionRecodeFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
