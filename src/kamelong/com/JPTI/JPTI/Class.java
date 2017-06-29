package kamelong.com.JPTI.JPTI;

import kamelong.com.JPTI.OuDia.TrainType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;

/**
 * 列車種別のクラス
 */
public class Class {
    /**
     * 種別名
     */
    private String name="";
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

    private static final String NAME="class_name";
    private static final String SHORT_NAME="class_short_name";
    private static final String TEXT_COLOR="class_text_color";
    private static final String DIA_COLOR="class_dia_color";
    private static final String STYLE="class_dia_style";
    private static final String BOLD="class_dia_bold";
    private static final String SHOWSTOP="class_dia_showstop";

    public Class(TrainType trainType){
        name=trainType.getName();
        shortName=trainType.getShortName();
        textColor=trainType.getTextColor();
        diaColor=trainType.getDiaColor();

        diaStyle=trainType.getLineStyle();
        diaBold=trainType.getLineBold();
        showStop=trainType.getShowStop();
    }
    public JSONObject makeJSONObject(){
        JSONObject json=new JSONObject();
        try{
            json.put(NAME,name);
            if(shortName!=null){
                json.put(SHORT_NAME,shortName);
            }
            if(textColor!=null){
                json.put(TEXT_COLOR,"#"+Integer.toHexString(textColor.getRGB()));
            }
            if(diaColor!=null){
                json.put(DIA_COLOR,"#"+Integer.toHexString(diaColor.getRGB()));
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

        }catch(Exception e){
            e.printStackTrace();
        }
        return json;
    }

}
