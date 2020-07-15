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
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseRefreshRecyclerActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshPageListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceListEntity;
import com.supcon.mes.module_lims.model.contract.QualityStandardReferenceApi;
import com.supcon.mes.module_lims.presenter.QualityStandardReferencePresenter;
import com.supcon.mes.module_lims.ui.adapter.QualityStandardReferenceAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/10
 * class name 质量标准参照页面
 */
@Router(value = "LIMS_QualityStdVerRef")
@Presenter(value = {QualityStandardReferencePresenter.class})
@Controller(value = {ReferenceController.class})
public class QualityStandardReferenceActivity extends BaseRefreshRecyclerActivity<QualityStandardReferenceEntity> implements QualityStandardReferenceApi.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("ll_select_all")
    LinearLayout llSelectAll;

    @BindByTag("iv_select")
    ImageView iv_select;

    @BindByTag("titleText")
    TextView titleText;

    private QualityStandardReferenceAdapter adapter;

    private Map<String, Object> params = new HashMap<>();

    private boolean isSelectAll = false;
    private String id = "";

    @Override
    protected IListAdapter<QualityStandardReferenceEntity> createAdapter() {
        adapter = new QualityStandardReferenceAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_quality_standard_reference;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList("质量标准","版本号");

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getString(R.string.lims_quality_standard_reference));

        id = getIntent().getStringExtra("id");

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

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 0){
                    adapter.getItem(position).setSelect(!adapter.getItem(position).isSelect());
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
                presenterRouter.create(com.supcon.mes.module_lims.model.api.QualityStandardReferenceApi.class).getQualityStandardReferenceList(pageIndex,id,params);
            }
        });
    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getQualityStandardReferenceListSuccess(QualityStandardReferenceListEntity entity) {
        if (entity.data.result.size() > 0){
            setSelectAllStyle(false);
        }
        refreshListController.refreshComplete(entity.data.result);
    }

    @Override
    public void getQualityStandardReferenceListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
    }

    private void setSelectAllStyle(boolean isSelectAll) {
        if (isSelectAll) {
            iv_select.setImageResource(R.drawable.ic_check_yes);
        } else {
            iv_select.setImageResource(R.drawable.ic_check_no);
        }
    }
}
