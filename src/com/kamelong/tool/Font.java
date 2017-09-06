package com.kamelong.tool;

import org.json.JSONObject;

public class Font {
    public static final Font OUDIA_DEFAULT=new Font("ＭＳ ゴシック",9,false,false);
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

    public Font(){

    }
    public Font(JSONObject json){
        name=json.optString(NAME);
        height=json.optInt(HEIGHT,-1);
        bold=json.optInt(BOLD)==1;
        itaric=json.optInt(ITARIC)==1;
    }
    public Font(String name,int height,boolean bold,boolean itaric){
        this.name=name;
        this.height=height;
        this.bold=bold;
        this.itaric=itaric;
    }
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
