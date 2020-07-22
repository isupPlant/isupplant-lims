package com.supcon.mes.module_lims.ui;

import android.annotation.SuppressLint;
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
import com.google.gson.Gson;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.event.InspectionItemEvent;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.InspectionItemsEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemsListEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.contract.InspectionItemsApi;
import com.supcon.mes.module_lims.presenter.InspectionItemsPresenter;
import com.supcon.mes.module_lims.ui.adapter.InspectionItemAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/21
 * class name
 */
@Router(value = Constant.AppCode.LIMS_InspectComDataByStdVerId)
@Controller(value = {ReferenceController.class})
@Presenter(value = {InspectionItemsPresenter.class})
public class InspectionItemActivity extends BaseRefreshRecyclerActivity<InspectionItemsEntity> implements InspectionItemsApi.View {
    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("ll_select_all")
    LinearLayout llSelectAll;

    @BindByTag("iv_select")
    ImageView iv_select;

    @BindByTag("btn_confirm")
    TextView btn_confirm;

    @BindByTag("llEdit")
    LinearLayout llEdit;

    private String selectTag;
    private String stdVersionId;
    private String inspectStdId;
    private boolean isEdit = false;
    private InspectionItemAdapter adapter;
    private String jsonList;
    private boolean isSelectAll = false;
    private List<StdVerComIdEntity> intoList = new ArrayList<>();
    private List<StdVerComIdEntity> outList = new ArrayList<>();
    @Override
    protected IListAdapter<InspectionItemsEntity> createAdapter() {
        adapter = new InspectionItemAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_inspection_items;
    }

    @Override
    protected void onInit() {
        super.onInit();
        searchTitle.showScan(false);
        searchTitle.getSearchBtn().setVisibility(View.GONE);

        selectTag = getIntent().getStringExtra(Constant.IntentKey.SELECT_TAG);
        stdVersionId = getIntent().getStringExtra("stdVersionId");
        jsonList = getIntent().getStringExtra("stdVerCom");
        isEdit = getIntent().getBooleanExtra("isEdit",false);
        inspectStdId = getIntent().getStringExtra("inspectStdId");
        if (!StringUtil.isEmpty(jsonList)){
            intoList.clear();
            intoList.addAll(GsonUtil.jsonToList(jsonList,StdVerComIdEntity.class));
        }

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(false);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_inspection_items));

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

        if (isEdit){
            llEdit.setVisibility(View.VISIBLE);
        }else {
            llEdit.setVisibility(View.GONE);
        }
        adapter.setIsEdit(isEdit);
        goRefresh();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    adapter.notifyDataSetChanged();
                    int a = 0;
                    for (int i = 0; i < adapter.getList().size(); i++) {
                        if (adapter.getList().get(i).isSelect()) { //集合中存在勾选状态的 数据的话
                            a++;
                        }
                    }
                    if (a == adapter.getList().size()) {
                        setSelectAllStyle(true);
                    } else {
                        setSelectAllStyle(false);
                    }

                }
            }
        });

        RxView.clicks(llSelectAll)
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (!isSelectAll) {
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                adapter.getList().get(i).setSelect(true);
                            }
                        } else {
                            for (int i = 0; i < adapter.getList().size(); i++) {
                                adapter.getList().get(i).setSelect(false);
                            }
                        }
                        isSelectAll = !isSelectAll;
                        setSelectAllStyle(isSelectAll);
                        adapter.notifyDataSetChanged();
                    }
                });

        //点击确定
        RxView.clicks(btn_confirm)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        outList.clear();
                        List<InspectionItemsEntity> myList = adapter.getList();
                        for (int i = 0; i < myList.size(); i++) {
                            if (myList.get(i).isSelect()){
                                outList.add(myList.get(i).getStdVerComId());
                            }
                        }

                        if (outList.size() > 0){
                            InspectionItemEvent event = new InspectionItemEvent();
                            event.setList(outList);
                            EventBus.getDefault().post(new SelectDataEvent<>(event,selectTag));
                            finish();
                        }else {
                            ToastUtils.show(context,"请至少选择一条数据!");
                        }


                    }
                });



        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (isEdit){
                    presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionItemsApi.class).getInspectionItemsList(stdVersionId);
                }else {
                    presenterRouter.create(com.supcon.mes.module_lims.model.api.InspectionItemsApi.class).getInspectComDataByInspectStdId(inspectStdId);
                }

            }
        });
    }

    @Override
    public void getInspectionItemsListSuccess(InspectionItemsListEntity entity) {
        if (entity.data.result.size() > 0){
            setSelectAllStyle(false);
        }
            if (null!= intoList && intoList.size() > 0){
                for (int i = 0; i < intoList.size(); i++) {
                    for (int j = 0; j < entity.data.result.size(); j++) {
                        if (intoList.get(i).getId().equals(entity.data.result.get(j).getStdVerComId().getId())){
                            entity.data.result.get(j).setSelect(true);
                        }
                    }
                }
            }



        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getInspectionItemsListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    public void getInspectComDataByInspectStdIdSuccess(InspectionItemsListEntity entity) {
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getInspectComDataByInspectStdIdFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    private void setSelectAllStyle(boolean isSelectAll) {
        if (isSelectAll) {
            iv_select.setImageResource(R.drawable.ic_check_yes);
        } else {
            iv_select.setImageResource(R.drawable.ic_check_no);
        }
    }
}
