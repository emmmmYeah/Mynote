package com.example.mynote.edu.swu

data class User(
    val Username:String,
    val password: String,
    val sqlName:Int
){
    companion object{
        val TABLE="Use"
        val SQL="sqlName"
        val USER="Username"
        val PASSWORD="password"

    }
    var id:Int?=null
}
