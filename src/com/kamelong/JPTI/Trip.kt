package com.kamelong.JPTI

import java.sql.Connection
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Trip(val id:UUID,val service: Service,val calendar: Calendar,var tripClass:TripClass) {
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
    enum class Direction(val value:Int){
        DOWN(0),UP(1)
    }
    val direction:Direction=Direction.DOWN



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

}