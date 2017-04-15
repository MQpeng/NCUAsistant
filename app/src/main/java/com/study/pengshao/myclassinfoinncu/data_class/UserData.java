package com.study.pengshao.myclassinfoinncu.data_class;

import cn.bmob.v3.BmobObject;

/**
 * Created by PengShao on 2016/10/2.
 */
public class UserData extends BmobObject{
    String userName;
    String userPassword;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
