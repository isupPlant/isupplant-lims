package com.supcon.mes.module_sample.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.app.annotation.BindByTag;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.supcon.common.view.base.adapter.BaseListDataRecyclerViewAdapter;
import com.supcon.common.view.base.adapter.viewholder.BaseRecyclerViewHolder;
import com.supcon.common.view.util.ToastUtils;
import com.supcon.mes.mbap.utils.GsonUtil;
import com.supcon.mes.mbap.utils.controllers.SinglePickController;
import com.supcon.mes.mbap.view.CustomEditText;
import com.supcon.mes.mbap.view.CustomSpinner;
import com.supcon.mes.mbap.view.CustomTextView;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.model.bean.SampleCheckResultEntity;
import com.supcon.mes.module_sample.model.bean.SampleInspectItemEntity;
import com.supcon.mes.middleware.model.listener.OnSuccessListener;
import com.supcon.mes.middleware.util.StringUtil;
import com.supcon.mes.module_lims.model.bean.StdJudgeEntity;
import com.supcon.mes.module_lims.model.bean.StdJudgeSpecEntity;
import com.supcon.mes.module_lims.utils.FileUtils;
import com.supcon.mes.module_lims.utils.Util;
import com.supcon.mes.module_sample.R;
import com.supcon.mes.module_sample.controller.LimsFileUpLoadController;
import com.supcon.mes.module_lims.model.bean.CalcParamInfoEntity;
import com.supcon.mes.module_lims.model.bean.InspectionItemColumnEntity;
import com.supcon.mes.module_lims.model.bean.InspectionSubEntity;
import com.supcon.mes.module_sample.model.bean.SampleCheckResultEntity;
import com.supcon.mes.module_sample.model.bean.SampleInspectItemEntity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.script.Invocable;
import javax.script.ScriptEngine;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SingleSampleInpectAdapter extends BaseListDataRecyclerViewAdapter<SampleInspectItemEntity> {

    public SingleSampleInpectAdapter(Context context) {
        super(context);
    }

    @Override
    protected BaseRecyclerViewHolder getViewHolder(int viewType) {
        return new SingleSampleInspectViewHolder(context);
    }

    public Activity activity;
    public ScriptEngine engine;
    Map<String, List<CustomEditText>> calculateListMap = new HashMap<>();
    Map<String, SampleInspectItemEntity> calculateMap = new HashMap<>();
    Map<String, List<SampleInspectItemEntity>> calculateInspectItemEntityMap = new HashMap<>();
    Map<String, CustomEditText> calculateTextMap = new HashMap<>();

    class SingleSampleInspectViewHolder extends BaseRecyclerViewHolder<SampleInspectItemEntity> {

        @BindByTag("ctInspectionItems")
        CustomTextView ctInspectionItems;//检验分项
        @BindByTag("busiVersionTv")
        CustomTextView busiVersionTv;//版本号
        @BindByTag("testItemsTv")
        CustomTextView testItemsTv;//检验分项
        @BindByTag("ceOriginalValue")
        CustomEditText ceOriginalValue;//编辑的原始值
        @BindByTag("cpOriginalValue")//选择的原始值
                CustomSpinner cpOriginalValue;
        @BindByTag("ctRoundOffValue")
        CustomTextView ctRoundOffValue;
        @BindByTag("ceReportedValue")
        CustomEditText ceReportedValue;
        @BindByTag("llQualityStandard")
        LinearLayout llQualityStandard;
        @BindByTag("ivExpand")
        ImageView ivExpand;
        @BindByTag("llRangeCheckResult")
        LinearLayout llRangeCheckResult;
        @BindByTag("llEnclosure")
        LinearLayout llEnclosure;
        @BindByTag("imageUpDown")
        ImageView imageUpDown;
        @BindByTag("imageFileView")
        ImageView imageFileView;
        boolean firstExpand = true;//是否是第一次展开的，默认是第一次
        private SinglePickController<String> mPickController;
        private List<String> cpOriginalValueList = new ArrayList<>();


        public SingleSampleInspectViewHolder(Context context) {
            super(context);
            mPickController = new SinglePickController(activity);
            mPickController.textSize(18);
        }

        @Override
        protected int layoutId() {
            return R.layout.item_single_input_result_inspect;
        }

        @Override
        protected void initView() {
            super.initView();

        }

        @Override
        protected void initListener() {
            super.initListener();
            RxView.clicks(llQualityStandard)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        int position = getAdapterPosition();
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(position);
                        Map<String, Object> dispMap = detailEntity.getDispMap();
                        if (!dispMap.containsKey("sampleComRes")) {
                            ToastUtils.show(context, "没有更多展开的内容了！");
                            return;
                        }
                        if (firstExpand) {
                            firstExpand = false;
                            try {
                                if (dispMap.containsKey("sampleComRes")) {
                                    Log.i("SampleInspectItemEntity", getAdapterPosition() + ":" + dispMap.get("sampleComRes").toString());
                                    List<SampleCheckResultEntity> sampleComRes = GsonUtil.jsonToList(dispMap.get("sampleComRes").toString(), SampleCheckResultEntity.class);
                                    List<String> specLimitStr = (List<String>) dispMap.get("specLimit");
                                    if (sampleComRes != null && !sampleComRes.isEmpty()) {
                                        addRangeCheckResultView(detailEntity.getId(), llRangeCheckResult, sampleComRes, specLimitStr);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        detailEntity.setExpand(!detailEntity.isExpand());
                        if (detailEntity.isExpand())
                            ivExpand.setImageResource(R.drawable.ic_drop_down);
                        else
                            ivExpand.setImageResource(R.drawable.ic_drop_up);
                        int visibility = detailEntity.isExpand() ? View.VISIBLE : View.GONE;
                        llRangeCheckResult.setVisibility(visibility);
                    });


            RxView.clicks(cpOriginalValue.findViewById(R.id.customSpinnerText))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(getAdapterPosition());
                        showSpinnerSelector(getAdapterPosition(), detailEntity, cpOriginalValue, ctRoundOffValue, ceReportedValue, cpOriginalValueList, mPickController);
                    });
            RxView.clicks(cpOriginalValue.findViewById(R.id.customSpinner))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(getAdapterPosition());
                        showSpinnerSelector(getAdapterPosition(), detailEntity, cpOriginalValue, ctRoundOffValue, ceReportedValue, cpOriginalValueList, mPickController);
                    });
            RxView.clicks(cpOriginalValue.findViewById(R.id.customSpinnerIcon))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(getAdapterPosition());
                        showSpinnerSelector(getAdapterPosition(), detailEntity, cpOriginalValue, ctRoundOffValue, ceReportedValue, cpOriginalValueList, mPickController);
                    });

            RxTextView.textChanges(ceOriginalValue.editText())
                    .skipInitialValue()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map(CharSequence::toString)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(originalValue -> {
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(getAdapterPosition());
                        editSampleInspectItem(detailEntity, originalValue, ceOriginalValue, ctRoundOffValue, ceReportedValue);
                    });
            RxTextView.textChanges(ceReportedValue.editText())
                    .skipInitialValue()
                    .debounce(500, TimeUnit.MILLISECONDS)
                    .map(CharSequence::toString)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(dispValue -> {
                        SampleInspectItemEntity detailEntity = (SampleInspectItemEntity) getItem(getAdapterPosition());
                        detailEntity.setDispValue(dispValue);
                        editSampleInspectReportValue(dispValue, detailEntity);
                    });
            RxView.clicks(imageUpDown)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(imageUpDown, 1);
                    });
            RxView.clicks(imageFileView)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        onItemChildViewClick(imageFileView, 2);
                    });
        }

        @Override
        protected void update(SampleInspectItemEntity data) {
            ctInspectionItems.setValue(data.getSampleTestId() != null && data.getSampleTestId().getTestId() != null ? data.getSampleTestId().getTestId().getName() : "");
            busiVersionTv.setValue(data.getSampleTestId() != null && data.getSampleTestId().getTestId() != null ? data.getSampleTestId().getTestId().getBusiVersion() : "");
            testItemsTv.setValue(data.getComName());
            ctRoundOffValue.setValue(data.getRoundValue());
            ceReportedValue.setContent(data.getDispValue());
            String busiVersion = data.getSampleTestId().getTestId().getBusiVersion();
            if ("LIMSBasic_valueKind/enum".equals(data.getValueKind().getId())) {
                String optionValues = data.getOptionValues();
                String[] optionValuesArr = optionValues.split("@##@");
                for (String optionValue : optionValuesArr) {
                    cpOriginalValueList.add(optionValue);
                }
                ceOriginalValue.setVisibility(View.GONE);
                cpOriginalValue.setVisibility(View.VISIBLE);
                cpOriginalValue.setContent(data.getOriginValue());
                cpOriginalValue.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            } else if ("LIMSBasic_valueKind/calculate".equals(data.getValueKind().getId())) {
                cpOriginalValue.setVisibility(View.GONE);
                ceOriginalValue.setEditable(false);
                ceOriginalValue.setVisibility(View.VISIBLE);
                ceOriginalValue.setContent(data.getOriginValue());
                calculateListMap.put(busiVersion, new ArrayList<>());
                calculateInspectItemEntityMap.put(busiVersion, new ArrayList<>());
                calculateTextMap.put(busiVersion, ceOriginalValue);
                calculateMap.put(busiVersion, data);
            } else if ("LIMSBasic_valueKind/number".equals(data.getValueKind().getId())) {
                cpOriginalValue.setVisibility(View.GONE);
                ceOriginalValue.setVisibility(View.VISIBLE);
                ceOriginalValue.setContent(data.getOriginValue());
                ceOriginalValue.setEditable(true);
                ceOriginalValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                if (calculateListMap.containsKey(busiVersion)) {
                    List<CustomEditText> originalValueList = calculateListMap.get(busiVersion);
                    originalValueList.add(ceOriginalValue);
                    List<SampleInspectItemEntity> sampleInspectItemEntities = calculateInspectItemEntityMap.get(busiVersion);
                    sampleInspectItemEntities.add(data);
                }
            } else if ("LIMSBasic_valueKind/text".equals(data.getValueKind().getId())) {
                cpOriginalValue.setVisibility(View.GONE);
                ceOriginalValue.setVisibility(View.VISIBLE);
                ceOriginalValue.setContent(data.getOriginValue());
                ceOriginalValue.setEditable(true);
            }
        }
    }


    //计算
    public void calculate(String busiVersion, CustomEditText ceOriginalValue) {
        List<SampleInspectItemEntity> sampleInspectItemEntities = calculateInspectItemEntityMap.get(busiVersion);
        for (int i = 0; i < sampleInspectItemEntities.size(); i++) {
            if (StringUtil.isEmpty(sampleInspectItemEntities.get(i).getCalcParamInfo())) {
//                ToastUtils.show(context,"暂无计算公式");
                return;
            }
            SampleInspectItemEntity itemEntity = calculateMap.get(busiVersion);
            StringBuffer script = new StringBuffer("function executeFunc(){");
            String calcParamInfo = sampleInspectItemEntities.get(i).getCalcParamInfo();
            List<CalcParamInfoEntity> calcParamInfoList = GsonUtil.jsonToList(calcParamInfo, CalcParamInfoEntity.class);
            for (int j = 0; j < calcParamInfoList.size(); j++) {
                String calculateParamValue = getCalculateParamValue(true, calcParamInfoList.get(j).getParamType().getId(),
                        calcParamInfoList.get(j).getTestItemName(), calcParamInfoList.get(j).getTestComName(),
                        calcParamInfoList.get(j).getIncomeType().getId(), calcParamInfoList.get(j).getOutcomeType().getId(),
                        calcParamInfoList.get(j).getDealFunc().getId());

                if (StringUtil.isEmpty(calculateParamValue)) {
                    return;
                }
                script.append("var " + calcParamInfoList.get(j).getParamName() + "=" + calculateParamValue + ";");
            }
            script.append(itemEntity.getCalculFormula() + "}");

            try {
                engine.eval(script.toString());
                Invocable inv2 = (Invocable) engine;
                Object res = inv2.invokeFunction("executeFunc");//执行计算公式
                ceOriginalValue.setContent(res.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private String getCalculateParamValue(boolean autoCalculate, String paramType, String testItem, String comName, String incomeType, String outcomeType, String dealFunc) {
        List<InspectionSubEntity> sampeComs = new ArrayList<>();
        if (null != sampeComs) {
            List<String> valueArr = new ArrayList<>();
            for (int i = 0; i < sampeComs.size(); i++) {
                if (incomeType.equals("LIMSBasic_incomeType/originValue")) {
                    if (StringUtil.isEmpty(sampeComs.get(i).getOriginValue())) {
                        if (!autoCalculate) {
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

            //能进到该方法中的 一定是数值类型的 所以直接将String 转 Double
            List<Double> valueList = new ArrayList<>();
            for (int i = 0; i < valueArr.size(); i++) {
                valueList.add(Double.valueOf(valueArr.get(i)));
            }

            if (outcomeType.equals("LIMSBasic_outcomeType/dealFunc")) {
                //输出类型为处理值

                if (dealFunc.equals("LIMSBasic_dealFunc/avg")) {
                    //平均值
                    return Util.getAvg(valueList) + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/sum")) {
                    //求和
                    return Util.getSum(valueList) + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/count")) {
                    //个数
                    return valueList.size() + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/max")) {
                    //最大值
                    return Util.getMax(valueList) + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/min")) {
                    //最小值
                    return Util.getMin(valueList) + "";
                } else if (dealFunc.equals("LIMSBasic_dealFunc/stdev")) {
                    //标准方差
                    return Util.standardDiviation(valueList) + "";
                }
            } else if (outcomeType.equals("LIMSBasic_outcomeType/arr")) {
                //输出类型为数组
                return "[" + valueArr.toString() + "]";
            }


        }


        return null;
    }

    private void editSampleInspectReportValue(String reportValue, SampleInspectItemEntity detailEntity) {
        List<CustomSpinner> reportedValueCheckResults = mapReportedValueCheckResult.get(detailEntity.getId());
        List<List<StdJudgeEntity>> stdJudegeList = stdJudegeListMap.get(detailEntity.getId());
        List<SampleCheckResultEntity> sampleComRes = sampleCheckResultMap.get(detailEntity.getId());
        if (reportedValueCheckResults != null && !reportedValueCheckResults.isEmpty()) {
            int size = reportedValueCheckResults.size();
            for (int i = 0; i < size; i++) {
                CustomSpinner customSpinner = reportedValueCheckResults.get(i);
                if (!TextUtils.isEmpty(reportValue)) {
                    List<StdJudgeEntity> stdJudgeEntities = stdJudegeList.get(i);
                    int stdJudgeSize = stdJudgeEntities.size();
                    for (int j = 0; j < stdJudgeSize; j++) {
                        StdJudgeEntity stdJudgeEntity = stdJudgeEntities.get(j);
                        if (TextUtils.isEmpty(detailEntity.getOptionValues())) {
                            if (Util.parseStrInRange(reportValue, stdJudgeEntity.dispValue)) {
                                customSpinner.setContent(stdJudgeEntity.resultValue);
                                sampleComRes.get(i).setTestResult(stdJudgeEntity.resultValue);
                                return;
                            } else {
                                customSpinner.setContent("不合格");
                                sampleComRes.get(i).setTestResult("不合格");
                            }
                        } else {
                            if (Util.parseInRangeStr(reportValue, stdJudgeEntity.dispValue)) {
                                customSpinner.setContent(stdJudgeEntity.resultValue);
                                sampleComRes.get(i).setTestResult(stdJudgeEntity.resultValue);
                                return;
                            } else {
                                customSpinner.setContent("不合格");
                                sampleComRes.get(i).setTestResult("不合格");
                            }
                        }
                    }
                } else {
                    customSpinner.setContent("");
                    sampleComRes.get(i).setTestResult(null);
                }
            }
        }
    }

    private void editSampleInspectItem(SampleInspectItemEntity detailEntity, String originValue, CustomEditText ceOriginalValue, CustomTextView ctRoundOffValue, CustomEditText ceReportedValue) {
        detailEntity.setOriginValue(originValue);
        detailEntity.setRoundValue(originValue);
        ctRoundOffValue.setValue(originValue);
        Map<String, Object> dispMap = detailEntity.getDispMap();
        List<String> specLimitStr = (List<String>) dispMap.get("specLimit");
        if (TextUtils.isEmpty(originValue) || (specLimitStr != null && !specLimitStr.isEmpty())) {
            detailEntity.setDispValue(originValue);
            ceReportedValue.setContent(originValue);
        } else {
            String minValue = detailEntity.getMinValue();
            double dispValue = Double.parseDouble(originValue);
            String maxValue = detailEntity.getMaxValue();
            if (minValue.contains(",")) {
                String[] minValueArr = minValue.split(",");
                double minValue2 = Double.parseDouble(minValueArr[0]);
                if (dispValue < minValue2) {
                    detailEntity.setDispValue(minValueArr[1]);
                    ceReportedValue.setContent(minValueArr[1]);
                } else if (dispValue == minValue2) {
                    detailEntity.setDispValue(originValue);
                    ceReportedValue.setContent(originValue);
                } else {
                    String[] maxValueArr = maxValue.split(",");
                    minValue2 = Double.parseDouble(maxValueArr[0]);
                    if (dispValue > minValue2) {
                        detailEntity.setDispValue(maxValueArr[1]);
                        ceReportedValue.setContent(maxValueArr[1]);
                    } else if (dispValue == minValue2) {
                        detailEntity.setDispValue(originValue);
                        ceReportedValue.setContent(originValue);
                    }
                }
            } else {
                double minValue2 = Double.parseDouble(minValue);
                if (dispValue < minValue2) {
                    if (detailEntity.getLimitType() != null && "LIMSBasic_limitType/reject".equals(detailEntity.getLimitType().getId())) {
                        detailEntity.setDispValue(null);
                        ceReportedValue.setContent(null);
                        ceOriginalValue.setContent(null);
                    } else {
                        detailEntity.setDispValue("<" + minValue);
                        ceReportedValue.setContent("<" + minValue);
                    }
                } else if (dispValue == minValue2) {
                    detailEntity.setDispValue("");
                    ceReportedValue.setContent("");
                } else {
                    minValue2 = Double.parseDouble(maxValue);
                    if (dispValue <= minValue2) {
                        detailEntity.setDispValue(originValue);
                        ceReportedValue.setContent(originValue);
                    } else if (dispValue > minValue2) {
                        detailEntity.setDispValue(">" + maxValue);
                        ceReportedValue.setContent(">" + maxValue);
                    }
                }
            }
        }

        String busiVersion = detailEntity.getSampleTestId().getTestId().getBusiVersion();
        if (calculateListMap.containsKey(busiVersion)) {
            List<CustomEditText> originalValueList = calculateListMap.get(busiVersion);
            boolean fillComplete = false;
            for (CustomEditText customEditText : originalValueList) {
                fillComplete = !TextUtils.isEmpty(customEditText.getContent());
            }
            if (fillComplete) {
                calculFormula(calculateTextMap.get(busiVersion), originalValueList);
            }
        }
    }

    private void calculFormula(CustomEditText ceOriginalValue, List<CustomEditText> originalValueList) {
        float calcul = 0;
        for (int i = 0; i < originalValueList.size(); i++) {
            calcul = Float.valueOf(originalValueList.get(i).getContent());
        }
        ceOriginalValue.setContent(Util.big(calcul / originalValueList.size()));
    }

    /**
     * @param position        操作的当前item
     * @param detailEntity    item的实体
     * @param spinner         原始值下拉选择控件
     * @param ctRoundOffValue 修约值控件
     * @param ceReportedValue 报出值控件
     * @param list            原始值选择的列表值
     * @param mPickController
     */
    private void showSpinnerSelector(int position, SampleInspectItemEntity detailEntity, CustomSpinner spinner, CustomTextView ctRoundOffValue, CustomEditText ceReportedValue, List<String> list, SinglePickController<String> mPickController) {
        int current = findPosition(spinner.getSpinnerValue(), list);
        mPickController
                .list(list)
                .listener((index, item) -> {
                    String optionValue = list.get(index);
                    detailEntity.setOriginValue(optionValue);
                    spinner.setSpinner(optionValue);
                    ctRoundOffValue.setValue(optionValue);
                    ceReportedValue.setContent(optionValue);
                    spinner.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                }).show(current);

    }

    private void showSpinnerSelectCheckResult(CustomSpinner spinner, SampleCheckResultEntity checkResultEntity, List<String> list, SinglePickController<String> mPickController) {
        int current = findPosition(spinner.getSpinnerValue(), list);
        mPickController
                .list(list)
                .listener((index, item) -> {
                    String testResult = list.get(index);
                    checkResultEntity.setTestResult(testResult);
                    spinner.setSpinner(testResult);
                    spinner.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
                }).show(current);
    }

    /**
     * 查找当前默认项的位置
     *
     * @return
     */
    private int findPosition(String str, List<String> mfaultTypeName) {
        for (int i = 0; i < mfaultTypeName.size(); i++) {
            String s = mfaultTypeName.get(i);
            if (str.equals(s)) {
                return i;
            }
        }
        return 0;
    }

    Map<Long, List<CustomSpinner>> mapReportedValueCheckResult = new HashMap<>();
    Map<Long, List<List<StdJudgeEntity>>> stdJudegeListMap = new HashMap<>();
    Map<Long, List<SampleCheckResultEntity>> sampleCheckResultMap = new HashMap<>();
    List<String> checkResultList = new ArrayList<>();

    private void addRangeCheckResultView(Long id, LinearLayout layout, List<SampleCheckResultEntity> sampleComRes, List<String> specLimitStr) {
        int size = sampleComRes.size();
        List<CustomSpinner> reportedValueCheckResults = new ArrayList<>();
        List<List<StdJudgeEntity>> stdJudegeList = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            SinglePickController mPickController = new SinglePickController(activity);
            mPickController.textSize(18);
            SampleCheckResultEntity checkResultEntity = sampleComRes.get(index);
            View view = LayoutInflater.from(context).inflate(R.layout.item_range_check_result, null);
            CustomSpinner checkResultTv = view.findViewById(R.id.checkResultTv);
            RelativeLayout rlRange = view.findViewById(R.id.rlRange);
            LinearLayout llRange = view.findViewById(R.id.llRange);
            ImageView ivExpand = view.findViewById(R.id.ivExpand);
            List<StdJudgeEntity> stdJudgeEntities = specLimitStr != null && !specLimitStr.isEmpty() ? GsonUtil.jsonToList(specLimitStr.get(index), StdJudgeEntity.class) : null;
            stdJudegeList.add(stdJudgeEntities);
            RxView.clicks(rlRange)
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        if (stdJudgeEntities == null || stdJudgeEntities.isEmpty()) {
                            ToastUtils.show(context, "没有更多展开的内容了！");
                            return;
                        }
                        if (checkResultEntity.isFirstExpand()) {
                            checkResultEntity.setFirstExpand(false);
                            addRangeView(llRange, stdJudgeEntities);
                        }
                        checkResultEntity.setExpand(!checkResultEntity.isExpand());
                        if (checkResultEntity.isExpand()) {
                            ivExpand.setImageResource(R.drawable.ic_drop_down);
                        } else {
                            ivExpand.setImageResource(R.drawable.ic_drop_up);
                        }
                        int visibility = checkResultEntity.isExpand() ? View.VISIBLE : View.GONE;
                        llRange.setVisibility(visibility);
                    });
            RxView.clicks(checkResultTv.findViewById(R.id.customSpinnerText))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        showSpinnerSelectCheckResult(checkResultTv, checkResultEntity, checkResultList, mPickController);
                    });
            RxView.clicks(checkResultTv.findViewById(R.id.customSpinner))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        showSpinnerSelectCheckResult(checkResultTv, checkResultEntity, checkResultList, mPickController);
                    });
            RxView.clicks(checkResultTv.findViewById(R.id.customSpinnerIcon))
                    .throttleFirst(2000, TimeUnit.MILLISECONDS)
                    .subscribe(o -> {
                        showSpinnerSelectCheckResult(checkResultTv, checkResultEntity, checkResultList, mPickController);
                    });
            checkResultTv.setKey(getConclusion(checkResultEntity.getResultKey()));
            checkResultTv.setContent(checkResultEntity.getTestResult());
            checkResultTv.findViewById(R.id.customDeleteIcon).setVisibility(View.GONE);
            reportedValueCheckResults.add(checkResultTv);
            layout.addView(view);
        }
        sampleCheckResultMap.put(id, sampleComRes);
        stdJudegeListMap.put(id, stdJudegeList);
        mapReportedValueCheckResult.put(id, reportedValueCheckResults);
    }

    private void addRangeView(LinearLayout layout, List<StdJudgeEntity> stdJudgeEntities) {
        int size = stdJudgeEntities != null ? stdJudgeEntities.size() : 0;
        for (int i = 0; i < size; i++) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_single_range, null);
            CustomTextView judgeRangeTv = view.findViewById(R.id.judgeRangeTv);
            StdJudgeEntity stdJudgeEntity = stdJudgeEntities.get(i);
            judgeRangeTv.setKey(stdJudgeEntity.resultValue + "范围");
            judgeRangeTv.setValue(stdJudgeEntity.dispValue);
            layout.addView(view);
        }
    }

    private List<InspectionItemColumnEntity> conclusionEntities;

    public void setConclusionEntities(List<InspectionItemColumnEntity> conclusionEntities) {
        this.conclusionEntities = conclusionEntities;
        if (conclusionEntities != null && !conclusionEntities.isEmpty()) {
            int size = conclusionEntities.size();
            for (int i = 0; i < size; i++) {
                InspectionItemColumnEntity inspectionItemColumnEntity = conclusionEntities.get(i);
                if (inspectionItemColumnEntity.getLoad() != null)
                    checkResultList.add(inspectionItemColumnEntity.getResult());
            }
        }
    }

    private String getConclusion(String resultKey) {
        for (InspectionItemColumnEntity conclusionEntity : conclusionEntities) {
            if (resultKey.equals(conclusionEntity.getColumnKey())) {
                return conclusionEntity.getColumnName();
            }
        }
        return null;
    }
}
