package com.supcon.mes.module_sample.ui.input;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.event.RefreshEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleInputController;

import com.supcon.mes.module_sample.model.bean.SampleEntity;

import com.supcon.mes.module_sample.presenter.SingleSamplePresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleListAdapter;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;
import com.supcon.mes.module_sample.model.contract.SampleListApi;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */
@Controller(value = {SampleInputController.class, CommonScanController.class})
@Presenter(value = SingleSamplePresenter.class)
@Router(Constant.AppCode.LIMS_RecordBySingleSample)
public class SingleSampleResultInputActivity extends BaseRefreshRecyclerActivity<SampleEntity> implements SampleListApi.View {


    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("scanRightBtn")
    CustomImageButton scanRightBtn;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;
    Map<String,Object> queryParam=new HashMap<>();
    SampleListAdapter adapter;

    @Override
    protected int getLayoutID() {
        return R.layout.ac_single_input_result;
    }

    @Override
    protected IListAdapter createAdapter() {
        return adapter=new SampleListAdapter(context);
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_single_sample_result_input));
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
        refreshListController.setAutoPullDownRefresh(true);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initListener() {
        super.initListener();

        getController(SampleInputController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                queryParam.clear();
                queryParam.putAll(map);
                refreshListController.refreshBegin();
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
        leftBtn.setOnClickListener(v -> back());
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(com.supcon.mes.module_sample.model.api.SampleListApi.class).getSampleList(queryParam);

            }
        });
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                SampleEntity sampleEntity=adapter.getItem(position);
                Bundle bundle=new Bundle();
                bundle.putSerializable("sampleEntity",sampleEntity);
                IntentRouter.go(context,Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM,bundle);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefresh(RefreshEvent event) {
        refreshListController.refreshBegin();
    }

    boolean scan=false;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReciver(CodeResultEvent codeResultEvent) {
        if (!TextUtils.isEmpty(codeResultEvent.scanResult)){
            scan=true;
            Map<String,Object> params=new HashMap<>();
            params.put(Constant.BAPQuery.CODE,codeResultEvent.ruleContent);
            onLoading("正在加载样品...");
            presenterRouter.create(com.supcon.mes.module_sample.model.api.SampleListApi.class).getSampleList(params);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getSampleListSuccess(CommonListEntity entity) {
        if (scan){
            scan=false;
            onLoadSuccessAndExit("加载成功", new OnLoaderFinishListener() {
                @Override
                public void onLoaderFinished() {
                    List<SampleEntity> sampleEntities=entity.result;
                    SampleEntity sampleEntity=sampleEntities.get(0);
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("sampleEntity",sampleEntity);
                    IntentRouter.go(context,Constant.Router.SINGLE_SAMPLE_RESULT_INPUT_ITEM,bundle);
                }
            });
        }else {
            refreshListController.refreshComplete(entity.result);
        }
    }

    @Override
    public void getSampleListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}
