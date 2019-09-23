package com.kamelong.OuDiaJPTIconvertor

import com.kamelong.JPTI.*
import com.kamelong.oudia.LineFile
import com.kamelong.oudia.StationTrack
import com.kamelong.oudia.Train
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

fun main(args:Array<String>){
    val startTime=System.currentTimeMillis()
    val jpti=JPTI()
    jpti.openSQLite("test.sqlite3")
    println(System.currentTimeMillis()-startTime)
    val converter=JPTI2OuDia2(jpti)

    /**
    ef220aec-ac77-4e24-acca-16544ccc9586|横浜
    8fe7f046-9e5d-4fde-b428-603efc29e134|平沼橋
    ad7bf67b-8877-4bae-ac13-6c4c4ecea61c|西横浜
    46fcde09-d0b1-485c-b31d-e58613f8c162|天王町
    ca0974ef-76ff-445b-b67c-e729e4732e89|星川
    d8b6acfd-9d7f-4b6b-8ab7-720d21e57e1c|和田町
    6c039dce-8391-42d8-bd84-1ea918ad4868|上星川
    2176e4d8-8ed8-4cae-9c1f-89aa1e1fc1f0|西谷
    5afef5de-0a7f-46e5-ac88-10bffd8d6297|鶴ヶ峰
    901eb80c-1818-490c-bcdc-43e3dfdf1461|二俣川
    5b40b0cc-248f-4838-8e6a-8a2a2a525411|希望ヶ丘
    8dbd5bdf-86e2-44a9-a443-ce95ee00e520|三ツ境
    1941913f-8ce2-4be2-8fc4-0357240e4c0b|瀬谷
    e450f2aa-f358-4f7b-a3a9-f10ea719f40b|大和
    1c7e97ac-6553-41a9-bcf8-313b5a2caa64|相模大塚
    2c6c722d-e3c2-46d4-a32a-2bdcd0d001d8|さがみ野
    67ae21cb-0377-43b0-b2bc-14cbf147d64d|かしわ台
    dc0e3111-0039-463a-8d6b-5c792592e1ac|海老名

     */
    //使用する駅のリスト
    val stationIDList= arrayListOf<String>(
        "ef220aec-ac77-4e24-acca-16544ccc9586",
        "8fe7f046-9e5d-4fde-b428-603efc29e134",
        "ad7bf67b-8877-4bae-ac13-6c4c4ecea61c",
        "46fcde09-d0b1-485c-b31d-e58613f8c162",
        "ca0974ef-76ff-445b-b67c-e729e4732e89",
        "d8b6acfd-9d7f-4b6b-8ab7-720d21e57e1c",
        "6c039dce-8391-42d8-bd84-1ea918ad4868",
        "2176e4d8-8ed8-4cae-9c1f-89aa1e1fc1f0",
        "5afef5de-0a7f-46e5-ac88-10bffd8d6297",
        "901eb80c-1818-490c-bcdc-43e3dfdf1461",
        "5b40b0cc-248f-4838-8e6a-8a2a2a525411",
        "8dbd5bdf-86e2-44a9-a443-ce95ee00e520",
        "1941913f-8ce2-4be2-8fc4-0357240e4c0b",
        "e450f2aa-f358-4f7b-a3a9-f10ea719f40b",
        "1c7e97ac-6553-41a9-bcf8-313b5a2caa64",
        "2c6c722d-e3c2-46d4-a32a-2bdcd0d001d8",
        "67ae21cb-0377-43b0-b2bc-14cbf147d64d",
        "dc0e3111-0039-463a-8d6b-5c792592e1ac"
    )
    val stationList= arrayListOf<Station>()
    for(id in stationIDList){
        stationList.add(jpti.getStation(UUID.fromString(id)))
    }
    /**
    e328de1f-9a83-426f-af3f-34520ae37784|本線
    75b26902-8705-4510-83a2-67f600e9f78e|いずみ野線
    1b08360f-2407-4a67-b294-467540e388c7|JR直通
     */
    //使用するサービスのリスト
    val subServiceList= arrayListOf<JPTI2OuDia2.SubService>(
        JPTI2OuDia2.SubService(
            jpti.services.get(UUID.fromString("e328de1f-9a83-426f-af3f-34520ae37784"))?:throw Exception("service not found"),
            jpti.getRouteStation(UUID.fromString("01e0aad5-8a91-4d4e-9718-702ecbff8fb7")),//横浜
            jpti.getRouteStation(UUID.fromString("32a84a5b-17ab-421d-acfa-55bcc4950e2e"))),//海老名
        JPTI2OuDia2.SubService(
            jpti.services.get(UUID.fromString("75b26902-8705-4510-83a2-67f600e9f78e"))?:throw Exception("service not found"),
            jpti.getRouteStation(UUID.fromString("481027e8-b4fe-4654-8120-bf3a5c630055")),//横浜
            jpti.getRouteStation(UUID.fromString("64cbdd23-9935-465c-b7d9-b51806a4c8b8"))),//二俣川
        JPTI2OuDia2.SubService(
            jpti.services.get(UUID.fromString("1b08360f-2407-4a67-b294-467540e388c7"))?:throw Exception("service not found"),
            jpti.getRouteStation(UUID.fromString("9279c2ef-c36b-4280-a058-3e1f62e11d34")),//西谷
            jpti.getRouteStation(UUID.fromString("ca210a27-a13a-424b-8d19-471ad60e78b3")))//海老名
    )


    println(System.currentTimeMillis()-startTime)
    val oudia=converter.makeOuDia(stationList,subServiceList)
    oudia.saveToFile("result.oud2")
}

class JPTI2OuDia2(val jpti:JPTI){
    var oudiaList= arrayListOf<LineFile>()


    /**
     * makeOuDia入力につかうデータクラス。
     * service:該当service
     * start:時刻表への組み込み開始駅(RouteStation)
     * end:時刻表への組み込み最終駅(RouteStation)
     */
    data class SubService(val service: Service, var start: Route.RouteStation, var end: Route.RouteStation)

    /**
     * 複数Serviceをまとめた時刻表を作製します
     *
     * 入力フォーマット
     * StationList:使用Stationリスト
     * subService:使用するServiceとそれぞれの区間のリスト
     */

    fun makeOuDia(stationList:ArrayList<Station>, subServiceList:ArrayList<SubService>):LineFile{
        //まず、serviceごとに時刻表を作る

        for(subService in subServiceList){
            val converter=JPTI2OuDia(jpti)
            converter.makeOuDiaFromService(subService.service.id)
            val oudia=converter.oudia
            //serviceごとに必要な部分だけ列車を切り出す
            val startIndex:Int=subService.service.getRouteStationIndex(subService.start)
            val endIndex:Int=subService.service.getRouteStationIndex(subService.end)
            for(stationIndex in oudia.stationNum-1 downTo endIndex+1){
                oudia.deleteStation(stationIndex)
            }
            for(stationIndex in startIndex-1 downTo 0){
                oudia.deleteStation(stationIndex)
            }
            for(diagram in oudia.diagram){
                var deleteList= arrayListOf<Train>()
                for(train in diagram.trains[0]){
                    if(train.isnull()){
                        deleteList.add(train)
                        continue
                    }
                    if(train.endStation==0){
                        deleteList.add(train)
                        continue
                    }
                    if(train.startStation==oudia.stationNum-1){
                        deleteList.add(train)
                        continue
                    }
                    if(train.timeExist(oudia.stationNum-1)&&!train.timeExist(oudia.stationNum-1,Train.ARRIVE)){
                        train.setAriTime(oudia.stationNum-1,train.getDepTime(oudia.stationNum-1))
                        train.setDepTime(oudia.stationNum-1,-1)
                    }
                }
                diagram.trains[0].removeAll(deleteList)
                deleteList= arrayListOf()
                for(train in diagram.trains[1]){
                    if(train.isnull()){
                        deleteList.add(train)
                        continue
                    }
                    if(train.endStation==oudia.stationNum-1){
                        deleteList.add(train)
                        continue
                    }
                    if(train.startStation==0){
                        deleteList.add(train)
                        continue
                    }
                    if(train.timeExist(0)&&!train.timeExist(0,Train.ARRIVE)){
                        train.setAriTime(0,train.getDepTime(0))
                        train.setDepTime(0,-1)
                    }
                }
                diagram.trains[1].removeAll(deleteList)

            }
            oudiaList.add(oudia)
        }
        //作られた時刻表のStationとJPTIのstationを対応させる
        //stationListとそれぞれの時刻表の駅順を合わせる
        for(oudia in oudiaList){
            //stationListとoudia stationの射影
            val stationProjection:ArrayList<com.kamelong.oudia.Station?> = arrayListOf()
            val oudiaStation = oudia.station.clone() as ArrayList<com.kamelong.oudia.Station>
            for(jptiStation in stationList){
                if(oudiaStation.size==0){
                    stationProjection.add(null)
                    continue

                }
                if(oudiaStation.first().jptiStationID==jptiStation.id){
                    stationProjection.add(oudiaStation.first())
                    oudiaStation.remove(oudiaStation.first())
                }
                else{
                    stationProjection.add(null)
                }
            }
            station@for(i in stationProjection.size-1 downTo 0){
                if(stationProjection[i]!=null){
                    continue
                }
                for (j in i-1 downTo 0){
                    if(stationList[i]==stationList[j]&&stationProjection[j]!=null){
                        stationProjection[i]=stationProjection[j]
                        stationProjection[j]=null
                        continue@station
                    }
                    if(stationProjection[j]!=null){
                        continue@station
                    }
                }
            }
            //stationProjectionが正しい順番になった
            //足りない駅を足す
            for(i in 0 until stationList.size){
                if(stationProjection[i]==null){
                    val station=com.kamelong.oudia.Station(oudia)
                    makeStation(station,stationList[i])
                    oudia.addStation(i,station,false)
                }
            }
        }
        var trainTypeNum=oudiaList[0].trainType.size
        for(i in 1 until oudiaList.size){
            for(trainType in oudiaList[i].trainType){
                oudiaList[0].addTrainType(-1,trainType.clone())
            }
            for(typeIndex in 0 until trainTypeNum){
                oudiaList[i].addTrainType(typeIndex,oudiaList[0].trainType[typeIndex].clone())
            }
            for(diagram in oudiaList[i].diagram.zip(oudiaList[0].diagram)){
                for(train in diagram.first.trains[0]){
                    diagram.second.addTrain(train.direction,-1,train)
                }
                for(train in diagram.first.trains[1]){
                    diagram.second.addTrain(train.direction,-1,train)
                }
            }
        }
        return oudiaList[0]

    }
    fun makeStation(oud:com.kamelong.oudia.Station,station:Station):ArrayList<Stop>{
        oud.name=station.name
        oud.jptiStationID=station.id
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


}