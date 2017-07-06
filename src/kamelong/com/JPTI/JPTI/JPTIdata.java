package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Created by kame on 2017/06/27.
 */
public class JPTIdata {
    private static final String JPTI_VERSION="JPTI_version";
    private static final String AGENCY="agency";
    private static final String STATION="station";
    private static final String ROUTE="route";
    private static final String CALENDAR="calendar";
    private static final String SERVICE="service";

    ArrayList<Agency> agency=new ArrayList<>();
    ArrayList<Station> stationList=new ArrayList<>();
    ArrayList<Route> routeList=new ArrayList<>();
    ArrayList<Calendar> calendarList=new ArrayList<>();
    ArrayList<Service>serviceList=new ArrayList<>();

    /**
     * テストコンストラクタ
     */
    public JPTIdata(){
        Agency agency1=new Agency();
        agency1.name="テスト１";
        Agency agency2=new Agency();
        agency2.name="テスト２";
        agency2.fareUrl="http://kamelong.com/JPTI";
        agency.add(agency1);
        agency.add(agency2);
    }

    /**
     * JPTIのJSONオブジェクトからのコンストラクタ
     */
    public JPTIdata(JSONObject json){
        try{
            JSONArray agencyArray=json.getJSONArray(AGENCY);
            for(int i=0;i<agencyArray.length();i++) {
                agency.add(new Agency(agencyArray.getJSONObject(i)));
            }
        }catch(JSONException e){
        }
        try{
            JSONArray routeArray=json.getJSONArray(ROUTE);
            for(int i=0;i<routeArray.length();i++) {
                routeList.add(new Route(routeArray.getJSONObject(i)));
            }
        }catch(JSONException e){
        }
        try{
            JSONArray stationArray=json.getJSONArray(STATION);
            for(int i=0;i<stationArray.length();i++) {
                stationList.add(new Station(stationArray.getJSONObject(i)));
            }
        }catch(JSONException e){
        }
        try{
            JSONArray serviceArray=json.getJSONArray(SERVICE);
            for(int i=0;i<serviceArray.length();i++) {
                serviceList.add(new Service(serviceArray.getJSONObject(i)));
            }
        }catch(JSONException e){
        }
        try{
            JSONArray calendarArray=json.getJSONArray(CALENDAR);
            for(int i=0;i<calendarArray.length();i++) {
                calendarList.add(new Calendar(calendarArray.getJSONObject(i)));
            }
        }catch(JSONException e){
        }


    }
    /**
     * OuDiaから生成するコンストラクタ
     * @param oudiaFile
     */
    public JPTIdata(OuDiaDiaFile oudiaFile){
        Service service=new Service();
        serviceList.add(service);
        ArrayList<Integer>borderList=service.loadOuDia(oudiaFile);
        int startStation=0;
        for(int border:borderList){
            if(border-startStation>0){
                service.route.put(routeList.size(),0);
                routeList.add(new Route(oudiaFile,startStation,border,this));
                startStation=border;
                if(oudiaFile.getStation(border).border()){
                    startStation++;
                }

            }
        }
    }




    /**
     * このオブジェクトが持つ時刻データをJSONファイルに書き出す。
     * @param outFile 出力ファイル
     */
    public void makeJSONdata(FileWriter outFile){
        try {
            JSONObject outJSON = new JSONObject();
            outJSON.put(JPTI_VERSION,"0.2");
            JSONArray agencyArray = new JSONArray();
            for(int i=0;i<agency.size();i++){
                agencyArray.put(agency.get(i).makeJSONObject());
            }
            outJSON.put(AGENCY,agencyArray);
            JSONArray stationArray=new JSONArray();
                for(Station station:stationList){
                    stationArray.put(station.makeJSONObject());
                }
            outJSON.put(STATION,stationArray);
            JSONArray routeArray=new JSONArray();
            for(Route route:routeList){
                routeArray.put(route.makeJSONObject());
            }
            outJSON.put(ROUTE,routeArray);
            JSONArray calendarArray=new JSONArray();
            for(Calendar calendar:calendarList){
                calendarArray.put(calendar.makeJSONObject());
            }
            outJSON.put(CALENDAR,calendarArray);
            JSONArray serviceArray=new JSONArray();
            for(Service service:serviceList){
                serviceArray.put(service.makeJSONObject());
            }
            outJSON.put(SERVICE,serviceArray);
            outFile.write(outJSON.toString());
            outFile.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private OptionalInt searchStation(String stationName){
        return IntStream.range(0, stationList.size())
                .filter(i -> stationList.get(i).name.equals(stationName))
                .findFirst();

    }


}
