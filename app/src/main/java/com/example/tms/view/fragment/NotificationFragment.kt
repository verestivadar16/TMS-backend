package com.example.tms.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.R
import com.example.tms.adapter.CustomAdapter
import com.example.tms.data.ConversationViewModel
import com.example.tms.databinding.NotificationPageBinding

class NotificationFragment : Fragment() {
    private lateinit var binding: NotificationPageBinding
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = NotificationPageBinding.inflate(layoutInflater)
        binding.imageButtonBack.setOnClickListener(View.OnClickListener {
            getActivity()?.onBackPressed()
        })
        return binding.root
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        val recyclerview = getView()?.findViewById<RecyclerView>(R.id.conversation_list)
        if (recyclerview != null) {
            recyclerview.layoutManager = LinearLayoutManager(getContext())
        }
        val data = ArrayList<ConversationViewModel>()
        data.add(
                ConversationViewModel(
                        R.drawable.map,
                        "Arany Janos",
                        "invited to an upcoming event",
                        "12:00"
                )
        )
        data.add(
                ConversationViewModel(
                        R.drawable.contacticon,
                        "Osama bin Laden",
                        "wants to be your friend",
                        "13:00"
                )
        )
        data.add(
                ConversationViewModel(
                        R.drawable.instaicon,
                        "Mia Khalifa",
                        "tagged in her new post",
                        "17:00"
                )
        )
        val adapter = CustomAdapter(data)
        if (recyclerview != null) {
            recyclerview.adapter = adapter
        }
        if (recyclerview != null) {
            recyclerview.adapter = adapter
        }
    }


}

