package kamelong.com.JPTI.JPTI;

import org.json.JSONObject;

/**
 * Created by kame on 2017/06/29.
 */
public class Calendar {
    String name="";

    private static final String NAME="calendar_name";
    public Calendar(){}
    public Calendar(JSONObject json){
        try{
            name=json.optString(NAME,"");

        }catch(Exception e){

        }
    }
    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }


}
