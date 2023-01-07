package com.example.tms.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.data.MarketData

class MarketAdaptor(private val mList: List<MarketData>) :
        RecyclerView.Adapter<MarketAdaptor.ViewHolder>() {

    var onItemClick: ((MarketData) -> Unit)? = null

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.market_item_view, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val MarketData = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.productImage.setImageBitmap(MarketData.productimage)
        holder.sellerImage.setImageBitmap(MarketData.sellerimage)

        // sets the text to the textview from our itemHolder class
        holder.sellerName.text = MarketData.sellername
        holder.productName.text = MarketData.productname
        holder.productDescription.text = MarketData.productdescription
        holder.location.text = MarketData.location
        holder.price.text = MarketData.price

        holder.itemView.setOnClickListener {

            onItemClick?.invoke(MarketData)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_icon)
        val sellerImage: ImageView = itemView.findViewById(R.id.seller_icon)
        val sellerName: TextView = itemView.findViewById(R.id.seller_name)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val productDescription: TextView = itemView.findViewById(R.id.product_description)
        val location: TextView = itemView.findViewById(R.id.location)
        val price: TextView = itemView.findViewById(R.id.price)
    }

}