package com.kamelong.JPTI

import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Service(val id:UUID){
    /**
     * 系統名
     */
    var name=""

    /**
     * このサービスがどのRouteを通るのか
     */
    data class ServiceRoute(val id:UUID,val start: Route.RouteStation,val end:Route.RouteStation)
    var  routeList= linkedMapOf<UUID,ServiceRoute>()

    /**
     * 列車種別
     */
    var tripClass= hashMapOf<UUID,TripClass>()
    /**
     * 所属列車
     */
    var trip= hashMapOf<UUID,Trip>()


    /**
     * 所属Routeの追加
     */
    fun addRoute(start: Route.RouteStation,end:Route.RouteStation){
        val serviceRoute=ServiceRoute(UUID.randomUUID(),start,end)
        routeList.put(serviceRoute.id,serviceRoute)

    }

    /**
     * oudファイルから列車を追加するまえの処理
     */
    fun resetServiceTrip(){
        trip= hashMapOf()
        tripClass= hashMapOf()
    }

}