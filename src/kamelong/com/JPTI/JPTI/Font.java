package kamelong.com.JPTI.JPTI;

import org.json.JSONObject;

public class Font{
    /**
     * フォント高さ
     */
    public int height=-1;
    /**
     * フォント名
     */
    public String name=null;
    /**
     * 太字なら１
     */
    public boolean bold=false;
    /**
     * 斜体なら１
     */
    public boolean itaric=false;

    private static final String HEIGHT="height";
    private static final String NAME="facename";
    private static final String BOLD="bold";
    private static final String ITARIC="itaric";

    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            if(height>-1){
                json.put(HEIGHT,height);
            }
            if(name!=null){
                json.put(NAME,name);
            }
            if(bold){
                json.put(BOLD,1);
            }
            if(itaric){
                json.put(ITARIC,1);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }



}
