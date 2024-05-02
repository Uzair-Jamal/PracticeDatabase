package com.app.practicedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.practicedatabase.database.UserDatabase
import com.app.practicedatabase.databinding.ActivityLoginBinding
import com.app.practicedatabase.repository.UserRepository
import com.app.practicedatabase.viewModel.UserViewModel
import com.app.practicedatabase.viewModel.UserViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDb = UserDatabase.getInstance(this)
        val userRepository = UserRepository(userDb.userDao())
        email = binding.email.text.toString()
        password = binding.password.text.toString()
        auth = FirebaseAuth.getInstance()
        userViewModel = ViewModelProvider(this,UserViewModelFactory(userRepository))[UserViewModel::class.java]

        binding.dontHaveAcc.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
           lifecycleScope.launch { signIn() }
            val currentUser = auth.currentUser
            if(currentUser == null) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this,"Successfully Logged In",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }
                        else{
                            Toast.makeText(this,"Not logged In",Toast.LENGTH_LONG).show()
                        }
                    }
            }
            else{
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
        }
    }

   private suspend fun signIn() {

        if(email.isNotEmpty() && password.isNotEmpty()){
            userViewModel.signIn(email,password)
            Toast.makeText(this,"Login Successfully",Toast.LENGTH_LONG).show()
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
       else{
           Toast.makeText(this,"Data not inserted", Toast.LENGTH_LONG).show()
       }
    }
}