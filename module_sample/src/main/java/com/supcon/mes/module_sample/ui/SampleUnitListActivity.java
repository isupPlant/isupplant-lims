package com.supcon.mes.module_sample.ui;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.BaseSystemBackEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleUnitController;
import com.supcon.mes.module_sample.model.api.SampleUnitRefAPI;
import com.supcon.mes.module_sample.model.contract.SampleUnitRefContract;
import com.supcon.mes.module_sample.presenter.SampleUnitRefPresenter;
import com.supcon.mes.module_sample.ui.adapter.UnitAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/2/3
 * Email:wanghaidong1@supcon.com
 * desc:
 */
@Presenter(value = SampleUnitRefPresenter.class)
@Controller(value = {SampleUnitController.class})
@Router(value = LimsConstant.AppCode.SAMPLE_UNIT_REF)
public class SampleUnitListActivity extends BaseRefreshRecyclerActivity<BaseSystemBackEntity> implements SampleUnitRefContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;

    private UnitAdapter adapter;
    Map<String, Object> queryParam = new HashMap<>();
    @Override
    protected IListAdapter<BaseSystemBackEntity> createAdapter() {
        adapter = new UnitAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sample_list;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(R.string.lims_unit_ref);
        contentView.setLayoutManager(new LinearLayoutManager(context));
        scanRightBtn.setVisibility(View.GONE);
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
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
        getController(SampleUnitController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                queryParam.clear();
                queryParam.putAll(map);
                refreshListController.refreshBegin();
            }
        });
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(SampleUnitRefAPI.class).getSampleUnitRefInfo(pageIndex,queryParam);
            }
        });
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                BaseSystemBackEntity entity=adapter.getItem(position);
                SelectDataEvent<BaseSystemBackEntity> dataEvent=new SelectDataEvent<>(entity,"UnitEntity");
                EventBus.getDefault().post(dataEvent);
                finish();
            }
        });
    }

    @Override
    public void getSampleUnitRefInfoSuccess(List entity) {
        if (entity.size()>0){
            refreshListController.refreshComplete(entity);
            refreshListController.setLoadMoreEnable(true);
        }else {
            refreshListController.refreshComplete();
            refreshListController.setLoadMoreEnable(false);
        }
    }

    @Override
    public void getSampleUnitRefInfoFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }




}
