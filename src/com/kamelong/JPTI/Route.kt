package com.kamelong.JPTI

import java.sql.Connection
import java.sql.ResultSet
import java.util.*
import kotlin.collections.ArrayList

/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Route(val id: UUID,val agency:Agency){
    constructor(rs:ResultSet,agency:Agency):this(UUID.fromString(rs.getString("id")),agency){
        name=rs.getString("route_name")
    }

    /**
     * 名前
     */
    var name:String=""

    data class RouteStation(val id:UUID,val route:Route,val station:Station)
    /**
     * 駅順
     * UUIDはRouteStationのID
     */
    var stationList= arrayListOf<RouteStation>()

    /**
     * 列車
     */
    var tripList= hashMapOf<UUID,Trip>()

    val jpti:JPTI
        get()=agency.jpti



    /**
     * SQLのRouteStationテーブルのデータから追加する
     * 先にJPTIにStationを用意しておくこと
     */
    fun addStation(conn: Connection){
        val sql = "SELECT * FROM routeStation where route_id=\"${id.toString()}\" order by station_sequence"
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        while (rs.next()) {
            val routeStation=RouteStation(UUID.fromString(rs.getString("id")),this,jpti.getStation(UUID.fromString(rs.getString("station_id"))))
            stationList.add(routeStation)
        }
    }
    /**
     * routeに駅を追加する
     */
    fun addStation(station:Station){
        val routeStation=RouteStation(UUID.randomUUID(),this,station)
        stationList.add(routeStation)
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
        if(!(stationList.contains(start)&&stationList.contains(end))){
            return arrayListOf()
        }
        var result= arrayListOf<Station>()
        var enable=false
        for(routeStation in stationList){
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

    fun saveToSQL(conn: Connection){
        val deleteSQL="delete from route where id=?"
        val insertSQL="insert into route (id,agency_id,route_name) values(?,?,?)"
        try {
            val ps=conn.prepareStatement(deleteSQL)
            ps.setString(1, id.toString())
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
        try {
            val ps=conn.prepareStatement(insertSQL)
            ps.setString(1, id.toString())
            ps.setString(2, agency.id.toString())
            ps.setString(3, name)
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
        var stationSeq=0
        for(routeStation in stationList){
            stationSeq++
            val deleteStationSQL="delete from routeStation where id=?"
            val insertStationSQL="insert into routeStation (id,route_id,station_id,station_sequence) values(?,?,?,?)"
            try {
                val ps=conn.prepareStatement(deleteStationSQL)
                ps.setString(1, routeStation.id.toString())
                ps.executeUpdate()
            } catch (e: Exception) {
                throw e
            }
            try {
                val ps=conn.prepareStatement(insertStationSQL)
                ps.setString(1, routeStation.id.toString())
                ps.setString(2, routeStation.route.id.toString())
                ps.setString(3, routeStation.station.id.toString())
                ps.setInt(4, stationSeq)
                ps.executeUpdate()
            } catch (e: Exception) {
                throw e
            }
        }
    }

    /**
     * UUIDからrouteStationを探索
     */
    fun getRouteStation(routeStationID:UUID):RouteStation{
        for(routeStation in stationList){
            if(routeStation.id==routeStationID){
                return routeStation
            }
        }
        throw Exception("routeStation not found")
    }

}