package com.kamelong

import java.util.*

fun main(){
    for(i in 1..100){
        var id= UUID.randomUUID()
        println(id.toString())
    }
}