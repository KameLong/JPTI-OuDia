package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.*;
import java.util.stream.IntStream;

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
    private static final String DIRECTION ="direction";
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

    public Service(){

    }
    public ArrayList<Integer>loadOuDia(OuDiaDiaFile diaFile){
        name=diaFile.getLineName();
        stationWidth=diaFile.stationNameLength;
        trainWidth=diaFile.ressyawidth;
        startTime=timeInt2String(diaFile.startTime);
        defaulyStationSpace=diaFile.zahyouKyoriDefault;
        comment=diaFile.getComment();
        diaTextColor=diaFile.diaMojiColor;
        diaBackColor=diaFile.diaHaikeiColor;
        diaTrainColor=diaFile.diaResyaColor;
        diaAxisColor=diaFile.diaJikuColor;
        timeTableFont=diaFile.jikokuhyouFont;
        timeTableVFont=diaFile.jikokuVFOnt;
        diaStationFont=diaFile.diaEkimeiFont;
        diaTimeFont=diaFile.diaJikokuFont;
        diaTrainFont=diaFile.diaRessyaFont;
        commentFont=diaFile.commentFont;

        ArrayList<Integer>borderStation=new ArrayList<>();
        for(int i=0;i<diaFile.getStationNum();i++){
            if(diaFile.getStation(i).border()){
                borderStation.add(i);
                String borderName=diaFile.getStation(i).getName();
                OptionalInt nextBorder=IntStream.range(i+1, diaFile.getStationNum())
                        .filter(j -> diaFile.getStation(j).getName().equals(borderName))
                        .findFirst();
                if(nextBorder.isPresent()){
                    borderStation.add(nextBorder.getAsInt());
                }
                if(i+1<diaFile.getStationNum()){
                    String borderName2=diaFile.getStation(i+1).getName();
                    OptionalInt beforeBorder=IntStream.range(0, i)
                            .filter(j -> diaFile.getStation(j).getName().equals(borderName2))
                            .findFirst();
                    if(beforeBorder.isPresent()){
                        borderStation.add(beforeBorder.getAsInt());
                    }
                }
            }
        }
        borderStation.add(diaFile.getStationNum()-1);
        Collections.sort(borderStation);
        return borderStation;
    }
    public Service(JSONObject json){
        try{
            name=json.optString(NAME,"");
            JSONArray routeArray=json.optJSONArray(ROUTE);
            for(int i=0;i<routeArray.length();i++){
                route.put(routeArray.getJSONObject(0).optInt(ROUTE_ID,0),routeArray.getJSONObject(i).optInt(DIRECTION,0));
            }
            stationWidth=json.optInt(STATION_WIDTH,7);
            trainWidth=json.optInt(TRAIN_WIDTH,5);
            startTime=json.optString(START_TIME);
            defaulyStationSpace=json.optInt(START_TIME);
            comment=json.optString(COMMENT);
            diaTextColor=Color.decode(json.optString(DIA_TEXT_COLOR,"#000000"));
            diaBackColor=Color.decode(json.optString(DIA_BACK_COLOR,"#ffffff"));
            diaTrainColor=Color.decode(json.optString(DIA_TRAIN_COLOR,"#000000"));
            diaAxisColor=Color.decode(json.optString(DIA_AXICS_COLOR,"#000000"));
            JSONArray timeTableFontArray=json.optJSONArray(TIMETABLE_FONT);
            for(int i=0;i<timeTableFontArray.length();i++){
                timeTableFont.add(new Font(timeTableFontArray.getJSONObject(i)));
            }
            timeTableVFont=new Font(json.getJSONObject(TIMETABLE_VFONT));
            diaStationFont=new Font(json.getJSONObject(DIA_STATION_FONT));
            diaTimeFont=new Font(json.getJSONObject(DIA_TIME_FONT));
            diaTrainFont=new Font(json.getJSONObject(DIA_TRAIN_FONT));
            commentFont=new Font(json.getJSONObject(COMMENT_FONT));


        }catch(Exception e){

        }
    }

    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
            JSONArray routeArray=new JSONArray();
            for (Map.Entry<Integer, Integer> bar : route.entrySet()) {
                JSONObject routeObject=new JSONObject();
                routeObject.put(ROUTE_ID,bar.getKey());
                routeObject.put(DIRECTION,bar.getValue());
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
    private String timeInt2String(int time){
        int ss=time%60;
        time=time/60;
        int mm=time%60;
        time=time/60;
        int hh=time%24;
        return String.format("%02d",hh)+":"+String.format("%02d",mm)+":"+String.format("%02d",ss);

    }


}
