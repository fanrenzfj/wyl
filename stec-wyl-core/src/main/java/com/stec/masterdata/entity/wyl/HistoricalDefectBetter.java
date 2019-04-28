package com.stec.masterdata.entity.wyl;

import java.io.Serializable;

public class HistoricalDefectBetter implements Serializable {
    private HistoricalDefect hd;
    //设施名称
    private String structureName;
    //设备名称
    private String deviceName;
    //缺陷类型
    private String defectTypeName;
    //是否占道
    private String blockRoad;
    //反馈处理图片
    private String deal_url;


    public HistoricalDefect getHd() {
        return hd;
    }

    public String getDeal_url() {
        return deal_url;
    }

    public void setDeal_url(String deal_url) {
        this.deal_url = deal_url;
    }

    public void setHd(HistoricalDefect hd) {
        this.hd = hd;
    }

    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDefectTypeName() {
        return defectTypeName;
    }

    public void setDefectTypeName(String defectTypeName) {
        this.defectTypeName = defectTypeName;
    }

    public String getBlockRoad() {
        return blockRoad;
    }

    public void setBlockRoad(String blockRoad) {
        this.blockRoad = blockRoad;
    }
}
