package com.example.myapplication.Presentation.NavigationDrawer


import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Data.MailData
import com.example.myapplication.Domain.Mail
import com.example.myapplication.R


class HomeFragment : Fragment() {

    lateinit var value: String
    val list = mutableListOf<Mail>()
    var adapter = ListAdapter(list)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recyclerView = root.findViewById<View>(R.id.recyclerView) as RecyclerView
        val context = context
        MailData.getInstance(context!!).getquery()
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        recyclerView.adapter = adapter
        displaylist("Inbox")
        return root
    }


    fun displaylist(foldername: String) {
        val view: TextView? = getView()?.findViewById(R.id.noitems)
        val view1: ImageView? = getView()?.findViewById(R.id.image)
        view?.setVisibility(View.INVISIBLE)
        view1?.setVisibility(View.INVISIBLE)
        val context = context
        value = foldername
        if (foldername == "Inbox") {
            list.clear()
            val list1 = MailData.getInstance(context!!).getitems().filter { it.folderId == 1 }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (foldername == "Drafts") {
            list.clear()
            val list1 = MailData.getInstance(context!!).getitems().filter { it.folderId == 2 }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()

        } else if (foldername == "Sent") {

            list.clear()
            val list1 = MailData.getInstance(context!!).getitems().filter { it.folderId == 3 }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (foldername == "Spam") {
            list.clear()
            val list1 = MailData.getInstance(context!!).getitems().filter { it.folderId == 4 }
            for (i in list1) {
                list.add(i)
            }
            if (list.isEmpty()) {
                view?.setVisibility(View.VISIBLE)
                view1?.setVisibility(View.VISIBLE)
            }
            adapter.notifyDataSetChanged()
        } else if (foldername == "Trash") {

            list.clear()
            val list1 = MailData.getInstance(context!!).getitems().filter { it.folderId == 5 }
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
        val context = context
        MailData.getInstance(context!!).cleardata()
        MailData.getInstance(context!!).getquery()
        displaylist(value)
    }

}