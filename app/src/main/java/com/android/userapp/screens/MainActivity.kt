package com.android.userapp.screens

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.userapp.App
import com.android.userapp.data.UserService
import com.android.userapp.data.UsersListener
import com.android.userapp.data.models.User
import com.android.userapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), UserActionListener {

   private lateinit var binding: ActivityMainBinding
   private lateinit var userAdapter: UserAdapter

   private val userService: UserService
      get() = (applicationContext as App).userService

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

      userAdapter = UserAdapter(this)
      val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
      binding.root.layoutManager = layoutManager
      binding.root.adapter = userAdapter

      userService.addListener(userListener)
   }

   private val userListener: UsersListener = {
      userAdapter.list = it
   }

   override fun onDestroy() {
      super.onDestroy()
      userService.removeListener(userListener)
   }

   override fun onUserMove(user: User, moveBy: Int) {
      userService.moveUser(user, moveBy)
   }

   override fun onUserDelete(user: User) {
      userService.deleteUser(user)
   }

   override fun onUserDetails(user: User) {
      Toast.makeText(this@MainActivity, "User: ${user.name}", Toast.LENGTH_SHORT).show()
   }
}