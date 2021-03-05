package com.supcon.mes.module_lims.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.api.SerialDeviceRefAPI;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.model.contract.SerialDeviceRefContract;
import com.supcon.mes.module_lims.presenter.SerialDeviceRefPresenter;
import com.supcon.mes.module_lims.service.SerialWebSocketService;
import com.supcon.mes.module_lims.ui.adapter.SerialDeviceReferenceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
@Router(value = LimsConstant.AppCode.LIMS_SerialRef)
@Presenter(value = {SerialDeviceRefPresenter.class})
@Controller(value = {ReferenceController.class})
public class SerialDeviceReferenceActivity extends BaseRefreshRecyclerActivity<SerialDeviceEntity> implements SerialDeviceRefContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;


    @BindByTag("btn_confirm")
    TextView btn_confirm;

    private SerialDeviceReferenceAdapter adapter;
    private String selectTag;
    private Map<String, Object> params = new HashMap<>();
    private List<SerialDeviceEntity> list = new ArrayList<>();

    @Override
    protected IListAdapter<SerialDeviceEntity> createAdapter() {
        adapter = new SerialDeviceReferenceAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_serial_device_reference;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList(getResources().getString(R.string.lims_equipment_name), getResources().getString(R.string.lims_equipment_code));

        selectTag = getIntent().getStringExtra(Constant.IntentKey.SELECT_TAG);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_sampling_point_reference));

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

        getController(ReferenceController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                params.clear();
                params.putAll(map);
                goRefresh();
            }
        });

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(SerialDeviceRefAPI.class).getSerialDeviceRef(pageIndex, params);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0) {
                    if (adapter.getItem(position).getSerialType().getId() == null || adapter.getItem(position).getSerialType().getId().equals("BaseSet_serialType/direct")) {
                        ToastUtils.show(context, getResources().getString(R.string.lims_acquisition_mode_error));
                        return;
                    }
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        adapter.getList().get(i).setSelect(false);
                    }
                    adapter.getItem(position).setSelect(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });


        RxView.clicks(btn_confirm)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        list.clear();
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()) {
                                list.add(adapter.getList().get(i));
                            }
                        }
                        if (list.size() > 0) {
                            String openSendMsg = "commConf:"+list.get(0).getSerialPort()+","+list.get(0).getBaudRate()+","+list.get(0).getCheckDigit()+","+list.get(0).getDataBits()+","+list.get(0).getStopBits();
                            Intent intent = new Intent(SupPlantApplication.getAppContext(), SerialWebSocketService.class);
                            intent.setAction(SerialWebSocketService.START_SERIAL_SERVICE);
                            intent.putExtra("url",list.get(0).getSerialServerIp());
                            intent.putExtra("openSendMsg",openSendMsg);
                            SupPlantApplication.getAppContext().startService(intent);
                            EventBus.getDefault().post(new SelectDataEvent<>(list.get(0), selectTag));
                            finish();
                        } else {
                            ToastUtils.show(context, context.getResources().getString(R.string.lims_please_select_at_last_one_data));
                        }
                    }
                });
    }


    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getSerialDeviceRefSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSerialDeviceRefFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }
}
