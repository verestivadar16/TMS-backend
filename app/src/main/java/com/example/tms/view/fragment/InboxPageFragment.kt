package com.example.tms.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.tms.R
import com.example.tms.databinding.InboxPageBinding
import com.example.tms.data.ConversationViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tms.adapter.CustomAdapter

class InboxPageFragment : Fragment() {
    private lateinit var binding: InboxPageBinding


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = InboxPageBinding.inflate(layoutInflater)
        binding.notificationButton.setOnClickListener(View.OnClickListener {
            findNavController().navigate(R.id.action_inboxpage_to_notificationpage)
        })
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
                        R.drawable.avatar4,
                        "Kiss Elemer ",
                        "Extremity sweetness difficult behaviour he of....",
                        "16:44"
                )
        )
        data.add(
                ConversationViewModel(
                        R.drawable.avatar4,
                        "Arany Janos ",
                        "This tires stonks",
                        "12:00"
                )
        )
        data.add(
                ConversationViewModel(
                        R.drawable.avatar4,
                        "Petofi Sandor",
                        "8pm at the parking lot?",
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
        adapter.onItemClick = {
            findNavController().navigate(R.id.action_inboxpage_to_chatpage)
        }
    }

}