package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.JPTI.Agency;
import kamelong.com.JPTI.OuDia.DiaFile;
import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import org.json.JSONArray;
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

    ArrayList<Agency> agency=new ArrayList<>();
    ArrayList<Station> stationList=new ArrayList<>();
    ArrayList<Route> routeList=new ArrayList<>();
    ArrayList<Calendar> calendarList=new ArrayList<>();

    public JPTIdata(){
        Agency agency1=new Agency();
        agency1.name="テスト１";
        Agency agency2=new Agency();
        agency2.name="テスト２";
        agency2.fareUrl="http://kamelong.com/JPTI";
        agency.add(agency1);
        agency.add(agency2);
    }
    public JPTIdata(OuDiaDiaFile oudiaFile){
        routeList.add(new Route(oudiaFile,0,oudiaFile.getStationNum()-1,this));
    }


    /**
     * このオブジェクトが持つ時刻データをJSONファイルに書き出す。
     * @param outFile 出力ファイル
     */
    public void makeJSONdata(FileWriter outFile){
        try {
            JSONObject outJSON = new JSONObject();
            outJSON.put(JPTI_VERSION,"0.1");
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
