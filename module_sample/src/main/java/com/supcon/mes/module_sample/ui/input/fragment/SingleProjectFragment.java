package com.supcon.mes.module_sample.ui.input.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.common.view.view.loader.base.OnLoaderFinishListener;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.IntentRouter;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.event.SelectDataEvent;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.controller.CalculationController;
import com.supcon.mes.module_lims.model.bean.AttachmentSampleInputEntity;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_lims.model.bean.SerialDeviceEntity;
import com.supcon.mes.module_lims.service.SerialWebSocketService;
import com.supcon.mes.module_lims.utils.SpaceItemDecoration;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.model.api.SampleAnalyseCollectDataAPI;
import com.supcon.mes.module_sample.model.api.SingleSampleResultInputAPI;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.contract.SampleAnalyseCollectDataContract;
import com.supcon.mes.module_sample.model.contract.SingleSampleResultInputContract;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.SampleAnalyseCollectDataPresenter;
import com.supcon.mes.module_sample.presenter.SingleSampleResultInputPresenter;
import com.supcon.mes.module_sample.ui.adapter.SingleProjectAdapter;
import com.supcon.mes.module_sample.ui.input.SampleResultInputPADActivity;
import com.supcon.mes.module_sample.ui.input.SingleSampleResultInputItemActivity;
import com.supcon.mes.module_sample.ui.input.SingleSampleResultInputPADActivity;

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
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */
@Presenter(value = {SingleSampleResultInputPresenter.class, InspectionSubProjectColumnPresenter.class, SampleAnalyseCollectDataPresenter.class})
@Controller(value = {SystemConfigController.class, CalculationController.class, LimsFileUpLoadController.class, SampleRecordResultSubmitController.class})
public class SingleProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements SingleSampleResultInputContract.View, SampleAnalyseCollectDataContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;

    @BindByTag("ll_bottom")
    LinearLayout ll_bottom;

    @BindByTag("rl_calculation")
    RelativeLayout rl_calculation;

    @BindByTag("rl_save")
    RelativeLayout rl_save;

    @BindByTag("rl_submit")
    RelativeLayout rl_submit;
    @BindByTag("tvSerial")
    TextView tvSerial;
    @BindByTag("tvFileAnalyse")
    TextView tvFileAnalyse;
    @BindByTag("tvAutoCollection")
    TextView tvAutoCollection;
    private SingleProjectAdapter adapter;
    private Long sampleTesId;
    private String sampleCode;
    private String filePath;
    BaseFragmentActivity activity;
    SpaceItemDecoration spaceItemDecoration;
    GridLayoutManager gridLayoutManager;
    LinearLayoutManager linearLayoutManager;
    LinearSpaceItemDecoration linearSpaceItemDecoration;


    private List<InspectionItemColumnEntity> columnList = new ArrayList<>();
    private List<ConclusionEntity> conclusionList = new ArrayList<>();
    private List<InspectionSubEntity> myInspectionSubList = new ArrayList<>();

    SampleRecordResultSubmitController submitController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SingleSampleResultInputItemActivity) {
            activity = (SingleSampleResultInputItemActivity) context;
        } else if (context instanceof SingleSampleResultInputPADActivity) {
            activity = (SingleSampleResultInputPADActivity) context;
        }
    }

    @Override
    protected IListAdapter<InspectionSubEntity> createAdapter() {
        adapter = new SingleProjectAdapter(context, contentView);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_single_project;
    }

    @Override
    protected void onInit() {
        super.onInit();
        EventBus.getDefault().register(this);
        refreshListController.setAutoPullDownRefresh(activity instanceof SingleSampleResultInputItemActivity);
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
        contentView.setLayoutManager(linearLayoutManager);
        contentView.addItemDecoration(linearSpaceItemDecoration);

        if (activity instanceof SingleSampleResultInputPADActivity) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }
//        if (activity instanceof SampleResultInputPADActivity) {
//            int orientation = ((SampleResultInputPADActivity) activity).getOrientation();
//            if (orientation == 2) { //横向
//                contentView.setLayoutManager(gridLayoutManager);
//                contentView.addItemDecoration(spaceItemDecoration);
//            } else if (orientation == 1) { //竖向
//                contentView.setLayoutManager(linearLayoutManager);
//                contentView.addItemDecoration(linearSpaceItemDecoration);
//            }
//        } else {
//
//        }

        submitController = new SampleRecordResultSubmitController();
    }

    public static int selectPosition = -1;
    InspectionSubEntity itemEntity;

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
                adapter.change = true;
            }
        } else if ("SampleAnalyseFile".equals(dataEvent.getSelectTag())) {
            boolean match=false;
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
            if (!match)
                ToastUtils.show(context,context.getResources().getString(R.string.lims_no_match_inspect_item));

        } else if ("WebSocketData".equals(dataEvent.getSelectTag())) {
            String originalValue = dataEvent.getEntity().toString();
            adapter.setOriginalValueChangeListener(selectPosition, originalValue);
        } else if ("deviceUrl".equals(dataEvent.getSelectTag())) {
            SerialDeviceEntity entity = (SerialDeviceEntity) dataEvent.getEntity();
            Intent intent = new Intent(SupPlantApplication.getAppContext(), SerialWebSocketService.class);
            intent.setAction(SerialWebSocketService.START_SERIAL_SERVICE);
            intent.putExtra("url", entity.getSerialServerIp());
            SupPlantApplication.getAppContext().startService(intent);
        }else if ("refreshData".equals(dataEvent.getSelectTag())){
            if (adapter!=null){
                sampleTesId=null;
                sampleCode=null;
                adapter.clear();
                adapter.notifyDataSetChanged();
            }
        }

    }

    @SuppressLint("CheckResult")
    @Override
    protected void initListener() {
        super.initListener();
        Bundle bundle = getArguments();
        if (null != bundle){
            sampleTesId = bundle.getLong("sampleId",1l);
        }
        if (activity instanceof SingleSampleResultInputPADActivity){
            ((SingleSampleResultInputPADActivity)activity).setOnNotifySubRefreshListener(new SingleSampleResultInputPADActivity.OnNotifySubRefreshListener() {
                @Override
                public void NotifySubRefresh(Long sampleId,String sampleCode) {
                    sampleTesId = sampleId == null ? 0L : sampleId;
                    SingleProjectFragment.this.sampleCode=sampleCode;
                    refreshListController.refreshBegin();
                }
            });
        }
        tvSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SingleSampleFragment.selectPosition==-1){
                    ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constant.IntentKey.SELECT_TAG,tvSerial.getTag().toString());
                IntentRouter.go(context, LimsConstant.AppCode.LIMS_SerialRef);
            }
        });

        RxView.clicks(tvAutoCollection)
                .throttleFirst(2000,TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (SingleSampleFragment.selectPosition==-1){
                        ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                        return;
                    }
                    String url = "http://" + SupPlantApplication.getIp() + ":9410//lims-collection-web/ws/rs/analysisDataWS/getFormatDataByCollectCode?collectCode="+sampleCode;
                    onLoading(context.getResources().getString(R.string.lims_parsing));
                    presenterRouter.create(SampleAnalyseCollectDataAPI.class).getFormatDataByCollectCode(url);
                });
        RxView.clicks(tvFileAnalyse)
                .throttleFirst(2000,TimeUnit.MILLISECONDS)
                .subscribe(o -> {
                    if (SingleSampleFragment.selectPosition==-1){
                        ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                        return;
                    }
                    IntentRouter.go(context, LimsConstant.AppCode.LIMS_SAMPLE_FILE_ANALYSE);
                });

        adapter.setEngine(getController(CalculationController.class).getEngine());

        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SingleSampleResultInputAPI.class).getSampleInspectItem(sampleTesId);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                itemEntity = adapter.getItem(position);
                selectPosition=position;
                if (action == 1) {
                    getController(LimsFileUpLoadController.class).
                            showPopup(getActivity(), SingleProjectFragment.this)
                            .setOnSuccessListener(new OnSuccessListener<FileDataEntity>() {
                                @Override
                                public void onSuccess(FileDataEntity fileDataEntity) {//上传成功附件之后，如果之前已有附件就把之前的附件ID记录下来，保存的时候，将之前的附件删除掉
                                    adapter.change=true;
                                    filePath = fileDataEntity.getLocalPath();
                                    File file=new File(fileDataEntity.getLocalPath());
                                    String name=file.getName();
                                    List<String> fileUploadMultiFileNames=itemEntity.getFileUploadMultiFileNames();
                                    fileUploadMultiFileNames.add(name);
                                    itemEntity.setFileUploadMultiFileNames(fileUploadMultiFileNames);

                                    String path=fileDataEntity.getPath();
                                    List<String> addPaths=itemEntity.getFileUploadFileAddPaths();
                                    addPaths.add(path);
                                    itemEntity.setFileUploadFileAddPaths(addPaths);

                                    List<String> fileUploadMultiFileIcons=itemEntity.getFileUploadMultiFileIcons();
                                    fileUploadMultiFileIcons.add(fileDataEntity.getFileIcon());
                                    itemEntity.setFileUploadMultiFileIcons(fileUploadMultiFileIcons);

                                    List<AttachmentSampleInputEntity> attachmentEntities=itemEntity.getAttachmentSampleInputEntities();
                                    AttachmentSampleInputEntity attachmentSampleInputEntity=new AttachmentSampleInputEntity();
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
                }
            }
        });


        adapter.setOriginalValueChangeListener(new SingleProjectAdapter.OriginalValueChangeListener() {
            @Override
            public void originalValueChange(String value, int position) {

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
        });
        adapter.setDispValueChangeListener(new SingleProjectAdapter.DispValueChangeListener() {
            @Override
            public void dispValueChange(String value, int position) {

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
        });

        if (activity instanceof SampleResultInputPADActivity) {
            ((SampleResultInputPADActivity) activity).setOnOrientationChangeListener(new SampleResultInputPADActivity.OnOrientationChangeListener() {
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

        RxView.clicks(rl_save)
                .throttleFirst(2000, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (SingleSampleFragment.selectPosition==-1){
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                            return;
                        }
                        if (!change()){
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_project_check_change));
                            return;
                        }
                        List<InspectionSubEntity> inspectionSubList = getInspectionSubList();
                        SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("save",sampleTesId,inspectionSubList);
                        submitController.recordResultSubmit(activity,1,submitEntity);
                    }
                });
        RxView.clicks(rl_submit)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(o ->  {
                    if (SingleSampleFragment.selectPosition==-1){
                        ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                        return;
                    }
                    List<InspectionSubEntity> inspectionSubList = getInspectionSubList();
                    SampleRecordResultSubmitEntity submitEntity=new SampleRecordResultSubmitEntity("submit",sampleTesId,inspectionSubList);
                    submitController.recordResultSubmit(activity,1,submitEntity);
                });
        RxView.clicks(rl_calculation)
                .throttleFirst(300,TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        if (SingleSampleFragment.selectPosition==-1){
                            ToastUtils.show(context,context.getResources().getString(R.string.lims_select_sample_data));
                            return;
                        }
                        manualCalculate();
                    }
                });

        submitController.setSubmitOnSuccessListener(new OnSuccessListener<Integer>() {
            @Override
            public void onSuccess(Integer type) {
                if (type==1){//保存
                    goRefresh();
                }else if (type==2){//提交
                    adapter.getList().clear();
                    adapter.notifyDataSetChanged();
                    ((SingleSampleResultInputPADActivity) activity).notifySampleRefresh();
                }
            }
        });

    }

    public boolean change() {
        return adapter.change;
    }

    public void goRefresh() {
        refreshListController.refreshBegin();
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

    public List<InspectionSubEntity> getInspectionSubList() {
        return adapter.getList();

    }

    private List<InspectionSubEntity> originalInspectionSubList = new ArrayList<>();

    @Override
    public void getSampleComSuccess(CommonBAP5ListEntity entity) {
        myInspectionSubList = entity.data.result;
        originalInspectionSubList = entity.data.result;
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
        refreshListController.refreshComplete(myInspectionSubList);
    }

    public List<InspectionSubEntity> getOriginalInspectionSubList() {
        return originalInspectionSubList;
    }

    @Override
    public void getSampleComFailed(String errorMsg) {
        refreshListController.refreshComplete();
    }

    @Override
    public void getSampleInspectItemSuccess(SingleInspectionItemListEntity entity) {
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
        int l = 0;
        //将合格范围 放入对应集合中
        for (int i = 0; i < conclusionList.size(); i++) {
            for (int j = l; j < columnList.size(); j++) {
                if (columnList.get(j).getColumnType().equals("range")) { //表示当前是结论
                    if (columnList.get(j).getColumnKey().equals(conclusionList.get(i).getColumnKey())) { //如果接口数据中的结论的key 跟截取出来的 结论集合的key是同一个key
                        List<InspectionItemColumnEntity> recordList = new ArrayList<>();
                        //将当前下标之前的范围放入属于这个结论的集合中
                        for (int a = j - 1; a >= k; a--) {
                            InspectionItemColumnEntity inspectionItemColumnEntity = columnList.get(a);
                            recordList.add(inspectionItemColumnEntity);
                        }
                        Collections.reverse(recordList);
                        conclusionList.get(i).setColumnList(recordList);
                        k = j + 1;
                    }
                }
                if (conclusionList.get(i).getColumnList().size() > 0){
                    l = j+1;
                    break;
                }
            }
        }
        presenterRouter.create(SingleSampleResultInputAPI.class).getSampleCom(sampleTesId);
    }

    @Override
    public void getSampleInspectItemFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context, errorMsg);
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

    @Override
    public void getFormatDataByCollectCodeFailed(String errorMsg) {
        onLoadFailed(errorMsg);
    }
}

