package com.supcon.mes.module_lims.controller;

import android.content.Context;
import android.view.View;

import com.supcon.common.view.base.controller.BaseViewController;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.middleware.SupPlantApplication;
import com.supcon.mes.middleware.controller.SystemConfigController;
import com.supcon.mes.middleware.model.bean.ModuleConfigEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.R;
import com.supcon.mes.module_lims.constant.LimsConstant;
import com.supcon.mes.module_lims.model.bean.BaseLongIdNameEntity;
import com.supcon.mes.module_lims.model.bean.CalcParamInfoEntity;
import com.supcon.mes.module_lims.model.bean.ConclusionEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_lims.model.bean.SampleComResEntity;
import com.supcon.mes.module_lims.model.bean.SpecLimitEntity;
import com.supcon.mes.module_lims.utils.Util;

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


/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class CalculationController extends BaseViewController {

    private boolean autoCalculate;  //判定当前是手动计算还是自动计算
    private ScriptEngine engine; //执行js 代码的实体对象
    private String specialResultStr = "";
    private SystemConfigController systemConfigController;

    private NotifyRefreshAdapterListener mNotifyRefreshAdapterListener;

    List<Double> list = new ArrayList<>();

    public CalculationController(View rootView) {
        super(rootView);
    }


    @Override
    public void initView() {
        super.initView();

        systemConfigController = new SystemConfigController(context);

        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("javascript");

            String path1 = getAssetsCacheFile(context, "numeric.js");
            String path2 = getAssetsCacheFile(context, "numeral.min.js");
            String path3 = getAssetsCacheFile(context, "jstat.min.js");
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
    }

    public ScriptEngine getEngine() {
        return engine;
    }

    @Override
    public void initListener() {
        super.initListener();

//        systemConfigController.getSystemConfig("LIMSBasic_1.0.0_testItem", "LIMSBasic.specialResult", "", new SystemConfigController.SystemConfigResultListener() {
//            @Override
//            public void systemConfigResult(boolean isSuccess, JsonObject jsonObject, String msg) {
//                if (isSuccess) {
//                    try {
//                        JsonElement jsonElement = jsonObject.get("LIMSBasic.specialResult");
//                        String asString = jsonElement.getAsString();
//                        specialResultStr = asString;
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
        systemConfigController.getModuleConfig(LimsConstant.ModuleCode.LIMS_MODULE_CODE, LimsConstant.Keys.SPECIAL_RESULT, new OnSuccessListener() {
            @Override
            public void onSuccess(Object result) {
                if (null != result){
                    try {
                        ModuleConfigEntity bean = (ModuleConfigEntity)result;
                        specialResultStr = bean.getSpecialResult();
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    //手动计算
    public void manualCalculate(List<InspectionSubEntity> adapterList , NotifyRefreshAdapterListener mNotifyRefreshAdapterListener) {
        this.mNotifyRefreshAdapterListener = mNotifyRefreshAdapterListener;
        //手动计算
        autoCalculate = false;
        readyCalculate(adapterList);
    }

    //报出值改变时的监听事件
    public void dispValueOnchange(String value, int nRow, List<InspectionSubEntity> adapterList,NotifyRefreshAdapterListener mNotifyRefreshAdapterListener) {
        this.mNotifyRefreshAdapterListener = mNotifyRefreshAdapterListener;
        setSampleComTestStaff(nRow,adapterList);
        dispValueChange(value, nRow, adapterList);
        //样品分项pt

        //值类型不为计算且参数中包含该分项的参与计算；值类型为计算时，改变报出值会自动计算，重新赋值导致无法修改报出值
        if (!adapterList.get(nRow).getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE) && checkAutoCalculate(nRow, adapterList)) {
            //自动计算
            autoCalculate = true;
            readyCalculate(adapterList);
        }
    }

    //报出值改变函数
    public void dispValueChange(String value, int nRow, List<InspectionSubEntity> adapterList) {
        //检测分项pt
        InspectionSubEntity sampleCom = adapterList.get(nRow);
        List<String> specLimitList = null;
        HashMap<String, Object> dispMap = sampleCom.getDispMap();
        for (String key : dispMap.keySet()) {
            if (key.equals("specLimit")) {
                specLimitList = (List<String>) dispMap.get(key);
                break;
            }
        }

        //对检验结果进行等级判定   specLimitList = disMap 中取到的 specLimit 类型为List<String>
        if (specLimitList != null && specLimitList.size() > 0) {
            for (int i = 0; i < specLimitList.size(); i++) {
                String resultGrade = null;
                List<SpecLimitEntity> specLimits = GsonUtil.jsonToList(specLimitList.get(i), SpecLimitEntity.class);
                Object[] specListsArr = specLimits.toArray();
                if (!StringUtil.isEmpty(value)) {
                    String limitType = "";
                    if (sampleCom.getLimitType() != null) {
                        limitType = sampleCom.getLimitType().getId();
                    }

                    try {
                        Invocable invoke = (Invocable) engine;
                        Object gradeDetermine = invoke.invokeFunction("gradeDetermine", value, specListsArr, specialResultStr, limitType);
                        resultGrade = (String) gradeDetermine;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                //根据dismap.specLimit中[[resultKey]] 的值 找到 disMap 中的建 并对其重新赋值  供提交
                for (String key : adapterList.get(nRow).getDispMap().keySet()) {
                    if (key.equals(specLimits.get(0).getResultKey())) {
                        adapterList.get(nRow).getDispMap().put(key, resultGrade);

                    }
                    if (key.equals("sampleComRes")){
                        List<SampleComResEntity> sampleComResList = GsonUtil.jsonToList((String) adapterList.get(nRow).getDispMap().get(key),SampleComResEntity.class);
                        for (int h = 0; h < sampleComResList.size(); h++) {
                            if (sampleComResList.get(h).getResultKey().equals(specLimits.get(0).getResultKey())){
                                sampleComResList.get(h).setTestResult(resultGrade);
                            }
                        }
                        adapterList.get(nRow).getDispMap().put("sampleComRes",GsonUtil.gsonString(sampleComResList));
                    }
                }

                //将结论放入对应的结论中  供展示
                List<ConclusionEntity> conclusionList = adapterList.get(nRow).getConclusionList();
                for (int j = 0; j < conclusionList.size(); j++) {
                    if (specLimits.get(0).getResultKey().equals(conclusionList.get(j).getColumnKey())){
                        conclusionList.get(j).setFinalResult(resultGrade);
                    }
                }

            }
        }
        //改变结论的颜色
        resultColorChange(nRow, adapterList, adapterList.get(nRow).getConclusionList());
    }

    //原始值改变时的监听
    public void originValOnChange(String value, int nRow, List<InspectionSubEntity> adapterList,NotifyRefreshAdapterListener mNotifyRefreshAdapterListener) {
        this.mNotifyRefreshAdapterListener = mNotifyRefreshAdapterListener;
        if (!judgValueLegal(value, nRow, "originValue",adapterList)) {
            ToastUtils.show(context, context.getResources().getString(R.string.LIMSSample_sample_error_valueKindError));
            return;
        }
        originValChange(value, nRow, adapterList);
        //样品检测分项pt
        if (checkAutoCalculate(nRow, adapterList)) {
            //自动计算
            autoCalculate = true;
            readyCalculate(adapterList);
        }
    }

    public void originValChange(String value, int nRow, List<InspectionSubEntity> adapterList) {
        //设置检验员
        setSampleComTestStaff(nRow,adapterList);
        //检测分项pt
        InspectionSubEntity sampleCom = adapterList.get(nRow);
        boolean clearFalg = false;

        if (!StringUtil.isEmpty(value)) {
            if (sampleCom.getValueKind().getId().equals(LimsConstant.ValueType.NUMBER) || sampleCom.getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
                //数值、计算类型
                //获取修约值
                Object roundValue = null;
                try {
                    Invocable invoke = (Invocable) engine;
                    roundValue = invoke.invokeFunction("roundingValue", value, sampleCom.getDigitType(), sampleCom.getCarrySpace(), sampleCom.getCarryType(), sampleCom.getCarryFormula());
                    roundValue = checkValue(roundValue+"");
                    sampleCom.setOriginValue(value);
                    sampleCom.setRoundValue(roundValue + "");
                    sampleCom.setDispValue(roundValue + "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //高低限判断
                if (sampleCom.getLimitType() != null) {

                    try {
                        Invocable invoke = (Invocable) engine;
                        Object dispValue = invoke.invokeFunction("sectionJudgment", value, sampleCom.getLimitType().getId(), sampleCom.getMaxValue(), sampleCom.getMinValue());
                        if (dispValue != "reject") {
                            if ((dispValue + "").equals(value) ) {
                                sampleCom.setDispValue(dispValue + "");
                            } else {
                                sampleCom.setDispValue(roundValue + "");
                            }
                        } else {
                            clearFalg = true;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

            } else {
                sampleCom.setOriginValue(value);
                sampleCom.setRoundValue(value);
                sampleCom.setDispValue(value);
            }
        } else {
            clearFalg = true;
        }

        if (clearFalg) {
            sampleCom.setOriginValue(null);
            sampleCom.setRoundValue(null);
            sampleCom.setDispValue(null);
        }

        adapterList.set(nRow, sampleCom);
        dispValueChange(sampleCom.getRoundValue(), nRow, adapterList);
    }

    //整理计算顺序
    public void readyCalculate(List<InspectionSubEntity> myInspectionSubList) {
        Map<Integer, Object> map = new HashMap<>();//存放不同深度的对象集合，用于确定计算顺序
        List<Integer> keyArr = new ArrayList<>();//存放key,用于遍历
        List<InspectionSubEntity> calArrays = null;//存放不同计算层级的对象
        for (int i = 0; i < myInspectionSubList.size(); i++) {
            if (myInspectionSubList.get(i).getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
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
            calculate(calArrays, myInspectionSubList);
        }

    }

    // 确定计算顺序
    public int calOrder(InspectionSubEntity sampleCom, List<InspectionSubEntity> myInspectionSubList) {
        if (sampleCom == null || !sampleCom.getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
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
    public void calculate(List<InspectionSubEntity> sampleComs, List<InspectionSubEntity> adapterList) {
        for (int i = 0; i < sampleComs.size(); i++) {
            if (StringUtil.isEmpty(sampleComs.get(i).getCalcParamInfo())) {
                ToastUtils.show(context, context.getResources().getString(R.string.lims_not_calculation_formula));
                return;
            }

            StringBuffer script = new StringBuffer("function executeFunc(){");
            String calcParamInfo = sampleComs.get(i).getCalcParamInfo();
            List<CalcParamInfoEntity> calcParamInfoList = GsonUtil.jsonToList(calcParamInfo, CalcParamInfoEntity.class);
            for (int j = 0; j < calcParamInfoList.size(); j++) {
                String calculateParamValue = getCalculateParamValue(adapterList, calcParamInfoList.get(j).getParamType().getId(),
                        calcParamInfoList.get(j).getTestItemName(), calcParamInfoList.get(j).getTestComName(),
                        calcParamInfoList.get(j).getIncomeType().getId(), calcParamInfoList.get(j).getOutcomeType().getId(),
                        calcParamInfoList.get(j).getDealFunc().getId());

                calculateParamValue = checkValue(calculateParamValue);
                script.append("var " + calcParamInfoList.get(j).getParamName() + "=" + calculateParamValue + ";");
            }
            script.append(sampleComs.get(i).getCalculFormula() + "}");

            try {
                engine.eval(script.toString());
                Invocable inv2 = (Invocable) engine;
                Object res = inv2.invokeFunction("executeFunc");//执行计算公式
                String result = checkValue(String.valueOf(res));
                if (Util.isNumeric(result)) {
                    for (int k = 0; k < adapterList.size(); k++) {
                        if (adapterList.get(k).getId().equals(sampleComs.get(i).getId())) {
                            originValChange(result, k, adapterList);
                            break;
                        }
                    }
                } else {
                    for (int k = 0; k < adapterList.size(); k++) {
                        if (adapterList.get(k).getId().equals(sampleComs.get(i).getId())) {
                            if (!StringUtil.isEmpty(sampleComs.get(i).getOriginValue())) {
                                originValChange(null, k, adapterList);
                            }
                        }
                    }

                }
            } catch (Exception e) {
                for (int k = 0; k < adapterList.size(); k++) {
                    if (adapterList.get(k).getId().equals(sampleComs.get(i).getId())) {
                        originValChange(null, k, adapterList);
                        break;
                    }
                }
            }
        }
    }

    //获取计算所需的参数
    private String getCalculateParamValue(List<InspectionSubEntity> adapterList, String paramType, String testItem, String comName, String incomeType, String outcomeType, String dealFunc) {
        List<InspectionSubEntity> sampeComs = new ArrayList<>();
        if (paramType.equals("LIMSBasic_paramType/sameItem")) {
            //同样品同检测项目下分项 （按样品录入结果）
            sampeComs = searchSampleCom(comName, adapterList);
        } else {
            ////同样品不同检测项目下分项 (单样品录入结果)
        }

        if (null != sampeComs) {
            List<String> valueArr = new ArrayList<>();
            for (int i = 0; i < sampeComs.size(); i++) {
                if (incomeType.equals("LIMSBasic_incomeType/originValue")) {
                    if (StringUtil.isEmpty(sampeComs.get(i).getOriginValue())) {
                        if (!autoCalculate) { //手动计算
                            //原始值为空，非自动计算时报错
                            String string = context.getResources().getString(R.string.LIMSSample_sample_originValIsNull);
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
                            String string = context.getResources().getString(R.string.LIMSSample_sample_roundValIsNull);
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
                            String string = context.getResources().getString(R.string.LIMSSample_sample_dispValIsNull);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    } else if (!StringUtil.isEmpty(sampeComs.get(i).getDispValue()) && !Util.isNumeric(sampeComs.get(i).getDispValue())) {
                        if (!autoCalculate) {
                            //报出值非数字，非自动计算时报错
                            String string = context.getResources().getString(R.string.LIMSSample_sample_dispValTypeError);
                            String format = String.format(string, sampeComs.get(i).getComName(), sampeComs.get(i).getParallelNo() + "");
                            ToastUtils.show(context, format);
                        }
                        return null;
                    }
                    valueArr.add(sampeComs.get(i).getDispValue());
                }

            }

//            //能进到该方法中的 一定是数值类型的 所以直接将String 转 Double
//            List<Double> valueList = new ArrayList<>();
//            for (int i = 0; i < valueArr.size(); i++) {
//                valueList.add(Double.valueOf(valueArr.get(i)));
//            }


            if (outcomeType.equals("LIMSBasic_outcomeType/dealFunc")) {
                //输出类型为处理值
                    Object res = null;
                if (dealFunc.equals("LIMSBasic_dealFunc/avg")) {
                        list.clear();
                    try {
                        for (int i = 0; i < valueArr.size(); i++) {
                            list.add(Double.valueOf(valueArr.get(i)));
                        }
                        res = Util.getAvg(list);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    //平均值
                    return res + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/sum")) {
                    //求和
                    try {
                        Invocable invoke = (Invocable) engine;
                        res = invoke.invokeFunction("sum", valueArr.toArray());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/count")) {
                    //个数
                    return valueArr.size() + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/max")) {
                    //最大值
                    list.clear();
                    try {
                        for (int i = 0; i < valueArr.size(); i++) {
                            list.add(Double.valueOf(valueArr.get(i)));
                        }
                        res = Util.getMax(list);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/min")) {
                    //最小值
                    list.clear();
                    try {
                        for (int i = 0; i < valueArr.size(); i++) {
                            list.add(Double.valueOf(valueArr.get(i)));
                        }
                        res = Util.getMin(list);
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/stdev")) {
                    //标准方差
                    try {
                        Invocable invoke = (Invocable) engine;
                        res = invoke.invokeFunction("stdev", valueArr.toArray());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }

                    return res + "";
                }
            } else if (outcomeType.equals("LIMSBasic_outcomeType/arr")) {
                //输出类型为数组
                return "[" + valueArr.toString() + "]";
            }


        }


        return null;
    }

    //设置样品检测分项的检验员
    public void setSampleComTestStaff(int nRow, List<InspectionSubEntity> adapterList) {
        //样品分项pt
        BaseLongIdNameEntity entity = new BaseLongIdNameEntity();
        entity.setId(SupPlantApplication.getAccountInfo().staffId);
        entity.setName(SupPlantApplication.getAccountInfo().staffName);
        adapterList.get(nRow).setTestStaffId(entity);
    }

    //结论颜色改变函数
    public void resultColorChange(int nRow, List<InspectionSubEntity> adapterList, List<ConclusionEntity> gradeResult) {
        //检测分项pt
        InspectionSubEntity sampleCom = adapterList.get(nRow);
        //subList.get(nRow).setConclusionState(false);
        HashMap<String, Object> dispMap = sampleCom.getDispMap();

        if (gradeResult != null && gradeResult.size() > 0) {
            for (int i = 0; i < gradeResult.size(); i++) {
                for (String key : dispMap.keySet()) {
                    if (gradeResult.get(i).getColumnKey().equals(key)) {
                        if (String.valueOf(dispMap.get(key)).equals(gradeResult.get(i).getColumnList().get(0).getResult())) { //表示如果当前的结论是 不合格的话
                            adapterList.get(nRow).getConclusionList().get(i).setQualified(false);
                        } else {
                            adapterList.get(nRow).getConclusionList().get(i).setQualified(true);
                        }
                        if (mNotifyRefreshAdapterListener != null){
                            mNotifyRefreshAdapterListener.notifyRefreshAdapter(nRow);
                        }
                        //adapter.notifyItemChanged(nRow);
                        return;
                    }
                }

            }
        }
    }

    //判断是否需要自动计算
    public boolean checkAutoCalculate(int nRow, List<InspectionSubEntity> sampleComDgData) {
        //当前修改的分项
        InspectionSubEntity sampleCom = sampleComDgData.get(nRow);

        //遍历分项，查找是否存在值类型为计算的分项，如果有则查找其参数是否包含本次修改的分项
        for (int i = 0; i < sampleComDgData.size(); i++) {
            if (sampleComDgData.get(i).getValueKind().getId().equals(LimsConstant.ValueType.CALCULATE)) {
                String paramNameArr = sampleComDgData.get(i).getCalculateParamNames();
                if (!StringUtil.isEmpty(paramNameArr) && paramNameArr.contains(sampleCom.getComName())) {
                    return true;
                }
            }
        }
        return false;
    }

    //输入值是否合法判断
    public boolean judgValueLegal(String value, int nRow, String dataKey, List<InspectionSubEntity> adapterList) {
        //样品分项pt
        String valueKind = adapterList.get(nRow).getValueKind().getId();
        if (valueKind.equals(LimsConstant.ValueType.CALCULATE) || valueKind.equals(LimsConstant.ValueType.NUMBER)) {
            if (!Util.isNumeric(value)) {
                setValueByKey(adapterList, nRow, dataKey);
                return false;
            }
        }
        return true;
    }

    //通过key 设置值
    public void setValueByKey(List<InspectionSubEntity> list, int nRow, String dataKey) {
        if (dataKey.equals("originValue")) {
            list.get(nRow).setOriginValue(null);
        }

        if (mNotifyRefreshAdapterListener != null){
            mNotifyRefreshAdapterListener.notifyRefreshAdapter(nRow);
        }
        //adapter.notifyDataSetChanged();
    }


    //获取文件缓存位置
    public String getAssetsCacheFile(Context context, String fileName) {
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

    public String checkValue(String str){
        if (StringUtil.isEmpty(str)){
            return "";
        }
        if (com.supcon.mes.middleware.util.Util.isNumeric(str)){
            if (com.supcon.mes.middleware.util.Util.isContainPoint(str)){
                return com.supcon.mes.middleware.util.Util.removePoint(str);
            }else {
                return str;
            }
        }else {
            return str;
        }

    }


    public interface NotifyRefreshAdapterListener{
        void notifyRefreshAdapter(int position);
    }
}
