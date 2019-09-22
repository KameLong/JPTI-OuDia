package com.kamelong.JPTI

import java.io.File
import java.util.*
import kotlin.collections.HashMap
import java.sql.Connection
import java.sql.SQLException
import java.sql.DriverManager
import java.sql.Statement
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class JPTI() {
    /**
     * 企業一覧
     */
    var agencies:HashMap<UUID,Agency> = hashMapOf()
    /**
     * 系統一覧
     */
    var services= hashMapOf<UUID,Service>()

    var calenders= hashMapOf<UUID, Calendar>()

    /**
     * 駅一覧
     */
    var stations= hashMapOf<UUID,Station>()

    /**
     * SQLiteファイルを開く
     */
    fun openSQLite(filePath:String){

        var conn: Connection? = null
        var stmt: Statement? = null
        try {
            // db parameters
            val url = "jdbc:sqlite:"+filePath
            // create a connection to the database
            conn = DriverManager.getConnection(url)
            conn.autoCommit=false
            println("Connection to SQLite has been established.")

            val stationSQL="SELECT * from station"
            stmt = conn.createStatement();
            val stationRS = stmt.executeQuery(stationSQL)
            while (stationRS.next()) {
                val station=Station(stationRS,this)
                station.addStop(conn)
                stations.put(station.id,station)
            }

            val calendarSQL="SELECT * from calendar"
            stmt = conn.createStatement();
            val calendarRS = stmt.executeQuery(calendarSQL)
            while (calendarRS.next()) {
                val calendar=Calendar(calendarRS,this)
                calenders.put(calendar.id,calendar)
            }
            val agencySQL = "SELECT * FROM agency"
            stmt = conn.createStatement();
            val agencyRS = stmt.executeQuery(agencySQL)
            while (agencyRS.next()) {
                val agency=Agency(agencyRS,this)
                agency.addRoute(conn)
                agencies.put(agency.id,agency)
            }
            val serviceSQL = "SELECT * FROM service"
            stmt = conn.createStatement();
            val serviceRS = stmt.executeQuery(serviceSQL)
            while (serviceRS.next()) {
                val service=Service(serviceRS,this)
                service.addRoute(conn)
                service.addTripClass(conn)
                service.addTrip(conn)
                services.put(service.id,service)
            }
            conn.commit()
            conn.autoCommit=true
        } catch (e: SQLException) {
            e.printStackTrace()
            println(e.message)
            conn?.rollback()
        } finally {

            try {
                conn?.close()
            } catch (ex: SQLException) {
                println(ex.message)
            }

        }

    }

    /**
     * 既存SQLiteにこのJPTIを上書きします
     * 同一キーの元データ
     */
    fun updateSQLite(filePath:String){
        var conn: Connection? = null
        var stmt: Statement? = null
        try {
            // db parameters
            val url = "jdbc:sqlite:"+filePath
            // create a connection to the database
            conn = DriverManager.getConnection(url)
            conn.autoCommit=false
            println("Connection to SQLite has been established.")
            for(agency in agencies.values){
                agency.saveToSQL(conn)
            }
            for(calendar in calenders.values){
                calendar.saveToSQL(conn)
            }
            for(station in stations.values){
                station.saveToSQL(conn)
            }
            for(service in services.values){
                service.saveToSQL(conn)
            }

            conn.commit()
            conn.autoCommit=true
        } catch (e: SQLException) {
            e.printStackTrace()
            println(e.message)
            conn?.rollback()
        } finally {

            try {
                conn?.close()
            } catch (ex: SQLException) {
                println(ex.message)
            }

        }

    }

    /**
     * 新規SQLiteを作る
     */
    fun saveAsNewSQLiteFile(filePath:String){
        var conn: Connection? = null
        var stmt: Statement? = null
        val file= File(filePath)
        if(file.exists()){
            file.delete()
        }
        try {
            // db parameters
            val url = "jdbc:sqlite:"+filePath
            // create a connection to the database
            conn = DriverManager.getConnection(url)
            stmt = conn.createStatement();
            println("Connection to SQLite has been established.")

            val createAgency=createTableSQL("agency", arrayListOf(
                Pair("id","text"),Pair("agency_name","text")))
            val createService=createTableSQL("service", arrayListOf(
                Pair("id","text"),Pair("service_name","text")))
            val createRoute=createTableSQL("route", arrayListOf(
                Pair("id","text"),Pair("agency_id","text"),Pair("route_name","text")))
            val createRouteStation=createTableSQL("routeStation", arrayListOf(
                Pair("id","text"),Pair("route_id","text"),Pair("station_id","text"),Pair("station_sequence","integer")))
            val createLineSystem=createTableSQL("LineSystem", arrayListOf(
                Pair("id","text"),Pair("route_id","text"),Pair("service_id","text"),
                Pair("sequence","integer"),Pair("start_route_station_id","text"),Pair("end_route_station_id","text")))
            val createStation=createTableSQL("station", arrayListOf(
                Pair("id","text"),Pair("station_name","text")))
            val createStop=createTableSQL("STOP", arrayListOf(
                Pair("id","text"),Pair("station_id","text"),Pair("stop_name","text")))
            val createTrip=createTableSQL("trip", arrayListOf(
                Pair("id","text"),Pair("service_id","text"),Pair("trip_No","text"),
                Pair("trip_class_id","text"),Pair("trip_name","text"),Pair("trip_direction_id","text"),
                Pair("calender_id","text")))
            val createTime=createTableSQL("time", arrayListOf(
                Pair("id","text"),Pair("trip_id","text"),Pair("station_id","text"),
                Pair("stop_id","text"),Pair("stop_sequence","integer"),Pair("pickup_type","text"),
                Pair("dropoff_type","text"),Pair("arrival_time","text"),Pair("departure_time","text")))
            val createTripClass=createTableSQL("trip_class", arrayListOf(
                Pair("id","text"),Pair("service_id","text"),
                Pair("trip_class_name","text"),Pair("trip_class_color","text")))
            val createCalendar=createTableSQL("calendar", arrayListOf(
                Pair("id","text"),Pair("service_id","text"),Pair("calendar_name","text")))

            stmt.executeUpdate(createAgency)
            stmt.executeUpdate(createService)
            stmt.executeUpdate(createRoute)
            stmt.executeUpdate(createRouteStation)
            stmt.executeUpdate(createLineSystem)
            stmt.executeUpdate(createStation)
            stmt.executeUpdate(createStop)
            stmt.executeUpdate(createTrip)
            stmt.executeUpdate(createTime)
            stmt.executeUpdate(createTripClass)
            stmt.executeUpdate(createCalendar)

        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {

            try {
                conn?.close()
            } catch (ex: SQLException) {
                println(ex.message)
            }

        }
        updateSQLite(filePath)

    }

    /**
     * 新規SQLにtableを準備する
     */
    fun createTableSQL(tableName:String,columns:ArrayList<Pair<String,String>>):String{
        var result="Create table $tableName ("
        val first=columns.first()
        result+=" ${first.first} ${first.second} primary key"
        for( column in columns.subList(1,columns.size)){
            result+=", ${column.first} ${column.second}"
        }
        result+=")"
        return result
    }

    fun getStation(stationID:UUID):Station{
        return stations.get(stationID)?:throw Exception("Station is not found")
    }
    fun getStop(stopID:UUID):Stop{
        for(station in stations.values){
            if(stopID in station.stops){
                return station.stops.get(stopID)!!
            }
        }
        throw Exception("Stop not found")
    }
    fun getRoute(routeID:UUID):Route{
        for(agency in agencies.values){
            if(routeID in agency.routes){
                return agency.routes.get(routeID)!!
            }
        }
        throw Exception("Route not found")
    }

}