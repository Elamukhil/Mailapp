package com.example.myapplication.Presentation.NavigationDrawer

import android.content.Intent
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Domain.Mail
import com.example.myapplication.Presentation.Detail.DetailActivity
import com.example.myapplication.R
import java.text.SimpleDateFormat
import java.util.*


class ListAdapter(private val listdata: List<Mail>) :
        RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(listItem)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = listdata[position].from
        holder.textView1.text = listdata[position].subject
        if (listdata[position].isRead == 1) {
            holder.textView.setTypeface(Typeface.DEFAULT_BOLD)
        } else {
            holder.textView.setTypeface(Typeface.DEFAULT)
        }
        val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH)
        val myDate: Date = sdf.parse(listdata[position].date)
        sdf.applyPattern("d MMM")
        val Date: String = sdf.format(myDate)
        holder.textView2.text = Date
        holder.textView3.text = listdata[position].from.substring(0, 1).toUpperCase()
        holder.imageView.setImageResource(R.drawable.bg_circle)
        holder.relativeLayout.setOnClickListener { view ->
            var intent = Intent(view.context, DetailActivity::class.java)
            intent.putExtra("mail", listdata[position].mailId)
            view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listdata.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imageView: ImageView
        var textView: TextView
        var textView1: TextView
        var textView2: TextView
        var textView3: TextView
        var relativeLayout: RelativeLayout

        init {
            imageView = itemView.findViewById<View>(R.id.imageView) as ImageView
            textView = itemView.findViewById<View>(R.id.textView) as TextView
            textView1 = itemView.findViewById<View>(R.id.textView1) as TextView
            textView2 = itemView.findViewById<View>(R.id.textView2) as TextView
            textView3 = itemView.findViewById<View>(R.id.textView3) as TextView
            relativeLayout = itemView.findViewById<View>(R.id.relativeLayout) as RelativeLayout
        }
    }
}
