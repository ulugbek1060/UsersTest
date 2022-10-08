package com.android.userapp

import android.app.Application
import com.android.userapp.data.UserService

class App : Application() {

   lateinit var userService: UserService

   override fun onCreate() {
      super.onCreate()
      userService = UserService()
   }
}