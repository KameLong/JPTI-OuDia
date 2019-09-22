package com.kamelong.JPTI

import java.sql.Connection
import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class StopTime(val id: UUID,var trip: Trip,var stop:Stop) {


    companion object{
        /**
         * ariTimeやdepTimeの開始時刻を午前3時にする
         * 午前0時から2時台までは前日の列車とみなす
         */
        const val timeBase=3600*3
    }
    /**
     * 着時刻
     */
    var ariTime:Int?=null
//    set(value){
//        if(value==null){
//            field=null
//            return
//        }
//        if(value<0){
//            field=null
//        }else{
//            if(value<timeBase){
//                field=value+24*3600;
//            }else if(value> timeBase+24*3600){
//                field=value-24*3600
//            }else{
//                field=value
//            }
//        }
//    }
    /**
     * 発時刻
     */
    var depTime:Int?=null
//        set(value){
//            if(value==null){
//                field=null
//                return
//            }
//            if(value<0){
//                field=null
//            }else{
//                if(value<timeBase){
//                    field=value+24*3600;
//                }else if(value> timeBase+24*3600){
//                    field=value-24*3600
//                }else{
//                    field=value
//                }
//            }
//        }


    /**
     * 所属駅
     */
    var station:Station=stop.station

    enum class StopType(type:Int){
        none(0),
        stop(1),pass(2)
    }

    /**
     * 停車タイプ
     * 一時的にpickup_type=dropoff_typeとして処理する
     */
    var stopType:StopType=StopType.none
    fun saveToSQL(conn: Connection,seq:Int) {
        val deleteSQL = "delete from time where id=?"
        val insertSQL =
            "insert into time (id,trip_id,station_id,stop_id,stop_sequence,pickup_type,dropoff_type,arrival_time,departure_time) values(?,?,?,?,?,?,?,?,?)"
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
            ps.setString(2, trip.id.toString())
            ps.setString(3, station.id.toString())
            ps.setString(4, stop.id.toString())
            ps.setInt(5, seq)
            ps.setInt(6, when(stopType){
                StopType.stop->0
                StopType.none->1
                StopType.pass->1
            })
            ps.setInt(7, when(stopType){
                StopType.stop->0
                StopType.none->1
                StopType.pass->1
            })
            if(ariTime!=null){
                ps.setInt(8, ariTime!!)
            }else{
  //              ps.setNull(8,java.sql.Types.INTEGER)
            }
            if(depTime!=null){
                ps.setInt(9, depTime!!)
            }else{
//                ps.setNull(9,java.sql.Types.INTEGER)
            }
            ps.executeUpdate()


        } catch (e: Exception) {
            throw e
        }
    }




}