package kamelong.com.JPTI.JPTI;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 停留所情報を格納するクラス
 */
class Stop {
    /**
     例：1番線
     例：5番乗り場
     */
    String name="";
    /**
     * 停留所番号
     例：「1」番線
     例：「5」番乗り場
     */
    private int number=-1;
    /**
     例：大和・海老名方面
     例：110系統 杉田平和町行etc...
     */
    private String description=null;
    /**
     * 緯度
     */
    private String lat=null;
    /**
     * 経度
     */
    private String lon=null;
    /**
     * 運賃区間id
     */
    private int zoneID=-1;

    private static final String NAME="stop_name";
    private static final String NUMBER="stop_number";
    private static final String DESCRIPTION="stop_description";
    private static final String LAT="stop_lat";
    private static final String LON="stop_lon";
    private static final String ZONE_ID="zone_id";

    public Stop(){
    }
    public Stop(JSONObject json){
        try{
            name=json.optString(NAME);
            description=json.optString(DESCRIPTION);
            lat=json.optString(LAT);
            lon=json.optString(LON);
            zoneID=json.optInt(ZONE_ID,-1);
            number=json.optInt(NUMBER);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
            if(number>-1){
                json.put(NUMBER,number);
            }
            if(description!=null){
                json.put(DESCRIPTION,description);
            }
            if(lat!=null){
                json.put(LAT,lat);
            }
            if(lon!=null){
                json.put(LON,lon);
            }
            if(zoneID>0){
                json.put(ZONE_ID,zoneID);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }



}
