package com.kamelong.OudiaToJpti

import com.kamelong.JPTI.*
import com.kamelong.oudia.LineFile
import com.kamelong.oudia.StationTime
import com.kamelong.oudia.Train
import com.sun.jdi.connect.Connector
import java.io.File
import java.util.UUID
import kotlin.collections.ArrayList


fun main(args : Array<String>) {
    val oudiaFile:String=""
    val lineFile=LineFile(File(oudiaFile))
    val jpti=JPTI()
    if(jpti.agency.size==0){
        val agency=Agency(UUID.randomUUID())
        agency.name="新規会社"
        jpti.agency.put(agency.id,agency)
    }
    val converter=OuDia2JPTI(jpti,lineFile)
    val service=converter.makeService(    jpti.agency.values.first())
    converter.convertToJPTI(service.id)
    println(jpti)
}


/**
 * このクラスはOuDiaファイルをJPTI形式に変換します。
 * 事前準備
 * ・OuDiaファイルを読み込んでおく
 * ・JPTI側にOuDiaとマッチするServiceを作っておく
 */
class OuDia2JPTI(val jpti: JPTI, val oudia:LineFile){
    //oudia路線にマッチしたサービスを作ります
    //駅から路線から全部作ります
    fun makeService(agency: Agency):Service{
        val route=Route(UUID.randomUUID(),agency)
        val service=Service(UUID.randomUUID())

        for(station in oudia.station){
            val jptiStation=Station(UUID.randomUUID())
            jptiStation.name=station.name
            jpti.station.put(jptiStation.id,jptiStation)
            route.addStation(jptiStation)
        }
        service.addRoute(route.stationList.values.first(),route.stationList.values.last())
        agency.route.put(route.id,route)
        jpti.service.put(service.id,service)
        return service
    }

    /**
     * serviceIDで示された系統の列車と列車種別を削除し、OuDiaの列車と列車種別で置き換えます。
     */
    fun convertToJPTI(serviceID: UUID){

        val service=jpti.service.get(serviceID)?:return//returnはserviceがnullであるとき
        //駅数チェック
        if(!checkStation(service)) {
            //両者の駅が異なります
            return
        }
        service.resetServiceTrip()
        //これが使用するカレンダー(Diagram順に並んでいる)
        val calendarList=makeCalenderList()
        //列車種別をJPTIに追加
        for(trainType in oudia.trainType){
            val tripClass=TripClass(UUID.randomUUID(),service)
            tripClass.name=trainType.name
            tripClass.color=trainType.textColor.htmlColor
            service.tripClass.put(tripClass.id,tripClass)
        }
        val stationList=getStationList(service)
        for(diagram in oudia.diagram.zip(calendarList)){
            //diagram.first=Diagram
            //diagram.second=Calendar

            //Tripリストを作成する
            for(train in diagram.first.trains[0]){
                val trip=Trip(UUID.randomUUID(),service,diagram.second)

                makeTrip(trip,train,stationList)
                service.trip.put(trip.id,trip)
            }
            for(train in diagram.first.trains[1]){
                val trip=Trip(UUID.randomUUID(),service,diagram.second)
                makeTrip(trip,train,stationList)
                service.trip.put(trip.id,trip)
            }
        }
    }
    fun makeCalenderList():ArrayList<Calendar>{
        var result= arrayListOf<Calendar>()
        diagramLoop@ for(diagram in oudia.diagram){
            for(calendar in jpti.calender.values){
                if(calendar.name==diagram.name){
                    result.add(calendar)
                    continue@diagramLoop
                }
            }
            //ダイヤ名と同名のCalendarがないので追加する
            val newCalendar=Calendar(UUID.randomUUID())
            newCalendar.name=diagram.name
            jpti.calender.put(newCalendar.id,newCalendar)
            result.add(newCalendar)
        }
        return result
    }
    fun makeTrip(trip:Trip,train: Train,stationList:ArrayList<Station>){
        var stationSet=oudia.station.zip(stationList)

        var stationTimes=train.stationTimes.zip(stationSet)
        if(train.direction==Train.UP) {
            stationTimes=stationTimes.reversed()
        }
        for(stationTimeSet in stationTimes){
            //stationTimeSet.first=StationTime
            //stationTimeSet.second.first=oudiaStation
            //stationTimeSet.second.second=jptiStation
            if(stationTimeSet.first.stopType==StationTime.STOP_TYPE_STOP||stationTimeSet.first.stopType==StationTime.STOP_TYPE_PASS){
                val stopName=stationTimeSet.second.first.getTrackName(stationTimeSet.first.getStopTrack())
                val stop=stationTimeSet.second.second.getStop(stopName)
                val stopTime=StopTime(UUID.randomUUID(),trip,stop)
                stopTime.ariTime=stationTimeSet.first.ariTime
                stopTime.depTime=stationTimeSet.first.depTime
                if(stationTimeSet.first.stopType==StationTime.STOP_TYPE_STOP) {
                    stopTime.stopType = StopTime.StopType.stop
                }else{
                    stopTime.stopType = StopTime.StopType.pass
                }
                trip.stopList.add(stopTime)
                continue
            }
        }
    }
    fun getStationList(service:Service):ArrayList<Station>{
        val result= arrayListOf<Station>()
        for(route:Service.ServiceRoute in service.routeList.values){
            if(result.size>0){
                result.remove(result.last())
            }
            for(station: Station in route.start.route.getStationList(route.start,route.end)){
                result.add(station)
            }
        }
        return result
    }

    /**
     * JPTI側とOuDia側で駅配置が同じかどうか調べる
     * return true:同じ　false:違う
     */
    fun checkStation(service:Service):Boolean{
        var oudStationIndex=0

        for(route:Service.ServiceRoute in service.routeList.values){
            for(station: Station in route.start.route.getStationList(route.start,route.end)){
                if(oudia.stationNum<=oudStationIndex){
                    //JPTIの方が駅数が多い
                    return false
                }
                if(oudia.getStation(oudStationIndex).name!=station.name){
                    //駅名が違う
                    return false
                }
                oudStationIndex++
            }
            oudStationIndex--
        }
        if(oudStationIndex==oudia.stationNum-1){
            return true
        }
        //OuDiaの方が駅数が多い
        return false
    }
}