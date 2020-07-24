package com.supcon.mes.module_sample.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.DateUtil;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.constant.BusinessType;
import com.supcon.mes.module_lims.controller.SampleInquiryController;
import com.supcon.mes.module_lims.listener.OnScanToResultListener;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.SampleInquiryEntity;
import com.supcon.mes.module_lims.model.bean.SampleInquiryListEntity;
import com.supcon.mes.module_lims.model.contract.SampleInquiryApi;
import com.supcon.mes.module_lims.presenter.SampleInquiryPresenter;
import com.supcon.mes.module_lims.ui.adapter.SampleInquiryAdapter;
import com.supcon.mes.module_sample.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/2
 * class name 取样列表
 */

@Router(Constant.AppCode.LIMS_Sampling)
@Presenter(value = {SampleInquiryPresenter.class})
@Controller(value = {SampleInquiryController.class})
public class SamplingActivity extends BaseRefreshRecyclerActivity<SampleInquiryEntity> implements SampleInquiryApi.View {

    @BindByTag("ll_select_all")
    LinearLayout llSelectAll;

    @BindByTag("iv_select")
    ImageView iv_select;

    @BindByTag("btn_sampling")
    TextView btn_sampling;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("contentView")
    RecyclerView contentView;

    private SampleInquiryAdapter adapter;
    private Map<String, Object> params = new HashMap<>();
    private List<SampleInquiryEntity> submitList = new ArrayList<>();

    private boolean isSelectAll = false;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sampling;
    }

    @Override
    protected IListAdapter createAdapter() {
        adapter = new SampleInquiryAdapter(context);
        return adapter;
    }



    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_sampling));

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
        goRefresh();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        getController(SampleInquiryController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                params.clear();
                params.putAll(map);
                goRefresh();
            }
        });

        getController(SampleInquiryController.class).setScanToResultListener(new OnScanToResultListener() {
            @Override
            public void scanToResultClick(String result) {
                for (int i = 0; i < adapter.getList().size(); i++) {
                    if (adapter.getList().get(i).getCode().equals(result)){
                        adapter.getList().get(i).setSelect(true);
                        break;
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    adapter.getItem(position).setSelect(!adapter.getItem(position).isSelect());
                    adapter.notifyDataSetChanged();
                    int a = 0;
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (adapter.getList().get(i).isSelect()) { //集合中存在勾选状态的 数据的话
                            a++;
                        }
                    }
                    if (a == adapter.getList().size()) {
                        setSelectAllStyle(true);
                    } else {
                        setSelectAllStyle(false);
                    }
                }else if (action == 1){
                    ToastUtils.show(context,"进入打印页面");
                }
            }
        });

        RxView.clicks(llSelectAll)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!isSelectAll) {
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                adapter.getList().get(i).setSelect(true);
                            }
                        } else {
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                adapter.getList().get(i).setSelect(false);
                            }
                        }
                        isSelectAll = !isSelectAll;
                        setSelectAllStyle(isSelectAll);
                        adapter.notifyDataSetChanged();
                    }
                });

        RxView.clicks(btn_sampling)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        submitList.clear();
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()){
                                submitList.add(adapter.getList().get(i));
                            }
                        }
                        if (submitList.size() > 0){
                            //弹出窗口 询问用户 是否收样
//                        new AlertDialog.Builder(context)
//                                .setTitle("提示")
//                                .setMessage("确定对这"+submitList.size()+"项进行取样吗？")
//                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        onLoading("取样中...");
//                                        String time = DateUtil.dateFormat(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
//                                        presenterRouter.create(com.supcon.mes.module_lims.model.api.SampleInquiryApi.class).sampleSubmit(BusinessType.Sample.SAMPLING,time, SupPlantApplication.getAccountInfo().staffId+"",submitList);
//                                    }
//                                })
//                                .setNegativeButton("取消", null)
//                                .show();
                            onLoading("取样中...");
                            String time = DateUtil.dateFormat(System.currentTimeMillis(),"yyyy-MM-dd HH:mm:ss");
                            presenterRouter.create(com.supcon.mes.module_lims.model.api.SampleInquiryApi.class).sampleSubmit(BusinessType.Sample.SAMPLING,time, SupPlantApplication.getAccountInfo().staffId+"",submitList);
                        }else {
                            ToastUtils.show(context,"请至少选择一条样品");
                        }
//                        IntentRouter.go(context,"LIMS_QualityStdVerRef");
                    }
                });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(com.supcon.mes.module_lims.model.api.SampleInquiryApi.class).getSampleInquiryList(BusinessType.Sample.SAMPLING, pageIndex, params);
            }
        });

    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }


    @Override
    public void getSampleInquiryListSuccess(SampleInquiryListEntity entity) {
        if (entity.data.result.size() > 0){
            setSelectAllStyle(false);
        }
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getSampleInquiryListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    public void sampleSubmitSuccess(BAP5CommonEntity entity) {
        onLoadSuccessAndExit("取样成功", new OnLoaderFinishListener() {
            @Override
            public void onLoaderFinished() {
                goRefresh();
            }
        });
    }

    @Override
    public void sampleSubmitFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }

    private void setSelectAllStyle(boolean isSelectAll) {
        if (isSelectAll) {
            iv_select.setImageResource(R.drawable.ic_check_yes);
        } else {
            iv_select.setImageResource(R.drawable.ic_check_no);
        }
    }
}
