package com.supcon.mes.module_retention.ui;

import android.content.Intent;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnRefreshListener;
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
    RetentionViewRecordAdapter adapter;
    Long id;
    String dataDg;

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
        dataDg=intent.getStringExtra("dataDg");
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this,R.color.themeColor);
        titleText.setText(R.string.lims_retention_view_record);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
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
                presenterRouter.create(RetentionRecordAPI.class).getRetentionRecode("data-"+dataDg,"LIMSRetain_5.0.4.1_retention_retRecordView"+dataDg,id);
            }
        });
    }

    @Override
    public void getRetentionRecodeSuccess(CommonBAP5ListEntity entity) {
        if (entity.data!=null)
            refreshListController.refreshComplete(entity.data.result);
        else
            refreshListController.refreshComplete();
    }

    @Override
    public void getRetentionRecodeFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
