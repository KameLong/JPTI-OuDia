package com.kamelong.JPTI

import java.sql.Connection
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Station(val id: UUID){
    /**
     * 駅名
     */
    var name=""

    var stops= hashMapOf<UUID,Stop>()

    /**
     * 指定stopNameのStopを返す
     * もしstopがなければ作る
     */
    fun getStop(stopName:String):Stop{
        for(stop in stops.values){
            if(stop.name==stopName){
                return stop
            }
        }
        //該当Stopが存在しない
        val newStop=Stop(UUID.randomUUID(),this)
        newStop.name=stopName
        stops.put(newStop.id,newStop)
        return newStop
    }

    fun saveToSQL(conn: Connection){
        val deleteSQL="delete from station where id=?"
        val insertSQL="insert into station (id,station_name) values(?,?)"
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
            ps.setString(2, name)
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
        for(stop in stops.values){
            stop.saveToSQL(conn)
        }
    }

}