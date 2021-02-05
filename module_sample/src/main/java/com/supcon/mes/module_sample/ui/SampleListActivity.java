package com.supcon.mes.module_sample.ui;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GridSpaceItemDecoration;
import com.supcon.mes.mbap.utils.SpaceItemDecoration;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.DeivceHelper;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleController;
import com.supcon.mes.module_sample.model.api.SampleRefAPI;
import com.supcon.mes.module_sample.model.contract.SampleRefContract;
import com.supcon.mes.module_sample.presenter.SampleRefPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleListAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/2/2
 * Email:wanghaidong1@supcon.com
 * desc:
 */
@Presenter(value = SampleRefPresenter.class)
@Controller(value = {SampleController.class})
@Router(value = LimsConstant.AppCode.SAMPLE_REF)
public class SampleListActivity extends BaseRefreshRecyclerActivity<SampleEntity> implements SampleRefContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;

    private SampleListAdapter adapter;
    Map<String, Object> queryParam = new HashMap<>();
    @Override
    protected IListAdapter<SampleEntity> createAdapter() {
        adapter = new SampleListAdapter(context);
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
        titleText.setText(R.string.lims_sample_ref);
        if (DeivceHelper.getInstance().isTabletDevice(SupPlantApplication.getAppContext())){
            contentView.setLayoutManager(new GridLayoutManager(context,2));
            contentView.addItemDecoration(new GridSpaceItemDecoration(DisplayUtil.dip2px(5, context),2));
        }else {
            contentView.setLayoutManager(new LinearLayoutManager(context));
            contentView.addItemDecoration(new SpaceItemDecoration(DisplayUtil.dip2px(5, context)));
        }
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
        getController(SampleController.class).setSearchOverListener(new OnSearchOverListener() {
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
                presenterRouter.create(SampleRefAPI.class).getSampleRefInfo(pageIndex,queryParam);
            }
        });
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                SampleEntity entity=adapter.getItem(position);
                SelectDataEvent<SampleEntity> dataEvent=new SelectDataEvent<>(entity,"SampleEntity");
                EventBus.getDefault().post(dataEvent);
                finish();
            }
        });

    }

    @Override
    public void getSampleRefInfoSuccess(List entity) {
        if (entity.size()>0){
            refreshListController.refreshComplete(entity);
            refreshListController.setLoadMoreEnable(true);
        }else {
            refreshListController.refreshComplete();
            refreshListController.setLoadMoreEnable(false);
        }

    }

    @Override
    public void getSampleRefInfoFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }




}
