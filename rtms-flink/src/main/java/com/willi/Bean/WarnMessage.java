package com.willi.Bean;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.Bean
 * \* author: Willi Wei
 * \* date: 2020-07-16 10:13:36
 * \* description:
 * \
 */
public class WarnMessage {
    private String id;
    private String type;
    private Long timestamp;
    private String productionLineName;
    private String deviceName;
    private String warnInformation;
    private String warnStatus;
    private String warnDuration;


    /**
     * 设备报警信息
     * @param id 事件id
     * @param type 事件类型
     * @param timestamp 事件的eventtime
     * @param productionLineName 生产线的名称
     * @param deviceName 生产的设备
     * @param warnInformation 报警信息
     * @param warnStatus 是否已经报警，分为报警，已经报警，解除报警
     * @param warnDuration 事件的发生时长
     */
    public WarnMessage(String id, String type, Long timestamp, String productionLineName, String deviceName, String warnInformation, String warnStatus, String warnDuration) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.productionLineName = productionLineName;
        this.deviceName = deviceName;
        this.warnInformation = warnInformation;
        this.warnStatus = warnStatus;
        this.warnDuration = warnDuration;
    }

    public WarnMessage(String id, Long timestamp, String productionLineName, String deviceName, String warnInformation, String warnStatus) {
        this.id = id;
        this.timestamp = timestamp;
        this.productionLineName = productionLineName;
        this.deviceName = deviceName;
        this.warnInformation = warnInformation;
        this.warnStatus = warnStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getProductionLineName() {
        return productionLineName;
    }

    public void setProductionLineName(String productionLineName) {
        this.productionLineName = productionLineName;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getWarnInformation() {
        return warnInformation;
    }

    public void setWarnInformation(String warnInformation) {
        this.warnInformation = warnInformation;
    }

    public String getWarnStatus() {
        return warnStatus;
    }

    public void setWarnStatus(String warnStatus) {
        this.warnStatus = warnStatus;
    }

    public String getWarnDuration() {
        return warnDuration;
    }

    public void setWarnDuration(String warnDuration) {
        this.warnDuration = warnDuration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WarnMessage{" +
                "id='" + id + '\'' +
                ", type='" + type + '\'' +
                ", timestamp=" + timestamp +
                ", productionLineName='" + productionLineName + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", warnInformation='" + warnInformation + '\'' +
                ", warnStatus='" + warnStatus + '\'' +
                ", warnDuration='" + warnDuration + '\'' +
                '}';
    }
}