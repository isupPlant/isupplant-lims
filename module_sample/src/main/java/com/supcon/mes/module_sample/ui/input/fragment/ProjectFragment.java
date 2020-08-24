package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.app.annotation.BindByTag;
import com.app.annotation.Controller;
import com.app.annotation.Presenter;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.custom.SpaceItemDecoration;
import com.supcon.mes.module_sample.model.bean.CalcParamInfoEntity;
import com.supcon.mes.module_sample.model.bean.ConclusionEntity;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.SpecLimitEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectApi;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnApi;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;
import com.supcon.mes.module_sample.ui.adapter.ProjectAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import static java.lang.Float.parseFloat;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {InspectionSubProjectPresenter.class, InspectionSubProjectColumnPresenter.class})
@Controller(value = {SystemConfigController.class})
public class ProjectFragment extends BaseRefreshRecyclerFragment<InspectionSubEntity> implements InspectionSubProjectApi.View, InspectionSubProjectColumnApi.View {

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

    private boolean autoCalculate;
    private boolean dataChangeFlag;
    ScriptEngine engine;
    private String specialResultStr = "";

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


        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("javascript");

            String path1 = getAssetsCacheFile(context,"numeric.js");
            String path2 = getAssetsCacheFile(context,"numeral.min.js");
            String path3 = getAssetsCacheFile(context,"jstat.min.js");
            String path4 = getAssetsCacheFile(context, "formula.js");
            String path5 = getAssetsCacheFile(context, "generalCal.js");

            FileReader reader1 = new FileReader(path1);
            FileReader reader2 = new FileReader(path2);
            FileReader reader3 = new FileReader(path3);
            FileReader reader4 = new FileReader(path4);
            FileReader reader5 = new FileReader(path5);

            engine.eval(reader1);
            engine.eval(reader2);
            engine.eval(reader3);
            engine.eval(reader4);
            engine.eval(reader5);
        } catch (Exception e) {
            e.printStackTrace();
        }



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

        getController(SystemConfigController.class).getSystemConfig("LIMSBasic_1.0.0_testItem", "LIMSBasic.specialResult", "", new SystemConfigController.SystemConfigResultListener() {
            @Override
            public void systemConfigResult(boolean isSuccess, JsonObject jsonObject, String msg) {
                if (isSuccess){
                    JsonElement jsonElement = jsonObject.get("LIMSBasic.specialResult");
                    String asString = jsonElement.getAsString();
                    specialResultStr = asString;
                }
            }
        });

        adapter.setOriginalValueChangeListener(new ProjectAdapter.OriginalValueChangeListener() {
            @Override
            public void originalValueChange(boolean hasFocus, String value, int position) {
                if (!hasFocus){
                    originValOnChange(value,position);
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
        //presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionSubProjectApi.class).getInspectionSubProjectList(sampleTesId+"");
        refreshListController.refreshBegin();
    }

    public void getInspectionItemSubColumn() {
        presenterRouter.create(com.supcon.mes.module_sample.model.api.InspectionSubProjectColumnApi.class).getInspectionSubProjectColumn(sampleTesId + "");
    }

    @Override
    public void getInspectionSubProjectListSuccess(CommonListEntity entity) {
//        onLoadSuccessAndExit("加载成功！", new OnLoaderFinishListener() {
//            @Override
//            public void onLoaderFinished() {
//
//            }
//        });
        myInspectionSubList.clear();
        myInspectionSubList.addAll(entity.result);
        for (int i = 0; i < myInspectionSubList.size(); i++) {
            if (null != myInspectionSubList.get(i).getDispMap()) {
                myInspectionSubList.get(i).setConclusionList(conclusionList);
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

    public void originValOnChange(String value, int nRow){
        if(!judgValueLegal(value, nRow, "originValue")){
            ToastUtils.show(context,getResources().getString(R.string.LIMSSample_sample_error_valueKindError));
            return;
        }
        originValChange(value,nRow);
        //样品检测分项pt
        if(checkAutoCalculate(nRow, myInspectionSubList)){
            //自动计算
            autoCalculate = true;
            readyCalculate(myInspectionSubList);
        }
    }

    //输入值是否合法判断
    public boolean judgValueLegal(String value, int nRow, String dataKey){
        //样品分项pt
        String valueKind = myInspectionSubList.get(nRow).getValueKind().getId();
        if(valueKind.equals("LIMSBasic_valueKind/calculate")  || valueKind.equals("LIMSBasic_valueKind/number") ){
            if(!Util.isNumeric(value)){
                setValueByKey(myInspectionSubList,nRow,dataKey);
                return false;
            }
        }
        return true;
    }

    public void setValueByKey(List<InspectionSubEntity> list, int nRow, String dataKey){
        if (dataKey.equals("originValue")){
            list.get(nRow).setOriginValue(null);
        }
        adapter.notifyDataSetChanged();
    }

    public void originValChange(String value, int nRow){
        dataChangeFlag = true;
        //设置检验员
        setSampleComTestStaff(nRow);
        //检测分项pt
        InspectionSubEntity sampleCom = myInspectionSubList.get(nRow);
        boolean clearFalg = false;

        if(!StringUtil.isEmpty(value)){
            if(sampleCom.getValueKind().getId().equals("LIMSBasic_valueKind/number")  || sampleCom.getValueKind().getId().equals("LIMSBasic_valueKind/calculate")){
                //数值、计算类型
                    //获取修约值
                Object roundValue = null;
                try {
                    Invocable invoke = (Invocable)engine;
                    roundValue = invoke.invokeFunction("roundingValue", parseFloat(value), sampleCom.getDigitType(), sampleCom.getCarrySpace(), sampleCom.getCarryType(), sampleCom.getCarryFormula());
                    sampleCom.setOriginValue(value);
                    sampleCom.setRoundValue(roundValue+"");
                    sampleCom.setDispValue(roundValue+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                    //高低限判断
                    if(sampleCom.getLimitType() != null){

                        try {
                            Invocable invoke = (Invocable)engine;
                            Object dispValue = invoke.invokeFunction("sectionJudgment", parseFloat(value), sampleCom.getLimitType().getId(), sampleCom.getMaxValue(), sampleCom.getMinValue());
                            if(dispValue != "reject"){
                                if(parseFloat(dispValue+"") != parseFloat(value)){
                                    sampleCom.setDispValue(dispValue+"");
                                }else{
                                    sampleCom.setDispValue(roundValue+"");
                                }
                            }else{
                                clearFalg = true;
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

            }else{
                sampleCom.setOriginValue(value);
                sampleCom.setRoundValue(value);
                sampleCom.setDispValue(value);
            }
        }else{
            clearFalg = true;
        }

        if(clearFalg){
            sampleCom.setOriginValue(null);
            sampleCom.setRoundValue(null);
            sampleCom.setDispValue(null);
        }

        myInspectionSubList.set(nRow,sampleCom);
        dispValueChange(sampleCom.getRoundValue(),nRow);
    }

    //报出值改变函数
    public void dispValueChange(String value,int nRow){
        //检测分项pt
        InspectionSubEntity sampleCom = myInspectionSubList.get(nRow);
        List<String> specLimitList = null;
        HashMap<String, Object> dispMap = sampleCom.getDispMap();
        for (String key: dispMap.keySet()){
            if (key.equals("specLimit")){
                specLimitList = (List<String>) dispMap.get(key);
                break;
            }
        }

        //对检验结果进行等级判定   specLimitList = disMap 中取到的 specLimit 类型为List<String>
        if(specLimitList != null && specLimitList.size()> 0){
            for(int i = 0; i < specLimitList.size(); i ++){
                String resultGrade = null;
                List<SpecLimitEntity> specLimits = GsonUtil.jsonToList(specLimitList.get(i), SpecLimitEntity.class);
                Object[] specListsArr = specLimits.toArray();
                if(!StringUtil.isEmpty(value)){
                    String limitType = "";
                    if(sampleCom.getLimitType() != null){
                        limitType = sampleCom.getLimitType().getId();
                    }

                    try {
                        Invocable invoke = (Invocable)engine;
                        Object gradeDetermine = invoke.invokeFunction("gradeDetermine", value, specListsArr, specialResultStr, limitType);
                        resultGrade = (String) gradeDetermine;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //根据dismap.specLimit中[[resultKey]] 的值 找到 disMap 中的建 并对其重新赋值
                for (String key : myInspectionSubList.get(nRow).getDispMap().keySet()){
                    if (key.equals(specLimits.get(0).getResultKey())){
                        myInspectionSubList.get(nRow).getDispMap().put(key,resultGrade);
                    }
                }

//                if(sampleComDg.getSelecteds().length != 0){
//                    if(nRow == sampleComDg.getSelecteds()[0].rowIndex){
//                        $("#" + specLimits[0].resultKey + "select").val(resultGrade);
//                    }
//                }
            }
        }
        //改变结论列的颜色
//        resultColorChange(nRow, sampleComDg, gradeResult);
    }


    //设置样品检测分项的检验员
    public void setSampleComTestStaff(int nRow){
        //样品分项pt
        BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
        entity.setId(SupPlantApplication.getAccountInfo().staffId);
        entity.setName(SupPlantApplication.getAccountInfo().staffName);
        myInspectionSubList.get(nRow).setTestStaffId(entity);
    }


    public void manualCalculate() {
        //手动计算
        autoCalculate = false;
        readyCalculate(myInspectionSubList);
    }

    public void dispValueOnchange(String value, int nRow){
        dataChangeFlag = true;
        setSampleComTestStaff(nRow);
        dispValueChange(value,nRow);
        //样品分项pt

        //值类型不为计算且参数中包含该分项的参与计算；值类型为计算时，改变报出值会自动计算，重新赋值导致无法修改报出值
        if(! myInspectionSubList.get(nRow).getValueKind().getId().equals("LIMSBasic_valueKind/calculate") && checkAutoCalculate(nRow, myInspectionSubList)){
            //自动计算
            autoCalculate = true;
            readyCalculate(myInspectionSubList);
        }
    }

    //判断是否需要自动计算
    public boolean checkAutoCalculate(int nRow, List<InspectionSubEntity> sampleComDgData){
        //当前修改的分项
        InspectionSubEntity sampleCom = sampleComDgData.get(nRow);

        //遍历分项，查找是否存在值类型为计算的分项，如果有则查找其参数是否包含本次修改的分项
        for(int i = 0; i < sampleComDgData.size(); i++){
            if(sampleComDgData.get(i).getValueKind().getId().equals("LIMSBasic_valueKind/calculate") ){
                String paramNameArr = sampleComDgData.get(i).getCalculateParamNames();
                if(paramNameArr.contains(sampleCom.getComName())){
                    return true;
                }
            }
        }
        return false;
    }

    //整理计算顺序
    public void readyCalculate(List<InspectionSubEntity> myInspectionSubList) {
        Map<Integer, Object> map = new HashMap<>();//存放不同深度的对象集合，用于确定计算顺序
        List<Integer> keyArr = new ArrayList<>();//存放key,用于遍历
        List<InspectionSubEntity> calArrays = null;//存放不同计算层级的对象
        for (int i = 0; i < myInspectionSubList.size(); i++) {
            if (myInspectionSubList.get(i).getValueKind().getId().equals("LIMSBasic_valueKind/calculate")) {
                int corder = calOrder(myInspectionSubList.get(i), myInspectionSubList);
                if (map.get(corder) != null) {
                    calArrays = (List<InspectionSubEntity>) map.get(corder);
                } else {
                    calArrays = new ArrayList<>();
                    keyArr.add(corder);
                }
                calArrays.add(myInspectionSubList.get(i));
                map.put(corder, calArrays);
            }
        }
        if (keyArr.size() == 0) {
            ToastUtils.show(context, R.string.LIMSSample_sample_doNotNeedCalculate);
            return;
        }
        Collections.sort(keyArr);
        for (int i = 0; i < keyArr.size(); i++) {
            calArrays = (List<InspectionSubEntity>) map.get(keyArr.get(i));
            calculate(calArrays);
        }

    }

    // 确定计算顺序
    public int calOrder(InspectionSubEntity sampleCom, List<InspectionSubEntity> myInspectionSubList) {
        if (sampleCom == null || !sampleCom.getValueKind().getId().equals("LIMSBasic_valueKind/calculate")) {
            return 0;
        }
        int maxDepth = 0;//输出的计算层级
        String calcParamNames = sampleCom.getCalculateParamNames();
        String[] paramNameArr = calcParamNames.split("@##@");
        for (int i = 0; i < paramNameArr.length; i++) { //当前检验分项中的 参数
            //根据检测分项名称查找页面中的对象
            List<InspectionSubEntity> resultComs = searchSampleCom(paramNameArr[i], myInspectionSubList);
            InspectionSubEntity inspectionSubItem = null;
            if (null != resultComs) {
                inspectionSubItem = resultComs.get(0);
            }
            int depth = calOrder(inspectionSubItem, myInspectionSubList) + 1;
            if (maxDepth < depth) {
                maxDepth = depth;
            }

        }
        return maxDepth;
    }

    //根据检测分项名称查找页面中的数据
    public List<InspectionSubEntity> searchSampleCom(String comName, List<InspectionSubEntity> myInspectionSubList) {
        List<InspectionSubEntity> list = new ArrayList<>();
        for (int i = 0; i < myInspectionSubList.size(); i++) {
            if (myInspectionSubList.get(i).getComName().equals(comName)) {
                list.add(myInspectionSubList.get(i));
            }
        }
        if (list.size() > 0) {
            return list;
        }
        return null;
    }



    //计算
    public void calculate(List<InspectionSubEntity> sampleComs){
        for(int i = 0; i < sampleComs.size(); i++){
            if (StringUtil.isEmpty(sampleComs.get(i).getCalcParamInfo())){
                ToastUtils.show(context,"暂无计算公式");
                return;
            }

            StringBuffer script = new StringBuffer("function executeFunc(){");
            String calcParamInfo = sampleComs.get(i).getCalcParamInfo();
            List<CalcParamInfoEntity> calcParamInfoList = GsonUtil.jsonToList(calcParamInfo, CalcParamInfoEntity.class);
            for (int j = 0; j < calcParamInfoList.size(); j++) {
                String calculateParamValue = getCalculateParamValue(calcParamInfoList.get(j).getParamType().getId(),
                        calcParamInfoList.get(j).getTestItemName(), calcParamInfoList.get(j).getTestComName(),
                        calcParamInfoList.get(j).getIncomeType().getId(), calcParamInfoList.get(j).getOutcomeType().getId(),
                        calcParamInfoList.get(j).getDealFunc().getId());

                if (StringUtil.isEmpty(calculateParamValue)){
                    return;
                }
                script.append("var "+calcParamInfoList.get(j).getParamName()+ "=" +calculateParamValue+ ";");
            }
            script.append(sampleComs.get(i).getCalculFormula() + "}");

            try {
                engine.eval(script.toString());
                Invocable inv2 = (Invocable) engine;
                Object res = inv2.invokeFunction("executeFunc");//执行计算公式
                if (res instanceof Double && Util.isNumeric(String.valueOf(res) )){
                    for (int k = 0; k < myInspectionSubList.size(); k++) {
                        if (myInspectionSubList.get(k).getId().equals(sampleComs.get(i).getId())){
                            originValChange(String.valueOf(res) ,k);
                            break;
                        }
                    }
                }else {
                    for (int k = 0; k < myInspectionSubList.size(); k++) {
                        if (myInspectionSubList.get(k).getId().equals(sampleComs.get(i).getId())){
                            if (!StringUtil.isEmpty(sampleComs.get(i).getOriginValue())){
                                originValChange(null, k);
                            }
                        }
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }




    private String getCalculateParamValue(String paramType, String testItem, String comName, String incomeType, String outcomeType, String dealFunc) {
        List<InspectionSubEntity> sampeComs = new ArrayList<>();
        if (paramType.equals("LIMSBasic_paramType/sameItem") ) {
            //同样品同检测项目下分项 （按样品录入结果）
            sampeComs = searchSampleCom(comName, myInspectionSubList);
        } else {
            ////同样品不同检测项目下分项 (单样品录入结果)
        }

        if (null != sampeComs) {
            List<String> valueArr = new ArrayList<>();
            for (int i = 0; i < sampeComs.size(); i++) {
                if (incomeType.equals("LIMSBasic_incomeType/originValue")) {
                    if (StringUtil.isEmpty(sampeComs.get(i).getOriginValue())) {
                        if (!autoCalculate) {
                            //原始值为空，非自动计算时报错
                            String string = getResources().getString(R.string.LIMSSample_sample_originValIsNull);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    }
                    valueArr.add(sampeComs.get(i).getOriginValue());
                } else if (incomeType.equals("LIMSBasic_incomeType/roundValue")) {
                    if (StringUtil.isEmpty(sampeComs.get(i).getRoundValue())) {
                        if (!autoCalculate) {
                            //修约值为空，非自动计算时报错
                            String string = getResources().getString(R.string.LIMSSample_sample_roundValIsNull);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    }
                    valueArr.add(sampeComs.get(i).getRoundValue());
                } else if (incomeType.equals("LIMSBasic_incomeType/dispValue")) {
                    if (StringUtil.isEmpty(sampeComs.get(i).getDispValue())) {
                        if (!autoCalculate) {
                            //报出值为空，非自动计算时报错
                            String string = getResources().getString(R.string.LIMSSample_sample_dispValIsNull);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    } else if (!StringUtil.isEmpty(sampeComs.get(i).getDispValue()) && !Util.isNumeric(sampeComs.get(i).getDispValue())) {
                        if (!autoCalculate) {
                            //报出值非数字，非自动计算时报错
                            String string = getResources().getString(R.string.LIMSSample_sample_dispValTypeError);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    }
                    valueArr.add(sampeComs.get(i).getDispValue());
                }

            }

            //能进到该方法中的 一定是数值类型的 所以直接将String 转 Double
            List<Double> valueList = new ArrayList<>();
            for (int i = 0; i < valueArr.size(); i++) {
                valueList.add(Double.valueOf(valueArr.get(i)));
            }

            if(outcomeType.equals("LIMSBasic_outcomeType/dealFunc") ){
                //输出类型为处理值

                if(dealFunc.equals("LIMSBasic_dealFunc/avg")){
                    //平均值
                    return Util.getAvg(valueList)+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/sum") ){
                    //求和
                    return Util.getSum(valueList)+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/count") ){
                    //个数
                    return valueList.size()+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/max") ){
                    //最大值
                    return Util.getMax(valueList)+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/min") ){
                    //最小值
                    return Util.getMin(valueList)+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/stdev") ){
                    //标准方差
                    return Util.standardDiviation(valueList)+"";
                }
            }else if(outcomeType.equals("LIMSBasic_outcomeType/arr") ){
                //输出类型为数组
                return "[" + valueArr.toString() + "]";
            }


        }


       return null;
    }

    public String getAssetsCacheFile(Context context,String fileName)   {
        File cacheFile = new File(context.getCacheDir(), fileName);
        try {
            InputStream inputStream = context.getAssets().open(fileName);
            try {
                FileOutputStream outputStream = new FileOutputStream(cacheFile);
                try {
                    byte[] buf = new byte[1024];
                    int len;
                    while ((len = inputStream.read(buf)) > 0) {
                        outputStream.write(buf, 0, len);
                    }
                } finally {
                    outputStream.close();
                }
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cacheFile.getAbsolutePath();
    }
}

