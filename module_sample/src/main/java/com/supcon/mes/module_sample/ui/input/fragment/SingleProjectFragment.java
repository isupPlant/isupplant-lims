package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;

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
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.constant.Constant;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonBAP5ListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.controller.CalculationController;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_lims.utils.FileUtils;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.IntentRouter;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_sample.controller.SampleRecordResultSubmitController;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.custom.SpaceItemDecoration;
import com.supcon.mes.module_sample.model.api.SingleSampleResultInputAPI;
import com.supcon.mes.module_sample.model.bean.FileDataEntity;
import com.supcon.mes.module_sample.model.bean.SampleRecordResultSubmitEntity;
import com.supcon.mes.module_sample.model.bean.SingleInspectionItemListEntity;
import com.supcon.mes.module_sample.model.bean.TestDeviceEntity;
import com.supcon.mes.module_sample.model.bean.TestMaterialEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectApi;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnApi;
import com.supcon.mes.module_sample.model.contract.SingleSampleResultInputContract;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;
import com.supcon.mes.module_sample.presenter.SingleSampleResultInputPresenter;
import com.supcon.mes.module_sample.ui.adapter.ProjectAdapter;
import com.supcon.mes.module_sample.ui.adapter.SingleProjectAdapter;
import com.supcon.mes.module_sample.ui.adapter.SingleSampleInpectAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import com.supcon.mes.module_sample.ui.input.SingleSampleResultInputItemActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.functions.Consumer;


/**
 * Created by wanghaidong on 2020/8/13
 * Email:wanghaidong1@supcon.com
 */
@Presenter(value = {SingleSampleResultInputPresenter.class, InspectionSubProjectColumnPresenter.class})
@Controller(value = {SystemConfigController.class, CalculationController.class, LimsFileUpLoadController.class, SampleRecordResultSubmitController.class})
public class SingleProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements SingleSampleResultInputContract.View {

    @BindByTag("contentView")
    RecyclerView contentView;
    private SingleProjectAdapter adapter;
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
        if (context instanceof SingleSampleResultInputItemActivity) {
            activity = (SingleSampleResultInputItemActivity) context;
        }
    }

    @Override
    protected IListAdapter<InspectionSubEntity> createAdapter() {
        adapter = new SingleProjectAdapter(context);
        return adapter;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_single_project;
    }

    @Override
    protected void onInit() {
        super.onInit();
        refreshListController.setAutoPullDownRefresh(true);
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
        Bundle bundle=getArguments();
        sampleTesId=bundle.getLong("sampleId");
        refreshListController.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenterRouter.create(SingleSampleResultInputAPI.class).getSampleInspectItem(sampleTesId);
            }
        });

        adapter.setOnItemChildViewClickListener(new OnItemChildViewClickListener() {
            @Override
            public void onItemChildViewClick(View childView, int position, int action, Object obj) {
                InspectionSubEntity itemEntity = adapter.getItem(position);
                if (action == 1) {
                    getController(LimsFileUpLoadController.class).
                            showPopup(getActivity(), SingleProjectFragment.this)
                            .setOnSuccessListener(new OnSuccessListener<FileDataEntity>() {
                                @Override
                                public void onSuccess(FileDataEntity fileDataEntity) {//上传成功附件之后，如果之前已有附件就把之前的附件ID记录下来，保存的时候，将之前的附件删除掉
                                    filePath=fileDataEntity.getLocalPath();
                                    itemEntity.setFileUploadFileAddPaths(fileDataEntity.getPath());
                                    itemEntity.setFileUploadFileDeleteIds(itemEntity.getFileUploadMultiFileIds());
                                    itemEntity.setFileUploadMultiFileNames(filePath.substring(filePath.lastIndexOf("/")+1));
                                    itemEntity.setFilePath(filePath);
                                }
                            });
                }else if (action==2){

                        if (!TextUtils.isEmpty(itemEntity.getFilePath())){
                            File file=new File(itemEntity.getFilePath());
                            if (FileUtils.imageFile(file) || FileUtils.videoFile(file)) {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("file", file);
                                IntentRouter.go(context, Constant.Router.FILE_LOOK, bundle);
                            }else {
                                Util.openFile(getActivity(),itemEntity.getFilePath());
                            }
                        }else {
                            ToastUtils.show(context,"没有可查看的文件");
                        }

                }
            }
        });


        adapter.setOriginalValueChangeListener(new SingleProjectAdapter.OriginalValueChangeListener() {
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
        adapter.setDispValueChangeListener(new SingleProjectAdapter.DispValueChangeListener() {
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

    public void goRefresh(){
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

    public List<InspectionSubEntity> getInspectionSubList(){
        return adapter.getList();

    }

    @Override
    public void getSampleComSuccess(CommonBAP5ListEntity entity) {
        myInspectionSubList=entity.data.result;

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
        presenterRouter.create(SingleSampleResultInputAPI.class).getSampleCom(sampleTesId);
    }

    @Override
    public void getSampleInspectItemFailed(String errorMsg) {
        refreshListController.refreshComplete();
        ToastUtils.show(context,errorMsg);
    }
}

