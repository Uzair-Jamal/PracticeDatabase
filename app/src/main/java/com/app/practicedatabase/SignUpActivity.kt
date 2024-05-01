package com.app.practicedatabase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.app.practicedatabase.database.UserDatabase
import com.app.practicedatabase.databinding.ActivitySignUpBinding
import com.app.practicedatabase.repository.UserRepository
import com.app.practicedatabase.viewModel.UserViewModel
import com.app.practicedatabase.viewModel.UserViewModelFactory
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userDb = UserDatabase.getInstance(this)
        val userRepository = UserRepository(userDb.userDao())
        userViewModel = ViewModelProvider(this,UserViewModelFactory(userRepository))[UserViewModel::class.java]

        binding.signupBtn.setOnClickListener {
            lifecycleScope.launch {
                signUp()
            }
        }
    }

    private suspend fun signUp() {

        val userName = binding.signupName.text.toString()
        val userEmail = binding.signupEmail.text.toString()
        val userPassword = binding.signupPassword.text.toString()

        userViewModel.signUp(userName,userEmail,userPassword)
    }
}