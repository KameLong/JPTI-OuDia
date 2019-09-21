package com.kamelong.JPTI

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
     * -1の時は着時刻が存在しない
     */
    var ariTime:Int=-1
    /**
     * 発時刻
     * -1の時は発時刻が存在しない
     */
    var depTime:Int=-1

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





}