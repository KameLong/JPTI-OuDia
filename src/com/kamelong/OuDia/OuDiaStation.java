package com.kamelong.OuDia;

/**
 *
 */
public class OuDiaStation {
    /**
     * 駅名。
     */
    protected String name="";
    /**
     * 発時刻、着時刻の表示非表示を管理する整数。
     * 4bitで記述し
     * 上り着、上り発、下り着、下り発
     * の順でバイナリ記述する。
     * この形を用いることであらゆるパターンの表示を可能とするであろう
     *
     * SHOW_XXXの形の定数はよく使われる発着表示のパターンを定数にしたもの
     */
    protected int timeShow=SHOW_HATU;

    public static final int SHOW_HATU=5;
    public static final int SHOW_HATUTYAKU=15;
    public static final int SHOW_KUDARITYAKU=6;
    public static final int SHOW_NOBORITYAKU=9;
    public static final int SHOW_KUDARIHATUTYAKU=7;
    public static final int SHOW_NOBORIHATUTYAKU=13;


    /**
     * 駅規模。
     */
    protected int size=SIZE_NORMAL;
    static final int SIZE_NORMAL=0;
    static final int SIZE_BIG=1;

    /**
     * 境界駅を示す。
     * 境界駅の場合1、境界駅ではない場合0が入る。
     */
    protected int border;

    /**
     * 発着表示を取得する際に使う定数
     */
    public static final int STOP_DEPART=0;
    public static final int STOP_ARRIVE=1;


    public StringBuilder makeStationText(boolean oudiaSecond){
        StringBuilder result=new StringBuilder("Eki.");
        result.append("\r\nEkimei=").append(name);
        switch (this.timeShow) {
            case SHOW_HATU:
                result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_Hatsu");
                break;
            case SHOW_HATUTYAKU:
                result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_Hatsuchaku");
                break;
            case SHOW_KUDARITYAKU:
                result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_KudariChaku");
                break;
            case SHOW_NOBORITYAKU:
                result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_NoboriChaku");
                break;
            case SHOW_KUDARIHATUTYAKU:
                if(oudiaSecond) {
                    result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_KudariHatsuchaku");
                }else {
                    result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_Hatsu");
                }
                break;
            case SHOW_NOBORIHATUTYAKU:
                if(oudiaSecond) {
                    result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_NoboriHatsuchaku");
                }else {
                    result.append("\r\nEkijikokukeisiki=").append("Jikokukeisiki_Hatsu");
                }
                break;
        }
        switch (size) {
            case 0:
                result.append("\r\nEkikibo=").append("Ekikibo_Ippan");
                break;
            case 1:
                result.append("\r\nEkikibo=").append("Ekikibo_Syuyou");
                break;
        }

        if(border==1){
        result.append("\r\nKyoukaisen=1");
        }
        result.append("\r\n.\r\n");
        return result;

    }
    protected void setName(String value){
        if(value.length()>0){
            name=value;
        }
    }


    /**
     * timeShowをセットする。
     * @param value timeShowを表す整数　0<=value<16
     */
    protected void setTimeShow(int value){
        if(value>0&&value<16){
            timeShow=value;
            return;
        }
        //error
    }

    /**
     * 境界駅をセットする。
     * @param value 境界駅の場合1、境界駅ではない場合0
     */
    public void setBorder(int value){
        border=value;
    }

    /**
     * 駅規模を入力する。
     * @param value SIZE_NORMALかSIZE_BIG
     */
    protected void setSize(int value){
        if(value<2&&value>0) {
            size = value;
        }
    }

}
