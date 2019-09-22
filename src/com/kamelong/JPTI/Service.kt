package com.kamelong.JPTI

import java.sql.Connection
import java.sql.ResultSet
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Service(val id:UUID,val jpti:JPTI) {
    constructor(rs:ResultSet,jpti:JPTI):this(UUID.fromString(rs.getString("id")),jpti){
        name=rs.getString("service_name")
    }
    /**
     * 系統名
     */
    var name = ""

    /**
     * このサービスがどのRouteを通るのか
     */
    data class ServiceRoute(val id: UUID, val start: Route.RouteStation, val end: Route.RouteStation)

    var routeList = arrayListOf<ServiceRoute>()

    /**
     * 列車種別
     */
    var tripClasses = hashMapOf<UUID, TripClass>()
    /**
     * 所属列車
     */
    var trips = hashMapOf<UUID, Trip>()
    /**
     * SQLのRouteStationテーブルのデータから追加する
     * 先にJPTIにRouteを用意しておくこと
     */
    fun addRoute(conn: Connection){
        val sql = "SELECT * FROM LineSystem where service_id=\"${id.toString()}\"order by sequence"
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        while (rs.next()) {
            val route:Route=jpti.getRoute(UUID.fromString(rs.getString("route_id")))
            val routeStation= ServiceRoute(
                UUID.fromString(rs.getString("id")),
                route.getRouteStation(UUID.fromString(rs.getString("start_route_station_id"))),
                route.getRouteStation(UUID.fromString(rs.getString("start_route_station_id")))
            )
                routeList.add(routeStation)
        }
    }
    fun addTripClass(conn: Connection){
        val sql = "SELECT * FROM trip_class where service_id=\"${id.toString()}\""
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        while (rs.next()) {
            val tripClass=TripClass(rs,this)
            tripClasses.put(tripClass.id,tripClass)
        }
    }
    /**
     * SQLのtripテーブルのデータを取得する
     * 先にStop,TripClassを用意しておくこと
     */
    fun addTrip(conn:Connection){
        val sql = "SELECT * FROM trip where service_id=\"${id.toString()}\""
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        while (rs.next()) {
            val trip=Trip(rs,this)
            trip.addStopTime(conn)
            trips.put(trip.id,trip)
        }
    }

    /**
     * 所属Routeの追加
     */
    fun addRoute(start: Route.RouteStation, end: Route.RouteStation) {
        val serviceRoute = ServiceRoute(UUID.randomUUID(), start, end)
        routeList.add( serviceRoute)

    }

    /**
     * oudファイルから列車を追加するまえの処理
     */
    fun resetServiceTrip() {
        trips = hashMapOf()
        tripClasses = hashMapOf()
    }

    fun saveToSQL(conn: Connection) {
        val deleteSQL = "delete from service where id=?"
        val insertSQL = "insert into service (id,service_name) values(?,?)"
        try {
            val ps = conn.prepareStatement(deleteSQL)
            ps.setString(1, id.toString())
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
        try {
            val ps = conn.prepareStatement(insertSQL)
            ps.setString(1, id.toString())
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
        var routeSeq = 0
        for (route in routeList) {
            routeSeq++
            val deleteRouteSQL = "delete from LineSystem where id=?"
            val insertRouteSQL = "insert into LineSystem (id,route_id,service_id,sequence,start_route_station_id,end_route_station_id) values(?,?,?,?,?,?)"

            try {
                val ps=conn.prepareStatement(deleteRouteSQL)
                ps.setString(1, route.id.toString())
                ps.executeUpdate()
            } catch (e: Exception) {
                throw e
            }
            try {
                val ps=conn.prepareStatement(insertRouteSQL)
                ps.setString(1, route.id.toString())
                ps.setString(2, route.start.route.id.toString())
                ps.setString(3, id.toString())
                ps.setInt(4,routeSeq)
                ps.setString(5,route.start.id.toString())
                ps.setString(6,route.end.id.toString())
                ps.executeUpdate()
            } catch (e: Exception) {
                throw e
            }



        }
        for(trip in trips.values){
            trip.saveToSQL(conn)
        }
        for(tripClass in tripClasses.values){
            tripClass.saveToSQL(conn)
        }
    }

    /**
     * Serviceに所属する駅のリスト
     */
    fun getStationList():ArrayList<Station>{
        val result= arrayListOf<Station>()
        for(route:ServiceRoute in routeList){
            if(result.size>0){
                result.remove(result.last())
            }
            for(station: Station in route.start.route.getStationList(route.start,route.end)){
                result.add(station)
            }
        }
        return result
    }

}