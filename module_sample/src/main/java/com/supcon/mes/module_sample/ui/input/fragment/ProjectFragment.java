package com.supcon.mes.module_sample.ui.input.fragment;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.app.annotation.BindByTag;
import com.app.annotation.Presenter;
import com.supcon.common.view.base.activity.BaseFragmentActivity;
import com.supcon.common.view.base.adapter.IListAdapter;
import com.supcon.common.view.base.fragment.BaseRefreshRecyclerFragment;
import com.supcon.common.view.listener.OnItemChildViewClickListener;
import com.supcon.common.view.listener.OnRefreshListener;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.middleware.model.bean.BAP5CommonListEntity;
import com.supcon.mes.middleware.model.bean.CommonListEntity;
import com.supcon.mes.middleware.util.EmptyAdapterHelper;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.custom.LinearSpaceItemDecoration;
import com.supcon.mes.module_sample.custom.SpaceItemDecoration;
import com.supcon.mes.module_sample.model.bean.ConclusionEntity;
import com.supcon.mes.module_sample.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_sample.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectApi;
import com.supcon.mes.module_sample.model.contract.InspectionSubProjectColumnApi;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectColumnPresenter;
import com.supcon.mes.module_sample.presenter.InspectionSubProjectPresenter;
import com.supcon.mes.module_sample.ui.adapter.ProjectAdapter;
import com.supcon.mes.module_sample.ui.input.ProjectInspectionItemsActivity;
import com.supcon.mes.module_sample.ui.input.SampleResultInputActivity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * author huodongsheng
 * on 2020/7/31
 * class name
 */
@Presenter(value = {InspectionSubProjectPresenter.class, InspectionSubProjectColumnPresenter.class})
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

//    //输入值是否合法判断
//    public boolean judgValueLegal(String value, int nRow, dataKey){
//        //样品分项pt
//        String valueKind = myInspectionSubList.get(nRow).getValueKind().getId();
//        if(valueKind.equals("LIMSBasic_valueKind/calculate")  || valueKind.equals("LIMSBasic_valueKind/number") ){
//            if(!Util.isNumeric(value)){
//                myInspectionSubList.setCellValueByKey(nRow, dataKey, null);
//                return false;
//            }
//        }
//        return true;
//    }

//    public void originValChange(String value, int nRow){
//        dataChangeFlag = true;
//        //设置检验员
//        setSampleComTestStaff(nRow);
//        //检测分项pt
//        InspectionSubEntity sampleCom = myInspectionSubList.get(nRow);
//        boolean clearFalg = false;
//
//        if(!StringUtil.isEmpty(value)){
//            if(sampleCom.getValueKind().getId().equals("LIMSBasic_valueKind/number")  || sampleCom.getValueKind().getId().equals("LIMSBasic_valueKind/calculate")){
//                //数值、计算类型
//                if(value != null){
//                    //获取修约值
//                    var roundValue = roundingValue(parseFloat(value), sampleCom.digitType, sampleCom.carrySpace, sampleCom.carryType, sampleCom.carryFormula);
//                    sampleCom.originValue = value;
//                    sampleCom.roundValue = roundValue;
//                    sampleCom.dispValue = roundValue;
//                    //高低限判断
//                    if(sampleCom.limitType != null){
//                        var dispValue = sectionJudgment(parseFloat(value), sampleCom.limitType.id, sampleCom.maxValue, sampleCom.minValue);
//                        if(dispValue != "reject"){
//                            if(dispValue != parseFloat(value)){
//                                sampleCom.dispValue = dispValue;
//                            }else{
//                                sampleCom.dispValue = roundValue;
//                            }
//                        }else{
//                            clearFalg = true;
//                        }
//                    }
//                }else{
//                    clearFalg = true;
//                }
//            }else{
//                sampleCom.originValue = value;
//                sampleCom.roundValue = value;
//                sampleCom.dispValue = value;
//            }
//        }else{
//            clearFalg = true;
//        }
//
//        if(clearFalg){
//            sampleCom.originValue = null;
//            sampleCom.roundValue = null;
//            sampleCom.dispValue = null;
//        }
//
//        sampleComDg.setRowData(nRow, sampleCom);
//        dispValueChange(sampleCom.roundValue,nRow);
//    }

    //设置样品检测分项的检验员
//    public void setSampleComTestStaff(int nRow){
//        //样品分项pt
//        myInspectionSubList.setCellValueByKey(nRow, "testStaffId", ReactAPI.getUserInfo().staff);
//    }

    public void manualCalculate() {
        //自动计算
        autoCalculate = false;
        readyCalculate(myInspectionSubList);
    }

//    function dispValueOnchange(value,nRow){
//        dataChangeFlag = true;
//        setSampleComTestStaff(nRow);
//        dispValueChange(value,nRow);
//        //样品分项pt
//        var sampleComDg = ReactAPI.getComponentAPI('SupDataGrid').APIs('LIMSSample_5.0.0.0_sample_recordBySampledg1592378259237');
//
//        //值类型不为计算且参数中包含该分项的参与计算；值类型为计算时，改变报出值会自动计算，重新赋值导致无法修改报出值
//        if(sampleComDg.getSelecteds()[0].valueKind.id !=="LIMSBasic_valueKind/calculate" && checkAutoCalculate(nRow, sampleComDg.getDatagridData())){
//            //自动计算
//            autoCalculate = true;
//            readyCalculate(sampleComDg.getDatagridData());
//        }
//    }

    //判断是否需要自动计算
//    function checkAutoCalculate(nRow, sampleComDgData){
//        //当前修改的分项
//        var sampleCom = sampleComDgData[nRow];
//
//        //遍历分项，查找是否存在值类型为计算的分项，如果有则查找其参数是否包含本次修改的分项
//        for(var i = 0; i < sampleComDgData.length; i++){
//            if(sampleComDgData[i].valueKind.id == "LIMSBasic_valueKind/calculate"){
//                var paramNameArr = sampleComDgData[i].calculateParamNames;
//                if(paramNameArr.indexOf(sampleCom.comName) > -1){
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

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
            //calculate(calArrays);
        }

    }

    //
    //确定计算顺序
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
//    function calculate(sampleComs){
//        for(var i = 0; i < sampleComs.length; i++){
//            eval(sampleComs[i].executableFormula);
//            var calculateRes = setAlgorithm();
//            if(typeof calculateRes == "number" && !isNaN(calculateRes) && isFinite(calculateRes)){
//                originValChange(calculateRes, sampleComs[i].rowIndex);
//            }else{
//                if(sampleComs[i].originValue !== null && sampleComs[i].originValue !== ""){
//                    originValChange(null, sampleComs[i].rowIndex);
//                }
//            }
//        }
//    }

//    public Object setAlgorithm(){
//        Object res = null;
//        ScriptEngineManager manager = new ScriptEngineManager();
//        ScriptEngine engine = manager.getEngineByName("javascript");
//        try {
//            engine.eval("function calucate(){\n" +
//                    "\tvar p = 0.3;\n" +
//                    "\tvar re = '';\n" +
//                    "    if(p > 0.1) {\n" +
//                    "\t\tre = p.toFixed(2);\n" +
//                    "\t} else {\n" +
//                    "\t\tre = p.toFixed(3);\n" +
//                    "\t}　\n" +
//                    "    return re;\n" +
//                    "}");
//
//            Invocable inv2 = (Invocable) engine;
//            res = inv2.invokeFunction("calucate");
//           return res;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return res;
//    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private String getCalculateParamValue(String paramType, String testItem, String comName, String incomeType, String outcomeType, String dealFunc) {
        List<InspectionSubEntity> sampeComs = new ArrayList<>();
        if (paramType == "LIMSBasic_paramType/sameItem") {
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
                    return valueList.stream().mapToDouble(Double::doubleValue).average()+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/sum") ){
                    //求和
                    return valueList.stream().mapToDouble(Double::doubleValue).sum()+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/count") ){
                    //个数
                    return valueList.size()+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/max") ){
                    //最大值
                    return valueList.stream().mapToDouble(Double::doubleValue).max()+"";
                }else if(dealFunc.equals("LIMSBasic_dealFunc/min") ){
                    //最小值
                    return valueList.stream().mapToDouble(Double::doubleValue).min()+"";
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

}

