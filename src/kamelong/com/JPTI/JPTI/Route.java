package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.OuDiaDiaFile;
import kamelong.com.JPTI.OuDia.Train;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * 路線情報を格納するクラス。
 * Routeは枝分かれを許容しない。
 */
public class Route {
    /**
     * 所属する法人ID
     */
    private int agencyID=-1;
    /**
     * 内部番号とか
     */
    private int No=-1;
    /**
     * 路線名称
     * ※何とか支線は分けて書く
     * 山陰本線仙崎支線とか
     */
    private String name="";
    /**
     * 路線愛称
     */
    private String nickName=null;
    /**
     * 路線の説明
     */
    private String description=null;
    /**
     1：高速鉄道（新幹線）
     2：普通鉄道（JR、私鉄等）
     3：地下鉄
     4：路面電車
     5：ケーブルカー
     6：ロープウェイ
     7：高速バス
     8：路線バス
     9：コミュニティバス
     10：フェリー
     11：渡船
     12：飛行機？
     13：その他
     */
    private int type=-1;
    /**
     * 路線のURL
     */
    private String url=null;
    /**
     * 路線カラー
     */
    private Color color=null;
    /**
     * 路線文字色
     */
    private Color textColor=null;
    private ArrayList<RouteStation>stationList=new ArrayList<>();
    private ArrayList<Class> classList=new ArrayList<>();
    private ArrayList<Trip> tripList=new ArrayList<>();


    private static final String AGENCY_ID="agency_id";
    private static final String NO="route_no";
    private static final String NAME="route_name";
    private static final String NICKNAME="route_nickname";
    private static final String DESCRIPTION="route_description";
    private static final String TYPE="route_type";
    private static final String URL="route_url";
    private static final String COLOR="route_color";
    private static final String TEXT_COLOR="route_text_color";
    private static final String STATION="route_station";
    private static final String CLASS="class";
    private static final String TRIP="trip";

    /**
     * OuDiaファイルの路線の一部分から生成する。
     * @param oudia
     * @param startStation 開始駅
     * @param endStaton 終了駅
     */
    public Route(OuDiaDiaFile oudia, int startStation, int endStaton,JPTIdata jpti){
        OptionalInt agencyIndex=IntStream.range(0, jpti.agency.size())
                .filter(i -> jpti.agency.get(i).name.equals("oudia:"+oudia.getLineName()))
                .findFirst();
        if(agencyIndex.isPresent()){
            agencyID=agencyIndex.getAsInt();
        }else{
            agencyID=jpti.agency.size();
            Agency agency=new Agency();
            agency.name="oudia:"+oudia.getLineName();
            jpti.agency.add(agency);
        }
        name=oudia.getStationName(startStation)+"~"+oudia.getStationName(endStaton);
        type=2;
        for(int i=startStation;i<endStaton+1;i++){
            RouteStation station=new RouteStation(oudia.getStation(i),jpti.stationList);
            stationList.add(station);
        }
        for(int i=0;i<oudia.getTypeNum();i++){
            classList.add(new Class(oudia.getTrainType(i)));
        }
        for(int diaNum=0;diaNum<oudia.getDiaNum();diaNum++) {
            String diaName=oudia.getDiaName(diaNum);
            int calendarID;
            OptionalInt calendar=IntStream.range(0, jpti.calendarList.size())
                    .filter(i -> jpti.calendarList.get(i).name.equals(diaName))
                    .findFirst();
            if(calendar.isPresent()){
                calendarID=calendar.getAsInt();
            }else{
                calendarID=jpti.calendarList.size();
                Calendar newCalendar=new Calendar();
                newCalendar.name=diaName;
                jpti.calendarList.add(newCalendar);
            }


            for (int i = 0; i < oudia.getTrainNum(0, 0); i++) {
                int useStationNum = 0;
                Train train = oudia.getTrain(0, 0, i);
                for (int j = startStation; j < endStaton + 1; j++) {
                    if (train.getStopType(j) == 1 || train.getStopType(j) == 2) {
                        useStationNum++;
                    }
                }
                if (useStationNum < 2) {
                    continue;
                }
                tripList.add(new Trip(train, startStation, endStaton, calendarID * 10000 + 0 + i + 1, 0,calendarID, oudia, jpti));
            }
            for (int i = 0; i < oudia.getTrainNum(0, 1); i++) {
                int useStationNum = 0;
                Train train = oudia.getTrain(0, 1, i);
                for (int j = startStation; j < endStaton + 1; j++) {
                    if (train.getStopType(j) == 1 || train.getStopType(j) == 2) {
                        useStationNum++;
                    }
                }
                if (useStationNum < 2) {
                    continue;
                }
                tripList.add(new Trip(train, startStation, endStaton, calendarID * 10000 + 5000 + i + 1, 1, calendarID, oudia, jpti));
            }
        }
    }

    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(AGENCY_ID,agencyID);
            if(No>-1){
                json.put(NO,No);
            }
            json.put(NAME,name);
            if(nickName!=null){
                json.put(NICKNAME,nickName);
            }
            if(description!=null){
                json.put(DESCRIPTION,description);
            }
            if(type>0&&type<13){
                json.put(TYPE,type);
            }
            if(url!=null){
                json.put(URL,url);
            }
            if(color!=null){
                json.put(COLOR,"#"+Integer.toHexString(color.getRGB()));
            }
            if(textColor!=null){
                json.put(TEXT_COLOR,"#"+Integer.toHexString(textColor.getRGB()));
            }
            JSONArray stationArray=new JSONArray();
            for(RouteStation station:stationList){
                stationArray.put(station.makeJSONObject());
            }
            json.put(STATION,stationArray);
            JSONArray classArray=new JSONArray();
            for(Class type:classList){
                classArray.put(type.makeJSONObject());
            }
            json.put(CLASS,classArray);
            JSONArray tripArray=new JSONArray();
            for(Trip trip:tripList){
                tripArray.put(trip.makeJSONObject());
            }
            json.put(TRIP,tripArray);

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }




}
