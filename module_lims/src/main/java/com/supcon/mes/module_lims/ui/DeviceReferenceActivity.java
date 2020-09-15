package com.supcon.mes.module_lims.ui;

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
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.DeviceReferenceEntity;
import com.supcon.mes.module_lims.model.contract.DeviceReferenceApi;
import com.supcon.mes.module_lims.presenter.DeviceReferencePresenter;
import com.supcon.mes.module_lims.presenter.DeviceTypeReferencePresenter;
import com.supcon.mes.module_lims.ui.adapter.DeviceReferenceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * author huodongsheng
 * on 2020/8/13
 * class name
 */
@Router(value = Constant.AppCode.LIMS_EamInfoRefPart)
@Presenter(value = {DeviceReferencePresenter.class})
@Controller(value = {ReferenceController.class})
public class DeviceReferenceActivity extends BaseRefreshRecyclerActivity<DeviceReferenceEntity> implements DeviceReferenceApi.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")

    TextView titleText;
    private String selectTag;
    private String eamClassId = "";
    private Map<String, Object> params = new HashMap<>();
    private DeviceReferenceAdapter adapter;
    @Override
    protected IListAdapter<DeviceReferenceEntity> createAdapter() {
        adapter = new DeviceReferenceAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_device_reference;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList(context.getResources().getString(R.string.lims_equipment_code),context.getResources().getString(R.string.lims_device_name));

        selectTag = getIntent().getStringExtra(Constant.IntentKey.SELECT_TAG);
        eamClassId = getIntent().getStringExtra("eamClassId");

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_device_reference));

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

    @Override
    protected void initListener() {
        super.initListener();
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        adapter.getList().get(i).setSelect(false);
                    }
                    adapter.getList().get(position).setSelect(true);
                    adapter.notifyDataSetChanged();

                    EventBus.getDefault().post(new SelectDataEvent<>(adapter.getList().get(position),selectTag));
                    finish();
                }
            }
        });

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
                presenterRouter.create(com.supcon.mes.module_lims.model.api.DeviceReferenceApi.class).getDeviceReferenceList(pageIndex,eamClassId,params);
            }
        });
    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getDeviceReferenceListSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getDeviceReferenceListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }
}
