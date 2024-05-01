package com.app.practicedatabase.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.practicedatabase.entity.User

@Dao
interface UserDao {

    @Insert
    fun registerUser(user: User)

    @Query("SELECT * FROM users WHERE email = :email")
    fun getUserEmail(email:String): User?
}