package com.supcon.mes.module_sample.model.bean;


import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

import java.util.Map;

/**
 * Created by wanghaidong on 2020/8/14
 * Email:wanghaidong1@supcon.com
 */
public class SampleInspectItemEntity extends InspectionItemsEntity {

    private SampleTestIdEntity sampleTestId;//检验项目
    private String originValue;//原始值
    private String roundValue;//修约值
    private String dispValue;//报出值
    private String comName;//检测分项
    private String unitName;//计量单位
    private String calculFormula;
    private String calcParamInfo;
    private String calculateParamNames;
    private String memoField;//备注
    private Map<String,Object> dispMap;
    private String optionNames;//原始值选项名称
    private String optionValues;//原始值选择值
    private String minValue;//原始值最小值
    private String maxValue;//原始值最大值
    private String fileUploadMultiFileIcons;//附件类型
    private String fileUploadMultiFileIds;//附件id
    private String fileUploadMultiFileNames;//附件名称
    private String executableFormula;
    private boolean isExpand;//扩展/收缩
    private BaseIdValueEntity limitType;
    private BaseIdValueEntity valueKind;//数据类型
    private String filePath;


    public String getCalcParamInfo() {
        return calcParamInfo;
    }

    public void setCalcParamInfo(String calcParamInfo) {
        this.calcParamInfo = calcParamInfo;
    }

    public String getOriginValue() {
        return originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public String getRoundValue() {
        return roundValue;
    }

    public void setRoundValue(String roundValue) {
        this.roundValue = roundValue;
    }

    public String getDispValue() {
        return dispValue;
    }

    public void setDispValue(String dispValue) {
        this.dispValue = dispValue;
    }


    public Map<String, Object> getDispMap() {
        return dispMap;
    }

    public void setDispMap(Map<String, Object> dispMap) {
        this.dispMap = dispMap;
    }

    public int getTypeView(){
        return 1;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }


    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMemoField() {
        return memoField;
    }

    public void setMemoField(String memoField) {
        this.memoField = memoField;
    }

    public SampleTestIdEntity getSampleTestId() {
        return sampleTestId;
    }

    public String getOptionNames() {
        return optionNames;
    }

    public void setOptionNames(String optionNames) {
        this.optionNames = optionNames;
    }

    public String getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(String optionValues) {
        this.optionValues = optionValues;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getFileUploadMultiFileIcons() {
        return fileUploadMultiFileIcons;
    }

    public void setFileUploadMultiFileIcons(String fileUploadMultiFileIcons) {
        this.fileUploadMultiFileIcons = fileUploadMultiFileIcons;
    }

    public String getFileUploadMultiFileIds() {
        return fileUploadMultiFileIds;
    }

    public void setFileUploadMultiFileIds(String fileUploadMultiFileIds) {
        this.fileUploadMultiFileIds = fileUploadMultiFileIds;
    }

    public String getFileUploadMultiFileNames() {
        return fileUploadMultiFileNames;
    }

    public void setFileUploadMultiFileNames(String fileUploadMultiFileNames) {
        this.fileUploadMultiFileNames = fileUploadMultiFileNames;
    }

    public void setSampleTestId(SampleTestIdEntity sampleTestId) {
        this.sampleTestId = sampleTestId;
    }

    public String getCalculFormula() {
        return calculFormula;
    }

    public String getCalculateParamNames() {
        return calculateParamNames;
    }

    public BaseIdValueEntity getLimitType() {
        return limitType;
    }

    public void setLimitType(BaseIdValueEntity limitType) {
        this.limitType = limitType;
    }

    public void setCalculateParamNames(String calculateParamNames) {
        this.calculateParamNames = calculateParamNames;
    }

    public void setCalculFormula(String calculFormula) {
        this.calculFormula = calculFormula;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public BaseIdValueEntity getValueKind() {
        return valueKind;
    }

    public void setValueKind(BaseIdValueEntity valueKind) {
        this.valueKind = valueKind;
    }

    public String getExecutableFormula() {
        return executableFormula;
    }

    public void setExecutableFormula(String executableFormula) {
        this.executableFormula = executableFormula;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
