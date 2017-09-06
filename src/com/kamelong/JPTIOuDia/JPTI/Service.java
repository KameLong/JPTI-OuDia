package com.kamelong.JPTIOuDia.JPTI;

import com.kamelong.JPTI.JPTIdata;
import com.kamelong.JPTIOuDia.OuDia.OuDiaFile;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * JPTI-OuDia用のServiceクラス
 * OuDiaとの変換機能を実装している
 */
public class Service extends com.kamelong.JPTI.Service{
    public Service(JPTIdata jpti){
        super(jpti);
    }
    public Service(JPTIdata jpti, JSONObject json){
        super(jpti,json);
    }

    public ArrayList<Integer> loadOuDia(OuDiaFile diaFile){
        name=diaFile.getLineName();
        stationWidth=diaFile.getStationNameLength();
        trainWidth=diaFile.getTrainWidth();
        startTime=timeInt2String(diaFile.getStartTime());
        defaulyStationSpace=diaFile.getStationDistanceDefault();
        comment=diaFile.getComment();
        diaTextColor=diaFile.getDiaTextColor();
        diaBackColor=diaFile.getBackGroundColor();
        diaTrainColor=diaFile.getTrainColor();
        diaAxisColor=diaFile.getAxisColor();
        timeTableFont=diaFile.getTableFont();
        timeTableVFont=diaFile.getVfont();
        diaStationFont=diaFile.getStationFont();
        diaTimeFont=diaFile.getDiaTimeFont();
        diaTrainFont=diaFile.getDiaTextFont();
        commentFont=diaFile.getCommnetFont();

        ArrayList<Integer>borderStation=new ArrayList<>();
        for(int i=0;i<diaFile.getStationNum();i++){
            if(diaFile.getStation(i).border()){
                borderStation.add(i);
                String borderName=diaFile.getStation(i).getName();
                OptionalInt nextBorder= IntStream.range(i+1, diaFile.getStationNum())
                        .filter(j -> diaFile.getStation(j).getName().equals(borderName))
                        .findFirst();
                if(nextBorder.isPresent()){
                    borderStation.add(nextBorder.getAsInt());
                }
                if(i+1<diaFile.getStationNum()){
                    String borderName2=diaFile.getStation(i+1).getName();
                    OptionalInt beforeBorder=IntStream.range(0, i)
                            .filter(j -> diaFile.getStation(j).getName().equals(borderName2))
                            .findFirst();
                    if(beforeBorder.isPresent()){
                        borderStation.add(beforeBorder.getAsInt());
                    }
                }
            }
        }
        borderStation.add(diaFile.getStationNum()-1);
        Collections.sort(borderStation);
        return borderStation;
    }
    public int getRouteNum(){
        return route.size();
    }
    public Route getRoute(int index,int direct){
        return (Route)route.keySet().toArray()[index*(1-2*direct)+direct*(route.size()-1)];
    }
    public int getRouteDirect(Route mroute){
        return route.get(mroute);
    }

}
