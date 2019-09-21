package com.kamelong.JPTI

import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Route(val id: UUID,val agency:Agency){
    /**
     * 名前
     */
    var name:String=""

    data class RouteStation(val id:UUID,val route:Route,val station:Station)
    /**
     * 駅順
     * UUIDはRouteStationのID
     */
    var stationList= linkedMapOf<UUID,RouteStation>()

    /**
     * 列車
     */
    var tripList= hashMapOf<UUID,Trip>()

    /**
     * routeに駅を追加する
     */
    fun addStation(station:Station){
        val routeStation=RouteStation(UUID.randomUUID(),this,station)
        stationList.put(routeStation.id,routeStation)
    }


    /**
     * 所属駅のうち一部のみ切り出す
     */
    fun getStationList(start:RouteStation,end:RouteStation):ArrayList<Station>{
        //startもendも同じとき
        if(start==end){
            return arrayListOf(start.station)
        }
        //startかendがこのrouteに含まれていない時
        if(!(stationList.values.contains(start)&&stationList.values.contains(end))){
            return arrayListOf()
        }
        var result= arrayListOf<Station>()
        var enable=false
        for(routeStation in stationList.values){
            if(routeStation==start||routeStation==end){
                if(enable){
                    result.add(routeStation.station)
                    return result
                }
                enable=true
            }
            if(enable){
                result.add(routeStation.station)
            }
        }
        return result
    }

}