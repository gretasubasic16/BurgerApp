package org.unizd.rma.subasic

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyapp.R

class BurgerAdapter(private val burgerList: List<String>, private val itemClickListener: ItemClickListener) : RecyclerView.Adapter<BurgerAdapter.BurgerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BurgerViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.burger_item, parent, false)
        return BurgerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BurgerViewHolder, position: Int) {
        val burger = burgerList[position]
        holder.bind(burger)
    }

    override fun getItemCount(): Int {
        return burgerList.size
    }


    inner class BurgerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        private val textViewBurgerInfo: TextView = itemView.findViewById(R.id.textViewBurgerInfo)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(burgerInfo: String) {
            textViewBurgerInfo.text = burgerInfo
        }

        override fun onClick(v: View?) {
            itemClickListener.onItemClick(adapterPosition)
        }
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}
