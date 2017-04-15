package com.study.pengshao.myclassinfoinncu.data_class;

import cn.bmob.v3.BmobObject;

/**
 * Created by PengShao on 2016/9/29.
 */
public class PickData extends BmobObject {
    private String pickName;
    private String pickContent;
    private String pickPosition;
    private String pickTime;
    private String pickImageUrl = null;
    private String pickerPhone;

    public String getPickName() {
        return pickName;
    }

    public void setPickName(String pickName) {
        this.pickName = pickName;
    }

    public String getPickContent() {
        return pickContent;
    }

    public void setPickContent(String pickContent) {
        this.pickContent = pickContent;
    }

    public String getPickPosition() {
        return pickPosition;
    }

    public void setPickPosition(String pickPosition) {
        this.pickPosition = pickPosition;
    }

    public String getPickImageUrl() {
        return pickImageUrl;
    }

    public void setPickImageUrl(String pickImageUrl) {
        this.pickImageUrl = pickImageUrl;
    }

    public String getPickTime() {
        return pickTime;
    }

    public void setPickTime(String pickTime) {
        this.pickTime = pickTime;
    }

    public String getPickerPhone() {
        return pickerPhone;
    }

    public void setPickerPhone(String pickerPhone) {
        this.pickerPhone = pickerPhone;
    }

    @Override
    public String toString() {
        return "PickData{" +
                "pickName='" + pickName + '\'' +
                ", pickContent='" + pickContent + '\'' +
                ", pickPosition='" + pickPosition + '\'' +
                ", pickTime='" + pickTime + '\'' +
                ", pickImageUrl='" + pickImageUrl + '\'' +
                ", pickerPhone='" + pickerPhone + '\'' +
                '}';
    }
}
