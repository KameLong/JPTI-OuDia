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

class Trip(val id:UUID,val service: Service,val calendar: Calendar,var tripClass:TripClass) {
    constructor(rs: ResultSet, service: Service):this(
        UUID.fromString(rs.getString("id")),
        service,
        service.jpti.calenders.get(UUID.fromString(rs.getString("calender_id")))?:throw Exception("Calendar not found"),
        service.tripClasses.get(UUID.fromString(rs.getString("trip_class_id")))?:throw Exception("tripClass not found"))
    {
        name=rs.getString("trip_name")
        tripNo=rs.getString("trip_No")
        direction.value=rs.getInt("trip_direction_id")
    }


    /**
     * 列車名
     */
    var name:String=""
    /**
     * 列車番号
     */
    var tripNo:String=""

    /**
     * 向き
     */
    enum class Direction(var value:Int){
        DOWN(0),UP(1)
    }
    val direction:Direction=Direction.DOWN

    val jpti:JPTI
    get()=service.jpti



    /**
     * 停車駅
     */
    val stopList= arrayListOf<StopTime>()


    fun saveToSQL(conn: Connection) {
        val deleteSQL = "delete from trip where id=?"
        val insertSQL =
            "insert into trip (id,service_id,trip_No,trip_class_id,trip_name,trip_direction_id,calender_id) values(?,?,?,?,?,?,?)"
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
            ps.setString(2, service.id.toString())
            ps.setString(3, tripNo)
            ps.setString(4, tripClass.id.toString())
            ps.setString(5, name)
            ps.setInt(6, direction.value)
            ps.setString(7, calendar.id.toString())
            ps.executeUpdate()


        } catch (e: Exception) {
            throw e
        }
        var timeIndex=0
        for(time in stopList){
            timeIndex++
            time.saveToSQL(conn,timeIndex)
        }
    }

    /**
     * timeを追加する
     */
    fun addStopTime(conn: Connection){
        val sql = "SELECT * FROM time where trip_id=\"${id.toString()}\"order by stop_sequence"
        val stmt = conn.createStatement()
        val rs = stmt.executeQuery(sql)
        while (rs.next()) {
            val stop=StopTime(rs,this)
            stopList.add(stop)
        }
    }

}