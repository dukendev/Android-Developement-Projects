package com.ysanjeet535.photomediaapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.ysanjeet535.photomediaapp.models.Post
import com.ysanjeet535.photomediaapp.models.User

private const val TAG = "UploadActivity"

class UploadActivity : AppCompatActivity() {
    val REQUEST_CODE = 100
    private lateinit var previewimage : ImageView
    private lateinit var uploadcaption:EditText
    private lateinit var uploadbtn:Button
    private var photoUri:Uri?=null
    private var singedUsername : User?=null
    private lateinit var FirebaseDB : FirebaseFirestore
    private lateinit var storageRef : StorageReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        storageRef = FirebaseStorage.getInstance().reference


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

        val choosebtn: Button = findViewById(R.id.bUpload_Choose)
        uploadbtn= findViewById(R.id.bUpload_Upload)
        previewimage = findViewById(R.id.ivUpload_preview)
        uploadcaption = findViewById(R.id.etUpload_Caption)

        choosebtn.setOnClickListener {


            openGalleryForImage()
        }

        uploadbtn.setOnClickListener {
            handleSubmitButtonClick()
        }





    }

    private fun handleSubmitButtonClick() {
        if(photoUri ==null){
            Toast.makeText(this,"please select a photo",Toast.LENGTH_SHORT).show()
            return
        }
        if(uploadcaption.text.isBlank()){
            Toast.makeText(this,"please add caption",Toast.LENGTH_SHORT).show()
            return
        }
        if(singedUsername == null){
            Toast.makeText(this,"Login first",Toast.LENGTH_SHORT).show()
            return
        }

        //disabling submit button to stop multiple requests
        uploadbtn.isEnabled = false

        //adding to firebase now
        val photoref = storageRef.child("images/${System.currentTimeMillis()}-photo.jpg")
        val photouploaduri = photoUri as Uri
        photoref.putFile(photouploaduri).continueWithTask {
            photouploadtask ->
            photoref.downloadUrl
        }.continueWithTask {
            downloadurltask ->
            val post = Post(uploadcaption.text.toString(),downloadurltask.result.toString(),
            System.currentTimeMillis(),
            singedUsername)
            FirebaseDB.collection("posts").add(post)
        }.addOnCompleteListener {
            postcreationtask ->

            uploadbtn.isEnabled = true
            if(!postcreationtask.isSuccessful){
                Log.e(TAG,"Something went wrong",postcreationtask.exception)
            }
            uploadcaption.text.clear()
            previewimage.setImageResource(0)
            val profileIntent = Intent(this,ProfileActivity::class.java)
            profileIntent.putExtra(EXTRA_USERNAME,singedUsername?.username)
            startActivity(profileIntent)
            finish()
        }




    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            photoUri = data?.data
            previewimage.setImageURI(photoUri)
        }
    }



}