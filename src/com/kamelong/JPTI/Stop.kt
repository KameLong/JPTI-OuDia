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

class Stop(val id:UUID,val station:Station) {
    constructor(rs: ResultSet, station:Station):this(UUID.fromString(rs.getString("id")),station){
        name=rs.getString("stop_name")
    }

    /**
     * 番線名
     */
    var name:String=""
    fun saveToSQL(conn: Connection) {
        val deleteSQL = "delete from STOP where id=?"
        val insertSQL = "insert into STOP (id,station_id,stop_name) values(?,?,?)"
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
            ps.setString(2, station.id.toString())
            ps.setString(3, name)
            ps.executeUpdate()
        } catch (e: Exception) {
            throw e
        }
    }

}