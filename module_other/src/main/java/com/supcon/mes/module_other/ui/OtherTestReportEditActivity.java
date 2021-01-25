package com.supcon.mes.module_other.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshActivity;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.WorkFlowButtonInfo;
import com.supcon.mes.middleware.model.bean.PendingEntity;
import com.supcon.mes.module_lims.controller.TestReportEditController;
import com.supcon.mes.module_lims.model.api.StdJudgeSpecAPI;
import com.supcon.mes.module_lims.model.api.TableTypeAPI;
import com.supcon.mes.module_lims.model.api.TestReportEditAPI;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecListEntity;
import com.supcon.mes.module_lims.model.bean.TableTypeIdEntity;
import com.supcon.mes.module_lims.model.bean.TestReportEditHeadEntity;
import com.supcon.mes.module_lims.model.contract.StdJudgeSpecContract;
import com.supcon.mes.module_lims.model.contract.TableTypeContract;
import com.supcon.mes.module_lims.model.contract.TestReportEditContract;
import com.supcon.mes.module_lims.presenter.StdJudgeSpecPresenter;
import com.supcon.mes.module_lims.presenter.TableTypePresenter;
import com.supcon.mes.module_lims.presenter.TestReportEditPresenter;
import com.supcon.mes.module_other.R;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/11/11
 * class name
 */
@Controller(value = {TestReportEditController.class})
@Presenter(value = {TestReportEditPresenter.class, StdJudgeSpecPresenter.class, TableTypePresenter.class})
@Router(value = "otherInspReportEdit")
public class OtherTestReportEditActivity extends BaseRefreshActivity implements TestReportEditContract.View, StdJudgeSpecContract.View, TableTypeContract.View {
    private boolean isAdd;
    private String id;
    private String pendingId;
    PendingEntity pendingEntity;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("leftBtn")
    ImageButton leftBtn;

    @BindByTag("ctSupplier")
    CustomTextView ctSupplier;

    private WorkFlowButtonInfo info;
    @Override
    protected int getLayoutID() {
        return R.layout.activity_test_report_edit;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.lims_other_inspection_report));

        refreshController.setAutoPullDownRefresh(false);
        refreshController.setPullDownRefreshEnabled(false);


        ctSupplier.setVisibility(View.GONE);

        isAdd = getIntent().getBooleanExtra("isAdd",false);
        id = getIntent().getStringExtra("id") == null ? "" : getIntent().getStringExtra("id");
        pendingId = getIntent().getStringExtra("pendingId") == null ? "" : getIntent().getStringExtra("pendingId");
        info = (WorkFlowButtonInfo) getIntent().getSerializableExtra("info");



        Intent intent = getIntent();
        if (intent.hasExtra(Constant.IntentKey.PENDING_ENTITY)) {
            pendingEntity = (PendingEntity) intent.getSerializableExtra(Constant.IntentKey.PENDING_ENTITY);
        }
        ctSupplier.setVisibility(View.GONE);
        titleText.setText(context.getResources().getString(R.string.lims_other_inspection_report));
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        RxView.clicks(leftBtn)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        onBackPressed();
                    }
                });

        refreshController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (pendingEntity != null && pendingEntity.id != null) {
                    presenterRouter.create(TestReportEditAPI.class).getTestReportEdit(pendingEntity.modelId + "", pendingEntity.id + "");
                } else {
                    presenterRouter.create(TestReportEditAPI.class).getTestReportEdit(id, "");
                }
            }
        });

        getController(TestReportEditController.class).setQualityChangeListener(new TestReportEditController.QualityChangeListener() {
            @Override
            public void qualityChangeClick(String inspectId, String stdVerId) {
                Map<String, Object> map = new HashMap<>();
                map.put("inspectId", inspectId);
                map.put("stdVerId", stdVerId);
                presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(map);
            }
        });

        getController(TestReportEditController.class).setTestProjectChangeListener(new TestReportEditController.TestProjectChangeListener() {
            @Override
            public void testProjectChangeClick(String inspectId, String stdVerId, String reportName) {
                Map<String, Object> map = new HashMap<>();
                map.put("inspectId", inspectId);
                map.put("stdVerId", stdVerId);
                map.put("reportNames", reportName);
                presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(map);
            }
        });

        getController(TestReportEditController.class).setOnRequestHeadListener(new TestReportEditController.OnRequestHeadListener() {
            @Override
            public void requestHeadClick() {
                goRefresh();
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
//        if (isAdd){
//            if (null != info){
//                presenterRouter.create(TableTypeAPI.class).getTableTypeByCode("other");
//                getController(TestReportEditController.class).setIsFrom("add");
//                getController(TestReportEditController.class).setDeploymentId(info.getDeploymentId(),"TaskEvent_1igkdn3");
//                getController(TestReportEditController.class).setStartTabHead(3, new TestReportEditController.TableHeadDataOverListener() {
//                    @Override
//                    public void tableHeadOver(String inspectId, String stdVerId) {
//                        Map<String, Object> map = new HashMap<>();
//                        map.put("inspectId",inspectId);
//                        map.put("stdVerId",stdVerId);
//                        presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(map);
//                    }
//                });
//            }
//        }else {
        if (isAdd) {
            getController(TestReportEditController.class).setIsFrom("add");
            presenterRouter.create(TableTypeAPI.class).getTableTypeByCode("other");
            getController(TestReportEditController.class).setStartTabHead(3, new TestReportEditController.TableHeadDataOverListener() {
                @Override
                public void tableHeadOver(String inspectId, String stdVerId) {
                    Map<String, Object> map = new HashMap<>();
                    map.put("inspectId", inspectId);
                    map.put("stdVerId", stdVerId);
                    presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(map);
                }
            });
        } else {
            goRefresh();
        }
    }

    public void goRefresh() {
        refreshController.refreshBegin();
    }


    @Override
    public void getTestReportEditSuccess(TestReportEditHeadEntity entity) {
        getController(TestReportEditController.class).setTableHead(3, entity, new TestReportEditController.TableHeadDataOverListener() {
            @Override
            public void tableHeadOver(String inspectId, String stdVerId) {
                Map<String, Object> map = new HashMap<>();
                map.put("inspectReportId", entity.getId());
                map.put("pageNo", 1);
                presenterRouter.create(StdJudgeSpecAPI.class).getReportComList(map);
            }
        });
    }

    @Override
    public void getTestReportEditFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void getReportComListSuccess(StdJudgeSpecListEntity entity) {
        refreshController.refreshComplete();
        getController(TestReportEditController.class).setTablePt(entity);
    }

    @Override
    public void getReportComListFailed(String errorMsg) {
        refreshController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void getTableTypeByCodeSuccess(TableTypeIdEntity entity) {
        getController(TestReportEditController.class).setTableType(entity);
    }

    @Override
    public void getTableTypeByCodeFailed(String errorMsg) {
        ToastUtils.show(context, errorMsg);
    }
}
