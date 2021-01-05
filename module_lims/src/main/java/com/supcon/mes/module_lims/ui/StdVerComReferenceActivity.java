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
import com.supcon.common.view.ptr.PtrFrameLayout;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.util.StatusBarUtils;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonBAPListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.event.QualityStandardEvent;
import com.supcon.mes.module_lims.event.StdVerComEvent;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.api.StdVerComRefAPI;
import com.supcon.mes.module_lims.model.bean.QualityStandardReferenceEntity;
import com.supcon.mes.module_lims.model.bean.StdVerComIdEntity;
import com.supcon.mes.module_lims.model.contract.StdVerComRefContract;
import com.supcon.mes.module_lims.presenter.StdVerComRefPresenter;
import com.supcon.mes.module_lims.ui.adapter.StdVerComReferenceAdapter;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/11/12
 * class name
 */
@Router(value = Constant.AppCode.LIMS_StdVerComRef)
@Presenter(value = {StdVerComRefPresenter.class})
@Controller(value = {ReferenceController.class})
public class StdVerComReferenceActivity extends BaseRefreshRecyclerActivity<StdVerComIdEntity> implements StdVerComRefContract.View {
    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;
    @BindByTag("contentView")
    RecyclerView contentView;
    @BindByTag("iv_select")
    ImageView iv_select;
    @BindByTag("ll_select_all")
    LinearLayout ll_select_all;
    @BindByTag("btn_confirm")
    TextView btn_confirm;

    private boolean isRadio;
    private String stdVerId;
    private String reportNames;
    private String selectTag;
    private boolean isSelectAll;
    private Map<String, Object> map = new HashMap<>();
    private StdVerComReferenceAdapter adapter;
    private List<StdVerComIdEntity> list = new ArrayList<>();
    private String title;
    @Override
    protected IListAdapter<StdVerComIdEntity> createAdapter() {
        adapter = new StdVerComReferenceAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_std_ver_com_ref;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList(getResources().getString(R.string.lims_inspection_items));

        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();
        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);


        stdVerId = getIntent().getStringExtra("stdVerId");
        reportNames =  getIntent().getStringExtra("reportNames");
        isRadio = getIntent().getBooleanExtra("isRadio", false);
        selectTag = getIntent().getStringExtra("selectTag");
        title = getIntent().getStringExtra("title");
        searchTitle.setTitle(title == null ? getString(R.string.lims_quality_standard_reference) : title);
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

        if (isRadio){
            ll_select_all.setVisibility(View.GONE);
        }else {
            ll_select_all.setVisibility(View.VISIBLE);
        }
        goRefresh();
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();

        refreshListController.setOnRefreshPageListener(new OnRefreshPageListener() {
            @Override
            public void onRefresh(int pageIndex) {
                presenterRouter.create(StdVerComRefAPI.class).getStdVerComRefList(stdVerId,reportNames,pageIndex+"",map);
            }
        });

        getController(ReferenceController.class).setSearchOverListener(new OnSearchOverListener() {
            @Override
            public void onSearchOverClick(Map<String, Object> map) {
                map.clear();
                map.putAll(map);
                goRefresh();
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                if (action == 1){
                    if (isRadio){
                        adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (i != position){
                                adapter.getList().get(i).setSelect(false);
                            }
                        }

                    }else {
                        adapter.getList().get(position).setSelect(!adapter.getList().get(position).isSelect());
                    }
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

        RxView.clicks(ll_select_all)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
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
                .throttleFirst(300, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        list.clear();
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            if (adapter.getList().get(i).isSelect()){
                                list.add(adapter.getList().get(i));
                            }
                        }
                        if (list.size() > 0){
                            EventBus.getDefault().post(new SelectDataEvent<>(new StdVerComEvent(list),selectTag));
                            finish();
                        }else {
                            ToastUtils.show(context,getResources().getString(R.string.lims_please_select_at_last_one_data));
                        }


                    }
                });
    }

    private void goRefresh(){
        refreshListController.refreshBegin();
    }

    @Override
    public void getStdVerComRefListSuccess(CommonBAPListEntity entity) {
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getStdVerComRefListFailed(String errorMsg) {
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
