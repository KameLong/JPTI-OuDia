package kamelong.com.JPTI.JPTI;

import com.sun.org.apache.bcel.internal.generic.FADD;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 時刻表路線を記録するクラス
 * 時刻表路線はOuDiaファイル１つに対応する
 */
public class Service {
    String name="";
    Map<Integer,Integer> route=new HashMap<Integer ,Integer>();
    int stationWidth=-1;
    int trainWidth=-1;
    String startTime=null;
    int defaulyStationSpace=-1;
    String comment=null;

    Color diaTextColor=null;
    Color diaBackColor=null;
    Color diaTrainColor=null;
    Color diaAxisColor=null;
    ArrayList<Font> timeTableFont=new ArrayList();
    Font timeTableVFont=null;
    Font diaStationFont=null;
    Font diaTimeFont=null;
    Font diaTrainFont=null;
    Font commentFont=null;

    private static final String NAME="service_name";
    private static final String ROUTE="route_array";
    private static final String ROUTE_ID="route_id";
    private static final String DORECTION="direction";
    private static final String STATION_WIDTH="station_width";
    private static final String TRAIN_WIDTH="train_width";
    private static final String START_TIME="timetable_start_time";
    private static final String STATION_SPACING="station_spacing";
    private static final String COMMENT="comment_text";

    private static final String DIA_TEXT_COLOR="dia_text_color";
    private static final String DIA_BACK_COLOR="dia_back_color";
    private static final String DIA_TRAIN_COLOR="dia_train_color";
    private static final String DIA_AXICS_COLOR="dia_axics_color";
    private static final String TIMETABLE_FONT="font_timetable";
    private static final String TIMETABLE_VFONT="font_vfont";
    private static final String DIA_STATION_FONT="font_dia_station";
    private static final String DIA_TIME_FONT="font_dia_time";
    private static final String DIA_TRAIN_FONT="font_dia_train";
    private static final String COMMENT_FONT="font_comment";

    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
            JSONArray routeArray=new JSONArray();
            for (Map.Entry<Integer, Integer> bar : route.entrySet()) {
                JSONObject routeObject=new JSONObject();
                routeObject.put(ROUTE_ID,bar.getKey());
                routeObject.put(DORECTION,bar.getValue());
                routeArray.put(routeObject);
            }
            json.put(ROUTE,routeArray);
            if(stationWidth>-1){
                json.put(STATION_WIDTH,stationWidth);
            }
            if(trainWidth>-1){
                json.put(TRAIN_WIDTH,trainWidth);
            }
            if(startTime!=null){
                json.put(START_TIME,startTime);
            }
            if(defaulyStationSpace>-1){
                json.put(STATION_SPACING,defaulyStationSpace);
            }
            if(comment!=null){
                json.put(COMMENT,comment);
            }
            if(diaTextColor!=null){
                json.put(DIA_TEXT_COLOR,"#"+Integer.toHexString(diaTextColor.getRGB()));
            }
            if(diaBackColor!=null){
                json.put(DIA_BACK_COLOR,"#"+Integer.toHexString(diaBackColor.getRGB()));
            }
            if(diaTrainColor!=null){
                json.put(DIA_TRAIN_COLOR,"#"+Integer.toHexString(diaTrainColor.getRGB()));
            }
            if(diaAxisColor!=null){
                json.put(DIA_AXICS_COLOR,"#"+Integer.toHexString(diaAxisColor.getRGB()));
            }
            JSONArray timetableFontArray=new JSONArray();
            for(Font font:timeTableFont){
                timetableFontArray.put(font.makeJSONObject());
            }
            json.put(TIMETABLE_FONT,timetableFontArray);
            if(timeTableVFont!=null){
                json.put(TIMETABLE_VFONT,timeTableVFont.makeJSONObject());
            }
            if(diaStationFont!=null){
                json.put(DIA_STATION_FONT,diaStationFont.makeJSONObject());
            }
            if(diaTimeFont!=null){
                json.put(DIA_TIME_FONT,diaTimeFont.makeJSONObject());
            }
            if(diaTrainFont!=null){
                json.put(DIA_TRAIN_FONT,diaTrainFont.makeJSONObject());
            }
            if(commentFont!=null){
                json.put(COMMENT_FONT,commentFont.makeJSONObject());
            }


        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }


}
class Font{
    /**
     * フォント高さ
     */
    private int height=-1;
    /**
     * フォント名
     */
    private String name=null;
    /**
     * 太字なら１
     */
    private boolean bold=false;
    /**
     * 斜体なら１
     */
    private boolean itaric=false;

    private static final String HEIGHT="height";
    private static final String NAME="facename";
    private static final String BOLD="bold";
    private static final String ITARIC="itaric";

    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            if(height>-1){
                json.put(HEIGHT,height);
            }
            if(name!=null){
                json.put(NAME,name);
            }
            if(bold){
                json.put(BOLD,1);
            }
            if(itaric){
                json.put(ITARIC,1);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }



}
