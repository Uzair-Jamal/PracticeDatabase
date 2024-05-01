package com.app.practicedatabase.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(

    @PrimaryKey(autoGenerate = true)
    val uid: Long = 0,

    @ColumnInfo(name = "User Name")
    val userName: String?,

    @ColumnInfo(name = "Email")
    val email: String?,

    @ColumnInfo(name = "Password")
    val password: String?

)
