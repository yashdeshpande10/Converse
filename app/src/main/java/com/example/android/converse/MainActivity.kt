package com.example.android.converse

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {
    //define rec view, arrayList of users, firebase auth and firebase realtime DB
    private lateinit var userRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //init firebase auth and firebase realtime DB
        mAuth = FirebaseAuth.getInstance()
        mDbRef = FirebaseDatabase.getInstance().getReference()

        //init arrayList of users
        userList = ArrayList()

        //init adapter which takes context and arraylist of users as args
        adapter = UserAdapter(this, userList)

        //init rec view
        //rec view always needs them to work with -> 1. Layout Manager and 2. Adapter
        userRecyclerView = findViewById(R.id.userRecyclerView)

        //init the layout manager
        userRecyclerView.layoutManager = LinearLayoutManager(this)

        //init adapter
        userRecyclerView.adapter = adapter

        //going inside the DB and reading the values
        //valueEventListener always implements 2 overridden methods
        // -> 1. onDataChange and 2. onCancelled
        mDbRef.child("user").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //snapshot is used to get data from DB,
                // so we need to traverse inside all the users of DB

                //empty the arrayList of users before begin traversing+fetching
                userList.clear()
                for(postSnapshot in snapshot.children){

                    //create user object and fetching from DB using postSnapshot,
                    // for adding in arrayList of users
                    val currentUser = postSnapshot.getValue(User::class.java)

                    //as we don't want to chat with ourselves, we don't display our own name
                    //hence make sure to add all the users except the current logged in user
                    if(mAuth.currentUser?.uid != currentUser?.uid){
                    userList.add(currentUser!!)
                    }
                }
                //we have to notify the adapter after any logic is performed on it
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    //we need a menu item for logout button -> create menu item xml
    //inflate the menu item using this overridden func
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        //if logout menu item is selected, logout the user via firebase singOut func
        if(item.itemId == R.id.logout){
            //write logic for logout
            mAuth.signOut()
            //after logging out, go to login activity
            val intent = Intent(this,Login::class.java)
            //On Clicking the back button from the New Activity,
            // the finish() method is called and the activity destroys
            // and returns to the mobile home screen
            finish()
            startActivity(intent)
            return true
        }
        return true
    }
}