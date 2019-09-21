package com.kamelong.JPTI

import java.util.*
import kotlin.collections.HashMap
/*
 * Copyright (c) 2019 KameLong
 * contact:kamelong.com
 *
 * This source code is released under GNU GPL ver3.
 */

class JPTI() {
    /**
     * 企業一覧
     */
    var agency:HashMap<UUID,Agency> = hashMapOf()
    /**
     * 系統一覧
     */
    var service= hashMapOf<UUID,Service>()

    var calender= hashMapOf<UUID, Calendar>()

    /**
     * 駅一覧
     */
    var station= hashMapOf<UUID,Station>()


}