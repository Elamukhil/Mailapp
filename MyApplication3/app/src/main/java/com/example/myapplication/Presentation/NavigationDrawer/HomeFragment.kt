package com.example.myapplication.Presentation.NavigationDrawer


import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.MailData
import com.example.myapplication.Domain.Mapvalues
import com.example.myapplication.Data.MyContentProvider
import com.example.myapplication.R


class HomeFragment : Fragment() {

    lateinit var value: String
    val list = mutableListOf<Mapvalues>().apply { addAll(MailData.getitems()) }
    var adapter = ListAdapter(list)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = root.findViewById<View>(R.id.recyclerView) as RecyclerView
        val context = context
        MyContentProvider.DatabaseHelper(context).getquery()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        return root
    }


    fun displaylist(myValue: String) {
        val view: TextView? = getView()?.findViewById(R.id.noitems)
        val view1: ImageView? = getView()?.findViewById(R.id.image)
        view?.setVisibility(View.INVISIBLE)
        view1?.setVisibility(View.INVISIBLE)
        value = myValue
        if (myValue == "Inbox") {
            list.clear()
            val list1 = MailData.getitems().filter { it.label == "Inbox" }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (myValue == "Drafts") {
            list.clear()
            val list1 = MailData.getitems().filter { it.label == "Drafts" }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()

        } else if (myValue == "Sent") {

            list.clear()
            val list1 = MailData.getitems().filter { it.label == "Sent" }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (myValue == "Spam") {
            list.clear()
            val list1 = MailData.getitems().filter { it.label == "Spam" }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (myValue == "Trash") {

            list.clear()
            val list1 = MailData.getitems().filter { it.label == "Trash" }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        }
    }


    override fun onResume() {
        super.onResume()
        MailData.cleardata()
        MyContentProvider.DatabaseHelper(context).getquery()
        displaylist(value)
    }

}