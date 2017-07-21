package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import kamelong.com.JPTI.OuDia.Train;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;

/**
 * 列車を記録するクラス
 */
public class Trip {
    JPTIdata jpti;
    /**
     * 列車番号
     */
    private String number=null;
    /**
     * 列車名、バス愛称、行先等の情報
     */
    private String name=null;
    /**
     * 0：Route順方向
     * 1：Route逆方向
     */
    int direction=0;
    /**
     * 種別id
     */
    private int classID=0;
    /**
     * 系統id
     */
    int blockID=0;
    /**
     * 運行する日id
     */
    private int calenderID=0;
    /**
     * 臨時運行日id
     */
    private int extraCalendarID=-1;

    ArrayList<Time> timeList=new ArrayList<>();


    private static final String NAME="trip_name";
    private static final String NUMBER="trip_No";
    private static final String CLASS="trip_class";
    private static final String DIRECTION="trip_direction";
    private static final String BLOCK="block_id";
    private static final String CALENDER="calender_id";
    private static final String EXTRA_CALENDER="extra_calendar";
    private static final String TIME="time";

    public Trip(Train train,int startStation,int endStation,int block,int direct,int calender,OuDiaDiaFile oudia,JPTIdata jptiData){
        jpti=jptiData;
        if(train.getName().length()>0){
            name=train.getName();
        }
        if(train.getNumber().length()>0){
            number=train.getNumber();
        }
        direction=direct;
        blockID=block;
        calenderID=calender;
        classID=train.getType();

        if(direct==0){
            for(int i=startStation;i<endStation+1;i++){
                if(train.getStopType(i)==1||train.getStopType(i)==2){
                    timeList.add(new Time(train,oudia,i,jptiData));
                }
            }
        }else{
            for(int i=endStation;i>startStation-1;i--){
                if(train.getStopType(i)==1||train.getStopType(i)==2){
                    timeList.add(new Time(train,oudia,i,jptiData));
                }
            }
        }
    }
    public Trip(JSONObject json,JPTIdata jptiData){
        jpti=jptiData;
        try{
            try{
                classID=json.getInt(CLASS);
            }catch(JSONException e){
                e.printStackTrace();
            }
            try{
                blockID=json.getInt(BLOCK);
            }catch(JSONException e){
                e.printStackTrace();
            }
            try{
                calenderID=json.getInt(CALENDER);
            }catch(JSONException e){
                e.printStackTrace();
            }
            number=json.optString(NUMBER);
            name=json.optString(NAME);
            direction=json.optInt(DIRECTION);
            extraCalendarID=json.optInt(EXTRA_CALENDER);
            JSONArray timeArray=json.getJSONArray(TIME);
            for(int i=0;i<timeArray.length();i++){
                timeList.add(new Time(timeArray.getJSONObject(i),jpti));
            }




        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            if(name!=null) {
                json.put(NAME, name);
            }
            if(number!=null) {
                json.put(NUMBER, number);
            }
            json.put(DIRECTION,direction);
            json.put(CLASS,classID);
            json.put(BLOCK,blockID);
            json.put(CALENDER,calenderID);
            if(extraCalendarID>-1){
                json.put(EXTRA_CALENDER,extraCalendarID);
            }
            JSONArray timeArray=new JSONArray();
            for(Time time:timeList){
                timeArray.put(time.makeJSONObject());
            }
            json.put(TIME,timeArray);


        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }
    Element makeSujiTaroData(Document document){
        Element train=document.createElement("列車明細");
        train.appendChild(createDom(document,"列車番号",number));
        train.appendChild(createDom(document,"列車名",name));
        train.appendChild(createDom(document,"列車号番号","0"));
        train.appendChild(createDom(document,"列車種別",classID+""));
        train.appendChild(createDom(document,"動力種別","1"));
        train.appendChild(createDom(document,"輸送種別","0"));
        train.appendChild(createDom(document,"運転日","0"));
        train.appendChild(createDom(document,"他線へ直通_起点側","False"));
        train.appendChild(createDom(document,"他線へ直通_起点側_反転","False"));
        train.appendChild(createDom(document,"他線へ直通_終点側","False"));
        train.appendChild(createDom(document,"他線へ直通_起点側_反転","False"));
        train.appendChild(createDom(document,"他線へ直通_中間部","False"));


        return train;
    }

    Element createDom(Document document,String tagName,String content){
        Element result=document.createElement(tagName);
        if(content==null){
            result.setTextContent("");
        }else {
            result.setTextContent(content);
        }
        return result;

    }


}
