package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import kamelong.com.JPTI.OuDia.Train;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.stream.IntStream;

/**
 * Created by kame on 2017/06/28.
 */
public class Time {
    private JPTIdata jpti;

    int stationID=0;
    private int stopID=0;
    private int pickupType=1;
    private int dropoffType=1;
    private int arrival_days=-1;
    private String arrivalTime=null;
    private int departure_days=-1;
    private String departureTime=null;

    private static final String STATION_ID="station_id";
    private static final String STOP_ID="stop_id";
    private static final String PICKUP="pickup_type";
    private static final String DROPOFF="dropoff_type";
    private static final String ARRIVAL_DAYS="arrival_days";
    private static final String ARRIVAL_TIME="arrivel_time";
    private static final String DEPARTURE_DAYS="depature_days";
    private static final String DEPARTURE_TIME ="departure_time";

    public Time(Train train, OuDiaDiaFile oudia,int station, JPTIdata data){
        jpti=data;
        stationID=IntStream.range(0, data.stationList.size())
                .filter(i -> data.stationList.get(i).name.equals(oudia.getStationName(station)))
                .findFirst().getAsInt();
        stopID=data.stationList.get(stationID).findOuDiaStation().getAsInt();
        if(train.getStopType(station)==1){
            pickupType=0;
            dropoffType=0;
        }else{
            pickupType=1;
            dropoffType=1;
        }
        if(train.arriveExist(station)){
            int arrive=train.getArriveTime(station);
            String ss=String.format("%02d",arrive%60);
            arrive=arrive/60;
            String mm=String.format("%02d",arrive%60);
            arrive=arrive/60;
            String hh=String.format("%02d",arrive%60);
            arrivalTime=hh+":"+mm+":"+ss;
        }
        if(train.departExist(station)){
            int depart=train.getArriveTime(station);
            String ss=String.format("%02d",depart%60);
            depart=depart/60;
            String mm=String.format("%02d",depart%60);
            depart=depart/60;
            String hh=String.format("%02d",depart%60);
            departureTime=hh+":"+mm+":"+ss;
        }
    }
    public Time(JSONObject json,JPTIdata data){
        jpti=data;
        try {
            try {
                stationID = json.getInt(STATION_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                stopID = json.getInt(STOP_ID);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pickupType = json.optInt(PICKUP);
            dropoffType = json.optInt(DROPOFF);
            arrival_days = json.optInt(ARRIVAL_DAYS);
            arrivalTime = json.optString(ARRIVAL_TIME);
            departure_days = json.optInt(DEPARTURE_DAYS);
            departureTime = json.optString(DEPARTURE_TIME);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try {
            json.put(STATION_ID, stationID);
            json.put(STOP_ID,stopID);
            json.put(PICKUP,pickupType);
            json.put(DROPOFF,dropoffType);
            if(arrival_days>0){
                json.put(ARRIVAL_DAYS,arrival_days);
            }
            if(arrivalTime!=null){
                json.put(ARRIVAL_TIME,arrivalTime);
            }
            if(departure_days>0){
                json.put(DEPARTURE_DAYS,departure_days);
            }
            if(departureTime!=null){
                json.put(DEPARTURE_TIME,departureTime);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return json;

    }
    Element makeSujiTaroData(Document document){
        Element timeDetail=document.createElement("時刻明細");
        timeDetail.appendChild(createDom(document,"駅名",jpti.stationList.get(stationID).name));

        if(departureTime==null||departureTime.length()==0){
            if(arrivalTime==null||arrivalTime.length()==0){
                timeDetail.appendChild(createDom(document,"発車時刻","00:00:00"));
                timeDetail.appendChild(createDom(document,"停車種類","2"));

            }
            timeDetail.appendChild(createDom(document,"発車時刻",arrivalTime));
            timeDetail.appendChild(createDom(document,"停車種類","0"));

        }else{
            timeDetail.appendChild(createDom(document,"発車時刻",departureTime));
            timeDetail.appendChild(createDom(document,"停車種類","0"));

        }
        timeDetail.appendChild(createDom(document,"停車時間",stoppingTime()));
        return timeDetail;

    }
    Element createDom(Document document,String tagName,String content){
        Element result=document.createElement(tagName);
        result.setTextContent(content);
        return result;

    }
    String stoppingTime(){
        if(arrivalTime==null||arrivalTime.length()==0||departureTime.length()==0){
            return "00:00:00";
        }
        int arrival=timeString2Int(arrivalTime);
        int departure=timeString2Int(departureTime);
        if(arrival>departure){
            departure+=24*60*60;
        }
        return timeInt2String(departure-arrival);

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
