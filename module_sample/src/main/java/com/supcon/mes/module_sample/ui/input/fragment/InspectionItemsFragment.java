package com.supcon.mes.module_sample.ui.input.fragment;

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
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_sample.model.contract.InspectionItemsApi;
import com.supcon.mes.module_sample.presenter.InspectionItemsPresenter;
import com.supcon.mes.module_sample.ui.adapter.InspectionItemsListAdapter;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
@Presenter(value = {InspectionItemsPresenter.class})
public class InspectionItemsFragment extends BaseRefreshRecyclerFragment<InspectionItemsEntity> implements InspectionItemsApi.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    private InspectionItemsListAdapter adapter;
    SampleResultInputActivity activity;

    private Long mSampleId;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (SampleResultInputActivity) context;
    }


    @Override
    protected IListAdapter<InspectionItemsEntity> createAdapter() {
        adapter = new InspectionItemsListAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_inspetion_items;
    }

    @Override
    protected void onInit() {
        super.onInit();
        searchTitle.showScan(false);
        searchTitle.findViewById(R.id.leftBtn).setVisibility(View.GONE);
        searchTitle.findViewById(R.id.ivSearchBtn).setVisibility(View.GONE);

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
                    outRect.set(DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context), DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                } else {
                    outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(10, context));
                }
            }
        });
    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionItemsApi.class).getInspectionItemList(mSampleId+"",1);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    List<InspectionItemsEntity> list = adapter.getList();
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setSelect(false);
                    }
                    list.get(position).setSelect(true);
                    adapter.notifyDataSetChanged();

                    activity.setSampleTesId(list.get(position).getId());

                }
            }
        });

        activity.setOnRefreshInspectionItemListener(new SampleResultInputActivity.OnRefreshInspectionItemListener() {
            @Override
            public void onRefreshInspectionItem(Long sampleId) {
                mSampleId = sampleId;
                goRefresh();
            }
        });

        activity.setOnSampleRefreshListener(new SampleResultInputActivity.OnSampleRefreshListener() {
            @Override
            public void onSampleRefresh() {
                mSampleId = -1l;
                goRefresh();
            }
        });
    }

    public void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getInspectionItemListSuccess(CommonListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getInspectionItemListFailed(String errorMsg) {
        //SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }


}
