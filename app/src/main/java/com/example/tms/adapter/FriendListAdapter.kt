package com.example.tms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.data.FriendListData

class FriendListAdapter(private val mList: List<FriendListData>) :
        RecyclerView.Adapter<FriendListAdapter.ViewHolder>() {

    var onItemClick: ((FriendListData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_contact, parent, false)

        return ViewHolder(view)
    }

        // binds the list items to a view
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

            val FriendListData= mList[position]

            // sets the image to the imageview from our itemHolder class
            holder.profileImage.setImageBitmap(FriendListData.profImage)


            // sets the text to the textview from our itemHolder class
            holder.profileName.text = FriendListData.username


            holder.itemView.setOnClickListener {

                onItemClick?.invoke(FriendListData)
            }

        }

        // return the number of the items in the list
        override fun getItemCount(): Int {
            return mList.size
        }

        // Holds the views for adding it to image and text
        class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
            val profileImage: ImageView = itemView.findViewById(R.id.profile_page_photo)
            val profileName: TextView = itemView.findViewById(R.id.profile_page_name)
        }
}