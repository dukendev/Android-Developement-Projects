package com.ysanjeet535.photomediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.ysanjeet535.photomediaapp.models.Post
import com.ysanjeet535.photomediaapp.models.User

private const val TAG = "PostActivity"
const val EXTRA_USERNAME = "EXTRA_USERNAME"
open class PostActivity : AppCompatActivity() {

    private lateinit var FirebaseDB : FirebaseFirestore

    private lateinit var posts:MutableList<Post>
    private lateinit var adapter: PostsAdapter

    private var singedUsername : User?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)



        //setting up recycler view
        posts = mutableListOf()
        adapter = PostsAdapter(this,posts)
        val recyclerView:RecyclerView
        recyclerView = findViewById(R.id.rvPosts)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val fabUpload:View = findViewById(R.id.fabUpload)







        FirebaseDB = FirebaseFirestore.getInstance()



        FirebaseDB.collection("users").document(FirebaseAuth.getInstance().currentUser?.uid as String).get()
            .addOnSuccessListener {
                userSnapshot ->
                singedUsername = userSnapshot.toObject(User::class.java)

            }
            .addOnFailureListener {
                exception ->
                Log.i(TAG,"exception ${exception}")
            }




        var postRef = FirebaseDB.collection("posts")
            .orderBy("creation_time",Query.Direction.DESCENDING)

        val username_ = intent.getStringExtra(EXTRA_USERNAME)
        if(username_ != null){
            supportActionBar?.title = username_
            postRef= postRef.whereEqualTo("user.username",username_)

        }
        postRef.addSnapshotListener { snapshot, exception ->
            if(exception!=null || snapshot==null){
                Toast.makeText(this,"Error occured",Toast.LENGTH_SHORT).show()
                return@addSnapshotListener
            }

            val postList = snapshot.toObjects(Post::class.java)
            //---------
            posts.clear()
            posts.addAll(postList)
            adapter.notifyDataSetChanged()
            //----------------

            for(post in postList){
                Log.i(TAG,"Document ${post}")
            }

        }



        //-----------------
        fabUpload.setOnClickListener {
            val intent = Intent(this,UploadActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_posts,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_profile){
            val intent = Intent(this,ProfileActivity::class.java)
            intent.putExtra(EXTRA_USERNAME,singedUsername?.username)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}