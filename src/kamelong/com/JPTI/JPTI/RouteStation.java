package kamelong.com.JPTI.JPTI;

import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * Route中に使用される、Routeに所属する駅リスト。
 * 時刻表表記に使用される情報はこのクラス内に記述する
 */
public class RouteStation {
    /**
     * 対応する駅ID
     */
    int stationID=-1;
    /**
     * 路線キロ程
     */
    double km=-1;
    /**
     * 駅ナンバリング
     */
    int numbering=-1;
    /**
     * 主要駅かどうか
     */
    boolean bigStation=false;
    /**
     2桁の数字文字列で表す
     10の位：上り
     1の位：下り
     +
     0：発のみ
     1：発着
     2：着のみ
     */
    int viewStyle=0;
    /**
     * 境界線を持つかどうか
     */
    private boolean border=false;

    private static final String STATION_ID="station_id";
    private static final String KM="km";
    private static final String NUMBERING="station_numbering";
    private static final String TYPE="station_type";
    private static final String VIEWSTYLE="viewstyle";
    private static final String BORDER="border";

    /**
     * oudiaの駅とJPTIの駅リストからRouteStationを作成する。
     * @param oudiaStation この駅を作るための情報を含んだOuDiaの駅
     * @param JPTIStationList この駅のstationIDを指定したい駅リスト。この駅と同名の駅が含まれている必要がある。
     */
    public RouteStation(kamelong.com.JPTI.OuDia.Station oudiaStation, ArrayList<Station> JPTIStationList){
        //stationIDを指定。JPTIStationList内に同駅名の駅があればそれを使用。
        //もしなければ、新しく駅を作り、JPTIStationListに追加し、そのインデックスを登録
        OptionalInt id=findStationID(JPTIStationList,oudiaStation.getName());
        if(id.isPresent()){
            stationID=id.getAsInt();
        }else{
            stationID=JPTIStationList.size();
            Station station=new Station(oudiaStation);
            JPTIStationList.add(station);
        }
        bigStation=oudiaStation.getBigStation();
        border=oudiaStation.border();

        //viewstyleの指定
        //上り
        int viewStyleInt=0;
        switch (oudiaStation.getTimeShow(1)){
            case 1:
                viewStyleInt+=0;
                break;
            case 2:
                viewStyleInt+=2;
                break;
            case 3:
                viewStyleInt+=1;
                break;
        }
        viewStyleInt=viewStyleInt*10;
        switch (oudiaStation.getTimeShow(0)){
            case 1:
                viewStyleInt+=0;
                break;
            case 2:
                viewStyleInt+=2;
                break;
            case 3:
                viewStyleInt+=1;
                break;
        }
        viewStyle=viewStyleInt;
    }
    public RouteStation(JSONObject json){
        try {
            try {
                stationID = json.getInt(STATION_ID);
            }catch(Exception e){
                e.printStackTrace();
            }
            km=json.optDouble(KM,-1);
            numbering=json.optInt(NUMBERING);
            bigStation=json.optInt(TYPE)==1;
            viewStyle=json.optInt(VIEWSTYLE);
            border=json.optInt(BORDER)==1;

        }catch(Exception e){

        }

    }
    public JSONObject makeJSONObject (){
        JSONObject json=new JSONObject();
        try{
            if(stationID>-1){
                json.put(STATION_ID,stationID);
            }
            if(km>-1){
                json.put(KM,km);
            }
            if(numbering>-1){
                json.put(NUMBERING,numbering);
            }
            if(bigStation){
                json.put(TYPE,1);
            }else{
                json.put(TYPE,0);
            }
            if(viewStyle>-1){
                json.put(VIEWSTYLE,String.format("%02d",viewStyle));
            }
            if(border){
                json.put(BORDER,1);
            }else{
                json.put(BORDER,0);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }
    Element makeSujiTaroData(Document document,String stationName){
        Element element=document.createElement("駅明細");
        element.appendChild(createDom(document,"駅名",stationName));
        if(km<0){
            element.appendChild(createDom(document,"距離","0"));
        }else{
            element.appendChild(createDom(document,"距離",km+""));
        }
        if(viewStyle==0){
            element.appendChild(createDom(document,"到着","False"));
        }else{
            element.appendChild(createDom(document,"到着","True"));
        }
        element.appendChild(createDom(document,"出発","True"));

        element.appendChild(createDom(document,"線種",bigStation ? "2" : "0"));
        element.appendChild(createDom(document,"駅種別","0"));
        return element;
    }

    /**
     * 駅リストから指定された駅名を持つ駅インデックスを返す。
     * @param stations 駅リスト
     * @param stationName 指定駅名
     * @return stationsの配列中の何番目が指定駅であるかのインデックス
     */
    private OptionalInt findStationID(ArrayList<Station> stations,String stationName){
        return IntStream.range(0, stations.size())
                .filter(i -> stations.get(i).name.equals(stationName))
                .findFirst();
    }

    Element createDom(Document document,String tagName,String content){
        Element result=document.createElement(tagName);
        result.setTextContent(content);
        return result;

    }

}
