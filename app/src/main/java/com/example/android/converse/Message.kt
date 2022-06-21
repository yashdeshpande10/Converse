package com.example.android.converse

//modal class for messages
class Message {
    //define data members
    var message: String? = null
    var senderId: String? = null
    var timestamp: String? = null

    //firebase always needs an empty const. to work with
    constructor(){}

    //class constructor for message and senderID

    constructor(message: String?, senderId: String?, timestamp: String?){
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp.toString()
    }
}