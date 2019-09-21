package com.kamelong.JPTI

import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*
import com.kamelong.JPTI.JPTI
import com.kamelong.JPTI.Route


/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Agency(val id: UUID){
    /**
     * 法人名
     */
    var name:String=""

    /**
     * RouteList
     */
    var routes= hashMapOf<UUID,Route>()


    fun saveToSQL(conn:Connection){
        val deleteSQL="delete from agency where id=?"
        val insertSQL="insert into agency (id,name) values(?,?)"
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
        for(route in routes.values){
            route.saveToSQL(conn)
        }
    }



}