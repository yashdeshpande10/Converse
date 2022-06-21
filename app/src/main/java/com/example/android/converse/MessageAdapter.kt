package com.example.android.converse

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

//rec view adapter for messages, takes context and arraylist of messages as parameters
class MessageAdapter(val context: Context, val messageList: ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    //flags for our 2 VHs
    val ITEM_RECEIVED = 1
    val ITEM_SENT = 2

    //rec view adapter always implements 3 funcs ->
    //1. onCreateViewholder
    //2. onBindViewholder
    //3. getItemCount


    //we have to attach the layouts according to the 2 VHs we have
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        if (viewType == 1){
            //inflate received message
            val view: View = LayoutInflater.from(context).inflate(R.layout.received_message, parent,false)
            return ReceivedViewHolder(view)
        }else{
            //inflate sent message
            val view: View = LayoutInflater.from(context).inflate(R.layout.sent_message, parent,false)
            return SentViewHolder(view)

        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        //get current message from arrayList of messages
        val currentMessage = messageList[position]

        //as we have 2 VHs, we select what logic to perform on each one

        //perform logic for sentVH
        if(holder.javaClass == SentViewHolder::class.java){

            //typecaste our VH as sentVH
            val viewHolder = holder as SentViewHolder

            //display current message
            holder.sentMessage.text = currentMessage.message
            holder.timestampSent.text = currentMessage.timestamp.toString()
        }

        //perform logic for receivedVH
        else{

            //typecaste our VH as receivedVH
            val viewHolder = holder as ReceivedViewHolder

            //display current message
            holder.receivedMessage.text = currentMessage.message
            holder.timestampReceived.text = currentMessage.timestamp.toString()
        }
    }

    //returns integer depending on view type
    override fun getItemViewType(position: Int): Int {
        //get current message from arrayList of messages
        val currentMessage = messageList[position]

        //current user's sent messages will return ITEM_SENT flag
        //else if it's a different user, it will return ITEM_received flag
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }else{
            return ITEM_RECEIVED
        }
    }

    //return size of arraylist of messages
    override fun getItemCount(): Int {
        return messageList.size
    }

    //we need 2 VHs -> for receiving messages and for sending messages
    //sentVH and receivedVH extends rec view VH

    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        //init the sentTV view we want to display
        val sentMessage = itemView.findViewById<TextView>(R.id.tv_sentMessage)
        val timestampSent = itemView.findViewById<TextView>(R.id.tv_timestamp_sent)


    }

    class ReceivedViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        //init the receivedTV view we want to display
        val receivedMessage = itemView.findViewById<TextView>(R.id.tv_receivedMessage)
        val timestampReceived = itemView.findViewById<TextView>(R.id.tv_timestamp_received)


    }
}