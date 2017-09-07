package com.kamelong.OuDia;

/**
 * 列車データを格納するクラス。
 * 一つの列車に関するデータはここに格納する
 * Stationクラスには全種類のダイヤ形式で統一できる入力と、出力を書く。
 * それぞれのダイヤ形式に合わせた変換はxxxDiaFileクラスに記述する
 * @author  KameLong
 */
public class OuDiaTrain {
    /**
     * 駅扱いの定数。long timeの9~12bitがstop typeに対応する。
     */
    public static final int STOP_TYPE_STOP=1;
    public static final int STOP_TYPE_PASS=2;
    public static final int STOP_TYPE_NOSERVICE=0;
    public static final int STOP_TYPE_NOVIA=3;
    /**
     * 列車種別
     */
    protected int type = 0;
    /**
     * 列車番号
     */
    protected String number ="";
    /**
     * 列車名
     */
    protected String name="";
    /**
     * 号数
     */
    protected String count="";
    /**
     * 備考
     */
    protected String remark="";
    /**
     * １列車の駅依存の情報を格納する。
     * このデータは駅数分できるため、サイズの大きいオブジェクトはメモリを圧迫します。
     * 省メモリのため文字列などを用いず、すべてlongで表記します。
     * longは64bitなので、各ビットごとに役割を持たせたいます。
     * 先頭より
     * 8bit フラグエリア：どの情報が存在するのかを示したもの（1:存在する,0:存在しない)
     *       [free,free,着時刻の存在,発時刻の存在,free,free,free,free]
     * 4bit 駅扱いを記述する。この4bitの値がそのままstopTypeとなる
     * 12bit 空き領域(free)
     * 20bit 着時刻（秒単位）
     * 20bit 発時刻（秒単位）
     */

    protected long time[];
    /**
     * この列車が所属するDiaFile
     */
    protected OuDiaFile diaFile;
    protected OuDiaTrain(){}
    /**
     * 列車の生成には所属するDiaFileが必要となります。
     * @param diaFile　呼び出し元のDiaFile
     */
    public OuDiaTrain(OuDiaFile diaFile) {
        this.diaFile=diaFile;
        try {
            time = new long[diaFile.getStationNum()];
            for (int i = 0; i < time.length; i++) {
                time[i] = 0;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * OuDia保存用の文字列を作成する
     * @param direct
     * @return
     */
    public StringBuilder makeTrainText(int direct){
        StringBuilder result=new StringBuilder("Ressya.\r\n");
        if(direct==0) {
            result.append("Houkou=Kudari\r\n");
        }else{
            result.append("Houkou=Nobori\r\n");
        }
        result.append("Syubetsu=").append(type).append("\r\n");
        if(number.length()>0){
            result.append("Ressyabangou=").append(number).append("\r\n");
        }
        if(name.length()>0){
            result.append("Ressyamei=").append(name).append("\r\n");
        }
        if(count.length()>0){
            result.append("Gousuu=").append(name).append("\r\n");
        }
        if(remark.length()>0){
            result.append("Bikou=").append(name).append("\r\n");
        }
        result.append("EkiJikoku=");
        for(int i=0;i<diaFile.getStationNum();i++){
            int stationIndex=i;
            if(direct==1){
                stationIndex=diaFile.getStationNum()-1-i;
            }
            result.append(makeStationTimeTxt(stationIndex)).append(",");
        }
        result.append("\r\n.\r\n");
        return result;
    }
    private String makeStationTimeTxt(int stationIndex){
        if(getStopType(stationIndex)==0){
            return "";
        }
        String result=""+getStopType(stationIndex);
        if(!timeExist(stationIndex)){
            return result;
        }
        result+=";";
        if(arriveExist(stationIndex)){
            result+=timeInt2String(getArriveTime(stationIndex))+"/";
        }
        if (departExist(stationIndex)) {

            result+=timeInt2String(getDepartureTime(stationIndex));
        }
        return result;
    }
    private String timeInt2String(int time){
        String hh=String.valueOf((time/3600)%24);
        String mm=String.format("%02d",(time/60)%60);
        String ss=String.format("%02d",time%60);
        if(time%60==0){
            return hh+mm;
        }else{
            return hh+mm+"-"+ss;

        }
    }

    /**
     * 列車番号を設定します
     * @param value　列車番号
     */
    public void setNumber(String value){
        try {
            if (value.length() == 0) {
                number = "";
                return;
            }
            number = value;
        }catch(Exception e){
            e.printStackTrace();
        }
        return;
    }

    /**
     * 列車種別を設定します
     * @param value　列車種別(int)
     */
    public void setType(int value) {
        if (value < 0) {
            //列車種別は負の数が許されていません
            type = 0;
            return;
        }
        type = value;
    }


    /**
     * 指定駅の着時刻を取得します。
     * データは秒単位のintで返ります。
     * 着時刻が存在しないとき発時刻を返します。
     * 発時刻も存在しないときは-1を返します。
     * 何らかのエラーが生じた際は-2を返します。
     * @param station　指定駅番号　null禁止
     * @return　着時刻(秒)
     */

    public int getArriveTime(int station){
        try {
            if ((time[station] & 0x2000000000000000L) == 0) {
                if ((time[station] & 0x1000000000000000L) == 0) {
                    return -1;
                }
                return getDepartureTime(station);
            }
            long result = time[station] & 0x0000ffffff000000L;
            result = result >>> 24;
            return (int) result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -2;
    }
    /**
     * 指定駅の発時刻を取得します。
     * データは秒単位のintで返ります。
     * 発時刻が存在しないとき着時刻を返します。
     * 着時刻も存在しないときは-1を返します。
     * 何らかのエラーが生じた際は-2を返します。
     * @param station　指定駅番号　null禁止
     * @return　発時刻(秒)
     */
    protected int getDepartureTime(int station){
        try {
            if ((time[station] & 0x1000000000000000L) == 0) {
                if ((time[station] & 0x2000000000000000L) == 0) {
                    return -1;
                }
                return getArriveTime(station);
            }
            long result = time[station] & 0x00000000ffffffL;
            return (int) result;
        }catch(Exception e){
            e.printStackTrace();
        }
        return -2;
    }

    /**
     * 駅扱いをセットする。
     * @param station　駅インデックス
     * @param value　停車駅扱い番号
     */

    public void setStopType(int station,int value){
        if(value>16||value<0){
            //error
            return;
        }
        if(value==8||value==9){
            value=STOP_TYPE_STOP;
        }
        long type=(long)value;
        type=type<<56;
        time[station]=time[station]&0xF0FFFFFFFFFFFFFFL;
        time[station]=time[station]|type;
    }

    /**
     * 着時刻をセットする。
     * 0:00,10:34,10:3420,000,1034,103420などの形式に対応。
     * 文字列を秒単位に変換してtime[]にセットします
     * @param station　駅インデックス
     * @param str 着時刻を文字列にしたもの
     */
    public void setArriveTime(int station,String str){
        if(station<0||station>=time.length){
            return;
        }
        long result=0;//minutes
        int h;
        int m;
        int s;
        if(str.length()==0){
            return;
        }
        if(str.equals("null")){
            return;
        }
        if(str.indexOf(":")<0) {
            //no ":" char so str is only number
            switch (str.length()) {
                case 3:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(1, 3));
                    result = h * 3600 + m*60;
                    break;
                case 4:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    result = h * 3600 + m*60;
                    break;
                case 5:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(1, 3));
                    s = Integer.parseInt(str.substring(3, 5));
                    result = h * 3600 + m*60+s;
                    break;
                case 6:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    s = Integer.parseInt(str.substring(4, 6));
                    result = h * 3600 + m*60+s;
                    break;
                default:
                    result = -1;

            }
        }else{
            //this str inclues ":" for example 12:17
            switch (str.length()) {
                case 4:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    result = h * 3600 + m*60;
                    break;
                case 5:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(3, 5));
                    result = h * 3600 + m*60;
                    break;
                case 6:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    s = Integer.parseInt(str.substring(4, 6));
                    result = h * 3600 + m*60+s;
                    break;
                case 7:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(3, 5));
                    s = Integer.parseInt(str.substring(5, 7));
                    result = h * 3600 + m*60+s;
                    break;
                default:
                    result = -1;

            }
        }
        if(result>0) {
            time[station] = time[station] & 0xDFFF000000FFFFFFL;
            time[station] = time[station] | 0x2000000000000000L;
            time[station] = time[station] | (result<<24);
        }
    }
    /**
     * 発時刻をセットする。
     * 0:00,10:34,10:3420,000,1034,103420などの形式に対応。
     * 文字列を秒単位に変換してtime[]にセットします
     * @param station　駅インデックス
     * @param str 発時刻を文字列にしたもの
     */

    public void setDepartTime(int station,String str){
        if(station<0||station>=time.length){
            return;
        }

        long result=0;//minutes
        int h;
        int m;
        int s;
        if(str.length()==0){
            return;
        }
        if(str.equals("null")){
            return;
        }

        if(str.indexOf(":")<0) {
            //no ":" char so str is only number
            switch (str.length()) {
                case 3:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(1, 3));
                    result = h * 3600 + m*60;
                    break;
                case 4:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    result = h * 3600 + m*60;
                    break;
                case 5:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(1, 3));
                    s = Integer.parseInt(str.substring(3, 5));

                    result = h * 3600 + m*60+s;
                    break;
                case 6:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    s = Integer.parseInt(str.substring(4, 6));

                    result = h * 3600 + m*60+s;
                    break;
                default:
                    result = -1;

            }
        }else{
            //this str inclues ":" for example 12:17
            switch (str.length()) {
                case 4:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    result = h * 3600 + m*60;
                    break;
                case 5:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(3, 5));
                    result = h * 3600 + m*60;
                    break;
                case 6:
                    h = Integer.parseInt(str.substring(0, 1));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(2, 4));
                    s = Integer.parseInt(str.substring(4, 6));
                    result = h * 3600 + m*60+s;
                    break;
                case 7:
                    h = Integer.parseInt(str.substring(0, 2));
                    if (h < 3) {
                        h = h + 24;
                    }
                    m = Integer.parseInt(str.substring(3, 5));
                    s = Integer.parseInt(str.substring(4, 6));
                    result = h * 3600 + m*60+s;
                    break;
                default:
                    result = -1;

            }
        }
        if(result>0) {
            time[station] = time[station] & 0xEFFFFFFFFF000000L;
            time[station] = time[station] | 0x1000000000000000L;
            time[station] = time[station] | (result);
        }
    }
    public int getStopType(int station) {
        if(station<0||station>=time.length){
            return 5;
        }
        long result = time[station] & 0x0F00000000000000L;
        result = result >>> 56;
        if (result < 4) {
            return (int) result;
        } else {
            return 5;
        }
    }
    /**
     * 着時刻が存在する時trueを返す。
     * 駅インデックスが範囲外の時はfalseを返す
     * @param station　駅インデックス
     * @return　着時刻存在フラグ
     */
    public boolean arriveExist(int station){
        if(station<0||station>=time.length){
            return false;
        }
        return (time[station] & 0x2000000000000000L) != 0;
    }
    /**
     * 発時刻が存在する時trueを返す。
     * 駅インデックスが範囲外の時はfalseを返す
     * @param station　駅インデックス
     * @return　発時刻存在フラグ
     */
    public boolean departExist(int station) {
        if(station<0||station>=time.length){
            return false;
        }
        return (time[station] & 0x1000000000000000L) != 0;
    }
    /**
     * 時刻の存在。
     * 発時刻と着時刻のどちらかが存在する時trueを返す。
     * 駅インデックスが範囲外の時はfalseを返す
     * @param station　駅インデックス
     * @return　時刻存在フラグ
     */

    public boolean timeExist(int station) {
        if(station<0||station>=time.length){
            return false;
        }
        return (time[station] & 0x3000000000000000L) != 0;
    }

    /**
     * 列車名をセットします。
     * @param value　列車名文字列
     */
    public void setName(String value){
        name=value;
    }

    /**
     * 列車名を取得します
     * @return　列車名
     */
    public String getName(){
        return name;
    }

    /**
     * 号数をセットします
     * @param value　号数文字列
     */
    public void setCount(String value){
        if(value.length()>0){
            count=value+"号";
        }
    }

    /**
     * 備考をセットします
     * @param value　備考文字列
     */
    public void setRemark(String value){
        remark=value;
    }





}
