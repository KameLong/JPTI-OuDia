package com.kamelong.OuDiaJPTIconvertor

import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

//JPTI SQLの駅をまとめる
fun main(args:Array<String>){
    var conn: Connection? = null
    var stmt: Statement? = null
    try {
        // db parameters
        val url = "jdbc:sqlite:"+"test.sqlite3"
        // create a connection to the database
        conn = DriverManager.getConnection(url)
        conn.autoCommit=false
        //同一駅のStopをまとめる
        var sql="select station_name from station"
        stmt = conn.createStatement();
        var rs:ResultSet = stmt.executeQuery(sql)
        while(rs.next()){
            val stationName=rs.getString("station_name")
            var sql2="select station.id,stop.id from stop inner join station on station.id=stop.station_id where station.station_name=\"$stationName\";"
            stmt = conn.createStatement();
            var rs2:ResultSet = stmt.executeQuery(sql2)
            rs2.next()
            val stopUUID= UUID.fromString(rs2.getString(2))
            val stationUUID= UUID.fromString(rs2.getString(1))
            while(rs2.next()){
                var sql3="update time set stop_id=\"$stopUUID\" where stop_id=\"${rs2.getString(2)}\""
                stmt = conn.createStatement();
                 stmt.executeUpdate(sql3)
                var sql4="update time set station_id=\"$stationUUID\" where station_id=\"${rs2.getString(1)}\""
                stmt = conn.createStatement();
               stmt.executeUpdate(sql4)
                var sql5="delete from station where id=\"${rs2.getString(1)}\""
                stmt = conn.createStatement();
                 stmt.executeUpdate(sql5)
                var sql6="delete from stop where id=\"${rs2.getString(2)}\""
                stmt = conn.createStatement();
                stmt.executeUpdate(sql6)

                var sql7="update routeStation set station_id=\"$stationUUID\" where station_id=\"${rs2.getString(1)}\""
                stmt = conn.createStatement();
                 stmt.executeUpdate(sql7)

            }
            conn.commit()
        }

    }catch (e:Exception){
    e.printStackTrace()
    }
}