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

class Calendar(val id: UUID,val jpti:JPTI){
    constructor(rs: ResultSet, jpti:JPTI):this(
        UUID.fromString(rs.getString("id")),jpti
    ){
        name=rs.getString("calendar_name")
    }
    /**
     * ダイヤ名に相当
     */
    var name:String=""
    fun saveToSQL(conn: Connection){
        val deleteSQL="delete from calendar where id=?"
        val insertSQL="insert into calendar (id,calendar_name) values(?,?)"
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
    }
}