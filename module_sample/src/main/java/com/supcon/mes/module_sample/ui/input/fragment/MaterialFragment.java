package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.graphics.Rect;
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
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;
import com.supcon.mes.module_sample.model.contract.TestMaterialListApi;
import com.supcon.mes.module_sample.presenter.TestMaterialPresenter;
import com.supcon.mes.module_sample.ui.adapter.TestMaterialAdapter;

import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {TestMaterialPresenter.class})
public class MaterialFragment extends BaseRefreshRecyclerFragment<TestMaterialEntity> implements TestMaterialListApi.View {
    @BindByTag("llDelete")
    LinearLayout llDelete;
    @BindByTag("llReference")
    LinearLayout llReference;
    @BindByTag("contentView")
    RecyclerView contentView;
    private Long sampleTesId;

    private TestMaterialAdapter adapter;
    @Override
    protected IListAdapter<TestMaterialEntity> createAdapter() {
        adapter = new TestMaterialAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_material;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        contentView.setLayoutManager(new LinearLayoutManager(context));
        contentView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                int childLayoutPosition = parent.getChildAdapterPosition(view);
                if (childLayoutPosition == 0) {
                    outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
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
                presenterRouter.create(com.supcon.mes.module_sample.model.api.TestMaterialListApi.class).getTestMaterial(sampleTesId+"");
            }
        });

        //参照按钮
        RxView.clicks(llReference)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {

                    }
                });

        //删除按钮
        RxView.clicks(llDelete)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        int a = 0;
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()){
                                adapter.remove(i);
                                adapter.notifyDataSetChanged();
                                a++;
                            }
                        }
                        if (a == 0){
                            ToastUtils.show(context,"请先选中一条记录进行操作！");
                        }
                    }
                });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (i != position){
                            adapter.getList().get(i).setSelect(false);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void setSampleTesId(Long sampleTesId){
        this.sampleTesId = sampleTesId;
        goRefresh();
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getTestMaterialSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getTestMaterialFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }
}