package kamelong.com.JPTI.OuDia;

import java.awt.Color;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by kame on 2017/02/16.
 */

public class OuDiaDiaFile extends DiaFile {
    /**
     * 推奨コンストラクタ。
     * @param file 開きたいファイル @code null then サンプルファイルを開く
     *
     * コンストラクタでは読み込みファイルが与えられるので、そのファイルを読み込む。
     * 読み込む処理はloadDia,loadNetgramに書かれているので適宜呼び出す。
     * oudファイルはShiftJisで書かれているので考慮する必要がある。
     *
     * ダイヤを読み込んだ後に最小所要時間を別スレッドで作成する。
     * 最小所要時間を必要とする際は、この計算が終了しているかどうかをチェックする必要がある。
     *
     * fileがnullの時はnullPointerExceptionが発生するため、サンプルを読み込む。
     * その他のエラーが発生した際にはToastでエラーを吐いた後、アプリを終了する。
     *
     */
    public OuDiaDiaFile(File file){
        try {
            FileInputStream is = new FileInputStream(file);
            if(file.getPath().endsWith(".oud")||file.getPath().endsWith(".oud2")){
                InputStreamReader filereader = new InputStreamReader(is, "Shift_JIS");
                BufferedReader br = new ShiftJISBufferedReader(filereader);
                loadDia(br);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * oudファイルを読み込んでオブジェクトを構成する。
     * @param br  BufferReader of .oud fille.  forbidden @null
     */
    private void loadDia(BufferedReader br){
        int diaNum=-1;
        try{
            String line="";
            while((line=br.readLine())!=null) {
                if(line.equals("Dia.")){
                    line=br.readLine();
                    diaName.add(line.split("=",-1)[1]);
                    ArrayList<Train>[] trainArray=new ArrayList[2];
                    trainArray[0]=new ArrayList<Train>();
                    trainArray[1]=new ArrayList<Train>();
                    while(!line.equals(".")){
                        if(line.equals("Ressya.")){
                            int direct=0;
                            Train t=new Train(this);
                            while(!line.equals(".")){

                                if(line.split("=",-1)[0].equals("Houkou")){
                                    if(line.split("=",-1)[1].equals("Kudari")){
                                        direct=0;
                                    }
                                    if(line.split("=",-1)[1].equals("Nobori")){
                                        direct=1;
                                    }
                                }

                                if(line.split("=",-1)[0].equals("Syubetsu")) {
                                    t.setType(Integer.parseInt(line.split("=",-1)[1]));
                                }
                                if(line.split("=",-1)[0].equals("Ressyamei")) {
                                    t.setName(line.split("=",-1)[1]);
                                }
                                if(line.split("=",-1)[0].equals("Gousuu")) {
                                    t.setCount(line.split("=",-1)[1]);
                                }
                                if(line.split("=",-1)[0].equals("Ressyabangou")) {
                                    t.setNumber(line.split("=",-1)[1]);
                                }
                                if(line.split("=",-1)[0].equals("Bikou")) {
                                    t.setRemark(line.split("=",-1)[1]);
                                }

                                if(line.split("=",-1)[0].equals("EkiJikoku")) {
                                    try {
                                        setTrainTime(t,line.split("=",-1)[1], direct);
                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }
                                }

                                line=br.readLine();
                            }
                            if(direct!=-1) {
                                trainArray[direct].add(t);
                            }
                        }
                        line=br.readLine();
                        //Diaの終わりは２つの終了行が並んだ時
                        if(line.equals(".")){
                            line=br.readLine();
                        }
                    }
                    trainArray[0].trimToSize();
                    trainArray[1].trimToSize();
                    train.add(trainArray);
                }
                if(line.equals("Ressyasyubetsu.")){
                    TrainType mTrainType=new TrainType();
                    while(!line.equals(".")){
                        if(line.split("=",-1)[0].equals("Syubetsumei")){
                            mTrainType.setName(line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("Ryakusyou")){
                            mTrainType.setShortName(line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("JikokuhyouMojiColor")){
                            setTrainTypeTextColor(mTrainType,line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("DiagramSenColor")){
                            setTrainTypeDiaColor(mTrainType,line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("DiagramSenStyle")){
                            mTrainType.setLineStyle(line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("DiagramSenIsBold")){
                            mTrainType.setLineBold(line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("StopMarkDrawType")){
                            mTrainType.setShowStop(line.split("=",-1)[1]);
                        }
                        line=br.readLine();
                    }
                    trainType.add(mTrainType);
                }
                if(line.equals("Eki.")){
                    Station mStation=new Station();
                    while(!line.equals(".")){
                        if(line.split("=",-1)[0].equals("Ekimei")){
                            mStation.setName(line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("Ekijikokukeisiki")){
                            setStationTimeShow(mStation,line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("Ekikibo")){
                            setStationSize(mStation,line.split("=",-1)[1]);
                        }
                        if(line.split("=",-1)[0].equals("Kyoukaisen")){
                            mStation.setBorder(Integer.valueOf(line.split("=",-1)[1]));
                        }
                        line=br.readLine();
                    }
                    station.add(mStation);
                }
                if(line.equals("Rosen.")){
                    line=br.readLine();
                    lineName=line.split("=",-1)[1];
                }
                if(line.split("=",-1)[0].equals("Comment")){
                    comment=line.split("=",-1)[1];
                    comment=comment.replace("\\n","\n");
                }
            }
        }catch(Exception e1){
            e1.printStackTrace();
        }
        if(checkDiaFile()){

        }else{
        }
    }
    /**
     * OuDiaのEkikiboの文字列から駅規模を入力する。
     * @param value OuDiaファイル内のEkikiboの文字列
     */
    public void setStationSize(Station station,String value){
        switch (value){
            case "Ekikibo_Ippan":
                station.setSize(0);
                break;
            case "Ekikibo_Syuyou":
                station.setSize(1);
                break;

            case "0":
                station.setSize(0);
                break;
            case "1":
                station.setSize(1);
                break;
        }
    }
    /**
     * OuDiaのJikokukeisikiの文字列から時刻表示形式を入力する。
     * @param value OuDiaファイル内のJikokukeisikiの文字列
     */
    public void setStationTimeShow(Station station,String value){
        switch (value){
            case "Jikokukeisiki_Hatsu":
                station.setTimeShow(5);
                break;
            case "Jikokukeisiki_Hatsuchaku":
                station.setTimeShow(15);
                break;
            case "Jikokukeisiki_NoboriChaku":
                station.setTimeShow(9);
                break;
            case "Jikokukeisiki_KudariChaku":
                station.setTimeShow(6);
                break;
            case "Jikokukeisiki_KudariHatsuchaku":
                station.setTimeShow(7);
                break;
            case "Jikokukeisiki_NoboriHatsuchaku":
                station.setTimeShow(13);
                break;

        }
    }
    /**
     * この列車の発着時刻を入力します。
     * oudiaのEkiJikoku形式の文字列を発着時刻に変換し、入力していきます。
     * @param str　oudiaファイル　EkiJikoku=の形式の文字列
     * @param direct　方向
     */
    public void setTrainTime(Train train,String str,int direct){
        try {
            String[] timeString = str.split(",");
            for (int i = 0; i < timeString.length; i++) {
                if (timeString[i].length() == 0) {
                    train.setStopType((1 - 2 * direct) * i + direct * (getStationNum()- 1), Train.STOP_TYPE_NOSERVICE);
                } else {
                    if (!timeString[i].contains(";")) {
                        train.setStopType((1 - 2 * direct) * i + direct * (getStationNum() - 1),Integer.parseInt(timeString[i]));
                    } else {
                        train.setStopType((1 - 2 * direct) * i + direct * (getStationNum()- 1), Integer.parseInt(timeString[i].split(";")[0]));
                        try {
                            String stationTime = timeString[i].split(";")[1];
                            if (!stationTime.contains("/")) {
                                train.setDepartTime((1 - 2 * direct) * i + direct * (getStationNum() - 1), stationTime);
                            } else {
                                if( stationTime.split("/").length==2) {
                                    train.setArriveTime((1 - 2 * direct) * i + direct * (getStationNum() - 1), stationTime.split("/")[0]);
                                    train.setDepartTime((1 - 2 * direct) * i + direct * (getStationNum()- 1), stationTime.split("/")[1]);
                                }else{
                                    train.setArriveTime((1 - 2 * direct) * i + direct * (getStationNum()- 1), stationTime.split("/")[0]);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    /**
     * 時刻表文字色をセットする
     *  oudiaファイルでの色表記は"aabbggrr"の8文字の文字列
     *  netgramの色表記は"#rrggbb"の7文字の文字列
     *              これらの違いを踏まえつつ、int型の色を作成します。
     * @param color 色を表す文字列
     */
    public void setTrainTypeTextColor(TrainType type,String color){
            int blue=Integer.parseInt(color.substring(2,4),16);
            int green=Integer.parseInt(color.substring(4,6),16);
            int red=Integer.parseInt(color.substring(6,8),16);
            type.setTextColor(new Color(red,green,blue));
    }
    /**
     * ダイヤグラム文字色をセットする
     *  oudiaファイルでの色表記は"aabbggrr"の8文字の文字列
     *  netgramの色表記は"#rrggbb"の7文字の文字列
     *              これらの違いを踏まえつつ、int型の色を作成します。
     * @param color 色を表す文字列
     */
    public void setTrainTypeDiaColor(TrainType type,String color) {
            int blue=Integer.parseInt(color.substring(2,4),16);
            int green=Integer.parseInt(color.substring(4,6),16);
            int red=Integer.parseInt(color.substring(6,8),16);
            type.setDiaColor(new Color(red,green,blue));
    }


}
