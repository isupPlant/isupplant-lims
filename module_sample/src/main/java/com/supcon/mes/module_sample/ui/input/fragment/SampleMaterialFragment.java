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
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.event.SampleMaterialDataEvent;
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleTestMaterialListAPI;
import com.supcon.mes.module_sample.model.api.TestMaterialListAPI;
import com.supcon.mes.module_sample.model.bean.SampleTestMaterialEntity;
import com.supcon.mes.module_sample.model.contract.SampleTestMaterialListContract;
import com.supcon.mes.module_sample.model.contract.TestMaterialListContract;
import com.supcon.mes.module_sample.presenter.SampleTestMaterialPresenter;
import com.supcon.mes.module_sample.presenter.TestMaterialPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleTestMaterialAdapter;

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
@Presenter(value = {SampleTestMaterialPresenter.class})
public class SampleMaterialFragment extends BaseRefreshRecyclerFragment<SampleTestMaterialEntity> implements SampleTestMaterialListContract.View {
    @BindByTag("llDelete")
    LinearLayout llDelete;
    @BindByTag("llReference")
    LinearLayout llReference;
    @BindByTag("contentView")
    RecyclerView contentView;
    public Long sampleTesId;

    private SampleTestMaterialAdapter adapter;

    private List<String> deleteList = new ArrayList<>();
    private List<SampleTestMaterialEntity> materialList = new ArrayList<>();
    @Override
    protected IListAdapter<SampleTestMaterialEntity> createAdapter() {
        adapter = new SampleTestMaterialAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_material;
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
    protected void initData() {
        super.initData();
        goRefresh();
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
                presenterRouter.create(SampleTestMaterialListAPI.class).getSampleTestMaterial(sampleTesId+"");
            }
        });

        //????????????
        RxView.clicks(llReference)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (i < adapter.getList().size() - 1) {
                                sb.append(adapter.getList().get(i).getMatCode()).append(",");
                            } else if (i == (adapter.getList().size() - 1)) {
                                sb.append(adapter.getList().get(i).getMatCode());
                            }
                        }
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("radio", false);
                        bundle.putString("matInfoCodeList",sb.toString());
                        IntentRouter.go(context, Constant.AppCode.LIMS_MatInfoRef, bundle);
                    }
                });

        //????????????
//        RxView.clicks(llDelete)
//                .throttleFirst(300, TimeUnit.MILLISECONDS)
//                .subscribe(new Consumer<Object>() {
//                    @Override
//                    public void accept(Object o) throws Exception {
//                        int a = 0;
//                        for (int i = 0; i < adapter.getList().size(); i++) {
//                            if (adapter.getList().get(i).isSelect()){
//                                if (null != adapter.getList().get(i).getId()){
//                                    deleteList.add(adapter.getList().get(i).getId()+"");
//                                }
//                                adapter.remove(i);
//                                adapter.notifyDataSetChanged();
//                                a++;
//                            }
//                        }
//                        if (a == 0){
//                            ToastUtils.show(context,context.getResources().getString(R.string.lims_select_one_operate));
//                        }
//                    }
//                });

//        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
//            @Override
//            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
//                if (action == 0){
//                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
//                    for (int i = 0; i < adapter.getList().size(); i++) {
//                        if (i != position){
//                            adapter.getList().get(i).setSelect(false);
//                        }
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//            }
//        });
    }

    public void setSampleTesId(Long sampleTesId){
        this.sampleTesId = sampleTesId;
        goRefresh();
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectMaterial(SampleMaterialDataEvent event){
//        if (!event.isRadio()){
//            if (null != event.getList()){
//                List<SampleTestMaterialEntity> list = event.getList();
//                for (int i = 0; i < list.size(); i++) {
//                    SampleTestMaterialEntity testMaterialEntity = new SampleTestMaterialEntity();
//                    testMaterialEntity.setBatchCode(list.get(i).getBatchCode());
//                    testMaterialEntity.setMatCode(list.get(i).getCode());
//                    list.get(i).getProductId().setMainUnit(list.get(i).getUnitId());
//                    testMaterialEntity.setProductId(list.get(i).getProductId());
//                    adapter.getList().add(testMaterialEntity);
//                }
//                refreshListController.refreshComplete(adapter.getList());
//            }
//        }
    }

    @Override
    public void getSampleTestMaterialSuccess(CommonListEntity entity) {
        materialList.clear();
        List<SampleTestMaterialEntity> list = entity.result;
        String string = GsonUtil.gsonString(list);
        materialList = GsonUtil.jsonToList(string, SampleTestMaterialEntity.class);
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSampleTestMaterialFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
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



    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
