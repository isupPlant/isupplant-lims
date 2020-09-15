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
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.controller.ReferenceController;
import com.supcon.mes.module_lims.event.SampleMaterialDataEvent;
import com.supcon.mes.module_lims.listener.OnSearchOverListener;
import com.supcon.mes.module_lims.model.bean.SampleMaterialEntity;
import com.supcon.mes.module_lims.model.contract.SampleMaterialListApi;
import com.supcon.mes.module_lims.presenter.SampleMaterialPresenter;
import com.supcon.mes.module_lims.ui.adapter.SampleMaterialReferenceAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */

@Router(value = Constant.AppCode.LIMS_MatInfoRef)
@Controller(value = {ReferenceController.class})
@Presenter(value = {SampleMaterialPresenter.class})
public class SampleMaterialReferenceActivity extends BaseRefreshRecyclerActivity<SampleMaterialEntity> implements SampleMaterialListApi.View {

    private SampleMaterialReferenceAdapter adapter;

    private Map<String, Object> params = new HashMap<>();
    private List<SampleMaterialEntity> list = new ArrayList<>();

    private boolean isSelectAll = false;
    private boolean radio = false; //是否为单选
    private String matInfoCodeList = "";

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("ll_select_all")
    LinearLayout llSelectAll;

    @BindByTag("iv_select")
    ImageView iv_select;

    @BindByTag("titleText")
    TextView titleText;

    @BindByTag("llIsRadio")
    LinearLayout llIsRadio;

    @BindByTag("btn_confirm")
    TextView btn_confirm;

    @Override
    protected IListAdapter<SampleMaterialEntity> createAdapter() {
        adapter = new SampleMaterialReferenceAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_sample_material_reference;
    }

    @Override
    protected void onInit() {
        super.onInit();
        getController(ReferenceController.class).setSearchTypeList(context.getResources().getString(R.string.lims_matCode),
                context.getResources().getString(R.string.lims_materiel_name),
                context.getResources().getString(R.string.lims_produce_batch));

        radio = getIntent().getBooleanExtra("radio",false);
        matInfoCodeList = getIntent().getStringExtra("matInfoCodeList");
        if (null == matInfoCodeList){
            matInfoCodeList = "";
        }
        refreshListController.setAutoPullDownRefresh(false);
        refreshListController.setPullDownRefreshEnabled(true);
        refreshListController.setEmpterAdapter(EmptyAdapterHelper.getRecyclerEmptyAdapter(context, getString(R.string.middleware_no_data)));
    }

    @Override
    protected void initView() {
        super.initView();

        StatusBarUtils.setWindowStatusBarColor(this, R.color.themeColor);
        titleText.setText(getResources().getString(R.string.lims_material_reagent_reference));
        setIsRadio();
        adapter.isRadio(radio);
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
                    if (radio){ //表示单选
                        for (int i = 0; i < adapter.getList().size(); i++) {
                            adapter.getList().get(i).setSelect(false);
                        }
                        adapter.getList().get(position).setSelect(true);
                        adapter.notifyDataSetChanged();

                        //单选的直接回传数据
                        SampleMaterialDataEvent event = new SampleMaterialDataEvent();
                        event.setData(adapter.getItem(position));
                        event.setRadio(radio);
                        EventBus.getDefault().post(event);
                        finish();
                    }else { //表示多选
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

        RxView.clicks(btn_confirm)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
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
                            SampleMaterialDataEvent event = new SampleMaterialDataEvent();
                            event.setList(list);
                            event.setRadio(radio);
                            EventBus.getDefault().post(event);
                            finish();
                        }else {
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_please_select_at_last_one_data));
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
                presenterRouter.create(com.supcon.mes.module_lims.model.api.SampleMaterialListApi.class).getSampleMaterialReference(pageIndex,params,matInfoCodeList);
            }
        });
    }

    private void goRefresh() {
        refreshListController.refreshBegin();
    }

    @Override
    public void getSampleMaterialReferenceSuccess(CommonListEntity entity) {
        if (entity.result.size() > 0){
            setSelectAllStyle(false);
        }
        refreshListController.refreshComplete(entity.result);
    }

    @Override
    public void getSampleMaterialReferenceFailed(String errorMsg) {
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

    private void setIsRadio(){
        if (radio){ //单选
            llIsRadio.setVisibility(View.GONE);
        }else {
            llIsRadio.setVisibility(View.VISIBLE);
        }
    }
}
