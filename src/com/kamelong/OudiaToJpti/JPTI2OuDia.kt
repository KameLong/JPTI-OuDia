package com.kamelong.OudiaToJpti

import com.kamelong.JPTI.*
import com.kamelong.JPTI.Station
import com.kamelong.oudia.*
import com.kamelong.tool.Color
import java.util.*
import kotlin.Exception
import kotlin.collections.ArrayList
fun main(args:Array<String>){
    val jpti2=JPTI()
    jpti2.openSQLite("output.sqlite3")
    print(jpti2)
    val converter=JPTI2OuDia(jpti2)
    converter.makeOuDiaFromService(jpti2.services.keys.first())
    converter.oudia.saveToFile("out.oud2")

}
/**
 * JPTIからOuDiaファイルを出力します
 */
class JPTI2OuDia(val jpti: JPTI){
    var stopList= arrayListOf<ArrayList<Stop>>()
    var stationList= arrayListOf<Station>()
    var tripClassList= arrayListOf<TripClass>()
    val oudia=LineFile()


    /**
     * 指定Serviceの路線を作成します
     */

    fun makeOuDiaFromService(serviceID: UUID){

        val service=jpti.services[serviceID]?:throw Exception()
        //まず駅リストを作ります。
        stationList= service.getStationList()
        for( station in stationList){
            val oudStation=com.kamelong.oudia.Station(oudia)
            //oudiaのStationを作ると同時にstationごとのstopのリストも作成
            stopList.add(makeStation(oudStation,station))
            oudia.addStation(-1,oudStation,false)
        }
        //列車種別の一覧を作ります

        for(tripClass in service.tripClasses.values){
            val trainType=TrainType()
            makeTrainType(trainType,tripClass)
            oudia.addTrainType(-1,trainType)
            tripClassList.add(tripClass)
        }
        //ダイヤ一覧を作り、それに列車をいれる
        for(calendar in jpti.calenders.values){
            val diagram=Diagram(oudia)
            diagram.name=calendar.name
            for( trip in service.trips.values){
                if(trip.calendar==calendar){
                    val train= Train(oudia,trip.direction.value)
                    makeTrain(train, trip)
                    diagram.addTrain(train.direction,-1,train)
                }
            }
            oudia.diagram.add(diagram)
        }
    }

    fun makeStation(oud:com.kamelong.oudia.Station,station:Station):ArrayList<Stop>{
        oud.name=station.name
        var stopList= arrayListOf<Stop>()
        for(stop in station.stops.values){
            stopList.add(stop)
        }
        stopList.sortWith(kotlin.Comparator({left,right->left.name.compareTo(right.name)}))
        var stopIndex=0
        for(stop in stopList){
            stopIndex++
            oud.addTrack(StationTrack(stop.name,makeStopShortName(stop.name,stopIndex)))
        }
        return stopList
    }
    fun makeStopShortName(name:String,index:Int):String{
        val result= arrayListOf<Char>()
        for(c in name){
            if(c<='9'&& c>='0'){
                result.add(c)
            }
            else{
                if(result.size>0){
                    return String(result.toCharArray())
                }
                return index.toString()
            }
        }
        return index.toString()
    }

    fun makeTrainType(trainType:TrainType,tripClass:TripClass){
        trainType.name=tripClass.name
        if(tripClass.name.length>2){
            trainType.shortName=tripClass.name.substring(0,2)
        }else{
            trainType.shortName=tripClass.name
        }
        trainType.textColor=Color(tripClass.color)
        trainType.diaColor=Color(tripClass.color)
    }

    fun makeTrain(train:Train,trip: Trip){
        train.name=trip.name
        train.number=trip.tripNo
        train.type=tripClassList.indexOf(trip.tripClass)
        val stationTimeStopTimeList= arrayListOf<StopTime?>()
        val stopTimeList= arrayListOf<StopTime>()
        for(time in trip.stopList){
            stopTimeList.add(time)
        }
        if(trip.direction==Trip.Direction.UP){
            stationList.reverse()
        }
        station@for(s in stationList){
            try {
                if(stopTimeList.isEmpty()){
                    stationTimeStopTimeList.add(null)
                    continue@station

                }
                if (stopTimeList.first().station == s) {
                    stationTimeStopTimeList.add(stopTimeList.first())
                    stopTimeList.remove(stopTimeList.first())
                    continue@station
                }
            }catch (e:Exception){
                e.printStackTrace()
            }
            stationTimeStopTimeList.add(null)

        }
        station@for(i in ((stationTimeStopTimeList.size-1) downTo 0)){
            if(stationTimeStopTimeList[i]==null){
                for(j in i until 0){
                    if(stationTimeStopTimeList[j]!=null){
                        continue@station
                    }
                    if(stationList[j]==stationList[i]){
                        stationTimeStopTimeList[i]=stationTimeStopTimeList[j]
                        stationTimeStopTimeList[j]=null
                    }
                }
            }
        }
        if(trip.direction==Trip.Direction.UP){
            stationTimeStopTimeList.reverse()
            stationList.reverse()
        }
        train.stationTimes.clear()
        for(d in stationTimeStopTimeList.zip(stopList.reversed())){
            val s=d.first
            val sTime=StationTime(train)
            if(s==null){
                sTime.stopType=StationTime.STOP_TYPE_NOSERVICE
            }else{
                sTime.ariTime=s.ariTime?:-1
                sTime.depTime=s.depTime?:-1
                sTime.stopType=when(s.stopType){
                    StopTime.StopType.NONE->StationTime.STOP_TYPE_NOSERVICE
                StopTime.StopType.STOP->StationTime.STOP_TYPE_STOP
                StopTime.StopType.PASS->StationTime.STOP_TYPE_PASS}
                sTime.stopTrack=d.second.indexOf(s.stop).toByte()
            }
            train.stationTimes.add(sTime)
        }
    }
}