package com.android.userapp.screens

import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.android.userapp.R
import com.android.userapp.data.models.User
import com.android.userapp.databinding.ItemUserBinding
import com.bumptech.glide.Glide

interface UserActionListener {

   fun onUserMove(user: User, moveBy: Int)

   fun onUserDelete(user: User)

   fun onUserDetails(user: User)

}


class UserAdapter(
   private val actionListener: UserActionListener
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>(), View.OnClickListener {

   var list: List<User> = emptyList()
      set(value) {
         field = value
         notifyDataSetChanged()
      }

   class UserViewHolder(val binding: ItemUserBinding) :
      RecyclerView.ViewHolder(binding.root)

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
      val inflater = LayoutInflater.from(parent.context)
      val binding = ItemUserBinding.inflate(inflater, parent, false)

      binding.root.setOnClickListener(this)
      binding.moreImageViewButton.setOnClickListener(this)

      return UserViewHolder(binding)
   }

   override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
      val user = list[position]
      with(holder.binding) {

         holder.itemView.tag = user
         moreImageViewButton.tag = user

         userNameTextView.text = user.name
         userCompanyTextView.text = user.company
         if (user.photo.isNotBlank()) {
            Glide.with(photoImageView.context)
               .load(user.photo)
               .circleCrop()
               .placeholder(R.drawable.ic_launcher_foreground)
               .error(R.drawable.ic_launcher_background)
               .into(photoImageView)
         } else {
            photoImageView.setImageResource(R.drawable.ic_launcher_foreground)
         }
      }
   }

   override fun getItemCount(): Int = list.size


   override fun onClick(v: View) {
      val user = v.tag as User
      when (v.id) {
         R.id.moreImageViewButton -> {
            showPopupMenu(v)
         }
         else -> {
            actionListener.onUserDetails(user)
         }
      }
   }

   private fun showPopupMenu(view: View) {
      val popupMenu = PopupMenu(view.context, view)
      val user = view.tag as User
      val position = list.indexOfFirst { it.id == user.id }

      popupMenu.menu.add(0, ID_MOVE_UP, Menu.NONE, "Move up").apply {
         isEnabled = position > 0
      }
      popupMenu.menu.add(0, ID_MOVE_DOWN, Menu.NONE, "Move down").apply {
         isEnabled = position < list.size - 1
      }
      popupMenu.menu.add(0, ID_REMOVE, Menu.NONE, "Remove")

      popupMenu.setOnMenuItemClickListener {
         when (it.itemId) {
            ID_MOVE_UP -> {
               actionListener.onUserMove(user, -1)
            }
            ID_MOVE_DOWN -> {
               actionListener.onUserMove(user, 1)
            }
            ID_REMOVE -> {
               actionListener.onUserDelete(user)
            }
         }
         return@setOnMenuItemClickListener true
      }

      popupMenu.show()
   }

   companion object {
      private const val ID_MOVE_UP = 1
      private const val ID_MOVE_DOWN = 2
      private const val ID_REMOVE = 3
   }

}