package com.app.practicedatabase.repository

import com.app.practicedatabase.database.UserDao
import com.app.practicedatabase.entity.User

class UserRepository(private val userDao: UserDao) {

    suspend fun signUp(name: String, email: String, password: String){
        userDao.registerUser(User(userName = name, email = email, password = password ))
    }

    suspend fun signIn(email:String, password: String): Boolean{
        val user = userDao.getUserEmail(email)
        return (user != null) && (user.password == password)
    }

}