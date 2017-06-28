package kamelong.com.JPTI.OuDia;

import java.util.ArrayList;

/**
 * @author  KameLong
 * oudiaの路線データを格納するクラス。
 * 一つのoudiaファイルは一つのDiaFileに対応する
 * 内部に複数のダイヤを格納することができるが、駅リスト、種別リストは一つしか持てない
 *
 * v0.9追記
 * oudiaに限らず、他形式のデータに関してもこのクラスで保持する。
 * 将来的には抽象クラス化する必要があるかも。
 * 当面はこのままデータを保持します。
 */

abstract public class DiaFile{
    /**
     *路線名。
     */
    protected String lineName="";
    /**
     * ダイヤ名。
     * 一ファイル内に複数のダイヤを格納することができるためArrayListを用いる
     */
    protected ArrayList<String> diaName=new ArrayList<String>();
    /**
     * 駅
     * 駅の数は不定
     */
    protected ArrayList<Station> station=new ArrayList<Station>();
    /**
     * 種別
     * 種別の数は不定
     */
    protected ArrayList<TrainType> trainType=new ArrayList<TrainType>();
    /**
     * Trainは1本の列車に相当する
     * 最初のArrayListはダイヤの数に相当する
     * ArrayListの中に配列があるが、これは上りと下りの２つ(確定)の時刻表があるため、配列を用いている
     * 配列の内部に再びArrayListがあるが、これは各時刻表中の列車の本数分の大きさを持つ
     */
    protected ArrayList<ArrayList<Train>[]> train=new ArrayList<ArrayList<Train>[]>();
    /**
     * コメント。
     * oudiaデータには路線ごとにコメントがついている。
     * ダイヤごとにコメントをつけたい場合はArrayListに拡張しないといけない。
     */
    protected String comment="";
    /**
     * ダイヤグラム起点時刻。
     * 今は3:00に固定されているが、oudiaに設定項目がある以上機能追加を考えたほうがよい。
     */
    protected int diagramStartTime=10800;
    /**
     * 最小所要時間
     */
    protected ArrayList<Integer>stationTime=new ArrayList<Integer>();

    /**
     * このオブジェクトの生成に成功したかチェックする。
     * チェックポイント
     * １、駅数が０ではいけない
     * ２、種別数が０ではいけない
     * ３、ダイヤ数が０ではいけない
     * @return 修正した場合falseを返す、修正が必要ないときtrue
     */
    protected boolean checkDiaFile(){
        boolean result=true;
        if(station.size()==0){
            station.add(new Station());
            result=false;
        }
        if(trainType.size()==0){
            trainType.add(new TrainType());
            result=false;
        }
        if(train.size()==0){
            ArrayList<Train>[] trainArray=new ArrayList[2];
            trainArray[0]=new ArrayList<Train>();
            trainArray[1]=new ArrayList<Train>();
            train.add(trainArray);
            result=false;
        }
        return result;
    }

    /**
     * 路線名を返す。
     * @return menber lineName
     */
    public String getLineName(){
        return lineName;
    }

    /**
     * 駅数を返す。
     * @return size of station(ArrayList)
     */
    public int getStationNum(){
        return station.size();
    }

    /**
     * 主要駅を返す。
     * 現在未使用
     * @return sum of main station
     */
    public int getMainStationNum(){
        int result=0;
        for(int i=0;i<station.size();i++){
            if(station.get(i).getBigStation()){
                result++;
            }
        }
        return result;
    }

    /**
     * 駅名を返す。
     * stationNumが範囲外の場合は空文字列を返す
     * getStation(stationNum).getName();とほぼ同じ機能
     * @param stationNum index of station, stationNum>=0
     * @return station name
     */
    public String getStationName(int stationNum){
        try{
            return station.get(stationNum).getName();
        }catch(Exception e){
            e.printStackTrace();
            return "";
        }
    }
    /**
     * この路線の全ての駅のリストを返す
     */
    public String[] getStationNameList(){
        String[] result=new String[station.size()];
        for(int i=0;i<station.size();i++){
            result[i]=getStationName(i);
        }
        return result;
    }


    /**
     * 駅を返す。
     * stationNumが範囲外の場合は空の駅を返す
     * @param stationNum index of station, 0<=stationNum<size of station
     * @return Station
     */
    public Station getStation(int stationNum){
        try{
            return station.get(stationNum);
        }catch(Exception e){
            e.printStackTrace();
            return new Station();
        }
    }

    /**
     * 種別を返す。
     * numが範囲外の場合は空の種別を返す
     * @param num index of traintype, 0<=num<size of trainType
     * @return TrainType
     */
    public TrainType getTrainType(int num){
        try{
            return trainType.get(num);
        }
        catch(Exception e){
            e.printStackTrace();
            return new TrainType();
        }
    }

    /**
     * 列車数を返す。
     * 何らかのエラーが発生した場合は迷わず0を返す
     * @param diaNum index of dia
     * @param direct down=0,up=1
     * @return number of selected timetable's train
     */
    public int getTrainNum(int diaNum,int direct){
        try {
            return train.get(diaNum)[direct].size();
        }catch(Exception e){
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 列車を返す。
     * @param diaNum index of dia
     * @param direct down=0,up=1
     * @param trainNum index of train
     * @return Train
     */
    public Train getTrain(int diaNum,int direct,int trainNum){
        try {
            return train.get(diaNum)[direct].get(trainNum);
        }catch(Exception e){
            e.printStackTrace();
            return new Train(this);
        }
    }

    /**
     * 種別の数を返す。
     * @return size of trainTyle
     */
    public int getTypeNum(){
        return trainType.size();
    }

    /**
     * ダイヤ数を返す。
     * @return size of train
     */
    public int getDiaNum(){
        return train.size();
    }

    /**
     * ダイヤ名を返す
     * @param diaN index of dia
     * @return name of dia
     */
    public String getDiaName(int diaN){
        try {
            return diaName.get(diaN);
        }catch(Exception e){
            e.printStackTrace();
            return "e";
        }
    }
    /**
     * 基準運転時間が定義されている時に最小所要時間を返す
     *
     */
    public int getMinReqiredTime2(int diaNum,int startStation,int endStation) {
        int result=360000;
            for(int train=0;train<this.train.get(diaNum)[0].size();train++){
                int value=this.getTrain(diaNum,0,train).getRequiredTime(startStation,endStation);
                if(value>0&&result>value){
                    result=value;
                }
            }
            for(int train=0;train<this.train.get(diaNum)[1].size();train++){
                int value=this.getTrain(diaNum,1,train).getRequiredTime(startStation,endStation);
                if(value>0&&result>value){
                    result=value;
                }
        }
        if(result==360000){
            result=120;
        }
        return result;

    }
    /**
     *  駅間最小所要時間を返す。
     *  startStatioin endStationの両方に止まる列車のうち、
     *  所要時間（着時刻-発時刻)の最も短いものを秒単位で返す。
     *  ただし、駅間所要時間が90秒より短いときは90秒を返す。
     *
     *  startStation endStationは便宜上区別しているが、順不同である。
     * @param startStation
     * @param endStation
     * @return time(second)
     */
    public int getMinReqiredTime(int startStation,int endStation){
        int result=360000;
        for(int i=0;i<getDiaNum();i++){
            if(getDiaName(i).equals("基準運転時分")){
                return getMinReqiredTime2(i,startStation,endStation);
            }
        }
        for(int i=0;i<this.train.size();i++){

            for(int train=0;train<this.train.get(i)[0].size();train++){
                int value=this.getTrain(i,0,train).getRequiredTime(startStation,endStation);
                if(value>0&&result>value){
                    result=value;
                }
            }
            for(int train=0;train<this.train.get(i)[1].size();train++){
                int value=this.getTrain(i,1,train).getRequiredTime(startStation,endStation);
                if(value>0&&result>value){
                    result=value;
                }
            }
        }
        if(result==360000){
            result=120;
        }
        if(result<90){
            result=90;
        }

        return result;
    }

    /**
     * コメントの文字列を返す。
     * @return コメント
     */
    public String getComment(){
        return comment;
    }

    /**
     * ダイヤグラム基準時間を返す。
     * @return
     */
    public int getDiagramStartTime(){
        return diagramStartTime;
    }


}
