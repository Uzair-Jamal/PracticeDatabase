package com.app.practicedatabase.viewModel

import androidx.lifecycle.ViewModel
import com.app.practicedatabase.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    suspend fun signUp(name: String, email: String, password: String){
        withContext(Dispatchers.IO){
            userRepository.signUp(name,email,password)
        }
    }
    suspend fun signIn(email:String, password: String): Boolean{
        return withContext(Dispatchers.IO){
            userRepository.signIn(email,password)
        }

    }

}