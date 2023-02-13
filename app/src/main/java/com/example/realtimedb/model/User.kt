package com.example.realtimedb.model

class User {

    lateinit var name:String
    lateinit var email:String

    constructor(){

    }

    constructor(name:String,email:String){
        this.name=name
        this.email=email
    }
}