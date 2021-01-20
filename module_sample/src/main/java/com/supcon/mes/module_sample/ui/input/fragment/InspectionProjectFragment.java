package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.listener.InspectionSubRefreshListener;
import com.supcon.mes.module_sample.model.api.InspectionItemsAPI;
import com.supcon.mes.module_sample.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_sample.model.contract.InspectionItemsContract;
import com.supcon.mes.module_sample.presenter.InspectionItemsPresenter;
import com.supcon.mes.module_sample.ui.adapter.InspectionItemsListAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
@SuppressLint("ValidFragment")
@Presenter(value = {InspectionItemsPresenter.class})
public class InspectionProjectFragment extends BaseRefreshRecyclerFragment<InspectionItemsEntity> implements InspectionItemsContract.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("title")
    TextView title;

    private InspectionItemsListAdapter adapter;
    BaseFragmentActivity activity;

    private Long mSampleId;
    private String mTitle;
    private boolean isOutNotify = false;
    private String notifyType = "";
    private int position = 0;
    private InspectionSubRefreshListener mInspectionSubRefreshListener;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SampleResultInputActivity){
            activity = (SampleResultInputActivity) context;
        }else {
            activity = (ProjectInspectionItemsActivity) context;
        }

    }

    @SuppressLint("ValidFragment")
    public InspectionProjectFragment(Long mSampleId, String mTitle) {
        this.mSampleId = mSampleId;
        this.mTitle = mTitle;
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
        EventBus.getDefault().register(this);
        title.setText(mTitle);

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

        if (activity instanceof ProjectInspectionItemsActivity){
            title.setVisibility(View.VISIBLE);
            goRefresh();
        }else {
            title.setVisibility(View.GONE);
        }

        goRefresh();
    }

    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(InspectionItemsAPI.class).getInspectionItemList(mSampleId+"",1);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){

                    itemClickListener(position);

                }
            }
        });

        if (activity instanceof SampleResultInputActivity){
            ((SampleResultInputActivity)activity).setOnRefreshInspectionItemListener(new SampleResultInputActivity.OnRefreshInspectionItemListener() {
                @Override
                public void onRefreshInspectionItem(Long sampleId) {
                    mSampleId = sampleId;
                    goRefresh();
                }
            });


            ((SampleResultInputActivity)activity).setOnSampleRefreshListener(new SampleResultInputActivity.OnSampleRefreshListener() {
                @Override
                public void onSampleRefresh() {
                    mSampleId = -1l;
                    goRefresh();
                }
            });

//            ((SampleResultInputPADActivity) activity).setOnSampleRefreshListener(new SampleResultInputPADActivity.OnSampleRefreshListener() {
//                @Override
//                public void onSampleRefresh() {
//                    mSampleId = null;
//                    goRefresh();
//                }
//            });


        }



    }

    public void goRefresh(){
        refreshListController.refreshBegin();
    }

    public void itemClickListener(int position){
        List<InspectionItemsEntity> list = adapter.getList();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(false);
        }
        list.get(position).setSelect(true);
        adapter.notifyDataSetChanged();

        if (activity instanceof SampleResultInputActivity){
            ((SampleResultInputActivity)activity).setSampleTesId(list.get(position).getId());
        }else if (activity instanceof ProjectInspectionItemsActivity){
            String name;
            if (null ==  list.get(position).getTestId()){
                name = "--";
            }else {
                name = list.get(position).getTestId().getName();
            }
            ((ProjectInspectionItemsActivity)activity).notifyInspectionItemsRefresh(list.get(position).getId(),name);
        }
    }


    public void refreshItem(int position){
        itemClickListener(position);
    }


    public void lookNext(InspectionSubRefreshListener mInspectionSubRefreshListener){
        this.mInspectionSubRefreshListener = mInspectionSubRefreshListener;
        isOutNotify = true;
        notifyType = "submit";
        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i).isSelect()){
                position = i;
                break;
            }
        }
//        if (position+1 == adapter.getList().size()){  //表示上次选中的已经是最后一条了
//            mInspectionSubRefreshListener.refreshOver(position, adapter.getList());
//        }else {
//            presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionItemsApi.class).getInspectionItemList(mSampleId+"",1);
//        }
        presenterRouter.create(InspectionItemsAPI.class).getInspectionItemList(mSampleId+"",1);

    }

    public void againRefresh(){
        isOutNotify = true;
        notifyType = "save";
        for (int i = 0; i < adapter.getList().size(); i++) {
            if (adapter.getList().get(i).isSelect()){
                position = i;
                break;
            }
        }
        presenterRouter.create(InspectionItemsAPI.class).getInspectionItemList(mSampleId+"",1);
    }

    @Override
    public void getInspectionItemListSuccess(CommonListEntity entity) {
        //请求数据回来默认 第一个item 为选中状态
        if (isOutNotify){
            List<InspectionItemsEntity> list = entity.result;
            refreshListController.refreshComplete(list);
            if (notifyType.equals("save")){  //保存
                itemClickListener(position);
            }else if (notifyType.equals("submit")){ //提交
                if (null != mInspectionSubRefreshListener){
                    mInspectionSubRefreshListener.refreshOver(list);
                }
            }
        }else {
            List<InspectionItemsEntity> list = entity.result;
            for (int i = 0; i < list.size(); i++) {
                if (i == 0){
                    list.get(i).setSelect(true);
                }
            }
            refreshListController.refreshComplete(list);

            //通知Activity 去刷新检验分项的数据
            if (activity instanceof ProjectInspectionItemsActivity){
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).isSelect()){
                        String name;
                        if (null ==  list.get(i).getTestId()){
                            name = "--";
                        }else {
                            name = list.get(i).getTestId().getName();
                        }
                        ((ProjectInspectionItemsActivity) activity).notifyInspectionItemsRefresh(list.get(i).getId(),name);
                        break;
                    }

                }

            }
        }

    }

    @Override
    public void getInspectionItemListFailed(String errorMsg) {
        refreshListController.refreshComplete(null);
        if (isOutNotify){
             if (notifyType.equals("submit")){ //提交
                if (null != mInspectionSubRefreshListener){
                    mInspectionSubRefreshListener.refreshOver(new ArrayList<>());
                }
            }
        }
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshEvent(SelectDataEvent dataEvent){
        if ("refreshData".equals(dataEvent.getSelectTag())){
            if (adapter!=null) {
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);

    }
}
