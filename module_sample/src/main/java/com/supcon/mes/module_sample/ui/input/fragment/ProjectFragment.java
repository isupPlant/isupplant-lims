package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.controller.CalculationController;

import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.custom.SpaceItemDecoration;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectApi;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnApi;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;
import com.supcon.mes.module_sample.ui.adapter.ProjectAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.mes.module_sample.ui.input.SingleSampleResultInputItemActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {InspectionSubProjectPresenter.class, InspectionSubProjectColumnPresenter.class})
@Controller(value = {SystemConfigController.class, CalculationController.class, LimsFileUpLoadController.class})
public class ProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements InspectionSubProjectApi.View, InspectionSubProjectColumnApi.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private ProjectAdapter adapter;
    private Long sampleTesId;
    private String filePath;

    BaseFragmentActivity activity;
    SpaceItemDecoration spaceItemDecoration;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    LinearSpaceItemDecoration linearSpaceItemDecoration;


    private List<InspectionItemColumnEntity> columnList = new ArrayList<>();
    private List<ConclusionEntity> conclusionList = new ArrayList<>();
    private List<InspectionSubEntity> myInspectionSubList = new ArrayList<>();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SampleResultInputActivity) {
            activity = (SampleResultInputActivity) context;
        } else if (context instanceof ProjectInspectionItemsActivity) {
            activity = (ProjectInspectionItemsActivity) context;
        }

    }

    @Override
    protected IListAdapter<InspectionSubEntity> createAdapter() {
        adapter = new ProjectAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_project;
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
        spaceItemDecoration = new SpaceItemDecoration(10, 2);
        gridLayoutManager = new GridLayoutManager(context, 2);
        linearLayoutManager = new LinearLayoutManager(context);
        linearSpaceItemDecoration = new LinearSpaceItemDecoration(context);

        if (activity instanceof SampleResultInputActivity) {
            int orientation = ((SampleResultInputActivity) activity).getOrientation();
            if (orientation == 2) { //横向
                ToastUtils.show(context, "横向");
                contentView.setLayoutManager(gridLayoutManager);
                contentView.addItemDecoration(spaceItemDecoration);

            } else if (orientation == 1) { //竖向
                ToastUtils.show(context, "竖向");
                contentView.setLayoutManager(linearLayoutManager);
                contentView.addItemDecoration(linearSpaceItemDecoration);
            }
        } else {
            contentView.setLayoutManager(linearLayoutManager);
            contentView.addItemDecoration(linearSpaceItemDecoration);
        }


    }

    @Override
    protected void initListener() {
        super.initListener();
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionSubProjectApi.class).getInspectionSubProjectList(sampleTesId + "");
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                InspectionSubEntity itemEntity = adapter.getItem(position);
                if (action == 1) {
                    getController(LimsFileUpLoadController.class).
                            showPopup(getActivity(),ProjectFragment.this)
                            .setOnSuccessListener(new OnSuccessListener<FileDataEntity>() {
                                @Override
                                public void onSuccess(FileDataEntity fileDataEntity) {
                                    filePath=fileDataEntity.getLocalPath();
                                    itemEntity.setFileUploadMultiFileNames(fileDataEntity.getPath());
                                    itemEntity.setFileUploadMultiFileIcons(fileDataEntity.getFileIcon());
                                    itemEntity.setFilePath(filePath);
                                }
                            });
                }
            }
        });

        adapter.setOriginalValueChangeListener(new ProjectAdapter.OriginalValueChangeListener() {
            @Override
            public void originalValueChange(boolean hasFocus, String value, int position) {
                if (!hasFocus) {
                    if (StringUtil.isEmpty(adapter.getList().get(position).getOriginValue()) && StringUtil.isEmpty(adapter.getList().get(position).getRecordOriginValue())){
                        return;
                    }
                    if (adapter.getList().get(position).getOriginValue().equals(adapter.getList().get(position).getRecordOriginValue())) {
                        return; //表示原始值只是获取又失去焦点 并未做修改
                    }

                    adapter.getList().get(position).setRecordOriginValue(value);
                    getController(CalculationController.class).originValOnChange(value, position, adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
                        @Override
                        public void notifyRefreshAdapter(int position) {
                            adapter.notifyItemChanged(position);
                        }
                    });
                }
            }
        });
        adapter.setDispValueChangeListener(new ProjectAdapter.DispValueChangeListener() {
            @Override
            public void dispValueChange(boolean hasFocus, String value, int position) {
                if (!hasFocus) {
                    if (StringUtil.isEmpty(adapter.getList().get(position).getDispValue()) && StringUtil.isEmpty(adapter.getList().get(position).getRecordDispValue())){
                        return;
                    }

                    if (adapter.getList().get(position).getDispValue().equals(adapter.getList().get(position).getRecordDispValue())) {
                        return; //表示原始值只是获取又失去焦点 并未做修改
                    }
                    adapter.getList().get(position).setRecordDispValue(value);
                    getController(CalculationController.class).dispValueOnchange(value, position, adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
                        @Override
                        public void notifyRefreshAdapter(int position) {
                            adapter.notifyItemChanged(position);
                        }
                    });
                }
            }
        });

        if (activity instanceof SampleResultInputActivity) {
            ((SampleResultInputActivity) activity).setOnOrientationChangeListener(new SampleResultInputActivity.OnOrientationChangeListener() {
                @Override
                public void orientationChange(int orientation) {
                    if (orientation == 2) { //横向
                        ToastUtils.show(context, "横向");
                        contentView.setLayoutManager(gridLayoutManager);

                        if (contentView.getItemDecorationCount() > 0) {
                            RecyclerView.ItemDecoration itemDecorationAt = contentView.getItemDecorationAt(0);
                            if (null == itemDecorationAt) {
                                contentView.addItemDecoration(spaceItemDecoration);
                            } else if (itemDecorationAt instanceof LinearSpaceItemDecoration) {
                                contentView.removeItemDecorationAt(0);
                                contentView.addItemDecoration(spaceItemDecoration);
                            }
                        } else {
                            contentView.addItemDecoration(spaceItemDecoration);
                        }

                    } else if (orientation == 1) { //竖向
                        ToastUtils.show(context, "竖向");
                        contentView.setLayoutManager(linearLayoutManager);
                        if (contentView.getItemDecorationCount() > 0) {
                            RecyclerView.ItemDecoration itemDecorationAt = contentView.getItemDecorationAt(0);
                            if (null == itemDecorationAt) {
                                contentView.addItemDecoration(linearSpaceItemDecoration);
                            } else if (itemDecorationAt instanceof SpaceItemDecoration) {
                                contentView.removeItemDecorationAt(0);
                                contentView.addItemDecoration(linearSpaceItemDecoration);
                            }
                        } else {
                            contentView.addItemDecoration(linearSpaceItemDecoration);
                        }

                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }


    }

    public void setSampleTesId(Long sampleTesId) {
        this.sampleTesId = sampleTesId;
        //onLoading("加载中...");
        getInspectionItemSubColumn();
    }


    public void goRefresh() {
        refreshListController.refreshBegin();
    }

    public void getInspectionItemSubColumn() {
        presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionSubProjectColumnApi.class).getInspectionSubProjectColumn(sampleTesId + "");
    }

    @Override
    public void getInspectionSubProjectListSuccess(CommonListEntity entity) {
        myInspectionSubList.clear();
        myInspectionSubList.addAll(entity.result);

        for (int i = 0; i < myInspectionSubList.size(); i++) {
            List<ConclusionEntity> conclusionListLocal = GsonUtil.jsonToList(GsonUtil.gsonString(conclusionList), ConclusionEntity.class);
            myInspectionSubList.get(i).setConclusionList(conclusionListLocal);
            myInspectionSubList.get(i).setRecordOriginValue(myInspectionSubList.get(i).getOriginValue());
            myInspectionSubList.get(i).setRecordDispValue(myInspectionSubList.get(i).getDispValue());

            if (null != myInspectionSubList.get(i).getDispMap()) {
                HashMap<String, Object> dispMap = myInspectionSubList.get(i).getDispMap();
                List<ConclusionEntity> conclusionList = myInspectionSubList.get(i).getConclusionList();
                for (String key : dispMap.keySet()) {
                    boolean isSuccess = false;
                    for (int j = 0; j < conclusionList.size(); j++) {
                        if (key.equals(conclusionList.get(j).getColumnKey())) {
                            conclusionList.get(j).setFinalResult((String) dispMap.get(key));
                            isSuccess = true;
                        }
                    }
                    if (isSuccess) {
                        break;
                    }

                }

            }
        }
        refreshListController.refreshComplete(myInspectionSubList);
    }

    @Override
    public void getInspectionSubProjectListFailed(String errorMsg) {
        //onLoadFailed(errorMsg);
        refreshListController.refreshComplete(null);
    }

    @Override
    public void getInspectionSubProjectColumnSuccess(BAP5CommonListEntity entity) {
        conclusionList.clear();

        columnList.clear();
        columnList.addAll(entity.data);
        //先把数据中的结论摘出来 作为父级实体
        for (int i = 0; i < columnList.size(); i++) {
            if (columnList.get(i).getColumnType().equals("range")) { //表示是结论
                ConclusionEntity conclusionEntity = new ConclusionEntity();
                conclusionEntity.setColumnType(columnList.get(i).getColumnType());
                conclusionEntity.setColumnKey(columnList.get(i).getColumnKey());
                conclusionEntity.setColumnName(columnList.get(i).getColumnName());
                conclusionEntity.setColumnList(new ArrayList<>());
                conclusionList.add(conclusionEntity);
            }
        }

        int k = 0;
        List<InspectionItemColumnEntity> recordList = new ArrayList<>();
        //将合格范围 放入对应集合中
        for (int i = 0; i < conclusionList.size(); i++) {
            for (int j = 0; j < columnList.size(); j++) {
                if (columnList.get(j).getColumnType().equals("range")) { //表示当前是结论
                    if (columnList.get(j).getColumnKey().equals(conclusionList.get(i).getColumnKey())) { //如果接口数据中的结论的key 跟截取出来的 结论集合的key是同一个key
                        recordList.clear();
                        //将当前下标之前的范围放入属于这个结论的集合中
                        for (int a = j - 1; a >= k; a--) {
                            InspectionItemColumnEntity inspectionItemColumnEntity = columnList.get(a);
                            recordList.add(inspectionItemColumnEntity);
                        }
                        conclusionList.get(i).setColumnList(recordList);
                        k = j + 1;
                    }
                }
            }
        }

        goRefresh();
    }

    @Override
    public void getInspectionSubProjectColumnFailed(String errorMsg) {
        //onLoadFailed("获取检验分项列名失败");
        columnList.clear();
    }

    public void manualCalculate() {
        //手动计算
        getController(CalculationController.class).manualCalculate(adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
            @Override
            public void notifyRefreshAdapter(int position) {
                adapter.notifyItemChanged(position);
            }
        });

    }

    public List<InspectionSubEntity> getInspectionSubList(){
        return adapter.getList();

    }
}

