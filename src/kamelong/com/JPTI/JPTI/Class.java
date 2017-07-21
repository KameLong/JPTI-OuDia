package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.TrainType;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.awt.*;

/**
 * 列車種別のクラス
 */
public class Class {
    /**
     * 種別名
     */
    String name="";
    /**
     * 種別略称
     */
    private String shortName=null;
    /**
    種別文字色
     */
    private Color textColor=null;
    /**
     * 種別ダイヤ色
     */
    private Color diaColor=null;

    /**
     * 種別ダイヤスタイル
     * 0:直線
     * 1:破線
     * 2:点線
     * 3:一点鎖線
     */
    private int diaStyle=0;
    /**
     * 種別ダイヤ太線
     */
    private boolean diaBold=false;
    /**
     * 種別ダイヤ停車駅明示
     */
    private boolean showStop=false;
    /**
     * 種別フォント
     */
    private int font=-1;

    private static final String NAME="class_name";
    private static final String SHORT_NAME="class_short_name";
    private static final String TEXT_COLOR="class_text_color";
    private static final String DIA_COLOR="class_dia_color";
    private static final String STYLE="class_dia_style";
    private static final String BOLD="class_dia_bold";
    private static final String SHOWSTOP="class_dia_showstop";
    private static final String FONT="class_font";

    public Class(TrainType trainType){
        name=trainType.getName();
        shortName=trainType.getShortName();
        textColor=trainType.getTextColor();
        diaColor=trainType.getDiaColor();

        diaStyle=trainType.getLineStyle();
        diaBold=trainType.getLineBold();
        showStop=trainType.getShowStop();
        font=trainType.fontNumber;
    }
    public Class(JSONObject json){
        try{
            try{
                name=json.getString(NAME);
            }catch(Exception e){
                e.printStackTrace();
            }
            try{
                String color=json.optString(TEXT_COLOR,"#000000");
                if(color.length()==9){
                    color="#"+color.substring(3);
                }

                textColor = Color.decode(color);
            }catch(Exception e){
                e.printStackTrace();
            }
            shortName=json.optString(SHORT_NAME);
            try{
                String color=json.optString(TEXT_COLOR,"#000000");
                if(color.length()==9){
                    color="#"+color.substring(3);
                }

                textColor = Color.decode(color);
            }catch(Exception e) {
            }
            diaStyle=json.optInt(STYLE);
            if(json.optInt(BOLD)==1){
                diaBold=true;
            }
            if(json.optInt(SHOWSTOP)==1){
                showStop=true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
            if(shortName!=null){
                json.put(SHORT_NAME,shortName);
            }
            if(textColor!=null){
                json.put(TEXT_COLOR,"#"+Integer.toHexString(textColor.getRGB()%0x1000000));
            }
            if(diaColor!=null){
                json.put(DIA_COLOR,"#"+Integer.toHexString(diaColor.getRGB()%0x1000000));
            }
            json.put(STYLE,diaStyle);
            if(diaBold){
                json.put(BOLD,1);
            }else{
                json.put(BOLD,0);
            }
            if(showStop){
                json.put(SHOWSTOP,1);
            }else{
                json.put(SHOWSTOP,0);
            }
            if(font>-1){
                json.put(FONT,font);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }
    void makeSujiTaroData(Document document,Element element){
        Element trainTypeDetail=document.createElement("列車種別詳細");
        trainTypeDetail.appendChild(createDom(document,"列車種別名",name));
        trainTypeDetail.appendChild(createDom(document,"描画色",textColor.getAlpha()+"-"+textColor.getRed()+"-"+textColor.getGreen()+"-"+textColor.getBlue()));
        if(diaBold) {
            trainTypeDetail.appendChild(createDom(document, "線種","2"));
        }else{
            trainTypeDetail.appendChild(createDom(document, "線種","1"));

        }
        trainTypeDetail.appendChild(createDom(document,"停車通過","0"));
        element.appendChild(trainTypeDetail);

    }
    Element createDom(Document document,String tagName,String content){
        Element result=document.createElement(tagName);
        result.setTextContent(content);
        return result;

    }


}
