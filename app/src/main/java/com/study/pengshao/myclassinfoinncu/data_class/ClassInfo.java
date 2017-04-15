package com.study.pengshao.myclassinfoinncu.data_class;

import java.util.List;
import java.util.Map;

/**
 * Created by PengShao on 2016/8/3.
 */
public class ClassInfo {
    List<ClassInfo> classInfoList;
    private String className;
    private String teacherName;
    private String placeInfo;
    private String classPart;
    private ClassWeekInfo classWeekInfo;
    private boolean isDoubleClass;
    private Map<String,String> map;
    private Map<String,String> map2;
    private String id;

    private  class ClassWeekInfo{
        private boolean isDouble;
        private boolean isSingle;
        private String weekInfo;

        public boolean isDouble() {
            return isDouble;
        }

        public void setDouble(boolean aDouble) {
            isDouble = aDouble;
        }

        public boolean isSingle() {
            return isSingle;
        }

        public void setSingle(boolean single) {
            isSingle = single;
        }

        public String getWeekInfo() {
            return weekInfo;
        }

        public void setWeekInfo(String weekInfo) {
            this.weekInfo = weekInfo;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getMap2() {
        return map2;
    }

    public void setMap2(Map<String, String> map2) {
        this.map2 = map2;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public String getClassPart() {
        return classPart;
    }

    public void setClassPart(String classPart) {
        this.classPart = classPart;
    }

    public List<ClassInfo> getClassInfoList() {
        return classInfoList;
    }

    public void setClassInfoList(List<ClassInfo> classInfoList) {
        this.classInfoList = classInfoList;
    }

    public boolean isDoubleClass() {
        return isDoubleClass;
    }

    public void setDoubleClass(boolean doubleClass) {
        isDoubleClass = doubleClass;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getPlaceInfo() {
        return placeInfo;
    }

    public void setPlaceInfo(String placeInfo) {
        this.placeInfo = placeInfo;
    }

    public ClassWeekInfo getClassWeekInfo() {
        return classWeekInfo;
    }

    public void setClassWeekInfo(ClassWeekInfo classWeekInfo) {
        this.classWeekInfo = classWeekInfo;
    }
}
