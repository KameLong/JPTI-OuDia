package com.kamelong.JPTI

import java.sql.Connection
import java.sql.PreparedStatement
import java.util.*
import com.kamelong.JPTI.JPTI
import com.kamelong.JPTI.Route
import java.sql.ResultSet


/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Agency(val id: UUID,val jpti:JPTI){
    /**
     * 法人名
     */
    var name:String=""

    /**
     * RouteList
     */
    var routes= hashMapOf<UUID,Route>()


    /**
     * SQLのresultsetから作る
     */
    constructor(rs: ResultSet,jpti:JPTI) :
            this(UUID.fromString(rs.getString("id")),jpti) {
        name=rs.getString("name")
    }

    /**
     * SQLのResultSetからRouteを追加
     */
    fun addRoute(rs:ResultSet){

    }

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