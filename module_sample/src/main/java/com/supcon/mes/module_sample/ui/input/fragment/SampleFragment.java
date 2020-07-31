package com.supcon.mes.module_sample.ui.input.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.com_http.BaseEntity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleEntity;
import com.supcon.mes.module_sample.model.contract.SampleListApi;
import com.supcon.mes.module_sample.presenter.SampleListPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleListAdapter;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
@Presenter(value = {SampleListPresenter.class})
public class SampleFragment extends BaseRefreshRecyclerFragment<SampleEntity> implements SampleListApi.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    private SampleListAdapter adapter;
    SampleResultInputActivity activity;

    private Map<String, Object> mParams = new HashMap<>();

    private int mPosition = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SampleResultInputActivity) context;
    }

    @Override
    protected IListAdapter<SampleEntity> createAdapter() {
        adapter = new SampleListAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_sample;
    }

    @Override
    protected void onInit() {
        super.onInit();
        searchTitle.showScan(false);
        searchTitle.findViewById(R.id.leftBtn).setVisibility(View.GONE);
        searchTitle.findViewById(R.id.ivSearchBtn).setVisibility(View.GONE);

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
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
        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(com.supcon.mes.module_sample.model.api.SampleListApi.class).getSampleList(pageIndex,mParams);
            }
        });

        adapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            if (action == 0){  //mPosition 为记录上次点击的下标位置，如果点击的是与上次的同一条目，就直接return
                if (mPosition == position){
                    return;
                }

                List<SampleEntity> list = adapter.getList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelect(false);
                }
                list.get(position).setSelect(true);
                adapter.notifyDataSetChanged();
                mPosition = position;
                //通知 检验项目更新数据
                activity.setSampleId(list.get(position).getId());
            }
        });

        activity.setOnChangeParamsListener(new SampleResultInputActivity.OnChangeParamsListener() {
            @Override
            public void onChangeParams(Map<String, Object> params) {
                mParams = params;
                goRefresh();
            }
        });
    }

    public void goRefresh(){
        refreshListController.refreshBegin();
    }



    @Override
    public void getSampleListSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
        activity.sampleRefresh();
    }

    @Override
    public void getSampleListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }
}
