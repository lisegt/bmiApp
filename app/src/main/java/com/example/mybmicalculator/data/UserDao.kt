package com.example.mybmicalculator.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    //query to display all users
    @Query("SELECT * FROM user_table")
    fun  readAllUsers(): LiveData<List<User>>

    //query to add 1 user
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    //query to delete 1 user
    @Delete
    suspend fun deleteUser(user: User)

    //query to delete all users
    @Query("DELETE FROM user_table")
    suspend fun deleteAllUsers()

    //query to retrieve the the current height of a user
    @Query("SELECT currentHeight FROM user_table WHERE userId=:userId")
    fun getCurrentHeight(userId: Int): Double

    //query to update the user's height
    @Query("UPDATE user_table SET currentHeight=:height WHERE userId=:id")
    fun updateHeightBy(id: Int, height: Double)

}