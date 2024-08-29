package com.yunho.king.presentation.ui.main.fragment.except

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yunho.king.R
import com.yunho.king.Utils.Util
import com.yunho.king.Utils.singleClickListener
import com.yunho.king.databinding.RecyclerAppListBinding
import com.yunho.king.databinding.RecyclerExAppListBinding
import com.yunho.king.domain.dto.ExAppList

class ExAdapter(
    val item: ArrayList<ExAppList>,
    val mContext: Context,
    val listener: ExAdapterListener
): RecyclerView.Adapter<ExAdapter.ImageViewHolder>() {

    lateinit var binding: RecyclerExAppListBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = RecyclerExAppListBinding.inflate(inflater, parent, false)

        return ImageViewHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) = with(binding) {
        listAppName.text = mContext.getString(R.string.app_list_name, item[position].appName)
        listAppIcon.setImageDrawable(item[position].appIcon)

        listAppUsageCount.text = mContext.getString(
            R.string.app_list_count,
            item[position].permUseCount.toString())

        listLastUsageDate.text = mContext.getString(
            R.string.app_list_last_use,
            Util.getDate(item[position].lastUseDateTime))

        holder.binding.moveAppDetail.singleClickListener {
            listener.deletePackage(item[position].appPackageName)
            item.forEachIndexed { index, exAppList ->
                if (exAppList.appPackageName == item[position].appPackageName) {
                    item.removeAt(index)
                    return@forEachIndexed
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    inner class ImageViewHolder(val binding: RecyclerExAppListBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


}

interface ExAdapterListener{
    fun deletePackage(pkgName: String)
}