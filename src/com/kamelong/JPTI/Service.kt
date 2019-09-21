package com.kamelong.JPTI

import java.sql.Connection
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Service(val id:UUID) {
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
}