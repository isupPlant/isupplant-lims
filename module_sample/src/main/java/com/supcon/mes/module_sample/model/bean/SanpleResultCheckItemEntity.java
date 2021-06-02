package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

/**
 * author huodongsheng
 * on 2021/3/5
 * class name
 */
public class SanpleResultCheckItemEntity extends BaseEntity {

    private String code;
    private Object dateparama;
    private ProductIdBean productId;
    private String registerTime;
    private String batchCode;
    private Object numberparama;
    private Object bigintparama;
    private PsIdBean psId;
    private SampleTypeBean sampleType;
    private SperaTypeBean speraType;
    private StdVerIdBean stdVerId;
    private String memoField;
    private String name;
    private Long id;
    private Object charparama;
    private SampleStateBean sampleState;

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

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Object getNumberparama() {
        return numberparama;
    }

    public void setNumberparama(Object numberparama) {
        this.numberparama = numberparama;
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

    public String getMemoField() {
        return memoField;
    }

    public void setMemoField(String memoField) {
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

    public static class ProductIdBean {
        private Object code;
        private Object name;
        private Object id;

        public Object getCode() {
            return code;
        }

        public void setCode(Object code) {
            this.code = code;
        }

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
            this.id = id;
        }
    }

    public static class PsIdBean {
        private Object name;
        private Object id;

        public Object getName() {
            return name;
        }

        public void setName(Object name) {
            this.name = name;
        }

        public Object getId() {
            return id;
        }

        public void setId(Object id) {
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
