package com.study.pengshao.myclassinfoinncu.http_thread;

import com.study.pengshao.myclassinfoinncu.data_class.ClassInfo;
import com.study.pengshao.myclassinfoinncu.data_class.MarkData;
import com.study.pengshao.myclassinfoinncu.data_class.PowerData;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by PengShao on 2016/8/3.
 */
public class ParseHTML {
    private static final String TAG = "ParseHTML";

    public List<List<ClassInfo>> parseMyClassInfoFromUrlData(String urlPath,List<String> Keys, int position,boolean getText){
        Document doc = Jsoup.parseBodyFragment(urlPath);
        Element body = doc.body();
        Elements elements = body.getElementsByTag(Keys.get(0));
        String classInfoData = null;
        if(elements.size()<=position)
            return null;
        if(getText){
            classInfoData = elements.get(position).text();
        }
        else{
            classInfoData = elements.get(position).toString();
        }
        doc = Jsoup.parseBodyFragment(classInfoData);
        Elements trs = elements.select(Keys.get(0)).select(Keys.get(1));
        List<List<ClassInfo>> allInfoList = new ArrayList<List<ClassInfo>>();

        for(int i = 2;i<trs.size();i++){
            List<ClassInfo> infoList = new ArrayList<ClassInfo>();
            Elements ths = trs.get(i).select(Keys.get(2));//<th>
            Elements tds = trs.get(i).select(Keys.get(3));//<td>
            for(int j = 0;j<tds.size();j++) {
                //获取横向信息

                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassPart(ths.text());
                Elements divs = tds.get(j).select(Keys.get(4));//<div>
                if(divs.isEmpty()){
                    continue;
                }
                String className = divs.get(1).text();
                String id = divs.attr("id");
                classInfo.setId(id);
                classInfo.setClassName(className);
                className =  className.substring(className.indexOf(")")+1);
                if(className.contains("(")){
                    classInfo.setDoubleClass(true);
                }
                Elements fonts = divs.get(1).select(Keys.get(5));//<font>
                List<String> lastTitle = new ArrayList<String>();
                Map<String ,String > map = new HashMap<String,String>();
                Map<String ,String > map2 = new HashMap<String,String>();
                for (int m = 0; m < fonts.size(); m++) {
                    Element font = fonts.get(m);
                    String title = font.attr("title");
                    String titleText = font.text();
                    if(lastTitle.contains(title)){
                        map2.put(title,titleText);
                        classInfo.setMap2(map2);
                    }else {
                        map.put(title,titleText);
                        classInfo.setMap(map);
                    }
                    lastTitle.add(title);
                }
                infoList.add(classInfo);
            }
            allInfoList.add(infoList);
        }
        return allInfoList;
    }

    public static List<MarkData> parseMyMarkDataFromUrlData(String urlPath, List<String> Keys, int position, boolean getText){

        Document doc = Jsoup.parseBodyFragment(urlPath);
        Element body = doc.body();
        Elements elements = body.getElementsByTag(Keys.get(0));
        if(elements.size()<=position)
            return null;
        String classInfoData = null;
        if(getText){
            classInfoData = elements.get(position).text();
        }
        else{
            classInfoData = elements.get(position).toString();
        }
        doc = Jsoup.parseBodyFragment(classInfoData);
        Elements trs = elements.select(Keys.get(0)).select(Keys.get(1));

        List<MarkData> datas = new ArrayList<MarkData>();

        for(int i = 1;i<trs.size();i++){
            MarkData markData = new MarkData();
            List<ClassInfo> infoList = new ArrayList<ClassInfo>();
            Elements tds = trs.get(i).select(Keys.get(2));//<td>

            if(tds.size()==10) {
                markData.className = tds.get(3).text().toString();
                markData.classInfo = tds.get(8).text().toString();
                markData.markInfo = tds.get(4).text().toString();
                datas.add(markData);
            }
        }
        return datas;
    }

    public static PowerData parsePowerInfoFromUrlData(String urlPath, List<String> Keys, int position, boolean getText) {
        PowerData datas = new PowerData();

        Document doc = Jsoup.parseBodyFragment(urlPath);
        Element body = doc.body();
        Elements elements = body.getElementsByTag(Keys.get(0));
        String classInfoData = null;
        if(getText){
            classInfoData = elements.get(position).text();
        }
        else{
            classInfoData = elements.get(position).toString();
        }
        doc = Jsoup.parseBodyFragment(classInfoData);
        Elements trs = elements.select(Keys.get(0)).select(Keys.get(1));

        Elements cash = trs.get(2).select(Keys.get(2));
        Elements power = trs.get(3).select(Keys.get(2));
        datas.cashReminder = cash.get(1).text().toString();
        datas.powerReminder = power.get(1).text().toString();
        datas.cashStudent = cash.get(3).text().toString();
        datas.powerStudent = power.get(3).text().toString();
        return datas;
    }

    public static List<String> parseCETInfoFromUrlData(String urlPath, List<String> Keys, int position, boolean getText) {

        List<String> list = new ArrayList<String>();
        Document doc = Jsoup.parseBodyFragment(urlPath);
        Element body = doc.body();
        Elements elements = body.getElementsByTag(Keys.get(0));

        if(elements.size()==0){
            return null;
        }
        String classInfoData;
        if(getText){
            classInfoData = elements.get(position).text();
        } else{
            classInfoData = elements.get(position).toString();
        }
        doc = Jsoup.parseBodyFragment(classInfoData);
        Elements trs = elements.select(Keys.get(0)).select(Keys.get(1));
        for (int i = 0;i<trs.size();i++) {
            list.add(trs.get(i).text().toString());
        }
        return list;
    }
}
