package com.kamelong.OudiaToJpti

import com.kamelong.JPTI.JPTI
import com.kamelong.JPTI.Station
import com.kamelong.JPTI.Stop
import com.kamelong.oudia.LineFile
import java.lang.Exception
import java.util.*
import kotlin.Exception

/**
 * JPTIからOuDiaファイルを出力します
 */
class JPTI2OuDia(val jpti: JPTI){

    /**
     * 指定Serviceの路線を作成します
     */

    fun makeOuDiaFromService(serviceID: UUID){
        val oudia=LineFile()

        val service=jpti.services[serviceID]?:throw Exception()
        val stationList= service.getStationList()
        for( station in stationList){
            val oudStation=com.kamelong.oudia.Station(oudia)
            makeStation()
            oudia.addStation()
        }
    }

    fun makeStation(oud:com.kamelong.oudia.Station,station:Station){
        oud.name=station.name
        var stopList= arrayListOf<Stop>()
        for(stop in station.stops.values){
            stopList.add(stop)
        }
        stopList.sortWith(kotlin.Comparator({left,right->left.name.compareTo(right.name)}))

    }

}