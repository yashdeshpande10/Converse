package com.example.android.converse

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

//adapter which takes context and arrayList of users as parameters
//and extends rec. view adapter
class UserAdapter(val context: Context, val userList: ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    // in viewholder, init the views we want to display
    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val textName = itemView.findViewById<TextView>(R.id.TV_userNameItem)
    }

    //rec view adapter always implements 3 funcs ->
    //1. onCreateViewholder
    //2. onBindViewholder
    //3. getItemCount


    //in onCreateVH we inflate the layout we want to display
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_layout, parent,false)
        return UserViewHolder(view)
    }

    //in onBindVH we bind the views which we want to display and perform logic on them
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {

        //init the current user from arrayList of users
        val currentUser = userList[position]

        //get current users name in the view which we initialized above in userVH
        holder.textName.text = currentUser.name

        //when we click on user item, we go to chat activity,
        // and also send user name and user uid data to chat activity
        holder.itemView.setOnClickListener {
            val intent = Intent(context,ChatActivity::class.java)

            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)

            context.startActivity(intent)
        }
    }

    //return size of user list
    override fun getItemCount(): Int {
        return userList.size
    }
}