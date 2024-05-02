package com.app.practicedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.practicedatabase.database.UserDatabase
import com.app.practicedatabase.databinding.ActivitySignUpBinding
import com.app.practicedatabase.model.UserModel
import com.app.practicedatabase.repository.UserRepository
import com.app.practicedatabase.viewModel.UserViewModel
import com.app.practicedatabase.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var userName: String
    private lateinit var userEmail: String
    private lateinit var userPassword: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        userName = binding.signupName.text.toString().trim()
        userEmail = binding.signupEmail.text.toString().trim()
        userPassword = binding.signupPassword.text.toString().trim()
        database = Firebase.database

        val userDb = UserDatabase.getInstance(this)
        val userRepository = UserRepository(userDb.userDao())
        userViewModel = ViewModelProvider(this,UserViewModelFactory(userRepository))[UserViewModel::class.java]

        binding.signupBtn.setOnClickListener {
            lifecycleScope.launch {
                signUp()
            }
            if(userName.isNotEmpty() && userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "User created successfully", Toast.LENGTH_LONG)
                                .show()
                            savedData()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "User not created", Toast.LENGTH_LONG).show()
                        }

                    }
            }else{
                Toast.makeText(this, "All fields are required", Toast.LENGTH_LONG).show()

            }

        }
        binding.haveAcc.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }

    }

    private fun savedData() {
        val userId = auth.currentUser!!.uid.toInt()
        val user = UserModel(userId,userName,userEmail,userPassword)
        val ref = database.reference
        ref.child("users").child(userId.toString()).setValue(user)
    }

    private suspend fun signUp() {
        if (userName.isNotEmpty() && userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
            userViewModel.signUp(userName, userEmail, userPassword)
            Toast.makeText(this, "Data Inserted Successfully",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,LoginActivity::class.java))
        }
        else{
            Toast.makeText(this,"Data not Inserted",Toast.LENGTH_LONG).show()
        }
    }
}