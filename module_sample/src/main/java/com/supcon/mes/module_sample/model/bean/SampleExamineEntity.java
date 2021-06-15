package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * @author : yaobing
 * @date : 2021/6/15 13:25
 * @desc :
 */
public class SampleExamineEntity  extends BaseEntity {

    private String code;
    private Object dateparama;
    private ProductIdBean productId;
    private String registerTime;
    private Object numberparama;
    private String batchCode;
    private Object bigintparama;
    private PsIdBean psId;
    private SampleTypeBean sampleType;
    private Integer version;
    private SperaTypeBean speraType;
    private StdVerIdBean stdVerId;
    private Object memoField;
    private String name;
    private Long id;
    private Object charparama;
    private SampleStateBean sampleState;
    private Integer cid;

    //以下属性为自己加的，用于走流程使用，正常返回数据不包含
    private boolean select = false;
    private boolean needCheck = false;
    private Integer rowIndex = 0;
    private String currClickColKey = "555";
    private Long key = 0l;
    private boolean isChecked = false;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getDateparama() {
        return dateparama;
    }

    public void setDateparama(Object dateparama) {
        this.dateparama = dateparama;
    }

    public ProductIdBean getProductId() {
        return productId;
    }

    public void setProductId(ProductIdBean productId) {
        this.productId = productId;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public Object getNumberparama() {
        return numberparama;
    }

    public void setNumberparama(Object numberparama) {
        this.numberparama = numberparama;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Object getBigintparama() {
        return bigintparama;
    }

    public void setBigintparama(Object bigintparama) {
        this.bigintparama = bigintparama;
    }

    public PsIdBean getPsId() {
        return psId;
    }

    public void setPsId(PsIdBean psId) {
        this.psId = psId;
    }

    public SampleTypeBean getSampleType() {
        return sampleType;
    }

    public void setSampleType(SampleTypeBean sampleType) {
        this.sampleType = sampleType;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public SperaTypeBean getSperaType() {
        return speraType;
    }

    public void setSperaType(SperaTypeBean speraType) {
        this.speraType = speraType;
    }

    public StdVerIdBean getStdVerId() {
        return stdVerId;
    }

    public void setStdVerId(StdVerIdBean stdVerId) {
        this.stdVerId = stdVerId;
    }

    public Object getMemoField() {
        return memoField;
    }

    public void setMemoField(Object memoField) {
        this.memoField = memoField;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getCharparama() {
        return charparama;
    }

    public void setCharparama(Object charparama) {
        this.charparama = charparama;
    }

    public SampleStateBean getSampleState() {
        return sampleState;
    }

    public void setSampleState(SampleStateBean sampleState) {
        this.sampleState = sampleState;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public boolean isNeedCheck() {
        return needCheck;
    }

    public void setNeedCheck(boolean needCheck) {
        this.needCheck = needCheck;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getCurrClickColKey() {
        return currClickColKey;
    }

    public void setCurrClickColKey(String currClickColKey) {
        this.currClickColKey = currClickColKey;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    public static class ProductIdBean {
        private String code;
        private String name;
        private Long id;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class PsIdBean {
        private String name;
        private Long id;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class SampleTypeBean {
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

    public static class SperaTypeBean {
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

    public static class StdVerIdBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class SampleStateBean {
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
}
