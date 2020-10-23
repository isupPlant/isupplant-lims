package com.supcon.mes.module_sample.ui.scan;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.app.annotation.apt.Router;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseControllerActivity;
import com.supcon.common.view.base.activity.BasePresenterActivity;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.BAP5CommonEntity;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.ScanSamplingPointIdAPI;
import com.supcon.mes.module_sample.model.contract.ScanSamplingPointIdContract;
import com.supcon.mes.module_sample.presenter.ScanSamplingPointPresenter;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/10/20
 * class name
 */
@Router(value = Constant.AppCode.LIMS_ScanToReg)
@Presenter(ScanSamplingPointPresenter.class)
@Controller(value = {CommonScanController.class})
public class ScanCodeRegisterSampleActivity extends BaseControllerActivity implements ScanSamplingPointIdContract.View {
    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    @BindByTag("llScan")
    LinearLayout llScan;


    @Override
    protected int getLayoutID() {
        return R.layout.activity_scan_code_register_sample;
    }

    @Override
    protected void onInit() {
        super.onInit();
        searchTitle.showScan(false);
        searchTitle.setTitle(context.getResources().getString(R.string.lims_scan_code_register_sample));
        searchTitle.getSearchBtn().setVisibility(View.INVISIBLE);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);

        EventBus.getDefault().register(this);

    }


    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        searchTitle.getLeftActionBar().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RxView.clicks(llScan)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @SuppressLint("CheckResult")
                    @Override
                    public void accept(Object o) throws Exception {
                        getController(CommonScanController.class).openCameraScan();
                    }
                });

    }

    @Override
    protected void initData() {
        super.initData();
        getController(CommonScanController.class).openCameraScan();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReciver(CodeResultEvent codeResultEvent) {
        if (!StringUtil.isEmpty(codeResultEvent.scanResult)){
            String sRegex = "^[1-9]\\d*$";
            Pattern pattern = Pattern.compile(sRegex);
            Matcher matcher = pattern.matcher(codeResultEvent.scanResult);
            boolean rs = matcher.matches();
            if (rs){
                presenterRouter.create(ScanSamplingPointIdAPI.class).scanSamplingPointId(codeResultEvent.scanResult);
            }else {
                ToastUtils.show(context,context.getResources().getString(R.string.lims_scan_data_not_integer));
            }

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void scanSamplingPointIdSuccess(BAP5CommonEntity entity) {
        IntentRouter.go(context, Constant.AppCode.LIMS_SampleQuery);
    }

    @Override
    public void scanSamplingPointIdFailed(String errorMsg) {
        if (errorMsg.contains("</Br>")){
            errorMsg = errorMsg.replaceAll("</Br>", "\n");
        }
        ToastUtils.show(context,errorMsg);

    }
}
