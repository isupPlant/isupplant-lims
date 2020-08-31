package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

import java.util.HashMap;
import java.util.List;

/**
 * author huodongsheng
 * on 2020/8/27
 * class name
 */
public class InspectionSubEntity extends BaseEntity {
    private String calcParamInfo;
    private String calculFormula;
    private String calculateParamNames;
    private String carryFormula;
    private BaseIdValueEntity digitType;
    private String carrySpace;
    private BaseIdValueEntity carryType;
    private BaseLongIdNameEntity comId;
    private String comName;
    private BaseIdValueEntity comState;
    private HashMap<String, Object> dispMap;
    private List<ConclusionEntity> conclusionList; // 多结论集合
    private String dispValue;
    private Long id;
    private Boolean isReport;
    private String originValue;
    private Integer parallelNo;
    private String reportName;
    private String roundValue;
    private BaseLongIdNameEntity sampleId;
    private SampleTestIdEntity sampleTestId;
    private Integer sort;
    private StdVerIdEntity testId;
    private BaseLongIdNameEntity testStaffId;
    private String unitName;
    private BaseIdValueEntity valueKind;
    private Integer version;
    private BaseIdValueEntity limitType;
    private String maxValue;
    private String minValue;
    private String recordOriginValue;
    private String recordDispValue;
    private String optionNames;
    private boolean conclusionState = true;  //结论状态，作用于 不合格为红色分项/合格为正常分项
    private boolean isOpen = false; //是否展开子项
    private String fileUploadMultiFileIcons;//附件类型
    private String fileUploadMultiFileIds;//附件id
    private String fileUploadMultiFileNames;//附件名称
    private String filePath;

    public String getCalcParamInfo() {
        return calcParamInfo;
    }

    public void setCalcParamInfo(String calcParamInfo) {
        this.calcParamInfo = calcParamInfo;
    }

    public String getCalculFormula() {
        return calculFormula;
    }

    public void setCalculFormula(String calculFormula) {
        this.calculFormula = calculFormula;
    }

    public String getCalculateParamNames() {
        return calculateParamNames;
    }

    public void setCalculateParamNames(String calculateParamNames) {
        this.calculateParamNames = calculateParamNames;
    }

    public String getCarryFormula() {
        return carryFormula;
    }

    public void setCarryFormula(String carryFormula) {
        this.carryFormula = carryFormula;
    }

    public BaseIdValueEntity getDigitType() {
        return digitType;
    }

    public void setDigitType(BaseIdValueEntity digitType) {
        this.digitType = digitType;
    }

    public String getCarrySpace() {
        return carrySpace;
    }

    public void setCarrySpace(String carrySpace) {
        this.carrySpace = carrySpace;
    }

    public BaseIdValueEntity getCarryType() {
        return carryType;
    }

    public void setCarryType(BaseIdValueEntity carryType) {
        this.carryType = carryType;
    }

    public BaseLongIdNameEntity getComId() {
        return comId;
    }

    public void setComId(BaseLongIdNameEntity comId) {
        this.comId = comId;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public BaseIdValueEntity getComState() {
        return comState;
    }

    public void setComState(BaseIdValueEntity comState) {
        this.comState = comState;
    }

    public HashMap<String, Object> getDispMap() {
        return dispMap;
    }

    public void setDispMap(HashMap<String, Object> dispMap) {
        this.dispMap = dispMap;
    }

    public List<ConclusionEntity> getConclusionList() {
        return conclusionList;
    }

    public void setConclusionList(List<ConclusionEntity> conclusionList) {
        this.conclusionList = conclusionList;
    }

    public String getDispValue() {
        return dispValue;
    }

    public void setDispValue(String dispValue) {
        this.dispValue = dispValue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getReport() {
        return isReport;
    }

    public void setReport(Boolean report) {
        isReport = report;
    }

    public String getOriginValue() {
        return originValue;
    }

    public void setOriginValue(String originValue) {
        this.originValue = originValue;
    }

    public Integer getParallelNo() {
        return parallelNo;
    }

    public void setParallelNo(Integer parallelNo) {
        this.parallelNo = parallelNo;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public String getRoundValue() {
        return roundValue;
    }

    public void setRoundValue(String roundValue) {
        this.roundValue = roundValue;
    }

    public BaseLongIdNameEntity getSampleId() {
        return sampleId;
    }

    public void setSampleId(BaseLongIdNameEntity sampleId) {
        this.sampleId = sampleId;
    }

    public SampleTestIdEntity getSampleTestId() {
        return sampleTestId;
    }

    public void setSampleTestId(SampleTestIdEntity sampleTestId) {
        this.sampleTestId = sampleTestId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public StdVerIdEntity getTestId() {
        return testId;
    }

    public void setTestId(StdVerIdEntity testId) {
        this.testId = testId;
    }

    public BaseLongIdNameEntity getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(BaseLongIdNameEntity testStaffId) {
        this.testStaffId = testStaffId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BaseIdValueEntity getValueKind() {
        return valueKind;
    }

    public void setValueKind(BaseIdValueEntity valueKind) {
        this.valueKind = valueKind;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BaseIdValueEntity getLimitType() {
        return limitType;
    }

    public void setLimitType(BaseIdValueEntity limitType) {
        this.limitType = limitType;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

//    public boolean isSelect() {
//        return isSelect;
//    }
//
//    public void setSelect(boolean select) {
//        isSelect = select;
//    }

    public boolean isConclusionState() {
        return conclusionState;
    }

    public void setConclusionState(boolean conclusionState) {
        this.conclusionState = conclusionState;
    }

    public String getRecordOriginValue() {
        return recordOriginValue;
    }

    public void setRecordOriginValue(String recordOriginValue) {
        this.recordOriginValue = recordOriginValue;
    }

    public String getRecordDispValue() {
        return recordDispValue;
    }

    public void setRecordDispValue(String recordDispValue) {
        this.recordDispValue = recordDispValue;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOptionNames() {
        return optionNames;
    }

    public void setOptionNames(String optionNames) {
        this.optionNames = optionNames;
    }
}