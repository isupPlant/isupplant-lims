package com.supcon.mes.module_sample.model.bean;

import com.supcon.common.com_http.BaseEntity;

import java.math.BigDecimal;

/**
 * author huodongsheng
 * on 2020/8/12
 * class name
 */
public class SampleTestMaterialEntity extends BaseEntity {

    private Object attrMap;
    private Object batchCode;
    private Long id;
    private Object matCode;
    private Object matRecordId;
    private Object memoField;
    private ProductIdBean productId;
    private SampleIdBean sampleId;
    private SampleTestIdBean sampleTestId;
    private Object sort;
    private BigDecimal useQty;
    private Integer version;

    public Object getAttrMap() {
        return attrMap;
    }

    public void setAttrMap(Object attrMap) {
        this.attrMap = attrMap;
    }

    public Object getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(Object batchCode) {
        this.batchCode = batchCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getMatCode() {
        return matCode;
    }

    public void setMatCode(Object matCode) {
        this.matCode = matCode;
    }

    public Object getMatRecordId() {
        return matRecordId;
    }

    public void setMatRecordId(Object matRecordId) {
        this.matRecordId = matRecordId;
    }

    public Object getMemoField() {
        return memoField;
    }

    public void setMemoField(Object memoField) {
        this.memoField = memoField;
    }

    public ProductIdBean getProductId() {
        return productId;
    }

    public void setProductId(ProductIdBean productId) {
        this.productId = productId;
    }

    public SampleIdBean getSampleId() {
        return sampleId;
    }

    public void setSampleId(SampleIdBean sampleId) {
        this.sampleId = sampleId;
    }

    public SampleTestIdBean getSampleTestId() {
        return sampleTestId;
    }

    public void setSampleTestId(SampleTestIdBean sampleTestId) {
        this.sampleTestId = sampleTestId;
    }

    public Object getSort() {
        return sort;
    }

    public void setSort(Object sort) {
        this.sort = sort;
    }

    public BigDecimal getUseQty() {
        return useQty;
    }

    public void setUseQty(BigDecimal useQty) {
        this.useQty = useQty;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public static class ProductIdBean {
        private String code;
        private Long id;
        private IsBatchBean isBatch;
        private MainUnitBean mainUnit;
        private String name;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public IsBatchBean getIsBatch() {
            return isBatch;
        }

        public void setIsBatch(IsBatchBean isBatch) {
            this.isBatch = isBatch;
        }

        public MainUnitBean getMainUnit() {
            return mainUnit;
        }

        public void setMainUnit(MainUnitBean mainUnit) {
            this.mainUnit = mainUnit;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public static class IsBatchBean {
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

        public static class MainUnitBean {
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
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

    public static class SampleTestIdBean {
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }
}
