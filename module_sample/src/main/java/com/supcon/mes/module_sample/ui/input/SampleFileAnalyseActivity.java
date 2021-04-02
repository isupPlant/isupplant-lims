package com.supcon.mes.module_sample.ui.input;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshListActivity;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.ModuleConfigEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.constant.TemporaryData;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.SampleFileAnalyseController;
import com.supcon.mes.module_sample.model.api.SampleAnalyseCollectDataAPI;
import com.supcon.mes.module_sample.model.api.SampleFileAnalyseAPI;
import com.supcon.mes.module_sample.model.bean.FileAnalyseEntity;
import com.supcon.mes.module_sample.model.bean.FileAnalyseListEntity;
import com.supcon.mes.module_sample.model.contract.SampleAnalyseCollectDataContract;
import com.supcon.mes.module_sample.model.contract.SampleFileAnalyseContract;
import com.supcon.mes.module_sample.presenter.SampleAnalyseCollectDataPresenter;
import com.supcon.mes.module_sample.presenter.SampleFileAnalyseListPresenter;
import com.supcon.mes.module_sample.ui.adapter.FileAnalyseAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by wanghaidong on 2021/1/19
 * Email:wanghaidong1@supcon.com
 * desc:解析文件参照
 */
@Controller(value = {SampleFileAnalyseController.class, SystemConfigController.class})
@Presenter(value = {SampleFileAnalyseListPresenter.class,SampleAnalyseCollectDataPresenter.class})
@Router(value = LimsConstant.AppCode.LIMS_SAMPLE_FILE_ANALYSE)
public class SampleFileAnalyseActivity extends BaseRefreshRecyclerActivity<FileAnalyseEntity> implements SampleFileAnalyseContract.View, SampleAnalyseCollectDataContract.View {

    @BindByTag("titleText")
    TextView titleText;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    FileAnalyseAdapter adapter;
    @BindByTag("rgRemark")
    RadioGroup rgRemark;
    @BindByTag("btnConfirm")
    Button btnConfirm;

    private String specialResultStr = "";
    private String fileId = "";

    @Override
    protected IListAdapter<FileAnalyseEntity> createAdapter() {
        return adapter = new FileAnalyseAdapter(context);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.ac_sample_analyse_file;
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(context.getResources().getString(R.string.lims_file_analyse));
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

        getController(SystemConfigController.class).getModuleConfig(LimsConstant.ModuleCode.LIMS_FILE_ANALYSIS_MENU_CODE, LimsConstant.Keys.LIMSDC_OCD_LIMSDCUrl, new OnSuccessListener() {
            @Override
            public void onSuccess(Object result) {
                if (null != result){
                    try {
                        ModuleConfigEntity bean = (ModuleConfigEntity)result;
                        specialResultStr = bean.getLimsDCUrl();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });
    }

    Map<String, Object> queryParam = new HashMap<>();
    boolean remark = false;

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        RxView.clicks(searchTitle.getLeftActionBar())
                .throttleFirst(2,TimeUnit.SECONDS)
                .subscribe(o -> {
                    back();
                });
        getController(SampleFileAnalyseController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                queryParam.clear();
                queryParam.putAll(map);
                refreshListController.refreshBegin();
            }
        });


        rgRemark.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                if (id == R.id.rbYes){
                    if (remark==true)
                        return;
                    remark = true;
                }
                else if (id == R.id.rbNo) {
                    if (remark==false)
                        return;
                    remark = false;
                }
                refreshListController.refreshBegin();
            }
        });



        RxView.clicks(btnConfirm)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (adapter.getItemCount() > 0) {
                        for (FileAnalyseEntity entity : adapter.getList()) {
                            if (entity.check) {
                                //String url = "http://" + SupPlantApplication.getIp() + ":9410/lims-collection-web/ws/rs/analysisDataWS/queryDataByFileId?fileId="+entity.id;
                                fileId = entity.id+"";
                                onLoading(context.getResources().getString(R.string.lims_parsing));
                                presenterRouter.create(SampleAnalyseCollectDataAPI.class).getFormatDataByCollectCode("http://" +specialResultStr+"/lims-collection-web/ws/rs/analysisDataWS/queryDataByFileId",false,entity.id+"");
                                return;
                            }
                        }
                        ToastUtils.show(context, context.getResources().getString(R.string.lims_select_parse_file));
                    } else {
                        ToastUtils.show(context, context.getResources().getString(R.string.lims_no_parse_file));
                    }
                });
        refreshListController.setOnRefreshPageListener(pageNo -> {

            queryParam.put(LimsConstant.BAPQuery.LIMS_REFED, remark?1:0);
            presenterRouter.create(SampleFileAnalyseAPI.class).getAnalyseList(pageNo, queryParam);
        });
    }

    @Override
    public void getAnalyseListSuccess(FileAnalyseListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getAnalyseListFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
    }

    @Override
    public void getFormatDataByCollectCodeSuccess(List entity) {
        if (entity != null && entity.size() > 0) {
            onLoadSuccess(context.getResources().getString(R.string.lims_parse_success));
            TemporaryData.temporaryFileId = fileId;
            SelectDataEvent<List> selectDataEvent=new SelectDataEvent<>(entity,"SampleAnalyseFile");
            EventBus.getDefault().post(selectDataEvent);
            finish();
        } else {
            onLoadFailed(context.getResources().getString(R.string.lims_no_parse_data));
        }
    }

    @Override
    public void getFormatDataByCollectCodeFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }
}
