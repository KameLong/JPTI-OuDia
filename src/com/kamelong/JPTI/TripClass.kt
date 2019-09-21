package com.kamelong.JPTI

import java.sql.Connection
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class TripClass(val id:UUID,var service:Service){

    /**
     * 種別名
     */
    var name:String=""
    var color:String=""

    fun saveToSQL(conn: Connection) {
        val deleteSQL = "delete from trip_class where id=?"
        val insertSQL =
            "insert into trip_class (id,service_id,trip_class_name,trip_class_color) values(?,?,?,?)"
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
            ps.setString(3, name)
            ps.setString(4, color)
            ps.executeUpdate()


        } catch (e: Exception) {
            throw e
        }
    }

}