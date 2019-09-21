package com.kamelong.JPTI

import java.util.*

class Service (val id:UUID){
    /**
     * 駅名
     */
    var name:String=""
    /**
     * routeArray
     */
    var routeArray= mutableListOf<SeriveRoute>()

    data class SeriveRoute(val route:Route,var startStation:Int,var endStation:Int){

    }
}
