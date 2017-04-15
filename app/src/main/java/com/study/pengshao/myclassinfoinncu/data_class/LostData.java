package com.study.pengshao.myclassinfoinncu.data_class;

import cn.bmob.v3.BmobObject;

/**
 * Created by PengShao on 2016/9/23.
 */
public class LostData extends BmobObject{
    private String lostName;
    private String lostContent;
    private String lostPosition;
    private String lostTime;
    private String lostImageUrlPath = null;

    private String lostPhone;

    public String getLostName() {
        return lostName;
    }

    public void setLostName(String lostName) {
        this.lostName = lostName;
    }

    public String getLostContent() {
        return lostContent;
    }

    public void setLostContent(String lostContent) {
        this.lostContent = lostContent;
    }

    public String getLostPosition() {
        return lostPosition;
    }

    public void setLostPosition(String lostPosition) {
        this.lostPosition = lostPosition;
    }

    public String getLostTime() {
        return lostTime;
    }

    public void setLostTime(String lostTime) {
        this.lostTime = lostTime;
    }

    public String getLostImageUrlPath() {
        return lostImageUrlPath;
    }

    public void setLostImageUrlPath(String lostImageUrlPath) {
        this.lostImageUrlPath = lostImageUrlPath;
    }

    public String getLostPhone() {
        return lostPhone;
    }

    public void setLostPhone(String lostPhone) {
        this.lostPhone = lostPhone;
    }

}
