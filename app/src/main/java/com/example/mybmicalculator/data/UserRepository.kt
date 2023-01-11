package com.example.mybmicalculator.data

import androidx.lifecycle.LiveData

//each method of this class calls the functions defined in UserDao
class UserRepository(private val userDao: UserDao) {
    val readAllUsers: LiveData<List<User>> = userDao.readAllUsers()

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    suspend fun updateHeight(id: Int, height: Double) {
        userDao.updateHeightBy(id, height)
    }

    fun getCurrentHeight(id: Int): Double {
        return userDao.getCurrentHeight(id)
    }

}