package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import kamelong.com.JPTI.OuDia.Train;
import org.json.JSONObject;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created by kame on 2017/06/28.
 */
public class Time {
    private int stationID=0;
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

    public Time(Train train, OuDiaDiaFile oudia,int station, JPTIdata jpti){
        stationID=IntStream.range(0, jpti.stationList.size())
                .filter(i -> jpti.stationList.get(i).name.equals(oudia.getStationName(station)))
                .findFirst().getAsInt();
        stopID=jpti.stationList.get(stationID).findOuDiaStation().getAsInt();
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

}
