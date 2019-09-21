package com.kamelong.JPTI

import java.util.*
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class Agency(val id: UUID){
    /**
     * 法人番号
     */
    var number:String=""
    /**
     * 法人名
     */
    var name:String=""

    /**
     * RouteList
     */
    var route= hashMapOf<UUID,Route>()




}