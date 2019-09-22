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
     * SQLのresultsetから作る
     */
    constructor(rs: ResultSet,jpti:JPTI) :
            this(UUID.fromString(rs.getString("id")),jpti) {
        name=rs.getString("agency_name")
    }

    /**
     * 法人名
     */
    var name:String=""

    /**
     * RouteList
     */
    var routes= hashMapOf<UUID,Route>()



    /**
     * SQLからRouteを追加
     * 先にStationを用意しておくこと
     */
    fun addRoute(conn:Connection){
        val routeSQL = "SELECT * FROM route where agency_id=\"${id.toString()}\""
        val stmt = conn.createStatement()
        val routeRS = stmt.executeQuery(routeSQL)
        while (routeRS.next()) {
            val route=Route(routeRS,this)
            route.addStation(conn)
            routes.put(route.id,route)
        }

    }

    fun saveToSQL(conn:Connection){
        val deleteSQL="delete from agency where id=?"
        val insertSQL="insert into agency (id,agency_name) values(?,?)"
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