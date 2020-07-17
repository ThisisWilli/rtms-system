package com.willi.Bean;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.Bean
 * \* author: Willi Wei
 * \* date: 2020-07-17 13:45:05
 * \* description:
 * \
 */
public class QualityMessage {
    private String id;
    private String type;
    private Long timestamp;
    private String productionLineName;
    private String deviceName;
    private String angleLocation;
    private Double angle;

    public QualityMessage(String id, String type, Long timestamp, String productionLineName, String deviceName, String angleLocation, Double angle) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.productionLineName = productionLineName;
        this.deviceName = deviceName;
        this.angleLocation = angleLocation;
        this.angle = angle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getAngleLocation() {
        return angleLocation;
    }

    public void setAngleLocation(String angleLocation) {
        this.angleLocation = angleLocation;
    }

    public Double getAngle() {
        return angle;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }
}