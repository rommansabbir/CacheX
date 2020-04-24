package com.rommansabbir.cachexdemo

class UserAuth constructor() {
    var username: String = ""
    var password: String = ""

    constructor(username: String, password: String) : this() {
        this.username = username
        this.password = password
    }
}