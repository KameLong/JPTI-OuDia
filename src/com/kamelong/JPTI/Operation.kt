package com.kamelong.JPTI

import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Operation (val id:UUID){
    val name:String=""

    var tripList= hashMapOf<UUID,Trip>()
    data class TripOperation(var id:UUID,var trip:Trip,var start:StopTime,var end:StopTime)
}