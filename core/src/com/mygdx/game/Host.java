package com.mygdx.game;

/**
 * Created by peise on 08.06.2016.
 */
public class Host {
    private String endpointId;
    private String deviceId;
    private String serviceId;
    private String endpointName;

    public Host(String endpointId, String deviceId, String serviceId, String endpointName) {
        this.endpointId = endpointId;
        this.deviceId = deviceId;
        this.serviceId = serviceId;
        this.endpointName = endpointName;
    }

    public String getEndpointId() {
        return endpointId;
    }

    public void setEndpointId(String endpointId) {
        this.endpointId = endpointId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getEndpointName() {
        return endpointName;
    }

    public void setEndpointName(String endpointName) {
        this.endpointName = endpointName;
    }
}
