package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.DeviceReferenceEntity;
import com.supcon.mes.module_lims.model.bean.DeviceTypeReferenceEntity;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.EamIdEntity;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.contract.TestDeviceListApi;
import com.supcon.mes.module_sample.presenter.TestDevicePresenter;
import com.supcon.mes.module_sample.ui.adapter.TestDeviceAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {TestDevicePresenter.class})
public class EquipmentFragment extends BaseRefreshRecyclerFragment<TestDeviceEntity> implements TestDeviceListApi.View {
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("llAdd")
    LinearLayout llAdd;
    @BindByTag("llDelete")
    LinearLayout llDelete;

    private Long sampleTesId;

    private TestDeviceAdapter adapter;
    private int mPosition = -1;

    private List<String> deleteList = new ArrayList<>();
    @Override
    protected IListAdapter<TestDeviceEntity> createAdapter() {
        adapter = new TestDeviceAdapter(context);
        return adapter;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        deleteList.clear();

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

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(com.supcon.mes.module_sample.model.api.TestDeviceListApi.class).getTestDevice(sampleTesId+"");
            }
        });

        RxView.clicks(llAdd)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        TestDeviceEntity entity = new TestDeviceEntity();
                        adapter.getList().add(entity);
                        refreshListController.refreshComplete(adapter.getList());
                    }
                });

        RxView.clicks(llDelete)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        int a = 0;
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()){

                                if (null != adapter.getList().get(i).getId()){
                                    deleteList.add(adapter.getList().get(i).getId()+"");
                                }

                                adapter.remove(i);
                                adapter.notifyDataSetChanged();
                                a++;
                            }
                        }
                        if (a == 0){
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_select_one_operate));
                        }
                    }
                });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                mPosition = position;
                if (action == 0){
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (i != position){
                            adapter.getList().get(i).setSelect(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }else if (action == 1){ //设备类型
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.SELECT_TAG,childView.getTag()+"");
                    IntentRouter.go(context,Constant.AppCode.LIMS_EamTypeRefPart,bundle);
                }else if (action == -1){ //设备类型删除动作
                    adapter.getList().get(position).setEamTypeId(null);
                    adapter.notifyItemChanged(position);
                }else if (action == 2){ //设备编码
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.IntentKey.SELECT_TAG,childView.getTag()+"");
                    bundle.putString("eamClassId",adapter.getList().get(position).getEamTypeId() == null ? "" : adapter.getList().get(position).getEamTypeId().getId()+"");
                    IntentRouter.go(context,Constant.AppCode.LIMS_EamInfoRefPart,bundle);
                }else if (action == -2){ //设备编码删除动作
                    adapter.getList().get(position).setEamId(null);
                    adapter.notifyItemChanged(position);
                }
            }
        });
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_equipment;
    }

    public void setSampleTesId(Long sampleTesId){
        this.sampleTesId = sampleTesId;
        goRefresh();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectDataEvent(SelectDataEvent selectDataEvent){
        if (selectDataEvent.getEntity() instanceof DeviceTypeReferenceEntity){
            if (selectDataEvent.getSelectTag().equals("ctDeviceType")){
                DeviceTypeReferenceEntity deviceTypeReferenceEntity = (DeviceTypeReferenceEntity) selectDataEvent.getEntity();
                BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
                entity.setName(deviceTypeReferenceEntity.getName());
                entity.setId(deviceTypeReferenceEntity.getId());
                adapter.getList().get(mPosition).setEamTypeId(entity);
                adapter.notifyItemChanged(mPosition);
            }
        }else if (selectDataEvent.getEntity() instanceof DeviceReferenceEntity){
            if (selectDataEvent.getSelectTag().equals("ctDeviceCode")){
                DeviceReferenceEntity deviceReferenceEntity = (DeviceReferenceEntity) selectDataEvent.getEntity();
                EamIdEntity bean = new EamIdEntity();
                bean.setName(deviceReferenceEntity.getName());
                bean.setCode(deviceReferenceEntity.getCode());
                bean.setState(deviceReferenceEntity.getState());
                bean.setId(deviceReferenceEntity.getId());
                adapter.getList().get(mPosition).setEamId(bean);
                adapter.notifyItemChanged(mPosition);
            }
        }
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getTestDeviceSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getTestDeviceFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    public List<TestDeviceEntity> getTestDeviceList(){
        return adapter.getList();
    }

    public String getDeleteList() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < deleteList.size(); i++) {
            if (i < deleteList.size() - 1) {
                sb.append(deleteList.get(i)).append(",");
            } else if (i == (deleteList.size() - 1)) {
                sb.append(deleteList.get(i));
            }
        }
        return sb.toString();
    }

    public boolean checkTestDevice(){
        for (int i = 0; i < adapter.getList().size(); i++) {
            if (null == adapter.getList().get(i).getEamId() || StringUtil.isEmpty(adapter.getList().get(i).getEamId().getCode())){
                ToastUtils.show(context,getResources().getString(R.string.lims_device_check));
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
