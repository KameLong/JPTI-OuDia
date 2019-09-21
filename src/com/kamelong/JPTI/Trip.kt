package com.kamelong.JPTI

import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Trip(val id:UUID,val service: Service,val calendar: Calendar) {
    /**
     * 列車名
     */
    val name:String=""
    /**
     * 列車番号
     */
    val tripNo:String=""

    /**
     * 向き
     */
    enum class Direction(val value:Int){
        DOWN(0),UP(1)
    }
    val direction:Direction=Direction.DOWN


    /**
     * 停車駅
     */
    val stopList= arrayListOf<StopTime>()

}