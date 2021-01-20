package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.DisplayUtil;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.view.CustomImageButton;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.DateFilterController;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.bean.SearchResultEntity;
import com.supcon.mes.middleware.model.event.EventInfo;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.SnackbarHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.middleware.util.TimeUtil;
import com.supcon.mes.module_lims.model.bean.SampleEntity;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.api.SampleListAPI;
import com.supcon.mes.module_sample.model.contract.SampleListContract;
import com.supcon.mes.module_sample.presenter.SampleListPresenter;
import com.supcon.mes.module_sample.ui.adapter.SampleListAdapter;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputPADActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputPDAActivity;
import com.supcon.mes.module_scan.controller.CommonScanController;
import com.supcon.mes.module_scan.model.event.CodeResultEvent;
import com.supcon.mes.module_scan.util.scanCode.CodeUtlis;
import com.supcon.mes.module_search.ui.view.SearchTitleBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;

/**
 * author huodongsheng
 * on 2020/7/29
 * class name
 */
@Controller(value = {CommonScanController.class,  DateFilterController.class})
@Presenter(value = {SampleListPresenter.class})
public class SampleFragment extends BaseRefreshRecyclerFragment<SampleEntity> implements SampleListContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("searchTitle")
    SearchTitleBar searchTitle;

    @BindByTag("leftBtn")
    CustomImageButton leftBtn;

    @BindByTag("titleText")
    TextView titleText;

    private List<String> searchTypeList = new ArrayList<>();
    private Map<String, Object> mParams = new HashMap<>();

    private SampleListAdapter adapter;
    BaseFragmentActivity activity;

    private SearchResultEntity resultEntity;
    private String searchKey;
    private String title;
    private boolean isFinish = false;
    private boolean scan = false;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SampleResultInputPADActivity){
            activity = (SampleResultInputPADActivity) context;
        }else if (context instanceof SampleResultInputActivity){
            activity = (SampleResultInputActivity) context;
        }
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
        EventBus.getDefault().register(this);
        titleText.setText(getString(R.string.lims_result_input));
        selectPosition=-1;
        searchTypeList.add(getString(R.string.lims_sample_code));
        searchTypeList.add(getString(R.string.lims_sample_name));
        searchTypeList.add(getString(R.string.lims_batch_number));

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
                    outRect.set(DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(13, context), DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(13, context));
                } else {
                    outRect.set(DisplayUtil.dip2px(12, context), 0, DisplayUtil.dip2px(12, context), DisplayUtil.dip2px(13, context));
                }
            }
        });

        if (activity instanceof SampleResultInputPADActivity){
            searchTitle.setVisibility(View.GONE);
        }else {
            searchTitle.setVisibility(View.VISIBLE);
        }

        goRefresh();
    }
    public static int selectPosition=-1;
    Map<String,Object> timeMap=new HashMap<>();
    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SampleListAPI.class).getSampleList(timeMap,mParams);
            }
        });


        if (activity instanceof  SampleResultInputPADActivity){
            ((SampleResultInputPADActivity) activity).setOnSampleRefreshListener(new SampleResultInputPADActivity.OnSampleRefreshListener() {
                @Override
                public void onSampleRefresh() {
                    goRefresh();
                }
            });
        }


        leftBtn.setOnClickListener(v -> activity.onBackPressed());

        getController(DateFilterController.class).setDateChecked(Constant.Date.TODAY);
        String[] dates= TimeUtil.getTimePeriod(Constant.Date.TODAY);
        String startTime = dates[0];
        String endTime = dates[1];
        timeMap.put(Constant.BAPQuery.IN_DATE_START, startTime);
        timeMap.put(Constant.BAPQuery.IN_DATE_END, endTime);

        getController(DateFilterController.class).setDateSelectListener((start, end) -> {
            if (start.isEmpty() && start.isEmpty()){
                timeMap.remove(Constant.BAPQuery.IN_DATE_START);
                timeMap.remove(Constant.BAPQuery.IN_DATE_END);
            }else {
                timeMap.put(Constant.BAPQuery.IN_DATE_START, start);
                timeMap.put(Constant.BAPQuery.IN_DATE_END, end);
            }
            refreshListController.refreshBegin();
        });
        //当前页面搜索图标的的点击事件
        RxView.clicks(searchTitle.getSearchBtn())
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList(Constant.IntentKey.SEARCH_LIST, (ArrayList<String>) searchTypeList);
                        IntentRouter.go(context, Constant.Router.SEARCH_HISTORY, bundle);
                    }
                });

        //从 历史搜索页面跳转到当前页面的搜索框的点击事件（只要点击 立马跳转到历史搜索页面）
        searchTitle.setSearchClick(new SearchTitleBar.SearchEventListener() {
            @Override
            public void searchClick(boolean isDelete) {
                SearchResultEntity resultEntity = new SearchResultEntity();
                resultEntity.key = searchKey;
                resultEntity.result = title;
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constant.IntentKey.SEARCH_LIST, (ArrayList<String>) searchTypeList);
                if (!isDelete) {
                    bundle.putSerializable(Constant.IntentKey.SEARCH_DATA, resultEntity);
                }
                IntentRouter.go(context, Constant.Router.SEARCH_HISTORY, bundle);
                isFinish = true;
            }
        });

        searchTitle.setSearchLayoutLisetner(new SearchTitleBar.SearchLayoutListener() {
            @Override
            public void searchHideClick() {
                removeParams();
                goRefresh();
            }
        });

        adapter.setOnItemChildViewClickListener((childView, position, action, obj) -> {
            selectPosition=position;
            if (action == 0){
                //刷新页面
                List<SampleEntity> list = adapter.getList();
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setSelect(false);
                }
                list.get(position).setSelect(true);
                adapter.notifyDataSetChanged();

                //通知 检验项目更新数据
//                if (activity instanceof SampleResultInputActivity){
//                    ((SampleResultInputActivity)activity).setSampleId(list.get(position).getId());
//                }else if (activity instanceof  SampleResultInputPDAActivity){
                if (activity instanceof SampleResultInputPADActivity){
                    ((SampleResultInputPADActivity)activity).setSampleId(list.get(position).getId(),list.get(position).getCode());
                }else if (activity instanceof SampleResultInputActivity){
                    Bundle bundle = new Bundle();
                    bundle.putLong("sampleId",list.get(position).getId());
                    if (!StringUtil.isEmpty(list.get(position).getName()) && !StringUtil.isEmpty(list.get(position).getCode())){
                        bundle.putString("title", list.get(position).getName()+"(" + list.get(position).getCode() + ")");
                    }else {
                        if (StringUtil.isEmpty(list.get(position).getName())){
                            bundle.putString("title", list.get(position).getCode());
                        }else {
                            bundle.putString("title", list.get(position).getName());
                        }
                    }
                    //LIMS_Sampling
                    //LIMS_InspectionItemPda
                    IntentRouter.go(context,Constant.AppCode.LIMS_InspectionItemPda, bundle);
                }

            }
        });

        RxView.clicks(searchTitle.getRightScanActionBar())
                .throttleFirst(200, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        openCameraScan();
                    }
                });

    }

    public void goRefresh(){
        refreshListController.refreshBegin();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSearchHistory(EventInfo result) {
        if (result.getEventId() == EventInfo.searchKey) {
            resultEntity = (SearchResultEntity) result.getValue();
            title = resultEntity.result;
            searchKey = resultEntity.key;
            if (!TextUtils.isEmpty(title)) {
                searchTitle.showSearchBtn(title, searchKey);
            }
            removeParams();
            if (searchKey.equals(getString(R.string.lims_sample_code))){
                mParams.put(Constant.BAPQuery.CODE,title);
            }else if (searchKey.equals(getString(R.string.lims_sample_name))){
                mParams.put(Constant.BAPQuery.NAME,title);
            }else if (searchKey.equals(getString(R.string.lims_batch_number))){
                mParams.put(Constant.BAPQuery.BATCH_CODE,title);
            }

            if (activity instanceof SampleResultInputPADActivity){
                ((SampleResultInputPADActivity)activity).setShowTitle(title,searchKey);
            }

            presenterRouter.create(SampleListAPI.class).getSampleList(timeMap,mParams);
        }
    }

    private String scanCode;
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCodeReciver(CodeResultEvent codeResultEvent) {
        if (CodeUtlis.QR_TYPE.equals(codeResultEvent.type)) {
            if (!StringUtil.isEmpty(codeResultEvent.scanResult)){
                scan = true;
                scanCode = codeResultEvent.scanResult;
                removeParams();
                mParams.put(Constant.BAPQuery.CODE, scanCode);
                onLoading(getResources().getString(R.string.lims_loading_sample));
                presenterRouter.create(SampleListAPI.class).getSampleList(timeMap,mParams);
//                for (int i = 0; i < adapter.getList().size(); i++) {
//                    adapter.getList().get(i).setSelect(false);
//                }
//                int position = -1;
//                for (int i = 0; i < adapter.getList().size(); i++) {
//                    if (adapter.getList().get(i).getCode().equals(codeResultEvent.scanResult)){
//                        adapter.getList().get(i).setSelect(true);
//                        position = i;
//                        break;
//                    }
//                }
//                adapter.notifyDataSetChanged();
//
//                if (position == -1){
//                    ToastUtils.show(context,context.getResources().getString(R.string.lims_not_scanned_code));
//                    return;
//                }
//
//                //通知 检验项目更新数据
//                if (activity instanceof SampleResultInputActivity){
//                    ((SampleResultInputActivity)activity).setSampleId(adapter.getList().get(position).getId());
//                }else if (activity instanceof  SampleResultInputPDAActivity){
//                    Bundle bundle = new Bundle();
//                    bundle.putLong("sampleId",adapter.getList().get(position).getId());
//                    if (!StringUtil.isEmpty(adapter.getList().get(position).getName()) && !StringUtil.isEmpty(adapter.getList().get(position).getCode())){
//                        bundle.putString("title", adapter.getList().get(position).getName()+"(" + adapter.getList().get(position).getCode() + ")");
//                    }else {
//                        if (StringUtil.isEmpty(adapter.getList().get(position).getName())){
//                            bundle.putString("title", adapter.getList().get(position).getCode());
//                        }else {
//                            bundle.putString("title", adapter.getList().get(position).getName());
//                        }
//                    }
//                    IntentRouter.go(context,Constant.AppCode.LIMS_InspectionItemPda, bundle);
//                }
            }
        }
    }

    public void removeParams(){
        if (null != mParams){
            mParams.clear();
        }
    }


    @Override
    public void getSampleListSuccess(CommonListEntity entity) {
        if (scan){
            scan = false;
            List<SampleEntity> sampleEntities = entity.result;
            if (!sampleEntities.isEmpty()){
                SampleEntity sampleEntity = sampleEntities.get(0);
                if (scanCode.equals(sampleEntity.getCode())){
                    onLoadSuccessAndExit(getResources().getString(R.string.lims_loading_succeed), new OnLoaderFinishListener() {
                        @Override
                        public void onLoaderFinished() {
                            Bundle bundle = new Bundle();
                            bundle.putLong("sampleId", sampleEntity.getId());
                            if (!StringUtil.isEmpty(sampleEntity.getName()) && !StringUtil.isEmpty(sampleEntity.getCode())) {
                                bundle.putString("title", sampleEntity.getName() + "(" + sampleEntity.getCode() + ")");
                            } else {
                                if (StringUtil.isEmpty(sampleEntity.getName())) {
                                    bundle.putString("title", sampleEntity.getCode());
                                } else {
                                    bundle.putString("title", sampleEntity.getName());
                                }
                            }
                            IntentRouter.go(context, Constant.AppCode.LIMS_InspectionItemPda, bundle);
                        }
                    });
                }
            }else {
                onLoadFailed(getResources().getString(R.string.lims_load_fail));
            }

        }else {
            selectPosition=-1;
            refreshListController.refreshComplete(entity.result);
            SelectDataEvent<String> dataEvent=new SelectDataEvent<>("refreshData","refreshData");
            EventBus.getDefault().post(dataEvent);
//            if (activity instanceof SampleResultInputActivity){
//                ((SampleResultInputActivity)activity).sampleRefresh();
//            }
        }

    }

    @Override
    public void getSampleListFailed(String errorMsg) {
        SnackbarHelper.showError(rootView, errorMsg);
        refreshListController.refreshComplete(null);
        SelectDataEvent<String> dataEvent=new SelectDataEvent<>("refreshData","refreshData");
        EventBus.getDefault().post(dataEvent);
//        SnackbarHelper.showError(rootView, errorMsg);
//        refreshListController.refreshComplete(null);
    }

    public void openCameraScan(){
        getController(CommonScanController.class).openCameraScan();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (isFinish) {
            searchTitle.hideSearchBtn();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
