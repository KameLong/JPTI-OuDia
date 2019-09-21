package com.kamelong.JPTI

import java.util.*

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





}