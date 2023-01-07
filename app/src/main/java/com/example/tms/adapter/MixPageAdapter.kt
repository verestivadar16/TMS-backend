package com.example.tms.adapter

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.data.MarketData
import com.example.tms.data.MixPageData

class MixPageAdapter(val context: Context, val postList: ArrayList<MixPageData>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val Insta_Post = 1
    val Market_Post = 2
    val Event_Post = 3
    val Warning_Post = 4
    val blank = 5

    val insta_on = 1
    val market_on = 1
    val event_on = 1
    val warning_on = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            1 -> {
                val view: View =
                        LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
                return InstaPostViewHolder(view)
            }

            2 -> {
                //inflate marketpost
                val view: View =
                        LayoutInflater.from(context).inflate(R.layout.market_item_view, parent, false)
                return MarketPostViewHolder(view)
            }

            3 -> {
                //inflate Event_Post
                val view: View =
                        LayoutInflater.from(context).inflate(R.layout.event_item, parent, false)
                return EventPostViewHolder(view)
            }

            4 -> {
                //inflate Warning_Post
                val view: View =
                        LayoutInflater.from(context).inflate(R.layout.traffic_info_item, parent, false)
                return WarningPostViewHolder(view)
            }
            else -> {
                val view: View = LayoutInflater.from(context).inflate(R.layout.blank, parent, false)
                return BlankPostViewHolder(view)
            }

        }

    }

    var onItemClick: ((MixPageData) -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentPost = postList[position]
        when (holder.javaClass) {

            MarketPostViewHolder::class.java -> {
                val viewHolder = holder as MarketPostViewHolder


                currentPost.productimage?.let { holder.prductimage.setImageBitmap(it) }
                currentPost.sellerimage?.let { holder.sellerimage.setImageBitmap(it) }
                holder.sellername.text = currentPost.sellername
                holder.productname.text = currentPost.productname
                holder.productdescription.text = currentPost.productdescription
                holder.price.text = currentPost.price


        }


            InstaPostViewHolder::class.java -> {
                val viewHolder = holder as InstaPostViewHolder


                currentPost.profImageId?.let { holder.profImageid.setImageBitmap(it) }
                currentPost.imageId?.let { holder.imageId.setImageBitmap(it) }
                holder.personName.text = currentPost.personName
                holder.postDescription.text = currentPost.postDescription
            }

            EventPostViewHolder::class.java -> {
                val viewHolder = holder as EventPostViewHolder

                currentPost.organiserPic?.let { holder.organiserPic.setImageResource(it) }
                holder.organiserName.text = currentPost.organiserName
                holder.eventDescription.text = currentPost.eventDescription
                currentPost.locationImage?.let { holder.locationImage.setImageBitmap(it) }
            }

            WarningPostViewHolder::class.java -> {
                val viewHolder = holder as WarningPostViewHolder

                currentPost.warningIcon?.let { holder.warningIcon.setImageResource(it) }
                holder.warningName.text = currentPost.warningName
                currentPost.warningImage?.let { holder.warningImage.setImageBitmap(it) }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        val currentPost = postList[position]

        when (currentPost.postTypeID) {
            "1" -> {
                if (currentPost.response == "true")
                    return Insta_Post
                else
                    return blank
            }

            "2" -> {
                if (currentPost.response == "true")
                    return Market_Post
                else
                    return blank
            }

            "3" -> {
                if (currentPost.response == "true")
                    return Event_Post
                else
                    return blank
            }

            "4" -> {
                if (currentPost.response == "true")
                    return Warning_Post
                else
                    return blank
            }

            else -> {
                return blank
            }
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }


    class MarketPostViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

//        val sw1: Switch = itemView.findViewById(R.id.market_switch)

        val prductimage: ImageView = itemView.findViewById(R.id.product_icon)
        val sellerimage: ImageView = itemView.findViewById(R.id.seller_icon)
        val sellername: TextView = itemView.findViewById(R.id.seller_name)
        val productname: TextView = itemView.findViewById(R.id.product_name)
        val productdescription: TextView = itemView.findViewById(R.id.product_description)
        val price: TextView = itemView.findViewById(R.id.price)

    }

    class InstaPostViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

//        val sw2: Switch = itemView.findViewById(R.id.posts_switch)

        val personName: TextView = itemView.findViewById(R.id.personName)
        val postDescription: TextView = itemView.findViewById(R.id.postDesciption)
        val imageId: ImageView = itemView.findViewById(R.id.postImage)
        val profImageid: ImageView = itemView.findViewById(R.id.profilePic)

    }

    class EventPostViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

//        val sw3: Switch = itemView.findViewById(R.id.event_switch)

        val organiserPic: ImageView = itemView.findViewById(R.id.organiserPic)
        val organiserName: TextView = itemView.findViewById(R.id.organiserName)
        val eventDescription: TextView = itemView.findViewById(R.id.eventDescription)
        val locationImage: ImageView = itemView.findViewById(R.id.locationImage)

    }

    class WarningPostViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {

//        val sw4: Switch = itemView.findViewById(R.id.warning_switch)

        val warningIcon: ImageView = itemView.findViewById(R.id.warningIcon)
        val warningName: TextView = itemView.findViewById(R.id.warningName)
        val warningImage: ImageView = itemView.findViewById(R.id.warningImage)

    }

    class BlankPostViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {




    }



}
