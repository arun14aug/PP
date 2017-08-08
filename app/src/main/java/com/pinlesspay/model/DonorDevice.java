package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/8/2017.
 */

public class DonorDevice {

    private String DeviceName, DeviceType, DonorId, DeviceIdentifier;

    public String getDeviceName() {
        return DeviceName;
    }

    public void setDeviceName(String deviceName) {
        DeviceName = deviceName;
    }

    public String getDeviceType() {
        return DeviceType;
    }

    public void setDeviceType(String deviceType) {
        DeviceType = deviceType;
    }

    public String getDonorId() {
        return DonorId;
    }

    public void setDonorId(String donorId) {
        DonorId = donorId;
    }

    public String getDeviceIdentifier() {
        return DeviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        DeviceIdentifier = deviceIdentifier;
    }
}
