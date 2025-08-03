package com.yunho.king.presentation.ui.main.fragment.usage.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.RecyclerAppListBinding
import com.yunho.king.databinding.RecyclerPageIndexBinding

class PageAdapter(
    val pageCount: Int,
    val listener: PageAdapterListener
): RecyclerView.Adapter<PageAdapter.PageViewHolder>() {
    lateinit var binding: RecyclerPageIndexBinding

    val itemList: List<Int> = List(pageCount) { it }
    var lastPageIndex: Int = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageAdapter.PageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerPageIndexBinding.inflate(inflater, parent, false)

        return PageViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: PageAdapter.PageViewHolder, position: Int) {
        holder.binding.pageIndex.text = (position + 1).toString()
        holder.binding.pageIndex.isSelected = (position + 1) == lastPageIndex

        holder.binding.pageIndex.singleClickListener {
            val lastSelectPosition = lastPageIndex - 1

            listener.loadPage(position+1)
            lastPageIndex = position + 1

            notifyItemChanged(lastSelectPosition)
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int = pageCount

    inner class PageViewHolder(
        val binding: RecyclerPageIndexBinding
    ) : RecyclerView.ViewHolder(binding.root) {

    }
}

interface PageAdapterListener {
    fun loadPage(pageIndex: Int)
}