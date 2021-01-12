package com.supcon.mes.module_lims.model.bean;

import com.supcon.common.com_http.BaseEntity;
import com.supcon.mes.middleware.model.bean.BaseIdValueEntity;

/**
 * author huodongsheng
 * on 2021/1/11
 * class name
 */
public class SerialDeviceEntity extends BaseEntity {
    private String baudRate;
    private String checkDigit;
    private Long cid;
    private String code;
    private String dataBits;
    private BaseLongIdNameEntity eamType;
    private Long id;
    private String name;
    private String serialPort;
    private String serialServerIp;
    private BaseIdValueEntity serialType;
    private BaseIdValueEntity state;
    private String stopBits;
    private boolean isSelect;

    public String getBaudRate() {
        return baudRate == null ? "" : baudRate;
    }

    public void setBaudRate(String baudRate) {
        this.baudRate = baudRate;
    }

    public String getCheckDigit() {
        return checkDigit == null ? "" : checkDigit;
    }

    public void setCheckDigit(String checkDigit) {
        this.checkDigit = checkDigit;
    }

    public Long getCid() {
        return cid == null ? 0L : cid;
    }

    public void setCid(Long cid) {
        this.cid = cid;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDataBits() {
        return dataBits == null ? "" : dataBits;
    }

    public void setDataBits(String dataBits) {
        this.dataBits = dataBits;
    }

    public BaseLongIdNameEntity getEamType() {
        return eamType == null ? new BaseLongIdNameEntity() : eamType;
    }

    public void setEamType(BaseLongIdNameEntity eamType) {
        this.eamType = eamType;
    }

    public Long getId() {
        return id == null ? 0L : id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSerialPort() {
        return serialPort == null ? "" : serialPort;
    }

    public void setSerialPort(String serialPort) {
        this.serialPort = serialPort;
    }

    public String getSerialServerIp() {
        return serialServerIp == null ? "" : serialServerIp;
    }

    public void setSerialServerIp(String serialServerIp) {
        this.serialServerIp = serialServerIp;
    }

    public BaseIdValueEntity getSerialType() {
        return serialType == null ? new BaseIdValueEntity() : serialType;
    }

    public void setSerialType(BaseIdValueEntity serialType) {
        this.serialType = serialType;
    }

    public BaseIdValueEntity getState() {
        return state == null ? new BaseIdValueEntity() : state;
    }

    public void setState(BaseIdValueEntity state) {
        this.state = state;
    }

    public String getStopBits() {
        return stopBits == null ? "" : stopBits;
    }

    public void setStopBits(String stopBits) {
        this.stopBits = stopBits;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
