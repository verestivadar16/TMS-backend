package com.example.tms.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.tms.R
import com.example.tms.data.InstaPostData

class InstaAdaptor(private val context: Activity, private val arrayList: ArrayList<InstaPostData>) :
        ArrayAdapter<InstaPostData>(context, R.layout.list_item, arrayList) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.list_item, null)

        val imageView: ImageView = view.findViewById(R.id.profilePic)
        val username: TextView = view.findViewById(R.id.personName)
        val description: TextView = view.findViewById(R.id.postDesciption)
        val postImage: ImageView = view.findViewById(R.id.postImage)

        imageView.setImageBitmap(arrayList[position].profImageId)
        username.text = arrayList[position].name
        description.text = arrayList[position].postDescription
        //postImage.setImageResource(arrayList[position].imageId)
        postImage.setImageBitmap(arrayList[position].imageId)

        return view
    }
}