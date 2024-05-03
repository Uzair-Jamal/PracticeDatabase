package com.app.practicedatabase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDb = UserDatabase.getInstance(this)
        val userRepository = UserRepository(userDb.userDao())
        auth = FirebaseAuth.getInstance()
        userViewModel =
            ViewModelProvider(this, UserViewModelFactory(userRepository))[UserViewModel::class.java]

        binding.dontHaveAcc.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch { signIn(email, password) }
            } else {
                Toast.makeText(this, "Please enter email & password", Toast.LENGTH_LONG).show()
            }


        }
    }

    private suspend fun signIn(email: String, password: String) {

            userViewModel.signIn(email, password)
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this, MainActivity::class.java))
                    } else {
                        Toast.makeText(this, "Not logged In", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Write all details", Toast.LENGTH_LONG).show()
                }
        }
    }