package com.example.android.converse

//modal class of user -> store values of user

//this is a normal class instead of data class,
// because firebase needs an empty constructor to work with
class User {
    var name: String? = null
    var email: String? = null
    var uid: String? = null

    //empty constructor
    constructor(){}

    //constructor for name, email and uid values
    constructor(name: String?, email: String?, uid: String?){
        this.name = name
        this.email = email
        this.uid = uid
    }

    //we will display all our users in main activity -> rec. view
}