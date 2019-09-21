package com.kamelong.JPTI

import java.util.*

class Route(val id: UUID){
    /**
     * 名前
     */
    var name:String=""
    /**
     * 駅順
     */
    var stationLis=mutableListOf<Station>()

}