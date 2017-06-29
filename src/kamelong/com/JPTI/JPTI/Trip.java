package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import kamelong.com.JPTI.OuDia.Train;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 列車を記録するクラス
 */
public class Trip {
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
    private int direction=0;
    /**
     * 種別id
     */
    private int classID=0;
    /**
     * 系統id
     */
    private int blockID=0;
    /**
     * 運行する日id
     */
    private int calenderID=0;
    /**
     * 臨時運行日id
     */
    private int extraCalendarID=-1;

    private ArrayList<Time> timeList=new ArrayList<>();


    private static final String NAME="trip_name";
    private static final String NUMBER="trip_No";
    private static final String CLASS="trip_class";
    private static final String DIRECTION="trip_direction";
    private static final String BLOCK="block_id";
    private static final String CALENDER="calender_id";
    private static final String EXTRA_CALENDER="extra_calendar";
    private static final String TIME="time";

    public Trip(Train train,int startStation,int endStation,int block,int direct,int calender,OuDiaDiaFile oudia,JPTIdata jpti){
        if(train.getName().length()>0){
            name=train.getName();
        }
        if(train.getNumber().length()>0){
            number=train.getNumber();
        }
        direction=direct;
        blockID=block;
        calenderID=calender;

        if(direct==0){
            for(int i=startStation;i<endStation+1;i++){
                if(train.getStopType(i)==1||train.getStopType(i)==2){
                    timeList.add(new Time(train,oudia,i,jpti));
                }
            }
        }else{
            for(int i=endStation;i>startStation-1;i--){
                if(train.getStopType(i)==1||train.getStopType(i)==2){
                    timeList.add(new Time(train,oudia,i,jpti));
                }
            }
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



}
