package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.CalculationController;

import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.service.SerialWebSocketService;
import com.supcon.mes.module_lims.utils.SpaceItemDecoration;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.api.InspectionSubProjectAPI;
import com.supcon.mes.module_sample.model.api.InspectionSubProjectColumnAPI;
import com.supcon.mes.module_sample.model.api.SampleAnalyseCollectDataAPI;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnContract;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectContract;
import com.supcon.mes.module_sample.model.contract.SampleAnalyseCollectDataContract;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;
import com.supcon.mes.module_sample.presenter.SampleAnalyseCollectDataPresenter;
import com.supcon.mes.module_sample.ui.adapter.ProjectAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputPADActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {InspectionSubProjectPresenter.class, InspectionSubProjectColumnPresenter.class, SampleAnalyseCollectDataPresenter.class})
@Controller(value = {SystemConfigController.class, CalculationController.class, LimsFileUpLoadController.class})
public class ProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements InspectionSubProjectContract.View,
        InspectionSubProjectColumnContract.View, SampleAnalyseCollectDataContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    private ProjectAdapter adapter;
    private Long sampleTesId;

    BaseFragmentActivity activity;
    SpaceItemDecoration spaceItemDecoration;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    LinearSpaceItemDecoration linearSpaceItemDecoration;


    private List<InspectionItemColumnEntity> columnList = new ArrayList<>();
    private List<ConclusionEntity> conclusionList = new ArrayList<>();
    private List<InspectionSubEntity> myInspectionSubList = new ArrayList<>();
    private List<InspectionSubEntity> recordList = new ArrayList<>();

    public int selectPosition = -1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SampleResultInputPADActivity) {
            activity = (SampleResultInputPADActivity) context;
        } else if (context instanceof ProjectInspectionItemsActivity) {
            activity = (ProjectInspectionItemsActivity) context;
        }

    }

    @Override
    protected IListAdapter<InspectionSubEntity> createAdapter() {
        adapter = new ProjectAdapter(context, contentView);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_project;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
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

        if (activity instanceof SampleResultInputPADActivity) {
            int orientation = ((SampleResultInputPADActivity) activity).getOrientation();
            if (orientation == 2) { //??????
                contentView.setLayoutManager(gridLayoutManager);
                contentView.addItemDecoration(spaceItemDecoration);

            } else if (orientation == 1) { //??????
                contentView.setLayoutManager(linearLayoutManager);
                contentView.addItemDecoration(linearSpaceItemDecoration);
            }
        } else {
            contentView.setLayoutManager(linearLayoutManager);
            contentView.addItemDecoration(linearSpaceItemDecoration);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataEvent(SelectDataEvent dataEvent) {
        if ("deleteIds".equals(dataEvent.getSelectTag())) {
            List<AttachmentSampleInputEntity> attachmentEntitys = (List<AttachmentSampleInputEntity>) dataEvent.getEntity();
            if (attachmentEntitys != null && !attachmentEntitys.isEmpty()) {
                List<String> deleteIds = itemEntity.getFileUploadFileDeleteIds();
                for (AttachmentSampleInputEntity attachmentEntity : attachmentEntitys) {
                    if (!TextUtils.isEmpty(attachmentEntity.getId())) {
                        deleteIds.add(attachmentEntity.getId());
                    }
                }
                itemEntity.setFileUploadFileDeleteIds(deleteIds);
                itemEntity.getAttachmentSampleInputEntities().removeAll(attachmentEntitys);
            }
        } else if ("SampleAnalyseFile".equals(dataEvent.getSelectTag())) {
            boolean match = false;
            List<Map<String, Object>> maps = (List<Map<String, Object>>) dataEvent.getEntity();
            for (Map<String, Object> map : maps) {
                outer:for (Map.Entry<String, Object> entry : map.entrySet()) {
                    List<InspectionSubEntity> entities = adapter.getList();
                    int count = entities.size();
                    for (int i = 0; i < count; i++) {
                        InspectionSubEntity data = adapter.getItem(i);
                        if (entry.getKey().equals(data.getComName())) {
                            match = true;
                            adapter.setOriginalValueChangeListener(i, entry.getValue().toString());
                            try {
                                Thread.sleep(1000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break outer;
                        }
                    }
                }
            }
            if (!match){
                ToastUtils.show(context, context.getResources().getString(R.string.lims_no_match_inspect_item));
            }
            goSave();
        } else if ("WebSocketData".equals(dataEvent.getSelectTag())) {
            String originalValue = dataEvent.getEntity().toString();
            adapter.setOriginalValueChangeListener(selectPosition, originalValue);
        } else if ("deviceUrl".equals(dataEvent.getSelectTag())) {
            SerialDeviceEntity entity = (SerialDeviceEntity) dataEvent.getEntity();
            Intent intent = new Intent(SupPlantApplication.getAppContext(), SerialWebSocketService.class);
            intent.setAction(SerialWebSocketService.START_SERIAL_SERVICE);
            intent.putExtra("url", entity.getSerialServerIp());
            SupPlantApplication.getAppContext().startService(intent);
        }else  if ("refreshData".equals(dataEvent.getSelectTag())){
            if (adapter!=null) {
                selectPosition = -1;
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }
    }

    InspectionSubEntity itemEntity;

    @Override
    protected void initListener() {
        super.initListener();
        adapter.setEngine(getController(CalculationController.class).getEngine());
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(InspectionSubProjectAPI.class).getInspectionSubProjectList(sampleTesId + "");
            }
        });


        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                itemEntity = adapter.getItem(position);
                if (action == 1) {
                    getController(LimsFileUpLoadController.class).
                            showPopup(getActivity(), ProjectFragment.this)
                            .setOnSuccessListener(new OnSuccessListener<FileDataEntity>() {
                                @Override
                                public void onSuccess(FileDataEntity fileDataEntity) {//????????????????????????????????????????????????????????????????????????ID????????????????????????????????????????????????????????????

                                    File file = new File(fileDataEntity.getLocalPath());
                                    String name = file.getName();
                                    List<String> fileUploadMultiFileNames = itemEntity.getFileUploadMultiFileNames();
                                    fileUploadMultiFileNames.add(name);
                                    itemEntity.setFileUploadMultiFileNames(fileUploadMultiFileNames);

                                    String path = fileDataEntity.getPath();
                                    List<String> addPaths = itemEntity.getFileUploadFileAddPaths();
                                    addPaths.add(path);
                                    itemEntity.setFileUploadFileAddPaths(addPaths);

                                    List<String> fileUploadMultiFileIcons = itemEntity.getFileUploadMultiFileIcons();
                                    fileUploadMultiFileIcons.add(fileDataEntity.getFileIcon());
                                    itemEntity.setFileUploadMultiFileIcons(fileUploadMultiFileIcons);

                                    List<AttachmentSampleInputEntity> attachmentEntities = itemEntity.getAttachmentSampleInputEntities();
                                    AttachmentSampleInputEntity attachmentSampleInputEntity = new AttachmentSampleInputEntity();
                                    attachmentSampleInputEntity.setName(name);
                                    attachmentSampleInputEntity.setFile(file);
                                    attachmentEntities.add(attachmentSampleInputEntity);
                                    itemEntity.setAttachmentSampleInputEntities(attachmentEntities);
                                }
                            });
                } else if (action == 2) {
                    if (itemEntity.getAttachmentSampleInputEntities() != null && !itemEntity.getAttachmentSampleInputEntities().isEmpty()) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("attachments", (Serializable) itemEntity.getAttachmentSampleInputEntities());
                        IntentRouter.go(context, Constant.Router.FILE_LIST_VIEW, bundle);
                    } else {
                        ToastUtils.show(context, context.getResources().getString(R.string.lims_not_file));
                    }
                } else if (action == 3) {
                    selectPosition = position;
                }
            }
        });


        adapter.setOriginalValueChangeListener(new ProjectAdapter.OriginalValueChangeListener() {
            @Override
            public void originalValueChange(String value, int position) {
                adapter.getList().get(position).setRecordOriginValue(value);
                getController(CalculationController.class).originValOnChange(value, position, adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
                    @Override
                    public void notifyRefreshAdapter(int position) {
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        });
        adapter.setDispValueChangeListener(new ProjectAdapter.DispValueChangeListener() {
            @Override
            public void dispValueChange(String value, int position) {
                adapter.getList().get(position).setRecordDispValue(value);
                getController(CalculationController.class).dispValueOnchange(value, position, adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
                    @Override
                    public void notifyRefreshAdapter(int position) {
                        adapter.notifyItemChanged(position);
                    }
                });
            }
        });

        if (activity instanceof SampleResultInputPADActivity) {
            ((SampleResultInputPADActivity) activity).setOnOrientationChangeListener(new SampleResultInputPADActivity.OnOrientationChangeListener() {
                @Override
                public void orientationChange(int orientation) {
                    if (orientation == 2) { //??????
//                        ToastUtils.show(context, "??????");
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

                    } else if (orientation == 1) { //??????
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
        //onLoading("?????????...");
        getInspectionItemSubColumn();
    }


    public void goRefresh() {
        refreshListController.refreshBegin();
    }

    public void getInspectionItemSubColumn() {
        presenterRouter.create(InspectionSubProjectColumnAPI.class).getInspectionSubProjectColumn(sampleTesId + "");
    }

    @Override
    public void getInspectionSubProjectListSuccess(CommonListEntity entity) {
        myInspectionSubList.clear();
        myInspectionSubList.addAll(entity.result);

        recordList.clear();


        for (int i = 0; i < myInspectionSubList.size(); i++) {
            List<ConclusionEntity> conclusionListLocal = GsonUtil.jsonToList(GsonUtil.gsonString(conclusionList), ConclusionEntity.class);
            myInspectionSubList.get(i).setConclusionList(conclusionListLocal);
            myInspectionSubList.get(i).setRecordOriginValue(myInspectionSubList.get(i).getOriginValue());
            myInspectionSubList.get(i).setRecordDispValue(myInspectionSubList.get(i).getDispValue());

            if (null != myInspectionSubList.get(i).getDispMap()) {
                HashMap<String, Object> dispMap = myInspectionSubList.get(i).getDispMap();
                List<ConclusionEntity> conclusionList = myInspectionSubList.get(i).getConclusionList();

                for (int j = 0; j < conclusionList.size(); j++) {
                    for (String key : dispMap.keySet()) {
                        if (key.equals(conclusionList.get(j).getColumnKey())) {
                            conclusionList.get(j).setFinalResult((String) dispMap.get(key));
                            break;
                        }
                    }

                }
            }
        }
        recordList = GsonUtil.jsonToList(GsonUtil.gsonString(myInspectionSubList), InspectionSubEntity.class);
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
        getController(CalculationController.class).setColumnRangeList(columnList);
        //????????????????????????????????? ??????????????????
        for (int i = 0; i < columnList.size(); i++) {
            if (columnList.get(i).getColumnType().equals("range")) { //???????????????
                ConclusionEntity conclusionEntity = new ConclusionEntity();
                conclusionEntity.setColumnType(columnList.get(i).getColumnType());
                conclusionEntity.setColumnKey(columnList.get(i).getColumnKey());
                conclusionEntity.setColumnName(columnList.get(i).getColumnName());
                conclusionEntity.setColumnList(new ArrayList<>());
                conclusionList.add(conclusionEntity);
            }
        }

        int k = 0;
        int l = 0;

        //??????????????? ?????????????????????
        for (int i = 0; i < conclusionList.size(); i++) {
            for (int j = l; j < columnList.size(); j++) {
                if (columnList.get(j).getColumnType().equals("range")) { //?????????????????????
                    if (columnList.get(j).getColumnKey().equals(conclusionList.get(i).getColumnKey())) { //?????????????????????????????????key ?????????????????? ???????????????key????????????key
                        List<InspectionItemColumnEntity> recordList = new ArrayList<>();
                        //??????????????????????????????????????????????????????????????????
                        for (int a = j - 1; a >= k; a--) {
                            InspectionItemColumnEntity inspectionItemColumnEntity = columnList.get(a);
                            recordList.add(inspectionItemColumnEntity);
                        }
                        Collections.reverse(recordList);
                        conclusionList.get(i).setColumnList(recordList);
                        k = j + 1;
                    }
                }

                if (conclusionList.get(i).getColumnList().size() > 0) {
                    l = j + 1;
                    break;
                }
            }

        }

        goRefresh();
    }

    @Override
    public void getInspectionSubProjectColumnFailed(String errorMsg) {
        //onLoadFailed("??????????????????????????????");
        columnList.clear();
    }

    public void manualCalculate() {
        //????????????
        getController(CalculationController.class).manualCalculate(adapter.getList(), new CalculationController.NotifyRefreshAdapterListener() {
            @Override
            public void notifyRefreshAdapter(int position) {
                adapter.notifyItemChanged(position);
            }
        });

    }

    public List<InspectionSubEntity> getInspectionSubList() {
        return adapter.getList();
    }

    public List<InspectionSubEntity> getRecordList() {
        return recordList;
    }

    public void getFormatDataByCollectCode(String url,String collectCode){
        onLoading(context.getResources().getString(R.string.lims_parsing));
        presenterRouter.create(SampleAnalyseCollectDataAPI.class).getFormatDataByCollectCode(url,true,collectCode);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void getFormatDataByCollectCodeSuccess(List entity) {
        if (entity != null && entity.size() > 0) {
            SelectDataEvent<List> selectDataEvent=new SelectDataEvent<>(entity,"SampleAnalyseFile");
            EventBus.getDefault().post(selectDataEvent);
            onLoadSuccess(context.getResources().getString(R.string.lims_parse_success));
        } else {
            onLoadFailed(context.getResources().getString(R.string.lims_no_parse_data));
        }
    }

    private void goSave(){
        if (activity instanceof SampleResultInputPADActivity){
            ((SampleResultInputPADActivity) activity).goSave();
        }else if (activity instanceof ProjectInspectionItemsActivity){
            ((ProjectInspectionItemsActivity) activity).goSave();
        }
    }

    @Override
    public void getFormatDataByCollectCodeFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }
}

