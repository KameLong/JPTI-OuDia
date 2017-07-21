package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import org.json.JSONArray;
import org.json.JSONObject;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.sound.sampled.Line;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.util.*;
import java.util.stream.IntStream;

/**
 * 時刻表路線を記録するクラス
 * 時刻表路線はOuDiaファイル１つに対応する
 */
public class Service {
    JPTIdata jpti;
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
    public ArrayList<Integer>loadOuDia(JPTIdata data,OuDiaDiaFile diaFile){
        jpti=data;
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
    public Service(JPTIdata data,JSONObject json){
        jpti=data;
        try{

            name=json.optString(NAME,"");
            JSONArray routeArray=json.optJSONArray(ROUTE);
            for(int i=0;i<routeArray.length();i++){
                route.put(routeArray.getJSONObject(i).optInt(ROUTE_ID,0),routeArray.getJSONObject(i).optInt(DIRECTION,0));
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

    public void makeSujiTaroData() {
        try {
            DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docbuilder = dbfactory.newDocumentBuilder(); // DocumentBuilderインスタンス
            Document document = docbuilder.newDocument();          // Documentの生成
            Element Sujitaro = document.createElement("Sujitaro");  // manyosyuノード作成
            Sujitaro.appendChild(createDom(document,"Version","2.2"));
            Element DiagramSetting=document.createElement("DiagramSetting");
            Element DiagramSetting2=document.createElement("ダイヤグラム設定");
            DiagramSetting2.appendChild(createDom(document,"何分目か","2"));
            DiagramSetting2.appendChild(createDom(document,"倍率","120"));
            DiagramSetting2.appendChild(createDom(document,"開始時刻",""+(timeString2Int(startTime)/3600)));
            DiagramSetting2.appendChild(createDom(document,"終了時刻",""+(timeString2Int(startTime)/3600+24)));
            DiagramSetting2.appendChild(createDom(document,"小さい列車番号","False"));
            DiagramSetting2.appendChild(createDom(document,"デフォルトの列車種別","0"));
            DiagramSetting2.appendChild(createDom(document,"車両運用の描画","True"));
            Element trainType=document.createElement("列車種別");

            ArrayList<String> className=new ArrayList<>();
            for(int i:route.keySet()){
                for(Class c:jpti.routeList.get(i).classList){
                    if(!className.contains(c.name)) {
                        className.add(c.name);
                        c.makeSujiTaroData(document, trainType);
                    }
                }
            }
            DiagramSetting2.appendChild(trainType);
            DiagramSetting.appendChild(DiagramSetting2);
            Sujitaro.appendChild(DiagramSetting);

            Element TimetableSetting=document.createElement("TimetableSetting");
            Element TimetableSetting2=document.createElement("時刻表設定");
            TimetableSetting2.appendChild(createDom(document,"運転停車表示タイプ","True"));
            TimetableSetting2.appendChild(createDom(document,"秒表示タイプ","False"));
            TimetableSetting.appendChild(TimetableSetting2);
            Sujitaro.appendChild(TimetableSetting);

            Element AutomaticTrainSetting =document.createElement("AutomaticTrainSetting");
            Element AutomaticTrainSetting2=document.createElement("自動列車追加用設定");
            AutomaticTrainSetting2.appendChild(document.createElement("下り列車種別単位情報"));
            AutomaticTrainSetting2.appendChild(document.createElement("上り列車種別単位情報"));
            AutomaticTrainSetting2.appendChild(document.createElement("線路本数情報"));
            AutomaticTrainSetting2.appendChild(document.createElement("駅情報"));
            AutomaticTrainSetting.appendChild(AutomaticTrainSetting2);
            Sujitaro.appendChild(AutomaticTrainSetting);

            Element Lines=document.createElement("Lines");
            Element LineInfo=document.createElement("lineInfo");
            LineInfo.appendChild(createDom(document,"名称",name));
            LineInfo.appendChild(document.createElement("開始駅名"));
            LineInfo.appendChild(document.createElement("終了駅名"));
            LineInfo.appendChild(createDom(document,"開始距離","0"));
            LineInfo.appendChild(createDom(document,"終了距離","0"));
            Element stationItems=document.createElement("StationItems");
            String lastStationName="";
            for(int i:route.keySet()){
                for(RouteStation station:jpti.routeList.get(i).stationList){
                    if(!jpti.stationList.get(station.stationID).name.equals(lastStationName)){
                        stationItems.appendChild(station.makeSujiTaroData(document,jpti.stationList.get(station.stationID).name));
                    }
                    lastStationName=jpti.stationList.get(station.stationID).name;
                }
            }
            LineInfo.appendChild(stationItems);
            Lines.appendChild(LineInfo);
            Sujitaro.appendChild(Lines);

            Element Trains=document.createElement("Trains");
            Element TrainItems=document.createElement("TrainItems");
            Element TrainsDown=document.createElement("TrainsDown");
            Element TrainsUp=document.createElement("TrainsUp");
            ArrayList<Integer> useblockID=new ArrayList<>();
            for(int i:route.keySet()){
                for(Trip baseTrip:jpti.routeList.get(i).tripList){
                    if(!useblockID.contains(baseTrip.blockID)){
                        useblockID.add(baseTrip.blockID);
                        Element train0=baseTrip.makeSujiTaroData(document);
                        Element train1=baseTrip.makeSujiTaroData(document);
                        Element timeElement0=document.createElement("時刻表要素");
                        Element timeElement1=document.createElement("時刻表要素");
                        ArrayList<String> useStation=new ArrayList<>();
                        for(int j:route.keySet()){
                            for(Trip trip:jpti.routeList.get(i).searchByBlockID(baseTrip.blockID)){
                                if(trip.direction==0){
                                    for(Time time:trip.timeList){
                                        if(!useStation.contains(jpti.stationList.get(time.stationID).name)){
                                            timeElement0.appendChild(time.makeSujiTaroData(document));
                                            useStation.add(jpti.stationList.get(time.stationID).name);
                                        }
                                    }
                                }else{
                                    for(Time time:trip.timeList){
                                        if(!useStation.contains(jpti.stationList.get(time.stationID).name)){
                                            timeElement1.appendChild(time.makeSujiTaroData(document));
                                            useStation.add(jpti.stationList.get(time.stationID).name);
                                        }
                                    }

                                }
                            }
                        }
                        if(timeElement0.getChildNodes().getLength()>0){
                            train0.appendChild(timeElement0);
                            TrainsDown.appendChild(train0);
                        }
                        if(timeElement1.getChildNodes().getLength()>0){
                            train1.appendChild(timeElement1);
                            TrainsUp.appendChild(train1);
                        }


                    }

                }
            }
            TrainItems.appendChild(TrainsDown);
            TrainItems.appendChild(TrainsUp);
            Trains.appendChild(TrainItems);
            //Sujitaro.appendChild(Trains);

            Element CarManagement=document.createElement("CarManagement");
            CarManagement.appendChild(document.createElement("車両運用情報"));
            Sujitaro.appendChild(CarManagement);

            Element TrainSplitMerge=document.createElement("TrainSplitMerge");
            TrainSplitMerge.appendChild(document.createElement("分割併合情報"));
            Sujitaro.appendChild(TrainSplitMerge);
            document.appendChild(Sujitaro);


         /*
          * DOMオブジェクトを文字列として出力
          */
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");

            transformer.transform(new DOMSource(document), new StreamResult(System.out));
        } catch (Exception e) {
            e.printStackTrace();

        }
    }
    Element createDom(Document document,String tagName,String content){
        Element result=document.createElement(tagName);
        result.setTextContent(content);
        return result;

    }
    static int timeString2Int(String time){
        int hh=Integer.parseInt(time.split(":",-1)[0]);
        int mm=Integer.parseInt(time.split(":",-1)[1]);
        int ss=Integer.parseInt(time.split(":",-1)[2]);
        return hh*3600+mm*60+ss;
    }
    static String timeInt2String(int time){
        int ss=time%60;
        time=time/60;
        int mm=time%60;
        time=time/60;
        int hh=time%24;
        return String.format("%02d",hh)+":"+String.format("%02d",mm)+":"+String.format("%02d",ss);

    }

}
