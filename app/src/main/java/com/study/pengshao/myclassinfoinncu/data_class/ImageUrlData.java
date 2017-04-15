package com.study.pengshao.myclassinfoinncu.data_class;

import cn.bmob.v3.BmobObject;

/**
 * Created by PengShao on 2016/10/1.
 */
public class ImageUrlData extends BmobObject{
    private String UrlPath;
    private String OtherStr;
    private Boolean IsLoadImage;

    public Boolean getLoadImage() {
        return IsLoadImage;
    }

    public void setLoadImage(Boolean loadImage) {
        IsLoadImage = loadImage;
    }

    public String getUrlPath() {
        return UrlPath;
    }

    public void setUrlPath(String urlPath) {
        UrlPath = urlPath;
    }

    public String getOtherStr() {
        return OtherStr;
    }

    public void setOtherStr(String otherStr) {
        OtherStr = otherStr;
    }
}
