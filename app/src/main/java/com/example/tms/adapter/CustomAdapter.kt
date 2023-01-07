package com.example.tms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.data.ConversationViewModel

class CustomAdapter(private val mList: List<ConversationViewModel>) :
        RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    var onItemClick: ((ConversationViewModel) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_view_design, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ConversationViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(ConversationViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.name.text = ConversationViewModel.name
        holder.message.text = ConversationViewModel.message
        holder.time.text = ConversationViewModel.time

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(ConversationViewModel)
        }

//        holder.itemView.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                val activity = v!!.context as AppCompatActivity
//                val nextFragment = ChatPageFragment()
//                activity.supportFragmentManager.beginTransaction().replace(R.id.conversation_list,nextFragment).addToBackStack(null).commit()
//            }
//        })

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.conversation_open_button)
        val name: TextView = itemView.findViewById(R.id.name)
        val message: TextView = itemView.findViewById(R.id.message)
        val time: TextView = itemView.findViewById(R.id.time)
    }

}