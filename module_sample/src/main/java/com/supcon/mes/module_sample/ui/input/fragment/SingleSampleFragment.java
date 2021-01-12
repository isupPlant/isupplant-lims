package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DateFilterController;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.TimeUtil;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.utils.SpaceItemDecoration;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleInputController;
import com.supcon.mes.module_sample.model.api.SampleListAPI;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.contract.SampleListContract;
import com.supcon.mes.module_sample.presenter.SingleSamplePresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleListAdapter;
import com.supcon.mes.module_sample.ui.input.SingleSampleResultInputPADActivity;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2021/1/7
 * class name
 */
@Controller(value = {SampleInputController.class, CommonScanController.class, DateFilterController.class})
@Presenter(value = SingleSamplePresenter.class)
public class SingleSampleFragment extends BaseRefreshRecyclerFragment<SampleEntity> implements SampleListContract.View {
    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    SampleListAdapter adapter;
    BaseFragmentActivity activity;
    boolean scan = false;

    SpaceItemDecoration spaceItemDecoration;
    GridLayoutManager gridLayoutManager;


    Map<String,Object> timeMap=new HashMap<>();
    Map<String, Object> queryParam = new HashMap<>();

    @Override
    protected IListAdapter<SampleEntity> createAdapter() {
        return adapter = new SampleListAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_single_sample;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SingleSampleResultInputPADActivity)context;

    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        titleText.setText(getString(R.string.lims_result_input));
    }

    @Override
    protected void initView() {
        super.initView();
        spaceItemDecoration = new SpaceItemDecoration(10, 2);
        gridLayoutManager = new GridLayoutManager(context, 2);
        contentView.setLayoutManager(gridLayoutManager);
        contentView.addItemDecoration(spaceItemDecoration);


        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));

        getController(DateFilterController.class).setDateChecked(Constant.Date.TODAY);
        String[] dates = TimeUtil.getTimePeriod(Constant.Date.TODAY);
        String startTime = dates[0];
        String endTime = dates[1];
        timeMap.put(Constant.BAPQuery.IN_DATE_START, startTime);
        timeMap.put(Constant.BAPQuery.IN_DATE_END, endTime);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        leftBtn.setOnClickListener(v -> activity.back());

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SampleListAPI.class).getSampleList(timeMap, queryParam);
            }
        });

        RxView.clicks(scanRightBtn)
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        getController(CommonScanController.class).openCameraScan();
                    }
                });

        getController(DateFilterController.class).setDateSelectListener((start, end) -> {
            if (start.isEmpty() && start.isEmpty()) {
                timeMap.remove(Constant.BAPQuery.IN_DATE_START);
                timeMap.remove(Constant.BAPQuery.IN_DATE_END);
            } else {
                timeMap.put(Constant.BAPQuery.IN_DATE_START, start);
                timeMap.put(Constant.BAPQuery.IN_DATE_END, end);
            }
            refreshListController.refreshBegin();
        });

        getController(SampleInputController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                queryParam.clear();
                queryParam.putAll(map);
                refreshListController.refreshBegin();
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                for (int i = 0; i < adapter.getList().size(); i++) {
                    adapter.getList().get(i).setSelect(false);
                }
                adapter.getItem(position).setSelect(true);
                adapter.notifyDataSetChanged();
                ((SingleSampleResultInputPADActivity)activity).notifySubRefresh(adapter.getItem(position).getId());
            }
        });

        ((SingleSampleResultInputPADActivity)activity).setOnNotifySampleRefreshListener(new SingleSampleResultInputPADActivity.OnNotifySampleRefreshListener() {
            @Override
            public void NotifySampleRefresh() {
                refreshListController.refreshBegin();
            }
        });

    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onRefresh(RefreshEvent event) {
//        refreshListController.refreshBegin();
//    }



    private String scanCode;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReciver(CodeResultEvent codeResultEvent) {
        if (!TextUtils.isEmpty(codeResultEvent.scanResult)) {
            scan = true;
            scanCode=codeResultEvent.scanResult;
            Map<String, Object> params = new HashMap<>();
            params.put(Constant.BAPQuery.CODE, scanCode);
            onLoading(getResources().getString(R.string.lims_loading_sample));
            presenterRouter.create(SampleListAPI.class).getSampleList(timeMap, params);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getSampleListSuccess(CommonListEntity entity) {
        if (scan) {
            scan = false;
            List<SampleEntity> sampleEntities = entity.result;
            if (!sampleEntities.isEmpty()) {
                SampleEntity sampleEntity = sampleEntities.get(0);
                if (scanCode.equals(sampleEntity.getCode())){
                    onLoadSuccessAndExit(getResources().getString(R.string.lims_loading_succeed), new OnLoaderFinishListener() {
                        @Override
                        public void onLoaderFinished() {
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sampleEntity", sampleEntity);
                            IntentRouter.go(context, Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM, bundle);
                        }
                    });
                }else {
                    onLoadFailed(getResources().getString(R.string.lims_load_fail));
                }
            }else {
                onLoadFailed(getResources().getString(R.string.lims_load_fail));
            }

        } else {
            refreshListController.refreshComplete(entity.result);
        }
    }

    @Override
    public void getSampleListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        if (scan) {
            onLoadFailed(errorMsg);
        } else
            ToastUtils.show(context, errorMsg);
    }
}
