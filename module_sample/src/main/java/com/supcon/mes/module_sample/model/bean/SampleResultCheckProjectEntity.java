package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2021/3/5
 * class name
 */
public class SampleResultCheckProjectEntity extends BaseEntity {


    private AnalystGroupIdBean analystGroupId;
    private Object approveStaffId;
    private Object attrMap;
    private Object humidity;
    private Long id;
    private Object memoField;
    private Integer parallelNo;
    private SampleIdBean sampleId;
    private Integer sort;
    private Object temperature;
    private TestIdBean testId;
    private TestMethodIdBean testMethodId;
    private TestStaffIdBean testStaffId = new TestStaffIdBean();
    private TestStateBean testState;
    private Long testTime;
    private Integer version;

    public AnalystGroupIdBean getAnalystGroupId() {
        return analystGroupId;
    }

    public void setAnalystGroupId(AnalystGroupIdBean analystGroupId) {
        this.analystGroupId = analystGroupId;
    }

    public Object getApproveStaffId() {
        return approveStaffId;
    }

    public void setApproveStaffId(Object approveStaffId) {
        this.approveStaffId = approveStaffId;
    }

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public Object getHumidity() {
        return humidity;
    }

    public void setHumidity(Object humidity) {
        this.humidity = humidity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getMemoField() {
        return memoField;
    }

    public void setMemoField(Object memoField) {
        this.memoField = memoField;
    }

    public Integer getParallelNo() {
        return parallelNo;
    }

    public void setParallelNo(Integer parallelNo) {
        this.parallelNo = parallelNo;
    }

    public SampleIdBean getSampleId() {
        return sampleId;
    }

    public void setSampleId(SampleIdBean sampleId) {
        this.sampleId = sampleId;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Object getTemperature() {
        return temperature;
    }

    public void setTemperature(Object temperature) {
        this.temperature = temperature;
    }

    public TestIdBean getTestId() {
        return testId;
    }

    public void setTestId(TestIdBean testId) {
        this.testId = testId;
    }

    public TestMethodIdBean getTestMethodId() {
        return testMethodId;
    }

    public void setTestMethodId(TestMethodIdBean testMethodId) {
        this.testMethodId = testMethodId;
    }

    public TestStaffIdBean getTestStaffId() {
        return testStaffId;
    }

    public void setTestStaffId(TestStaffIdBean testStaffId) {
        this.testStaffId = testStaffId;
    }

    public TestStateBean getTestState() {
        return testState;
    }

    public void setTestState(TestStateBean testState) {
        this.testState = testState;
    }

    public Long getTestTime() {
        return testTime;
    }

    public void setTestTime(Long testTime) {
        this.testTime = testTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static class AnalystGroupIdBean {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class SampleIdBean {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class TestIdBean {
        private String busiVersion;
        private Object docId;
        private Long id;
        private String name;

        public String getBusiVersion() {
            return busiVersion;
        }

        public void setBusiVersion(String busiVersion) {
            this.busiVersion = busiVersion;
        }

        public Object getDocId() {
            return docId;
        }

        public void setDocId(Object docId) {
            this.docId = docId;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TestMethodIdBean {
        private Long id;
        private String method;
        private String procedureNo;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getMethod() {
            return method;
        }

        public void setMethod(String method) {
            this.method = method;
        }

        public String getProcedureNo() {
            return procedureNo;
        }

        public void setProcedureNo(String procedureNo) {
            this.procedureNo = procedureNo;
        }
    }

    public static class TestStateBean {
        private String id;
        private String value;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
    public static class TestStaffIdBean {
        private String id;
        private String name = "";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String value) {
            this.name = value;
        }
    }
}
